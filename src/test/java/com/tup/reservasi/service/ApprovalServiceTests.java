package com.tup.reservasi.service;

/*
 * Penanggung jawab: 'Aarif Rahmaan Jalaluddin Faqiih.
 *
 * Arahan test service:
 * - Uji verifyReservation(): boolean.
 * - Uji approveReservation(): Approval.
 * - Uji rejectReservation(): Approval.
 * - Uji requestRevision(): Approval.
 * - Uji efek keputusan terhadap status Reservation.
 */

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.tup.reservasi.auth.LoginUser;
import com.tup.reservasi.auth.LoginUserRepository;
import com.tup.reservasi.auth.UserRole;
import com.tup.reservasi.dto.ApprovalRequest;
import com.tup.reservasi.entity.Approval;
import com.tup.reservasi.entity.Reservation;
import com.tup.reservasi.enums.ApprovalDecision;
import com.tup.reservasi.enums.ReservationStatus;
import com.tup.reservasi.exception.ReservationException;
import com.tup.reservasi.repository.ApprovalRepository;
import com.tup.reservasi.repository.ReservationRepository;

@DataJpaTest
@ActiveProfiles("test")
class ApprovalServiceTests {

    @Autowired
    private ApprovalRepository approvalRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private LoginUserRepository loginUserRepository;

    private ApprovalService approvalService;

    @BeforeEach
    void setUp() {
        approvalService = new ApprovalService(approvalRepository, reservationRepository, loginUserRepository);
        loginUserRepository.save(new LoginUser("adm", "hash", UserRole.ADMIN));
        loginUserRepository.save(new LoginUser("mhs", "hash", UserRole.MAHASISWA));
    }

    @Test
    void verifyReservationTrueUntukReservationPendingValid() {
        Reservation reservation = reservationRepository.save(sampleReservation("RSV-001", ReservationStatus.PENDING));

        assertTrue(approvalService.verifyReservation(reservation.getReservationId()));
    }

    @Test
    void verifyReservationFalseUntukReservationBukanPending() {
        Reservation reservation = reservationRepository.save(sampleReservation("RSV-001", ReservationStatus.APPROVED));

        assertFalse(approvalService.verifyReservation(reservation.getReservationId()));
    }

    @Test
    void approveReservationMenyimpanApprovalDanMengubahStatusReservation() {
        Reservation reservation = reservationRepository.save(sampleReservation("RSV-001", ReservationStatus.PENDING));
        ApprovalRequest request = new ApprovalRequest(null, null, null, "Disetujui");

        Approval approval = approvalService.approveReservation(reservation.getReservationId(), "adm", request);
        Reservation updated = reservationRepository.findById(reservation.getReservationId()).orElseThrow();

        assertEquals(ApprovalDecision.APPROVED, approval.getKeputusan());
        assertEquals(ReservationStatus.APPROVED, updated.getStatus());
        assertEquals(1, approvalRepository.findByReservationId(reservation.getReservationId()).size());
    }

    @Test
    void rejectReservationWajibCatatanDanMengubahStatusRejected() {
        Reservation reservation = reservationRepository.save(sampleReservation("RSV-001", ReservationStatus.PENDING));
        ApprovalRequest request = new ApprovalRequest(null, null, null, "Jadwal tidak sesuai");

        Approval approval = approvalService.rejectReservation(reservation.getReservationId(), "adm", request);
        Reservation updated = reservationRepository.findById(reservation.getReservationId()).orElseThrow();

        assertEquals(ApprovalDecision.REJECTED, approval.getKeputusan());
        assertEquals(ReservationStatus.REJECTED, updated.getStatus());
        assertEquals("Jadwal tidak sesuai", approval.getCatatan());
    }

    @Test
    void rejectReservationMenolakCatatanKosong() {
        Reservation reservation = reservationRepository.save(sampleReservation("RSV-001", ReservationStatus.PENDING));

        assertThrows(IllegalArgumentException.class,
                () -> approvalService.rejectReservation(reservation.getReservationId(), "adm", new ApprovalRequest()));
    }

    @Test
    void requestRevisionMenyimpanKeputusanRevisiDanStatusRejected() {
        Reservation reservation = reservationRepository.save(sampleReservation("RSV-001", ReservationStatus.PENDING));
        ApprovalRequest request = new ApprovalRequest(null, null, null, "Lengkapi data kegiatan");

        Approval approval = approvalService.requestRevision(reservation.getReservationId(), "adm", request);
        Reservation updated = reservationRepository.findById(reservation.getReservationId()).orElseThrow();

        assertEquals(ApprovalDecision.REVISION_REQUESTED, approval.getKeputusan());
        assertEquals(ReservationStatus.REJECTED, updated.getStatus());
    }

    @Test
    void approveReservationMenolakStatusYangTidakBisaDireview() {
        Reservation reservation = reservationRepository.save(sampleReservation("RSV-001", ReservationStatus.APPROVED));

        assertThrows(ReservationException.class,
                () -> approvalService.approveReservation(reservation.getReservationId(), "adm", new ApprovalRequest()));
    }

    @Test
    void approveReservationMenolakUserBukanAdmin() {
        Reservation reservation = reservationRepository.save(sampleReservation("RSV-001", ReservationStatus.PENDING));

        assertThrows(IllegalArgumentException.class,
                () -> approvalService.approveReservation(reservation.getReservationId(), "mhs", new ApprovalRequest()));
    }

    private Reservation sampleReservation(String reservationId, ReservationStatus status) {
        return Reservation.restore(
                reservationId,
                "mhs",
                "R-001",
                LocalDate.now().plusDays(1),
                LocalTime.of(18, 0),
                LocalTime.of(20, 0),
                "Rapat organisasi",
                status,
                null,
                null,
                null);
    }
}
