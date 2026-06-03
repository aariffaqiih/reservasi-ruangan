package com.belajar.perpustakaan.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

/**
 * ==============================================
 * ENTITY: Peminjaman
 * ==============================================
 * Mencatat transaksi peminjaman buku oleh anggota.
 *
 * RELASI:
 *   - Many To One ← Anggota (banyak peminjaman dari 1 anggota)
 *   - Many To One ← Buku    (banyak peminjaman untuk 1 buku)
 *
 * Tabel ini adalah "sisi many" dari relasi One To Many
 * yang ada di Anggota dan Buku.
 */
@Entity
@Table(name = "peminjaman")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Peminjaman {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // -----------------------------------------------
    // RELASI MANY TO ONE: Peminjaman → Anggota
    // @JoinColumn = di tabel peminjaman ada kolom "anggota_id" sebagai FK
    // -----------------------------------------------
    @ManyToOne
    @JoinColumn(name = "anggota_id", nullable = false)
    private Anggota anggota;

    // -----------------------------------------------
    // RELASI MANY TO ONE: Peminjaman → Buku
    // @JoinColumn = di tabel peminjaman ada kolom "buku_id" sebagai FK
    // -----------------------------------------------
    @ManyToOne
    @JoinColumn(name = "buku_id", nullable = false)
    private Buku buku;

    @NotNull(message = "Tanggal pinjam tidak boleh kosong")
    @Column(name = "tanggal_pinjam", nullable = false)
    private LocalDate tanggalPinjam;

    @Column(name = "tanggal_kembali")
    private LocalDate tanggalKembali; // null = belum dikembalikan

    @Column(name = "tanggal_harus_kembali", nullable = false)
    private LocalDate tanggalHarusKembali;

    // Status: "DIPINJAM", "DIKEMBALIKAN", "TERLAMBAT"
    @Column(nullable = false)
    private String status = "DIPINJAM";
}
