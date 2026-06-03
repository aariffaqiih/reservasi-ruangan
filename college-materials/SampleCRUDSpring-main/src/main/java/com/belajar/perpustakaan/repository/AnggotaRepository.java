package com.belajar.perpustakaan.repository;

import com.belajar.perpustakaan.entity.Anggota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * ==============================================
 * REPOSITORY: AnggotaRepository
 * ==============================================
 * Interface ini extend JpaRepository yang sudah menyediakan
 * method CRUD siap pakai tanpa perlu implementasi manual:
 *   - save()        = INSERT atau UPDATE
 *   - findById()    = SELECT by ID
 *   - findAll()     = SELECT semua
 *   - deleteById()  = DELETE by ID
 *   - count()       = COUNT(*)
 *
 * Kita juga bisa tambah query custom dengan nama method:
 *   findByNamaContaining() → WHERE nama LIKE '%...%'
 */
@Repository
public interface AnggotaRepository extends JpaRepository<Anggota, Long> {

    // Query otomatis dari nama method:
    // SELECT * FROM anggota WHERE nama LIKE '%keyword%'
    List<Anggota> findByNamaContainingIgnoreCase(String nama);

    // SELECT * FROM anggota WHERE email = ?
    boolean existsByEmail(String email);
}
