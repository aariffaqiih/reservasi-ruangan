package com.tup.reservasi.repository;

/*
 * Penanggung jawab: Tadzkiroh Aziziyah Haqia.
 *
 * Arahan test repository:
 * - Uji query AccessRecord berdasarkan Reservation.
 * - Uji query AccessRecord berdasarkan Satpam.
 * - Uji query AccessRecord yang belum check-out.
 * - Uji query catatan kendala/pelanggaran.
 */

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.tup.reservasi.entity.AccessRecord;
import com.tup.reservasi.entity.Mahasiswa;
import com.tup.reservasi.entity.Reservation;
import com.tup.reservasi.entity.Room;
import com.tup.reservasi.entity.Satpam;
import com.tup.reservasi.enums.ReservationStatus;

@DataJpaTest
@ActiveProfiles("test")
class AccessRecordRepositoryTests {

    @Autowired
    private AccessRecordRepository accessRecordRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    private Satpam satpam;
    private Satpam satpamLain;
    private Reservation reservasiSatu;
    private Reservation reservasiDua;

    /**
     * Helper: buat dan simpan Reservation dengan status APPROVED langsung,
     * menggunakan Reservation.restore() agar melewati validasi transisi status.
     */
    private Reservation buatReservasiApproved(Mahasiswa mahasiswa, Room room, String tujuan) {
        Reservation r = Reservation.restore(
                null,
                mahasiswa.getId(),
                room.getRoomId(),
                LocalDate.now(),
                LocalTime.of(9, 0),
                LocalTime.of(11, 0),
                tujuan,
                ReservationStatus.APPROVED,
                null, null, null
        );
        return reservationRepository.save(r);
    }

    /**
     * Helper: buat AccessRecord yang sudah check-in.
     */
    private AccessRecord buatRecordCheckIn(String reservationId, String satpamId) {
        AccessRecord record = AccessRecord.restore(
                null, reservationId, satpamId,
                LocalDateTime.now(), null, null
        );
        return accessRecordRepository.save(record);
    }

    /**
     * Helper: buat AccessRecord yang sudah check-in dan check-out.
     */
    private AccessRecord buatRecordCheckOut(String reservationId, String satpamId) {
        AccessRecord record = AccessRecord.restore(
                null, reservationId, satpamId,
                LocalDateTime.now().minusHours(2),
                LocalDateTime.now(),
                null
        );
        return accessRecordRepository.save(record);
    }

    /**
     * Helper: buat AccessRecord dengan catatan pelanggaran.
     */
    private AccessRecord buatRecordDenganKendala(String reservationId, String satpamId, String kendala) {
        AccessRecord record = AccessRecord.restore(
                null, reservationId, satpamId,
                LocalDateTime.now().minusHours(1),
                null,
                kendala
        );
        return accessRecordRepository.save(record);
    }

    @BeforeEach
    void setUp() {
        // Buat dua satpam dengan nomor telepon berbeda agar tidak konflik unique constraint
        satpam = new Satpam("SAT-001", "Budi Santoso", "budi@example.com", "08111", "hash", "Pagi", "Pos A");
        userRepository.save(satpam);

        satpamLain = new Satpam("SAT-002", "Amir Hamzah", "amir@example.com", "08222", "hash", "Sore", "Pos B");
        userRepository.save(satpamLain);

        Mahasiswa mahasiswa = new Mahasiswa("MHS-001", "Dina", "dina@example.com", "08333", "hash", "NIM-001", "IF", 2025);
        userRepository.save(mahasiswa);

        Room room = new Room("RM-001", "Lab Komputer", "B", 30, true);
        roomRepository.save(room);

        reservasiSatu = buatReservasiApproved(mahasiswa, room, "Seminar Pagi");
        reservasiDua = buatReservasiApproved(mahasiswa, room, "Seminar Sore");
    }

    // -------------------------------------------------------------------------
    // 1. Query AccessRecord berdasarkan Reservation
    // -------------------------------------------------------------------------

    @Test
    void testFindByReservationIdDitemukanJikaAdaRecord() {
        buatRecordCheckIn(reservasiSatu.getReservationId(), satpam.getId());

        Optional<AccessRecord> result = accessRecordRepository.findByReservationId(reservasiSatu.getReservationId());

        assertTrue(result.isPresent());
        assertEquals(reservasiSatu.getReservationId(), result.get().getReservationId());
    }

    @Test
    void testFindByReservationIdKosongJikaTidakAdaRecord() {
        Optional<AccessRecord> result = accessRecordRepository.findByReservationId(reservasiSatu.getReservationId());

        assertFalse(result.isPresent());
    }

    // -------------------------------------------------------------------------
    // 2. Query AccessRecord berdasarkan Satpam
    // -------------------------------------------------------------------------

    @Test
    void testFindBySatpamIdMengembalikanSemuaRecordSatpam() {
        buatRecordCheckIn(reservasiSatu.getReservationId(), satpam.getId());
        buatRecordCheckIn(reservasiDua.getReservationId(), satpamLain.getId());

        List<AccessRecord> result = accessRecordRepository.findBySatpamId(satpam.getId());

        assertEquals(1, result.size());
        assertEquals(satpam.getId(), result.get(0).getSatpamId());
    }

    @Test
    void testFindBySatpamIdKosongJikaSatpamBelumCheckin() {
        List<AccessRecord> result = accessRecordRepository.findBySatpamId(satpam.getId());

        assertTrue(result.isEmpty());
    }

    // -------------------------------------------------------------------------
    // 3. Query AccessRecord yang belum check-out
    // -------------------------------------------------------------------------

    @Test
    void testFindBelumCheckOutHanyaMengembalikanRecordTanpaCheckOut() {
        buatRecordCheckIn(reservasiSatu.getReservationId(), satpam.getId());
        buatRecordCheckOut(reservasiDua.getReservationId(), satpamLain.getId());

        List<AccessRecord> result = accessRecordRepository.findBelumCheckOut();

        assertEquals(1, result.size());
        assertEquals(reservasiSatu.getReservationId(), result.get(0).getReservationId());
    }

    @Test
    void testFindBelumCheckOutKosongJikaSemudahSudahCheckOut() {
        buatRecordCheckOut(reservasiSatu.getReservationId(), satpam.getId());
        buatRecordCheckOut(reservasiDua.getReservationId(), satpamLain.getId());

        List<AccessRecord> result = accessRecordRepository.findBelumCheckOut();

        assertTrue(result.isEmpty());
    }

    // -------------------------------------------------------------------------
    // 4. Query catatan kendala/pelanggaran
    // -------------------------------------------------------------------------

    @Test
    void testFindWithKendalaMengembalikanRecordBermasalah() {
        buatRecordDenganKendala(reservasiSatu.getReservationId(), satpam.getId(), "Kunci rusak");
        buatRecordCheckIn(reservasiDua.getReservationId(), satpamLain.getId()); // tanpa kendala

        List<AccessRecord> result = accessRecordRepository.findWithKendala();

        assertEquals(1, result.size());
        assertEquals("Kunci rusak", result.get(0).getCatatanPelanggaran());
    }

    @Test
    void testFindWithKendalaKosongJikaTidakAdaKendala() {
        buatRecordCheckIn(reservasiSatu.getReservationId(), satpam.getId());
        buatRecordCheckOut(reservasiDua.getReservationId(), satpamLain.getId());

        List<AccessRecord> result = accessRecordRepository.findWithKendala();

        assertTrue(result.isEmpty());
    }
}
