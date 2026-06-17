package com.tup.reservasi.entity;

import java.time.LocalDateTime;

import com.tup.reservasi.enums.ReservationStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * Penanggung jawab: Tadzkiroh Aziziyah Haqia - 103112400242.
 * Modul: AccessRecord.
 */
@Entity
@Table(name = "access_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long recordId;

    @OneToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "satpam_id")
    private Satpam satpam;

    @Column(name = "check_in_time")
    private LocalDateTime checkInTime;

    @Column(name = "check_out_time")
    private LocalDateTime checkOutTime;

    @Column(name = "catatan_pelanggaran")
    private String catatanPelanggaran;

    @PrePersist
    public void prePersist() {
        if (checkInTime == null && reservation != null && satpam != null) {
            checkInTime = LocalDateTime.now();
        }
    }

    public void checkIn() {
        this.checkInTime = LocalDateTime.now();
        if (reservation != null) {
            reservation.ubahStatus(ReservationStatus.ACTIVE);
            reservation.setAccessRecord(this);
        }
    }

    public void checkOut() {
        this.checkOutTime = LocalDateTime.now();
        if (reservation != null) {
            reservation.ubahStatus(ReservationStatus.COMPLETED);
            reservation.setAccessRecord(this);
        }
    }

    public void laporkanKendala(String deskripsi) {
        this.catatanPelanggaran = deskripsi;
    }
}
