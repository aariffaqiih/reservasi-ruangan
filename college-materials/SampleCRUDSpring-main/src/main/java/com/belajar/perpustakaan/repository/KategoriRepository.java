package com.belajar.perpustakaan.repository;

import com.belajar.perpustakaan.entity.Kategori;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KategoriRepository extends JpaRepository<Kategori, Long> {
    boolean existsByNama(String nama);
}
