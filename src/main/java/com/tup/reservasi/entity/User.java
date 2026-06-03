package com.tup.reservasi.entity;

/*
 * Penanggung jawab: Amelia Sofiana Makharomi.
 *
 * Arahan dari class-diagram:
 * - Jadikan User sebagai parent/abstract entity untuk Mahasiswa, Admin, dan Satpam.
 * - Atribut yang perlu disiapkan:
 *   id: String
 *   nama: String
 *   email: String
 *   noHp: String
 *   passwordHash: String
 * - Behaviour yang perlu dibuat:
 *   login(): boolean
 *   logout()
 *   ubahProfil()
 * - Catatan relasi:
 *   Mahasiswa extends User.
 *   Admin extends User.
 *   Satpam extends User.
 */

public abstract class User {

    private String id;
    private String nama;
    private String email;
    private String noHp;
    private String passwordHash;

    public User() {
    }
    public User(String id, String nama, String email, String noHp, String passwordHash) {
        this.id = id;
        this.nama = nama;
        this.email = email;
        this.noHp = noHp;
        this.passwordHash = passwordHash;
    }
    public boolean login() {
        return true;
    }
    public void logout() {
        System.out.println("User logout");
    }
    public void ubahProfil() {
        System.out.println("Profil berhasil diubah");
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
    public String getPasswordHash() {
        return passwordHash;
    }
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}