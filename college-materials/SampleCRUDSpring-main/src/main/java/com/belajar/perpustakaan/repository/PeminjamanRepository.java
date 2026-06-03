package com.belajar.perpustakaan.repository;

import com.belajar.perpustakaan.entity.Peminjaman;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PeminjamanRepository extends JpaRepository<Peminjaman, Long> {

    // Cari semua peminjaman milik anggota tertentu
    List<Peminjaman> findByAnggotaId(Long anggotaId);

    // Cari semua peminjaman yang masih aktif
    List<Peminjaman> findByStatus(String status);

    // Hitung berapa kali sebuah buku dipinjam
    long countByBukuId(Long bukuId);
}
