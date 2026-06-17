package com.tup.reservasi.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * Penanggung jawab: Atha Muyassar - 103112430185.
 * Modul: Admin.
 */
@Entity
@Table(name = "admins")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin extends User implements Notifiable {

    @Column(name = "unit_kerja", length = 100)
    private String unitKerja;

    @JsonIgnore
    @OneToMany(mappedBy = "admin")
    private List<Approval> approvals = new ArrayList<>();

    public boolean verifikasiReservasi(Reservation reservation) {
        return reservation != null && reservation.validasiWaktu();
    }

    public Approval setujuiReservasi(Reservation reservation, String catatan) {
        Approval approval = new Approval();
        approval.setReservation(reservation);
        approval.setAdmin(this);
        approval.setujui(catatan);
        this.approvals.add(approval);
        return approval;
    }

    public Approval tolakReservasi(Reservation reservation, String catatan) {
        Approval approval = new Approval();
        approval.setReservation(reservation);
        approval.setAdmin(this);
        approval.tolak(catatan);
        this.approvals.add(approval);
        return approval;
    }

    public Approval mintaRevisiData(Reservation reservation, String catatan) {
        Approval approval = new Approval();
        approval.setReservation(reservation);
        approval.setAdmin(this);
        approval.mintaRevisi(catatan);
        this.approvals.add(approval);
        return approval;
    }

    @Override
    public void receiveNotification(Notification notification) {
        if (notification != null) {
            this.getNotifications().add(notification);
        }
    }
}
