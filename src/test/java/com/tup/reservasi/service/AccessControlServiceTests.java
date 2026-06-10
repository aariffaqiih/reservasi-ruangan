package com.tup.reservasi.service;

/*
 * Penanggung jawab: Tadzkiroh Aziziyah Haqia.
 *
 * Arahan test service:
 * - Uji checkIn(): AccessRecord.
 * - Uji checkOut(): AccessRecord.
 * - Uji reportIssue().
 * - Uji check-out tidak boleh sebelum check-in.
 */

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.tup.reservasi.dto.AccessIssueRequest;
import com.tup.reservasi.entity.AccessRecord;
import com.tup.reservasi.entity.Mahasiswa;
import com.tup.reservasi.entity.Reservation;
import com.tup.reservasi.entity.Room;
import com.tup.reservasi.entity.Satpam;
import com.tup.reservasi.enums.ReservationStatus;
import com.tup.reservasi.exception.ReservationException;
import com.tup.reservasi.repository.AccessRecordRepository;
import com.tup.reservasi.repository.ReservationRepository;
import com.tup.reservasi.repository.RoomRepository;
import com.tup.reservasi.repository.UserRepository;

@DataJpaTest
@ActiveProfiles("test")
class AccessControlServiceTests {

    @Autowired
    private AccessRecordRepository accessRecordRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    private AccessControlService accessControlService;
    private Satpam satpam;
    private Mahasiswa mahasiswa;
    private Room room;

    @BeforeEach
    void setUp() {
        accessControlService = new AccessControlService(accessRecordRepository, reservationRepository, userRepository);

        satpam = new Satpam("S-001", "Satpam Satu", "satpam@example.com", "08123", "hash", "Pagi", "Pos 1");
        userRepository.save(satpam);

        mahasiswa = new Mahasiswa("M-001", "Mhs Satu", "mhs@example.com", "08124", "hash", "NIM-001", "IF", 2026);
        userRepository.save(mahasiswa);

        room = new Room("R-001", "Ruang Seminar", "A", 40, true);
        roomRepository.save(room);
    }

    /**
     * Buat Reservation dengan status APPROVED langsung memakai restore()
     * agar tidak perlu melewati validasi transisi status di test.
     */
    private Reservation simpanReservasiApproved() {
        Reservation reservation = Reservation.restore(
                null,
                mahasiswa.getId(),
                room.getRoomId(),
                LocalDate.now(),
                LocalTime.of(8, 0),
                LocalTime.of(10, 0),
                "Rapat",
                ReservationStatus.APPROVED,
                null, null, null
        );
        return reservationRepository.save(reservation);
    }

    @Test
    void testCheckInBerhasil() {
        Reservation reservation = simpanReservasiApproved();

        AccessRecord record = accessControlService.checkIn(reservation.getReservationId(), satpam.getId());

        assertNotNull(record);
        assertEquals(reservation.getReservationId(), record.getReservationId());
        assertEquals(satpam.getId(), record.getSatpamId());
        assertNotNull(record.getCheckInTime());
        assertNull(record.getCheckOutTime());
    }

    @Test
    void testCheckInGagalReservationsiBelumApproved() {
        // Reservation masih DRAFT, belum APPROVED
        Reservation reservation = Reservation.restore(
                null, mahasiswa.getId(), room.getRoomId(),
                LocalDate.now(), LocalTime.of(8, 0), LocalTime.of(10, 0),
                "Rapat", ReservationStatus.DRAFT, null, null, null
        );
        reservation = reservationRepository.save(reservation);

        final String resId = reservation.getReservationId();
        assertThrows(ReservationException.class,
                () -> accessControlService.checkIn(resId, satpam.getId()));
    }

    @Test
    void testCheckInGagalJikaSudahCheckIn() {
        Reservation reservation = simpanReservasiApproved();

        // Check-in pertama berhasil
        accessControlService.checkIn(reservation.getReservationId(), satpam.getId());

        // Check-in kedua harus gagal — bisa ReservationException atau
        // DataIntegrityViolationException tergantung urutan flush H2.
        // Keduanya adalah RuntimeException.
        final String resId = reservation.getReservationId();
        assertThrows(RuntimeException.class,
                () -> accessControlService.checkIn(resId, satpam.getId()));
    }

    @Test
    void testCheckOutBerhasil() {
        Reservation reservation = simpanReservasiApproved();

        accessControlService.checkIn(reservation.getReservationId(), satpam.getId());

        AccessRecord record = accessControlService.checkOut(reservation.getReservationId(), satpam.getId());

        assertNotNull(record);
        assertNotNull(record.getCheckOutTime());
    }

    @Test
    void testCheckOutGagalSebelumCheckIn() {
        Reservation reservation = simpanReservasiApproved();

        final String resId = reservation.getReservationId();
        assertThrows(ReservationException.class,
                () -> accessControlService.checkOut(resId, satpam.getId()));
    }

    @Test
    void testReportIssueBerhasil() {
        Reservation reservation = simpanReservasiApproved();

        accessControlService.checkIn(reservation.getReservationId(), satpam.getId());

        AccessIssueRequest request = new AccessIssueRequest(
                reservation.getReservationId(), satpam.getId(), "Kunci hilang");
        AccessRecord record = accessControlService.reportIssue(request);

        assertNotNull(record);
        assertEquals("Kunci hilang", record.getCatatanPelanggaran());
    }
}
