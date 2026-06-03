package com.tup.reservasi.service;

/*
 * Penanggung jawab: 'Aarif Rahmaan Jalaluddin Faqiih.
 *
 * Arahan dari class-diagram:
 * - Service menyimpan/mengelola:
 *   approvals: List<Approval>
 * - Behaviour yang perlu dibuat:
 *   verifyReservation(): boolean
 *   approveReservation(): Approval
 *   rejectReservation(): Approval
 *   requestRevision(): Approval
 * - Aturan yang perlu dipikirkan saat coding:
 *   verifyReservation() memeriksa data Reservation sebelum keputusan.
 *   approve/reject/revision mengisi keputusan, catatan, reviewedAt, dan Admin.
 *   perubahan Approval harus ikut mengubah status Reservation.
 */

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tup.reservasi.auth.LoginUser;
import com.tup.reservasi.auth.LoginUserRepository;
import com.tup.reservasi.auth.UserRole;
import com.tup.reservasi.dto.ApprovalRequest;
import com.tup.reservasi.entity.Admin;
import com.tup.reservasi.entity.Approval;
import com.tup.reservasi.entity.Reservation;
import com.tup.reservasi.enums.ApprovalDecision;
import com.tup.reservasi.enums.ReservationStatus;
import com.tup.reservasi.exception.ReservationException;
import com.tup.reservasi.repository.ApprovalRepository;
import com.tup.reservasi.repository.ReservationRepository;

@Service
public class ApprovalService {

    private final ApprovalRepository approvalRepository;
    private final ReservationRepository reservationRepository;
    private final LoginUserRepository loginUserRepository;

    public ApprovalService(
            ApprovalRepository approvalRepository,
            ReservationRepository reservationRepository,
            LoginUserRepository loginUserRepository) {
        this.approvalRepository = approvalRepository;
        this.reservationRepository = reservationRepository;
        this.loginUserRepository = loginUserRepository;
    }

    public boolean verifyReservation(String reservationId) {
        return reservationRepository.findById(reservationId)
                .map(this::verifyReservation)
                .orElse(false);
    }

    public boolean verifyReservation(Reservation reservation) {
        return reservation != null
                && reservation.getStatus() == ReservationStatus.PENDING
                && reservation.validasiWaktu();
    }

    public List<Reservation> getPendingReservations() {
        return reservationRepository.findByStatus(ReservationStatus.PENDING);
    }

    public List<Approval> getApprovalHistory() {
        return approvalRepository.findAllByOrderByReviewedAtDesc();
    }

    @Transactional
    public Approval approveReservation(String reservationId, String adminId, ApprovalRequest request) {
        return processDecision(reservationId, adminId, request, ApprovalDecision.APPROVED);
    }

    @Transactional
    public Approval rejectReservation(String reservationId, String adminId, ApprovalRequest request) {
        return processDecision(reservationId, adminId, request, ApprovalDecision.REJECTED);
    }

    @Transactional
    public Approval requestRevision(String reservationId, String adminId, ApprovalRequest request) {
        return processDecision(reservationId, adminId, request, ApprovalDecision.REVISION_REQUESTED);
    }

    private Approval processDecision(String reservationId, String adminId, ApprovalRequest request,
            ApprovalDecision decision) {
        if (reservationId == null || reservationId.isBlank()) {
            throw new IllegalArgumentException("ID reservasi tidak boleh kosong");
        }
        if (adminId == null || adminId.isBlank()) {
            throw new IllegalArgumentException("ID admin tidak boleh kosong");
        }

        validateAdmin(adminId);

        ApprovalRequest effectiveRequest = request == null ? new ApprovalRequest() : request;
        String catatan = effectiveRequest.getCatatan();
        if (decision.requiresCatatan() && (catatan == null || catatan.isBlank())) {
            throw new IllegalArgumentException("Catatan wajib diisi");
        }

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationException("Reservasi tidak ditemukan"));

        if (!verifyReservation(reservation)) {
            throw new ReservationException("Status reservasi tidak bisa diproses approval");
        }

        Admin admin = new Admin(adminId, adminId, null, null, null, "Administrasi");
        Approval approval = new Approval(reservation, admin);

        if (decision == ApprovalDecision.APPROVED) {
            approval.setujui(catatan);
            reservation.ubahStatus(ReservationStatus.APPROVED);
        } else if (decision == ApprovalDecision.REJECTED) {
            approval.tolak(catatan);
            reservation.ubahStatus(ReservationStatus.REJECTED);
        } else {
            approval.mintaRevisi(catatan);
            reservation.ubahStatus(ReservationStatus.REJECTED);
        }

        reservationRepository.save(reservation);
        return approvalRepository.save(approval);
    }

    private void validateAdmin(String adminId) {
        LoginUser loginUser = loginUserRepository.findByUsername(adminId)
                .orElseThrow(() -> new IllegalArgumentException("Admin tidak ditemukan"));
        if (loginUser.getRole() != UserRole.ADMIN) {
            throw new IllegalArgumentException("User bukan Admin");
        }
    }
}
