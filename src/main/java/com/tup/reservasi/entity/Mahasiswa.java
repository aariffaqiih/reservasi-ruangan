package com.tup.reservasi.entity;

import java.time.LocalDate;
import java.time.LocalTime;
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
 * Penanggung jawab: Amelia Sofiana Makharomi - 103112400233.
 * Modul: Mahasiswa.
 */
@Entity
@Table(name = "mahasiswa")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mahasiswa extends User implements Notifiable {

    @Column(name = "nim", unique = true, length = 30)
    private String nim;

    @Column(name = "prodi", length = 100)
    private String prodi;

    @Column(name = "angkatan")
    private int angkatan;

    @JsonIgnore
    @OneToMany(mappedBy = "mahasiswa")
    private List<Reservation> reservations = new ArrayList<>();

    public Reservation ajukanReservasi(Room room, LocalDate tanggal, LocalTime jamMulai,
            LocalTime jamSelesai, String tujuan) {
        Reservation reservation = new Reservation();
        reservation.setMahasiswa(this);
        reservation.setRoom(room);
        reservation.setTanggal(tanggal);
        reservation.setJamMulai(jamMulai);
        reservation.setJamSelesai(jamSelesai);
        reservation.setTujuan(tujuan);
        reservation.ajukan();
        this.reservations.add(reservation);
        return reservation;
    }

    public boolean batalkanReservasi(Long reservationId) {
        for (Reservation reservation : reservations) {
            if (reservation.getReservationId() != null
                    && reservation.getReservationId().equals(reservationId)
                    && reservation.isCanBeCancelled()) {
                reservation.batalkan("Dibatalkan mahasiswa");
                return true;
            }
        }
        return false;
    }

    public List<Reservation> lihatStatusReservasi() {
        return reservations;
    }

    @Override
    public void receiveNotification(Notification notification) {
        if (notification != null) {
            this.getNotifications().add(notification);
        }
    }
}
