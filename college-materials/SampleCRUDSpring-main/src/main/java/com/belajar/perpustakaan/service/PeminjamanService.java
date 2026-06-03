package com.belajar.perpustakaan.service;

import com.belajar.perpustakaan.entity.Peminjaman;
import com.belajar.perpustakaan.repository.PeminjamanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PeminjamanService {

    private final PeminjamanRepository peminjamanRepository;
    private final BukuService bukuService;

    public List<Peminjaman> semuaPeminjaman() {
        return peminjamanRepository.findAll();
    }

    public Peminjaman cariById(Long id) {
        return peminjamanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Peminjaman tidak ditemukan"));
    }

    /**
     * Buat peminjaman baru.
     * Otomatis set: tanggal pinjam = hari ini, harus kembali = 7 hari lagi
     */
    public Peminjaman pinjam(Peminjaman peminjaman) {
        peminjaman.setTanggalPinjam(LocalDate.now());
        peminjaman.setTanggalHarusKembali(LocalDate.now().plusDays(7));
        peminjaman.setStatus("DIPINJAM");

        // Kurangi stok buku
        bukuService.kurangiStok(peminjaman.getBuku().getId());

        return peminjamanRepository.save(peminjaman);
    }

    /**
     * Proses pengembalian buku.
     * Cek apakah terlambat atau tepat waktu.
     */
    public Peminjaman kembalikan(Long peminjamanId) {
        Peminjaman peminjaman = cariById(peminjamanId);

        peminjaman.setTanggalKembali(LocalDate.now());

        // Cek keterlambatan
        if (LocalDate.now().isAfter(peminjaman.getTanggalHarusKembali())) {
            peminjaman.setStatus("TERLAMBAT");
        } else {
            peminjaman.setStatus("DIKEMBALIKAN");
        }

        // Kembalikan stok buku
        bukuService.tambahStok(peminjaman.getBuku().getId());

        return peminjamanRepository.save(peminjaman);
    }

    public List<Peminjaman> peminjamanAktif() {
        return peminjamanRepository.findByStatus("DIPINJAM");
    }

    public void hapus(Long id) {
        peminjamanRepository.deleteById(id);
    }
}
