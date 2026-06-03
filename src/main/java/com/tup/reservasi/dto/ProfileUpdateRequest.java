package com.tup.reservasi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

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

    @NotBlank(message = "Nama tidak boleh kosong")
    @Size(max = 100, message = "Nama maksimal 100 karakter")
    private String nama;

    @NotBlank(message = "Email tidak boleh kosong")
    @Email(message = "Format email tidak valid")
    @Size(max = 100, message = "Email maksimal 100 karakter")
    private String email;

    @Size(max = 20, message = "Nomor HP maksimal 20 karakter")
    private String noHp;

    @Size(max = 100, message = "Prodi maksimal 100 karakter")
    // Khusus Mahasiswa
    private String prodi;

    @Min(value = 2000, message = "Angkatan tidak valid")
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
