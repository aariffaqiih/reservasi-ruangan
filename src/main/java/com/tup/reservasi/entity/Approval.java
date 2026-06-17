package com.tup.reservasi.entity;

import java.time.LocalDateTime;

import com.tup.reservasi.enums.ApprovalDecision;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * Penanggung jawab: Aarif Rahmaan Jalaluddin Faqiih - 103112430182.
 * Modul: Approval.
 */
@Entity
@Table(name = "approvals")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Approval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "approval_id")
    private Long approvalId;

    @OneToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @Enumerated(EnumType.STRING)
    @Column(name = "keputusan")
    private ApprovalDecision keputusan;

    @Column(name = "catatan")
    private String catatan;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @PrePersist
    public void prePersist() {
        if (reviewedAt == null) {
            reviewedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        reviewedAt = LocalDateTime.now();
    }

    public void setujui(String catatan) {
        this.keputusan = ApprovalDecision.SETUJUI;
        this.catatan = catatan;
        this.reviewedAt = LocalDateTime.now();
        if (reservation != null) {
            reservation.ubahStatus(ReservationStatus.APPROVED);
            reservation.setApproval(this);
        }
    }

    public void tolak(String catatan) {
        this.keputusan = ApprovalDecision.TOLAK;
        this.catatan = catatan;
        this.reviewedAt = LocalDateTime.now();
        if (reservation != null) {
            reservation.ubahStatus(ReservationStatus.REJECTED);
            reservation.setApproval(this);
        }
    }

    public void mintaRevisi(String catatan) {
        this.keputusan = ApprovalDecision.REVISI;
        this.catatan = catatan;
        this.reviewedAt = LocalDateTime.now();
        if (reservation != null) {
            reservation.ubahStatus(ReservationStatus.PENDING);
            reservation.setApproval(this);
        }
    }
}
