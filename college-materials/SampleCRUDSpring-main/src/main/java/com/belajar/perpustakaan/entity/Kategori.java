package com.belajar.perpustakaan.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;

/**
 * ==============================================
 * ENTITY: Kategori
 * ==============================================
 * Menyimpan kategori/genre buku.
 *
 * RELASI:
 *   - Many To Many ↔ Buku
 *   Satu kategori bisa memiliki banyak buku,
 *   dan satu buku bisa masuk banyak kategori.
 *
 *   Contoh: Buku "Harry Potter" bisa masuk
 *   kategori "Fiksi" DAN "Petualangan"
 */
@Entity
@Table(name = "kategori")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Kategori {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nama kategori tidak boleh kosong")
    @Column(nullable = false, unique = true)
    private String nama;

    @Column(columnDefinition = "TEXT")
    private String deskripsi;

    // -----------------------------------------------
    // RELASI MANY TO MANY: Kategori ↔ Buku
    // mappedBy = nama field di class Buku yang mendefinisikan relasi ini
    // Artinya tabel pivot dibuat dari sisi Buku
    // -----------------------------------------------
    @ManyToMany(mappedBy = "kategoriList")
    private List<Buku> bukuList;
}
