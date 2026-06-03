package com.tup.reservasi.dto;

/*
 * Penanggung jawab: 'Aarif Rahmaan Jalaluddin Faqiih.
 *
 * Arahan DTO response:
 * - Data Approval yang dikirim:
 *   approvalId: String
 *   reservationId: String
 *   adminId: String
 *   keputusan: ApprovalDecision
 *   catatan: String
 *   reviewedAt: LocalDateTime
 */

import java.time.LocalDateTime;

import com.tup.reservasi.entity.Approval;
import com.tup.reservasi.enums.ApprovalDecision;
import com.tup.reservasi.enums.ReservationStatus;

public class ApprovalResponse {

    private String approvalId;
    private String reservationId;
    private String adminId;
    private ApprovalDecision keputusan;
    private String catatan;
    private LocalDateTime reviewedAt;
    private ReservationStatus reservationStatus;

    public ApprovalResponse() {
    }

    public ApprovalResponse(String approvalId, String reservationId, String adminId,
            ApprovalDecision keputusan, String catatan, LocalDateTime reviewedAt,
            ReservationStatus reservationStatus) {
        this.approvalId = approvalId;
        this.reservationId = reservationId;
        this.adminId = adminId;
        this.keputusan = keputusan;
        this.catatan = catatan;
        this.reviewedAt = reviewedAt;
        this.reservationStatus = reservationStatus;
    }

    public static ApprovalResponse from(Approval approval) {
        if (approval == null) {
            return null;
        }

        ReservationStatus status = approval.getReservation() == null ? null : approval.getReservation().getStatus();
        return new ApprovalResponse(
                approval.getApprovalId(),
                approval.getReservationId(),
                approval.getAdminId(),
                approval.getKeputusan(),
                approval.getCatatan(),
                approval.getReviewedAt(),
                status);
    }

    public String getApprovalId() {
        return approvalId;
    }

    public void setApprovalId(String approvalId) {
        this.approvalId = approvalId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
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

    public void setKeputusan(ApprovalDecision keputusan) {
        this.keputusan = keputusan;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(LocalDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public ReservationStatus getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }
}
