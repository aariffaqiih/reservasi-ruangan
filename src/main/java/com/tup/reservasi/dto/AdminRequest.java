package com.tup.reservasi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/*
 * Penanggung jawab: Atha Muyassar.
 *
 * Arahan DTO request:
 * - Data User umum untuk Admin:
 *   nama: String
 *   email: String
 *   noHp: String
 * - Data khusus Admin:
 *   unitKerja: String
 * - Behaviour terkait yang akan memakai DTO ini:
 *   verifikasiReservasi(), setujuiReservasi(), tolakReservasi(), mintaRevisiData().
 */

public class AdminRequest {

    @NotBlank(message = "Nama admin tidak boleh kosong")
    @Size(max = 100, message = "Nama admin maksimal 100 karakter")
    private String nama;

    @NotBlank(message = "Email admin tidak boleh kosong")
    @Email(message = "Format email admin tidak valid")
    @Size(max = 100, message = "Email admin maksimal 100 karakter")
    private String email;

    @NotBlank(message = "Nomor HP admin tidak boleh kosong")
    @Size(max = 20, message = "Nomor HP admin maksimal 20 karakter")
    private String noHp;

    @NotBlank(message = "Unit kerja admin tidak boleh kosong")
    @Size(max = 100, message = "Unit kerja admin maksimal 100 karakter")
    private String unitKerja;

    public AdminRequest() {
    }

    public AdminRequest(String nama, String email, String noHp, String unitKerja) {
        this.nama = nama;
        this.email = email;
        this.noHp = noHp;
        this.unitKerja = unitKerja;
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

    public String getUnitKerja() {
        return unitKerja;
    }

    public void setUnitKerja(String unitKerja) {
        this.unitKerja = unitKerja;
    }
}
