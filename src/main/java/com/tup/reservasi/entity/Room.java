package com.tup.reservasi.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * Penanggung jawab: Ali Abdul Fattah 'Alim Kautsar - 103112400213.
 * Modul: Room.
 */
@Entity
@Table(name = "rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long roomId;

    @Column(name = "nama_ruang", nullable = false, length = 100)
    private String namaRuang;

    @Column(name = "gedung", nullable = false, length = 100)
    private String gedung;

    @Column(name = "kapasitas", nullable = false)
    private int kapasitas;

    @Column(name = "status_aktif", nullable = false)
    private boolean statusAktif;

    @JsonIgnore
    @OneToMany(mappedBy = "room")
    private List<Reservation> reservations = new ArrayList<>();

    public void aktifkan() {
        this.statusAktif = true;
    }

    public void nonaktifkan() {
        this.statusAktif = false;
    }

    public void ubahStatusAktif(boolean statusAktif) {
        this.statusAktif = statusAktif;
    }

    public String getInfoRuang() {
        return namaRuang + " - " + gedung + " - kapasitas " + kapasitas;
    }
}
