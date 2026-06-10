package com.tup.reservasi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/*
 * Penanggung jawab: Tadzkiroh Aziziyah Haqia.
 *
 * Arahan DTO request:
 * - Data pencatatan kendala:
 *   reservationId: String
 *   satpamId: String
 *   catatanPelanggaran: String
 * - Behaviour terkait:
 *   Satpam.catatKendala(), AccessRecord.laporkanKendala(),
 *   AccessControlService.reportIssue().
 */
public class AccessIssueRequest {

    @NotBlank(message = "ID reservasi tidak boleh kosong")
    private String reservationId;

    private String satpamId;

    @NotBlank(message = "Catatan pelanggaran tidak boleh kosong")
    @Size(max = 255, message = "Catatan pelanggaran maksimal 255 karakter")
    private String catatanPelanggaran;

    public AccessIssueRequest() {
    }

    public AccessIssueRequest(String reservationId, String satpamId, String catatanPelanggaran) {
        this.reservationId = reservationId;
        this.satpamId = satpamId;
        this.catatanPelanggaran = catatanPelanggaran;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getSatpamId() {
        return satpamId;
    }

    public void setSatpamId(String satpamId) {
        this.satpamId = satpamId;
    }

    public String getCatatanPelanggaran() {
        return catatanPelanggaran;
    }

    public void setCatatanPelanggaran(String catatanPelanggaran) {
        this.catatanPelanggaran = catatanPelanggaran;
    }
}
