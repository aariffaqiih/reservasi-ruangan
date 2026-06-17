package com.tup.reservasi.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * Penanggung jawab: Ajda Mutiara Zahra - 103112400210.
 * Modul: Notification.
 */
@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long notificationId;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "penerima_id")
    private Notifiable penerima;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Column(name = "pesan")
    private String pesan;

    @Column(name = "status_baca")
    private boolean statusBaca;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public void kirim() {
        this.createdAt = LocalDateTime.now();
        this.statusBaca = false;
        if (penerima != null) {
            penerima.receiveNotification(this);
        }
    }

    public void tandaiDibaca() {
        this.statusBaca = true;
    }

    public Long getPenerimaId() {
        if (penerima instanceof User user) {
            return user.getId();
        }
        return null;
    }
}
