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
 * Penanggung jawab: Tadzkiroh Aziziyah Haqia - 103112400242.
 * Modul: Satpam.
 */
@Entity
@Table(name = "satpam")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Satpam extends User implements Notifiable {

    @Column(name = "pos_jaga", length = 100)
    private String posJaga;

    @Column(name = "shift", length = 50)
    private String shift;

    @JsonIgnore
    @OneToMany(mappedBy = "satpam")
    private List<AccessRecord> accessRecords = new ArrayList<>();

    public AccessRecord konfirmasiCheckIn(Reservation reservation) {
        AccessRecord record = new AccessRecord();
        record.setReservation(reservation);
        record.setSatpam(this);
        record.checkIn();
        this.accessRecords.add(record);
        return record;
    }

    public AccessRecord konfirmasiCheckOut(AccessRecord record) {
        record.checkOut();
        return record;
    }

    public void catatKendala(AccessRecord record, String deskripsi) {
        record.laporkanKendala(deskripsi);
    }

    @Override
    public void receiveNotification(Notification notification) {
        if (notification != null) {
            this.getNotifications().add(notification);
        }
    }
}
