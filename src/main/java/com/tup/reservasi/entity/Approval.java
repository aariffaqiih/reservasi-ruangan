package com.tup.reservasi.entity;

/*
 * Penanggung jawab: 'Aarif Rahmaan Jalaluddin Faqiih.
 *
 * Arahan dari class-diagram:
 * - Atribut yang perlu disiapkan:
 *   approvalId: String
 *   reservation: Reservation
 *   admin: Admin
 *   keputusan: ApprovalDecision
 *   catatan: String
 *   reviewedAt: LocalDateTime
 * - Behaviour yang perlu dibuat:
 *   setujui()
 *   tolak()
 *   mintaRevisi()
 * - Catatan relasi:
 *   Setiap Approval terhubung ke tepat 1 Reservation.
 *   Setiap Approval dibuat/ditinjau oleh tepat 1 Admin.
 */

import java.time.LocalDateTime;
import java.util.UUID;

import com.tup.reservasi.enums.ApprovalDecision;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "approvals")
public class Approval {

    @Id
    @Column(name = "approval_id", nullable = false, length = 36)
    private String approvalId;

    @Transient
    private Reservation reservation;

    @NotBlank(message = "ID reservasi tidak boleh kosong")
    @Column(name = "reservation_id", nullable = false, length = 36)
    private String reservationId;

    @Transient
    private Admin admin;

    @NotBlank(message = "ID admin tidak boleh kosong")
    @Column(name = "admin_id", nullable = false, length = 50)
    private String adminId;

    @NotNull(message = "Keputusan approval tidak boleh kosong")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ApprovalDecision keputusan;

    @Size(max = 255, message = "Catatan approval maksimal 255 karakter")
    @Column(length = 255)
    private String catatan;

    @NotNull(message = "Waktu review tidak boleh kosong")
    @Column(name = "reviewed_at", nullable = false)
    private LocalDateTime reviewedAt;

    public Approval() {
    }

    public Approval(Reservation reservation, Admin admin) {
        setReservation(reservation);
        setAdmin(admin);
    }

    public static Approval restore(String approvalId, String reservationId, String adminId,
            ApprovalDecision keputusan, String catatan, LocalDateTime reviewedAt) {
        Approval approval = new Approval();
        approval.approvalId = approvalId;
        approval.reservationId = reservationId;
        approval.adminId = adminId;
        approval.keputusan = keputusan;
        approval.catatan = normalizeText(catatan);
        approval.reviewedAt = reviewedAt;
        return approval;
    }

    @PrePersist
    public void prePersist() {
        if (approvalId == null || approvalId.isBlank()) {
            approvalId = UUID.randomUUID().toString();
        }
        if (reviewedAt == null) {
            reviewedAt = LocalDateTime.now();
        }
    }

    public void setujui(String catatan) {
        putuskan(ApprovalDecision.APPROVED, catatan);
    }

    public void tolak(String catatan) {
        putuskan(ApprovalDecision.REJECTED, catatan);
    }

    public void mintaRevisi(String catatan) {
        putuskan(ApprovalDecision.REVISION_REQUESTED, catatan);
    }

    public String getApprovalId() {
        return approvalId;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
        this.reservationId = reservation == null ? null : reservation.getReservationId();
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
        this.adminId = admin == null ? null : admin.getId();
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public ApprovalDecision getKeputusan() {
        return keputusan;
    }

    public String getCatatan() {
        return catatan;
    }

    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }

    private void putuskan(ApprovalDecision keputusanBaru, String catatanBaru) {
        if (keputusanBaru.requiresCatatan() && (catatanBaru == null || catatanBaru.isBlank())) {
            throw new IllegalArgumentException("Catatan wajib diisi");
        }
        keputusan = keputusanBaru;
        catatan = normalizeText(catatanBaru);
        reviewedAt = LocalDateTime.now();
    }

    private static String normalizeText(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
