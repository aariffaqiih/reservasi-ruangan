package com.belajar.perpustakaan.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;

/**
 * ==============================================
 * ENTITY: Buku
 * ==============================================
 * Menyimpan data buku di perpustakaan.
 *
 * RELASI:
 *   - Many To Many ↔ Kategori (satu buku bisa punya banyak kategori)
 *   - One To Many  → Peminjaman (satu buku bisa dipinjam berkali-kali)
 */
@Entity
@Table(name = "buku")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Buku {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Judul tidak boleh kosong")
    @Column(nullable = false)
    private String judul;

    @NotBlank(message = "Penulis tidak boleh kosong")
    @Column(nullable = false)
    private String penulis;

    @NotBlank(message = "ISBN tidak boleh kosong")
    @Column(nullable = false, unique = true)
    private String isbn;

    @Min(value = 1900, message = "Tahun terbit minimal 1900")
    @Column(name = "tahun_terbit")
    private Integer tahunTerbit;

    @Min(value = 0, message = "Stok tidak boleh negatif")
    @Column(nullable = false)
    private Integer stok = 0;

    // -----------------------------------------------
    // RELASI MANY TO MANY: Buku ↔ Kategori
    //
    // @JoinTable = mendefinisikan tabel pivot (tabel perantara)
    // Tabel pivot bernama "buku_kategori" dengan:
    //   - joinColumns      = kolom yang menunjuk ke tabel BUKU (buku_id)
    //   - inverseJoinColumns = kolom yang menunjuk ke tabel KATEGORI (kategori_id)
    //
    // Di database akan ada tabel "buku_kategori" berisi:
    //   | buku_id | kategori_id |
    //   |---------|-------------|
    //   |    1    |      1      |  → Buku 1 masuk Kategori 1
    //   |    1    |      2      |  → Buku 1 juga masuk Kategori 2
    // -----------------------------------------------
    @ManyToMany
    @JoinTable(
        name = "buku_kategori",
        joinColumns = @JoinColumn(name = "buku_id"),
        inverseJoinColumns = @JoinColumn(name = "kategori_id")
    )
    private List<Kategori> kategoriList;

    // -----------------------------------------------
    // RELASI ONE TO MANY: Buku → Peminjaman
    // Satu buku bisa ada di banyak record peminjaman
    // -----------------------------------------------
    @OneToMany(mappedBy = "buku", cascade = CascadeType.ALL)
    private List<Peminjaman> peminjaman;
}
