package com.tup.reservasi.entity;

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

public class Mahasiswa extends User {

    private String nim;
    private String prodi;
    private int angkatan;

    public Mahasiswa() {
        super();
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
    }
    public Object ajukanReservasi() {
        return null;
    }
    public boolean batalkanReservasi() {
        return true;
    }
    public List<Object> lihatStatusReservasi() {
        return new ArrayList<>();
    }
    public void receiveNotification() {
        System.out.println("Notifikasi diterima");
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
}