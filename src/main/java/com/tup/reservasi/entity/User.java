package com.tup.reservasi.entity;

import java.util.UUID;

import com.tup.reservasi.auth.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

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

@Entity
@Table(name = "domain_users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "domain_role", discriminatorType = DiscriminatorType.STRING)
public abstract class User {

    @Id
    @Column(nullable = false, length = 50)
    private String id;

    @NotBlank(message = "Nama tidak boleh kosong")
    @Size(max = 100, message = "Nama maksimal 100 karakter")
    @Column(nullable = false, length = 100)
    private String nama;

    @NotBlank(message = "Email tidak boleh kosong")
    @Email(message = "Format email tidak valid")
    @Size(max = 100, message = "Email maksimal 100 karakter")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Size(max = 20, message = "Nomor HP maksimal 20 karakter")
    @Column(name = "no_hp", unique = true, length = 20)
    private String noHp;

    @Column(name = "password_hash", length = 100)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private UserRole role;

    public User() {
    }
    public User(String id, String nama, String email, String noHp, String passwordHash) {
        this.id = id;
        this.nama = nama;
        this.email = email;
        this.noHp = noHp;
        this.passwordHash = passwordHash;
    }

    @PrePersist
    public void prePersist() {
        if (id == null || id.isBlank()) {
            id = UUID.randomUUID().toString();
        }
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
    public UserRole getRole() {
        return role;
    }
    public void setRole(UserRole role) {
        this.role = role;
    }
}
