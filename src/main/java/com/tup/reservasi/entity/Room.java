package com.tup.reservasi.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/*
 * Penanggung jawab: Ali Abdul Fattah 'Alim Kautsar.
 *
 * Arahan dari class-diagram:
 * - Atribut yang perlu disiapkan:
 *   roomId: String
 *   namaRuang: String
 *   gedung: String
 *   kapasitas: int
 *   statusAktif: boolean
 * - Behaviour yang perlu dibuat:
 *   aktifkan()
 *   nonaktifkan()
 *   ubahStatusAktif()
 *   getInfoRuang(): String
 * - Catatan relasi:
 *   Satu Room dapat dipakai 0..* Reservation.
 *   Setiap Reservation memakai tepat 1 Room.
 */

@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @Column(name = "room_id", nullable = false, length = 50)
    private String roomId;

    @NotBlank(message = "Nama ruang tidak boleh kosong")
    @Column(name = "nama_ruang", nullable = false, length = 100)
    private String namaRuang;

    @NotBlank(message = "Gedung tidak boleh kosong")
    @Column(nullable = false, length = 100)
    private String gedung;

    @Min(value = 1, message = "Kapasitas minimal 1")
    @Column(nullable = false)
    private int kapasitas;

    @Column(name = "status_aktif", nullable = false)
    private boolean statusAktif;

    public Room() {
    }

    public Room(String roomId, String namaRuang, String gedung, int kapasitas, boolean statusAktif) {
        this.roomId = roomId;
        this.namaRuang = namaRuang;
        this.gedung = gedung;
        this.kapasitas = kapasitas;
        this.statusAktif = statusAktif;
    }

    @PrePersist
    public void prePersist() {
        if (roomId == null || roomId.isBlank()) {
            roomId = UUID.randomUUID().toString();
        }
    }

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
        return namaRuang + " - Gedung " + gedung + " (Kapasitas: " + kapasitas + " orang, Status: " + (statusAktif ? "Aktif" : "Nonaktif") + ")";
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getNamaRuang() {
        return namaRuang;
    }

    public void setNamaRuang(String namaRuang) {
        this.namaRuang = namaRuang;
    }

    public String getGedung() {
        return gedung;
    }

    public void setGedung(String gedung) {
        this.gedung = gedung;
    }

    public int getKapasitas() {
        return kapasitas;
    }

    public void setKapasitas(int kapasitas) {
        this.kapasitas = kapasitas;
    }

    public boolean isStatusAktif() {
        return statusAktif;
    }

    public void setStatusAktif(boolean statusAktif) {
        this.statusAktif = statusAktif;
    }
}
