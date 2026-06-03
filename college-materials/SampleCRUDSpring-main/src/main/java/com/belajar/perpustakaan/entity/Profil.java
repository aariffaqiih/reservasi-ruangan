package com.belajar.perpustakaan.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

/**
 * ==============================================
 * ENTITY: Profil
 * ==============================================
 * Menyimpan data detail anggota.
 * Dipisah dari Anggota untuk demonstrasi relasi ONE TO ONE.
 *
 * RELASI:
 *   - One To One ← Anggota (profil milik 1 anggota)
 *
 * Di tabel ini ada kolom "anggota_id" sebagai FOREIGN KEY
 * yang menunjuk ke tabel anggota → inilah yang disebut sisi "owning"
 */
@Entity
@Table(name = "profil")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Profil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Alamat tidak boleh kosong")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String alamat;

    @Column(name = "tanggal_lahir")
    private LocalDate tanggalLahir;

    @Column(name = "jenis_kelamin")
    private String jenisKelamin; // "L" atau "P"

    // -----------------------------------------------
    // RELASI ONE TO ONE: Profil → Anggota
    // @JoinColumn = kolom foreign key ada di tabel "profil"
    // Artinya di tabel profil ada kolom "anggota_id"
    // -----------------------------------------------
    @OneToOne
    @JoinColumn(name = "anggota_id", nullable = false)
    private Anggota anggota;
}
