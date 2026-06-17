package com.tup.reservasi.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.tup.reservasi.enums.ReservationStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * Penanggung jawab: Aarif Rahmaan Jalaluddin Faqiih - 103112430182.
 * Modul: Reservation.
 */
@Entity
@Table(name = "reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long reservationId;

    @ManyToOne
    @JoinColumn(name = "mahasiswa_id")
    private Mahasiswa mahasiswa;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(name = "tanggal")
    private LocalDate tanggal;

    @Column(name = "jam_mulai")
    private LocalTime jamMulai;

    @Column(name = "jam_selesai")
    private LocalTime jamSelesai;

    @Column(name = "tujuan")
    private String tujuan;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ReservationStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @OneToOne(mappedBy = "reservation")
    @JsonIgnore
    private Approval approval;

    @OneToOne(mappedBy = "reservation")
    @JsonIgnore
    private AccessRecord accessRecord;

    @JsonIgnore
    @OneToMany(mappedBy = "reservation")
    private List<Notification> notifications = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public void ajukan() {
        this.status = ReservationStatus.PENDING;
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public void ubahStatus(ReservationStatus statusBaru) {
        this.status = statusBaru;
    }

    public void batalkan(String alasan) {
        this.status = ReservationStatus.CANCELLED;
        this.cancelledAt = LocalDateTime.now();
    }

    public boolean validasiWaktu() {
        return tanggal != null
                && jamMulai != null
                && jamSelesai != null
                && jamSelesai.isAfter(jamMulai);
    }

    public boolean isCanBeCancelled() {
        return status == ReservationStatus.DRAFT
                || status == ReservationStatus.PENDING
                || status == ReservationStatus.APPROVED;
    }
}
