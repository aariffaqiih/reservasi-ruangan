package com.belajar.perpustakaan.service;

import com.belajar.perpustakaan.entity.Anggota;
import com.belajar.perpustakaan.repository.AnggotaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * ==============================================
 * SERVICE: AnggotaService
 * ==============================================
 * Layer Service adalah tempat logika bisnis berada.
 * Controller memanggil Service, bukan langsung Repository.
 *
 * Alur: Controller → Service → Repository → Database
 *
 * @Service = menandai class ini sebagai Spring Bean (komponen)
 * @RequiredArgsConstructor (Lombok) = buat constructor untuk field final
 *   sehingga Spring bisa inject dependency secara otomatis (Dependency Injection)
 */
@Service
@RequiredArgsConstructor
public class AnggotaService {

    // Dependency Injection: Spring otomatis inject AnggotaRepository
    private final AnggotaRepository anggotaRepository;

    /**
     * Ambil semua anggota dari database.
     */
    public List<Anggota> semuaAnggota() {
        return anggotaRepository.findAll();
    }

    /**
     * Cari anggota berdasarkan ID.
     * Jika tidak ditemukan, lempar exception.
     */
    public Anggota cariById(Long id) {
        return anggotaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Anggota dengan ID " + id + " tidak ditemukan"));
    }

    /**
     * Simpan anggota baru atau update anggota yang sudah ada.
     * JPA otomatis tahu: jika id null → INSERT, jika id ada → UPDATE
     */
    public Anggota simpan(Anggota anggota) {
        return anggotaRepository.save(anggota);
    }

    /**
     * Hapus anggota berdasarkan ID.
     */
    public void hapus(Long id) {
        anggotaRepository.deleteById(id);
    }

    /**
     * Cari anggota berdasarkan nama (untuk fitur pencarian).
     */
    public List<Anggota> cariByNama(String nama) {
        return anggotaRepository.findByNamaContainingIgnoreCase(nama);
    }

    /**
     * Cek apakah email sudah dipakai.
     */
    public boolean emailSudahAda(String email) {
        return anggotaRepository.existsByEmail(email);
    }
}
