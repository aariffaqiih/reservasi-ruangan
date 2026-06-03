package com.belajar.perpustakaan.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;

/**
 * ==============================================
 * ENTITY: Anggota
 * ==============================================
 * Merepresentasikan tabel "anggota" di database.
 *
 * RELASI:
 *   - One To One  → Profil (setiap anggota punya 1 profil)
 *   - One To Many → Peminjaman (1 anggota bisa meminjam banyak buku)
 *
 * Anotasi penting:
 *   @Entity    = kelas ini adalah tabel database
 *   @Table     = nama tabel di database
 *   @Id        = primary key
 *   @Column    = nama kolom di tabel
 */
@Entity
@Table(name = "anggota")
@Data                   // Lombok: generate getter, setter, toString
@NoArgsConstructor      // Lombok: generate constructor kosong
@AllArgsConstructor     // Lombok: generate constructor semua field
public class Anggota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
    private Long id;

    @NotBlank(message = "Nama tidak boleh kosong")
    @Size(min = 2, max = 100, message = "Nama harus 2-100 karakter")
    @Column(nullable = false)
    private String nama;

    @NotBlank(message = "Email tidak boleh kosong")
    @Email(message = "Format email tidak valid")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "No HP tidak boleh kosong")
    @Pattern(regexp = "^[0-9]{10,13}$", message = "No HP harus 10-13 digit angka")
    @Column(name = "no_hp", nullable = false)
    private String noHp;

    // -----------------------------------------------
    // RELASI ONE TO ONE: Anggota → Profil
    // Satu anggota memiliki tepat satu profil detail
    // cascade = jika anggota dihapus, profil juga ikut terhapus
    // -----------------------------------------------
    @OneToOne(mappedBy = "anggota", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Profil profil;

    // -----------------------------------------------
    // RELASI ONE TO MANY: Anggota → Peminjaman
    // Satu anggota bisa memiliki banyak peminjaman
    // mappedBy = nama field di class Peminjaman yang merujuk ke sini
    // -----------------------------------------------
    @OneToMany(mappedBy = "anggota", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Peminjaman> peminjaman;
}
