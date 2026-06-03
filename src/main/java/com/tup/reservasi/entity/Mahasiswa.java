package com.tup.reservasi.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import com.tup.reservasi.auth.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;

/*
 * Penanggung jawab: Amelia Sofiana Makharomi.
 *
 * Arahan dari class-diagram:
 * - Mahasiswa harus extends User.
 * - Atribut khusus Mahasiswa:
 *   nim: String
 *   prodi: String
 *   angkatan: int
 * - Behaviour yang perlu dibuat:
 *   ajukanReservasi(): Reservation
 *   batalkanReservasi(): boolean
 *   lihatStatusReservasi(): List<Reservation>
 *   receiveNotification()
 * - Catatan relasi:
 *   Satu Mahasiswa dapat memiliki 0..* Reservation.
 *   Mahasiswa juga menerima Notification melalui kontrak Notifiable jika dipakai final.
 */

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("MAHASISWA")
public class Mahasiswa extends User implements Notifiable {

    @Column(unique = true, length = 30)
    private String nim;

    @Column(length = 100)
    private String prodi;

    @Column
    private int angkatan;

    @Transient
    private int jumlahNotifikasiDiterima;

    @Transient
    private String notifikasiTerakhir;

    public Mahasiswa() {
        super();
        setRole(UserRole.MAHASISWA);
    }
    public Mahasiswa(
            String id,
            String nama,
            String email,
            String noHp,
            String passwordHash,
            String nim,
            String prodi,
            int angkatan) {

        super(id, nama, email, noHp, passwordHash);

        this.nim = nim;
        this.prodi = prodi;
        this.angkatan = angkatan;
        setRole(UserRole.MAHASISWA);
    }
    public Reservation ajukanReservasi(Room room, LocalDate tanggal, LocalTime jamMulai, LocalTime jamSelesai, String tujuan) {
        return new Reservation(this, room, tanggal, jamMulai, jamSelesai, tujuan);
    }
    public Reservation ajukanReservasi() {
        return null;
    }
    public boolean batalkanReservasi() {
        return true;
    }
    public List<Reservation> lihatStatusReservasi() {
        return new ArrayList<>();
    }
    @Override
    public void receiveNotification(String pesan) {
        jumlahNotifikasiDiterima++;
        notifikasiTerakhir = pesan == null || pesan.trim().isEmpty() ? null : pesan.trim();
    }
    public void receiveNotification() {
        receiveNotification(null);
    }
    public String getNim() {
        return nim;
    }
    public void setNim(String nim) {
        this.nim = nim;
    }
    public String getProdi() {
        return prodi;
    }
    public void setProdi(String prodi) {
        this.prodi = prodi;
    }
    public int getAngkatan() {
        return angkatan;
    }
    public void setAngkatan(int angkatan) {
        this.angkatan = angkatan;
    }
    public int getJumlahNotifikasiDiterima() {
        return jumlahNotifikasiDiterima;
    }
    public String getNotifikasiTerakhir() {
        return notifikasiTerakhir;
    }
}
