package com.tup.reservasi.dto;

/*
 * Penanggung jawab: Amelia Sofiana Makharomi.
 *
 * Arahan DTO response:
 * - Data dari User yang boleh dikirim:
 *   id: String
 *   nama: String
 *   email: String
 *   noHp: String
 * - Data turunan yang mungkin ikut dikirim:
 *   Mahasiswa: nim, prodi, angkatan.
 *   Admin: unitKerja.
 *   Satpam: shift, posJaga.
 * - Jangan pernah kirim passwordHash.
 */

public class UserResponse {

    private String id;
    private String nama;
    private String email;
    private String noHp;

    // Mahasiswa
    private String nim;
    private String prodi;
    private Integer angkatan;

    // Admin
    private String unitKerja;

    // Satpam
    private String shift;
    private String posJaga;

    public UserResponse() {
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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