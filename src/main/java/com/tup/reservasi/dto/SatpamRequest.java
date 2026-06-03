package com.tup.reservasi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/*
 * Penanggung jawab: Tadzkiroh Aziziyah Haqia.
 *
 * Arahan DTO request:
 * - Data User umum untuk Satpam:
 *   nama: String
 *   email: String
 *   noHp: String
 * - Data khusus Satpam:
 *   shift: String
 *   posJaga: String
 * - Behaviour terkait yang akan memakai DTO ini:
 *   konfirmasiCheckIn(), konfirmasiCheckOut(), catatKendala().
 */
public class SatpamRequest {

    @NotBlank(message = "Nama satpam tidak boleh kosong")
    @Size(max = 100, message = "Nama satpam maksimal 100 karakter")
    private String nama;

    @NotBlank(message = "Email satpam tidak boleh kosong")
    @Email(message = "Format email satpam tidak valid")
    @Size(max = 100, message = "Email satpam maksimal 100 karakter")
    private String email;

    @NotBlank(message = "Nomor HP satpam tidak boleh kosong")
    @Size(max = 20, message = "Nomor HP satpam maksimal 20 karakter")
    private String noHp;

    @NotBlank(message = "Shift satpam tidak boleh kosong")
    @Size(max = 50, message = "Shift satpam maksimal 50 karakter")
    private String shift;

    @NotBlank(message = "Pos jaga satpam tidak boleh kosong")
    @Size(max = 100, message = "Pos jaga satpam maksimal 100 karakter")
    private String posJaga;

    public SatpamRequest() {
    }

    public SatpamRequest(String nama, String email, String noHp, String shift, String posJaga) {
        this.nama = nama;
        this.email = email;
        this.noHp = noHp;
        this.shift = shift;
        this.posJaga = posJaga;
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
