package com.tup.reservasi.dto;

/*
 * Penanggung jawab: Amelia Sofiana Makharomi.
 *
 * Arahan DTO request:
 * - Data profil User yang boleh diubah:
 *   nama: String
 *   email: String
 *   noHp: String
 * - Data tambahan bila role Mahasiswa:
 *   prodi: String
 *   angkatan: int
 * - Jangan gabungkan perubahan passwordHash di DTO ini kecuali flow final memang mengatur.
 */

public class ProfileUpdateRequest {

    private String nama;
    private String email;
    private String noHp;

    // Khusus Mahasiswa
    private String prodi;
    private Integer angkatan;

    public ProfileUpdateRequest() {
    }
    public String getNama() {
        return nama;
    }
    public void setNama(String nama) {
        this.nama = nama;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getNoHp() {
        return noHp;
    }
    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }
    public String getProdi() {
        return prodi;
    }
    public void setProdi(String prodi) {
        this.prodi = prodi;
    }
    public Integer getAngkatan() {
        return angkatan;
    }
    public void setAngkatan(Integer angkatan) {
        this.angkatan = angkatan;
    }
}