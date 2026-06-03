package com.tup.reservasi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/*
 * Penanggung jawab: Amelia Sofiana Makharomi.
 *
 * Arahan DTO request:
 * - Data User umum:
 *   nama: String
 *   email: String
 *   noHp: String
 *   password: String
 * - Data role yang perlu dipilih/disepakati:
 *   role: String atau enum role final.
 * - Data tambahan sesuai role:
 *   Mahasiswa: nim, prodi, angkatan.
 *   Admin: unitKerja.
 *   Satpam: shift, posJaga.
 */

public class RegistrationRequest {

    @NotBlank(message = "Nama tidak boleh kosong")
    @Size(max = 100, message = "Nama maksimal 100 karakter")
    private String nama;

    @NotBlank(message = "Email tidak boleh kosong")
    @Email(message = "Format email tidak valid")
    @Size(max = 100, message = "Email maksimal 100 karakter")
    private String email;

    @Size(max = 20, message = "Nomor HP maksimal 20 karakter")
    private String noHp;

    @NotBlank(message = "Password tidak boleh kosong")
    @Size(min = 3, max = 100, message = "Password harus 3-100 karakter")
    private String password;

    @NotBlank(message = "Role tidak boleh kosong")
    private String role;

    @Size(max = 30, message = "NIM maksimal 30 karakter")
    // Mahasiswa
    private String nim;

    @Size(max = 100, message = "Prodi maksimal 100 karakter")
    private String prodi;

    @Min(value = 2000, message = "Angkatan tidak valid")
    private Integer angkatan;

    @Size(max = 100, message = "Unit kerja maksimal 100 karakter")
    // Admin
    private String unitKerja;

    @Size(max = 50, message = "Shift maksimal 50 karakter")
    // Satpam
    private String shift;

    @Size(max = 100, message = "Pos jaga maksimal 100 karakter")
    private String posJaga;

    public RegistrationRequest() {
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
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
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
    public Integer getAngkatan() {
        return angkatan;
    }
    public void setAngkatan(Integer angkatan) {
        this.angkatan = angkatan;
    }
    public String getUnitKerja() {
        return unitKerja;
    }
    public void setUnitKerja(String unitKerja) {
        this.unitKerja = unitKerja;
    }
    public String getShift() {
        return shift;
    }
    public void setShift(String shift) {
        this.shift = shift;
    }
    public String getPosJaga() {
        return posJaga;
    }
    public void setPosJaga(String posJaga) {
        this.posJaga = posJaga;
    }
}
