package com.tup.reservasi.auth;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/*
 * Penanggung jawab: Amelia Sofiana Makharomi.
 *
 * Arahan:
 * - File ini adalah starter login database yang sudah aktif.
 * - Jangan samakan langsung dengan entity domain User di class-diagram.
 * - Entity domain final User ada di package entity dan diarahkan memiliki:
 *   id: String, nama: String, email: String, noHp: String, passwordHash: String.
 * - Jika nanti tim menggabungkan starter login dengan User domain final,
 *   pastikan tetap ada data untuk autentikasi dan role:
 *   username, passwordHash, role.
 */
@Entity
@Table(name = "users")
public class LoginUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password_hash", nullable = false, length = 100)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role;

    protected LoginUser() {
    }

    public LoginUser(String username, String passwordHash, UserRole role) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public UserRole getRole() {
        return role;
    }
}
