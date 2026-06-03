package com.tup.reservasi.entity;

/*
 * Penanggung jawab: 'Aarif Rahmaan Jalaluddin Faqiih.
 *
 * Arahan test dari class-diagram:
 * - Uji atribut Approval:
 *   approvalId, reservation, admin, keputusan, catatan, reviewedAt.
 * - Uji behaviour:
 *   setujui() mengisi keputusan setuju dan reviewedAt.
 *   tolak() mengisi keputusan tolak dan catatan.
 *   mintaRevisi() mengisi keputusan revisi dan catatan.
 */

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import com.tup.reservasi.enums.ApprovalDecision;

class ApprovalTests {

    @Test
    void setujuiMengisiKeputusanDanReviewedAt() {
        Approval approval = sampleApproval();

        approval.setujui("  Disetujui  ");

        assertEquals(ApprovalDecision.APPROVED, approval.getKeputusan());
        assertEquals("Disetujui", approval.getCatatan());
        assertNotNull(approval.getReviewedAt());
    }

    @Test
    void tolakMengisiKeputusanDanWajibCatatan() {
        Approval approval = sampleApproval();

        approval.tolak("  Jadwal tidak sesuai  ");

        assertEquals(ApprovalDecision.REJECTED, approval.getKeputusan());
        assertEquals("Jadwal tidak sesuai", approval.getCatatan());
        assertNotNull(approval.getReviewedAt());
    }

    @Test
    void tolakMenolakCatatanKosong() {
        Approval approval = sampleApproval();

        assertThrows(IllegalArgumentException.class, () -> approval.tolak(" "));
    }

    @Test
    void mintaRevisiMengisiKeputusanRevisi() {
        Approval approval = sampleApproval();

        approval.mintaRevisi("  Lengkapi tujuan kegiatan  ");

        assertEquals(ApprovalDecision.REVISION_REQUESTED, approval.getKeputusan());
        assertEquals("Lengkapi tujuan kegiatan", approval.getCatatan());
        assertNotNull(approval.getReviewedAt());
    }

    private Approval sampleApproval() {
        return new Approval(sampleReservation(), sampleAdmin());
    }

    private Reservation sampleReservation() {
        Reservation reservation = new Reservation(
                sampleMahasiswa(),
                sampleRoom(),
                LocalDate.now().plusDays(1),
                LocalTime.of(18, 0),
                LocalTime.of(20, 0),
                "Rapat organisasi");
        reservation.prePersist();
        reservation.ajukan();
        return reservation;
    }

    private Admin sampleAdmin() {
        return new Admin("adm", "Admin Starter", "adm@example.com", "080000000002", "hash", "Administrasi");
    }

    private Mahasiswa sampleMahasiswa() {
        return new Mahasiswa(
                "mhs",
                "Mahasiswa Starter",
                "mhs@example.com",
                "080000000001",
                "hash",
                "2026001",
                "Teknik Informatika",
                2026);
    }

    private Room sampleRoom() {
        return new Room("R-001", "Ruang Seminar", "A", 40, true);
    }
}
