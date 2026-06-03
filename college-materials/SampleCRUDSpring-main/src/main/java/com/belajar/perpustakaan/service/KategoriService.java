package com.belajar.perpustakaan.service;

import com.belajar.perpustakaan.entity.Kategori;
import com.belajar.perpustakaan.repository.KategoriRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KategoriService {

    private final KategoriRepository kategoriRepository;

    public List<Kategori> semuaKategori() {
        return kategoriRepository.findAll();
    }

    public Kategori cariById(Long id) {
        return kategoriRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kategori tidak ditemukan"));
    }

    public Kategori simpan(Kategori kategori) {
        return kategoriRepository.save(kategori);
    }

    public void hapus(Long id) {
        kategoriRepository.deleteById(id);
    }
}
