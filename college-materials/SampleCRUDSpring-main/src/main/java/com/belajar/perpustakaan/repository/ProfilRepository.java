package com.belajar.perpustakaan.repository;

import com.belajar.perpustakaan.entity.Profil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProfilRepository extends JpaRepository<Profil, Long> {
    // Cari profil berdasarkan ID anggota
    Optional<Profil> findByAnggotaId(Long anggotaId);
}
