package com.tup.reservasi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tup.reservasi.entity.Admin;
import com.tup.reservasi.entity.Approval;
import com.tup.reservasi.entity.Reservation;
import com.tup.reservasi.entity.User;
import com.tup.reservasi.enums.ReservationStatus;
import com.tup.reservasi.repository.ApprovalRepository;
import com.tup.reservasi.repository.ReservationRepository;
import com.tup.reservasi.repository.UserRepository;

/*
 * Penanggung jawab: Aarif Rahmaan Jalaluddin Faqiih - 103112430182.
 * Modul: ApprovalService.
 */
@Service
@Transactional
public class ApprovalService {

    private final ApprovalRepository approvalRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final List<Approval> approvals = new ArrayList<>();

    public ApprovalService(ApprovalRepository approvalRepository,
            ReservationRepository reservationRepository,
            UserRepository userRepository,
            NotificationService notificationService) {
        this.approvalRepository = approvalRepository;
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    @Transactional(readOnly = true)
    public List<Approval> getAllApprovals() {
        return this.approvalRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Approval getApprovalById(Long approvalId) {
        return this.approvalRepository.findById(approvalId)
                .orElseThrow(() -> new RuntimeException("Approval tidak ditemukan"));
    }

    @Transactional(readOnly = true)
    public List<Reservation> getPendingReservations() {
        return this.reservationRepository.findByStatus(ReservationStatus.PENDING);
    }

    public boolean verifyReservation(Reservation reservation) {
        return reservation != null
                && reservation.getStatus() == ReservationStatus.PENDING
                && reservation.validasiWaktu();
    }

    public Approval approveReservation(Admin admin, Reservation reservation, String catatan) {
        Approval approval = getOrCreateApproval(admin, reservation);
        approval.setujui(catatan);
        return saveApproval(approval);
    }

    public Approval rejectReservation(Admin admin, Reservation reservation, String catatan) {
        Approval approval = getOrCreateApproval(admin, reservation);
        approval.tolak(catatan);
        return saveApproval(approval);
    }

    public Approval requestRevision(Admin admin, Reservation reservation, String catatan) {
        Approval approval = getOrCreateApproval(admin, reservation);
        approval.mintaRevisi(catatan);
        return saveApproval(approval);
    }

    public Approval approveReservation(Long adminId, Long reservationId, String catatan) {
        return approveReservation(getAdmin(adminId), getReservation(reservationId), catatan);
    }

    public Approval rejectReservation(Long adminId, Long reservationId, String catatan) {
        return rejectReservation(getAdmin(adminId), getReservation(reservationId), catatan);
    }

    public Approval requestRevision(Long adminId, Long reservationId, String catatan) {
        return requestRevision(getAdmin(adminId), getReservation(reservationId), catatan);
    }

    public Approval createApproval(Approval approval) {
        Approval saved = saveAndLinkApproval(approval);
        this.approvals.add(saved);
        return saved;
    }

    public Approval updateApproval(Long approvalId, Approval updatedData) {
        Approval approvalExisting = getApprovalById(approvalId);
        approvalExisting.setReservation(updatedData.getReservation());
        approvalExisting.setAdmin(updatedData.getAdmin());
        approvalExisting.setKeputusan(updatedData.getKeputusan());
        approvalExisting.setCatatan(updatedData.getCatatan());
        approvalExisting.setReviewedAt(updatedData.getReviewedAt());
        return saveAndLinkApproval(approvalExisting);
    }

    private Approval saveApproval(Approval approval) {
        Approval saved = saveAndLinkApproval(approval);
        this.approvals.add(saved);
        if (saved.getReservation() != null && saved.getReservation().getMahasiswa() != null) {
            this.notificationService.sendStatusUpdate(
                    saved.getReservation().getMahasiswa(),
                    saved.getReservation(),
                    "Keputusan reservasi: " + saved.getKeputusan());
        }
        return saved;
    }

    private Approval getOrCreateApproval(Admin admin, Reservation reservation) {
        Approval approval = findApprovalForReservation(reservation);
        if (approval == null) {
            approval = new Approval();
            approval.setReservation(reservation);
        }
        approval.setAdmin(admin);
        return approval;
    }

    private Approval saveAndLinkApproval(Approval approval) {
        Reservation reservation = approval.getReservation();
        if (reservation != null && reservation.getApproval() == approval) {
            reservation.setApproval(null);
        }
        Admin admin = approval.getAdmin();
        unlinkApprovalFromAdmin(admin, approval);
        Approval existing = findApprovalForReservation(reservation);
        if (existing != null && !isSameApproval(existing, approval)) {
            Reservation existingReservation = existing.getReservation() != null ? existing.getReservation() : reservation;
            existing.setAdmin(approval.getAdmin());
            existing.setKeputusan(approval.getKeputusan());
            existing.setCatatan(approval.getCatatan());
            existing.setReviewedAt(approval.getReviewedAt());
            approval = existing;
            approval.setReservation(existingReservation);
        }
        reservation = approval.getReservation();
        if (reservation != null && reservation.getApproval() == approval) {
            reservation.setApproval(null);
        }
        Approval saved = this.approvalRepository.save(approval);
        linkApprovalToAdmin(admin, saved);
        if (reservation != null) {
            saved.setReservation(reservation);
            reservation.setApproval(saved);
            this.reservationRepository.save(reservation);
        }
        return saved;
    }

    private Approval findApprovalForReservation(Reservation reservation) {
        if (reservation == null || reservation.getReservationId() == null) {
            return null;
        }
        return this.approvalRepository.findByReservation_ReservationId(reservation.getReservationId())
                .stream()
                .findFirst()
                .orElse(null);
    }

    private void unlinkApprovalFromAdmin(Admin admin, Approval approval) {
        if (admin == null || admin.getApprovals() == null) {
            return;
        }
        admin.getApprovals().removeIf(existing -> existing == approval);
    }

    private void linkApprovalToAdmin(Admin admin, Approval approval) {
        if (admin == null || admin.getApprovals() == null) {
            return;
        }
        for (Approval existing : admin.getApprovals()) {
            if (isSameApproval(existing, approval)) {
                return;
            }
        }
        admin.getApprovals().add(approval);
    }

    private boolean isSameApproval(Approval first, Approval second) {
        if (first == second) {
            return true;
        }
        return first != null
                && second != null
                && first.getApprovalId() != null
                && first.getApprovalId().equals(second.getApprovalId());
    }

    private Admin getAdmin(Long adminId) {
        User user = this.userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin tidak ditemukan"));
        if (!(user instanceof Admin admin)) {
            throw new RuntimeException("User bukan Admin");
        }
        return admin;
    }

    private Reservation getReservation(Long reservationId) {
        return this.reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservasi tidak ditemukan"));
    }

    @Transactional(readOnly = true)
    public List<Approval> getApprovals() {
        return this.approvals;
    }

    public void deleteApproval(Long approvalId) {
        Approval approval = getApprovalById(approvalId);
        Reservation reservation = approval.getReservation();
        Admin admin = approval.getAdmin();
        if (reservation != null && isSameApproval(reservation.getApproval(), approval)) {
            reservation.setApproval(null);
        }
        unlinkApprovalFromAdmin(admin, approval);
        approval.setReservation(null);
        approval.setAdmin(null);
        this.approvals.removeIf(existing -> isSameApproval(existing, approval));
        this.approvalRepository.delete(approval);
    }
}
