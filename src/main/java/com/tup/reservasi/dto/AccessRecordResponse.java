package com.tup.reservasi.dto;

import java.time.LocalDateTime;

import com.tup.reservasi.entity.AccessRecord;

/*
 * Penanggung jawab: Tadzkiroh Aziziyah Haqia.
 *
 * Arahan DTO response:
 * - Data AccessRecord yang dikirim:
 *   recordId: String
 *   reservationId: String
 *   satpamId: String
 *   checkInTime: LocalDateTime
 *   checkOutTime: LocalDateTime
 *   catatanPelanggaran: String
 */
public class AccessRecordResponse {

    private String recordId;
    private String reservationId;
    private String satpamId;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private String catatanPelanggaran;

    public AccessRecordResponse() {
    }

    public AccessRecordResponse(String recordId, String reservationId, String satpamId,
            LocalDateTime checkInTime, LocalDateTime checkOutTime, String catatanPelanggaran) {
        this.recordId = recordId;
        this.reservationId = reservationId;
        this.satpamId = satpamId;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.catatanPelanggaran = catatanPelanggaran;
    }

    public static AccessRecordResponse from(AccessRecord record) {
        if (record == null) {
            return null;
        }
        return new AccessRecordResponse(
                record.getRecordId(),
                record.getReservationId(),
                record.getSatpamId(),
                record.getCheckInTime(),
                record.getCheckOutTime(),
                record.getCatatanPelanggaran());
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
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

    public LocalDateTime getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(LocalDateTime checkInTime) {
        this.checkInTime = checkInTime;
    }

    public LocalDateTime getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(LocalDateTime checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public String getCatatanPelanggaran() {
        return catatanPelanggaran;
    }

    public void setCatatanPelanggaran(String catatanPelanggaran) {
        this.catatanPelanggaran = catatanPelanggaran;
    }
}
