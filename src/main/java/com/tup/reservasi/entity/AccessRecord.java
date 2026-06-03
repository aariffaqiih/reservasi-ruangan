package com.tup.reservasi.entity;

import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/*
 * Penanggung jawab: Tadzkiroh Aziziyah Haqia.
 *
 * Arahan dari class-diagram:
 * - Atribut yang perlu disiapkan:
 *   recordId: String
 *   reservation: Reservation
 *   satpam: Satpam
 *   checkInTime: LocalDateTime
 *   checkOutTime: LocalDateTime
 *   catatanPelanggaran: String
 * - Behaviour yang perlu dibuat:
 *   checkIn()
 *   checkOut()
 *   laporkanKendala()
 * - Catatan relasi:
 *   Setiap AccessRecord terhubung ke tepat 1 Reservation.
 *   Setiap AccessRecord ditangani oleh tepat 1 Satpam.
 */
@Entity
@Table(name = "access_records")
public class AccessRecord {

    @Id
    @Column(name = "record_id", nullable = false, length = 36)
    private String recordId;

    @Transient
    private Reservation reservation;

    @NotBlank(message = "ID reservasi tidak boleh kosong")
    @Column(name = "reservation_id", nullable = false, length = 36)
    private String reservationId;

    @Transient
    private Satpam satpam;

    @NotBlank(message = "ID satpam tidak boleh kosong")
    @Column(name = "satpam_id", nullable = false, length = 50)
    private String satpamId;

    @Column(name = "check_in_time")
    private LocalDateTime checkInTime;

    @Column(name = "check_out_time")
    private LocalDateTime checkOutTime;

    @Size(max = 255, message = "Catatan pelanggaran maksimal 255 karakter")
    @Column(name = "catatan_pelanggaran", length = 255)
    private String catatanPelanggaran;

    public AccessRecord() {
    }

    public AccessRecord(Reservation reservation, Satpam satpam) {
        setReservation(reservation);
        setSatpam(satpam);
    }

    public static AccessRecord restore(String recordId, String reservationId, String satpamId,
            LocalDateTime checkInTime, LocalDateTime checkOutTime, String catatanPelanggaran) {
        AccessRecord record = new AccessRecord();
        record.recordId = recordId;
        record.reservationId = reservationId;
        record.satpamId = satpamId;
        record.checkInTime = checkInTime;
        record.checkOutTime = checkOutTime;
        record.catatanPelanggaran = normalizeText(catatanPelanggaran);
        return record;
    }

    @PrePersist
    public void prePersist() {
        if (recordId == null || recordId.isBlank()) {
            recordId = UUID.randomUUID().toString();
        }
    }

    public void checkIn() {
        this.checkInTime = LocalDateTime.now();
    }

    public void checkOut() {
        this.checkOutTime = LocalDateTime.now();
    }

    public void laporkanKendala(String catatan) {
        if (catatan == null || catatan.isBlank()) {
            throw new IllegalArgumentException("Catatan kendala tidak boleh kosong");
        }
        this.catatanPelanggaran = normalizeText(catatan);
    }

    public void laporkanKendala() {
        laporkanKendala("Kendala tidak spesifik");
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
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

    public Satpam getSatpam() {
        return satpam;
    }

    public void setSatpam(Satpam satpam) {
        this.satpam = satpam;
        this.satpamId = satpam == null ? null : satpam.getId();
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
        this.catatanPelanggaran = normalizeText(catatanPelanggaran);
    }

    private static String normalizeText(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
