package com.belajar.perpustakaan.repository;

import com.belajar.perpustakaan.entity.Buku;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository untuk Buku.
 * Contoh penggunaan @Query untuk query kustom dengan JPQL.
 */
@Repository
public interface BukuRepository extends JpaRepository<Buku, Long> {

    // Cari buku berdasarkan judul (case-insensitive)
    List<Buku> findByJudulContainingIgnoreCase(String judul);

    // Cari buku berdasarkan penulis
    List<Buku> findByPenulisContainingIgnoreCase(String penulis);

    // Contoh JPQL Query: ambil buku yang stoknya > 0
    @Query("SELECT b FROM Buku b WHERE b.stok > 0")
    List<Buku> findBukuTersedia();

    // Cek apakah ISBN sudah ada
    boolean existsByIsbn(String isbn);
}
