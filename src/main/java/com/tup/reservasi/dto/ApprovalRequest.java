package com.tup.reservasi.dto;

/*
 * Penanggung jawab: 'Aarif Rahmaan Jalaluddin Faqiih.
 *
 * Arahan DTO request:
 * - Data keputusan approval:
 *   reservationId: String
 *   adminId: String
 *   keputusan: ApprovalDecision
 *   catatan: String
 * - Behaviour terkait:
 *   Approval.setujui(), Approval.tolak(), Approval.mintaRevisi(),
 *   Admin.setujuiReservasi(), Admin.tolakReservasi(), Admin.mintaRevisiData().
 */

import com.tup.reservasi.enums.ApprovalDecision;

import jakarta.validation.constraints.Size;

public class ApprovalRequest {

    private String reservationId;

    private String adminId;

    private ApprovalDecision keputusan;

    @Size(max = 255, message = "Catatan approval maksimal 255 karakter")
    private String catatan;

    public ApprovalRequest() {
    }

    public ApprovalRequest(String reservationId, String adminId, ApprovalDecision keputusan, String catatan) {
        this.reservationId = reservationId;
        this.adminId = adminId;
        this.keputusan = keputusan;
        this.catatan = catatan;
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
}
