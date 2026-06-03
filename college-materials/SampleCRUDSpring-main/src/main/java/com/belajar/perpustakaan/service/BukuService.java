package com.belajar.perpustakaan.service;

import com.belajar.perpustakaan.entity.Buku;
import com.belajar.perpustakaan.repository.BukuRepository;
import com.belajar.perpustakaan.repository.KategoriRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BukuService {

    private final BukuRepository bukuRepository;
    private final KategoriRepository kategoriRepository;

    public List<Buku> semuaBuku() {
        return bukuRepository.findAll();
    }

    public Buku cariById(Long id) {
        return bukuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Buku dengan ID " + id + " tidak ditemukan"));
    }

    public Buku simpan(Buku buku) {
        return bukuRepository.save(buku);
    }

    public void hapus(Long id) {
        bukuRepository.deleteById(id);
    }

    public List<Buku> cariByPenulis(String penulis) {
        return bukuRepository.findByPenulisContainingIgnoreCase(penulis);
    }

    public List<Buku> cariByJudul(String judul) {
        return bukuRepository.findByJudulContainingIgnoreCase(judul);
    }

    public List<Buku> bukuTersedia() {
        return bukuRepository.findBukuTersedia();
    }

    /**
     * Kurangi stok buku saat dipinjam.
     * Validasi: stok tidak boleh jadi negatif.
     */
    public void kurangiStok(Long bukuId) {
        Buku buku = cariById(bukuId);
        if (buku.getStok() <= 0) {
            throw new RuntimeException("Stok buku '" + buku.getJudul() + "' sudah habis!");
        }
        buku.setStok(buku.getStok() - 1);
        bukuRepository.save(buku);
    }

    /**
     * Tambah stok buku saat dikembalikan.
     */
    public void tambahStok(Long bukuId) {
        Buku buku = cariById(bukuId);
        buku.setStok(buku.getStok() + 1);
        bukuRepository.save(buku);
    }
}