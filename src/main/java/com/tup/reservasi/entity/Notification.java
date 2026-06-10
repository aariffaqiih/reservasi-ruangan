package com.tup.reservasi.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @Column(name = "notification_id", nullable = false, length = 50)
    private String notificationId;

    @Transient
    private Notifiable penerima;

    @Column(name = "penerima_id", nullable = false, length = 50)
    private String penerimaId;

    @Column(name = "penerima_nama", nullable = false, length = 100)
    private String penerimaNama;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "penerima_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User penerimaUser;

    @Transient
    private Reservation reservation;

    @Column(name = "reservation_id", length = 36)
    private String reservationId;

    @ManyToOne(targetEntity = Reservation.class)
    @JoinColumn(name = "reservation_id", referencedColumnName = "reservation_id", insertable = false, updatable = false)
    private Reservation reservationEntity;

    @NotBlank(message = "Pesan tidak boleh kosong")
    @Size(max = 255, message = "Pesan maksimal 255 karakter")
    @Column(name = "pesan", nullable = false, length = 255)
    private String pesan;

    @Column(name = "status_baca", nullable = false)
    private boolean statusBaca;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Notification() {
    }

    public Notification(Notifiable penerima, String pesan) {
        setPenerima(penerima);
        this.pesan = normalizeText(pesan);
        this.statusBaca = false;
        this.createdAt = LocalDateTime.now();
    }

    public Notification(Notifiable penerima, String pesan, Reservation reservation) {
        setPenerima(penerima);
        this.pesan = normalizeText(pesan);
        setReservation(reservation);
        this.statusBaca = false;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        if (notificationId == null || notificationId.isBlank()) {
            notificationId = UUID.randomUUID().toString();
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public void kirim() {
        Notifiable rec = getPenerima();
        if (rec != null) {
            rec.receiveNotification(this.pesan);
        }
    }

    public void tandaiDibaca() {
        this.statusBaca = true;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public Notifiable getPenerima() {
        if (penerima == null && penerimaUser instanceof Notifiable) {
            penerima = (Notifiable) penerimaUser;
        }
        return penerima;
    }

    public void setPenerima(Notifiable penerima) {
        this.penerima = penerima;
        if (penerima instanceof User user) {
            this.penerimaUser = user;
            this.penerimaId = user.getId();
            this.penerimaNama = user.getNama();
        } else {
            this.penerimaUser = null;
            this.penerimaId = null;
            this.penerimaNama = null;
        }
    }

    public String getPenerimaId() {
        return penerimaId;
    }

    public void setPenerimaId(String penerimaId) {
        this.penerimaId = penerimaId;
    }

    public String getPenerimaNama() {
        return penerimaNama;
    }

    public void setPenerimaNama(String penerimaNama) {
        this.penerimaNama = penerimaNama;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = normalizeText(pesan);
    }

    public boolean isStatusBaca() {
        return statusBaca;
    }

    public void setStatusBaca(boolean statusBaca) {
        this.statusBaca = statusBaca;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Reservation getReservation() {
        if (reservation == null) {
            reservation = reservationEntity;
        }
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
        if (reservation != null) {
            this.reservationEntity = reservation;
            this.reservationId = reservation.getReservationId();
        } else {
            this.reservationEntity = null;
            this.reservationId = null;
        }
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    private static String normalizeText(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
