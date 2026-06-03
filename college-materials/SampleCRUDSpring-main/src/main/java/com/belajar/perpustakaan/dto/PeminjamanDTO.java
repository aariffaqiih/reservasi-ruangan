package com.belajar.perpustakaan.dto;

import com.belajar.perpustakaan.entity.Peminjaman;
import lombok.Data;

/**
 * DTO untuk Peminjaman.
 * Menghindari circular: Peminjaman → Anggota → Peminjaman → ...
 * Hanya kirim nama anggota dan judul buku, bukan object penuh.
 */
@Data
public class PeminjamanDTO {

    private Long id;

    // Dari relasi Many To One → Anggota
    private Long anggotaId;
    private String namaAnggota;

    // Dari relasi Many To One → Buku
    private Long bukuId;
    private String judulBuku;

    private String tanggalPinjam;
    private String tanggalHarusKembali;
    private String tanggalKembali;
    private String status;

    public static PeminjamanDTO dari(Peminjaman p) {
        PeminjamanDTO dto = new PeminjamanDTO();
        dto.setId(p.getId());

        dto.setAnggotaId(p.getAnggota().getId());
        dto.setNamaAnggota(p.getAnggota().getNama());

        dto.setBukuId(p.getBuku().getId());
        dto.setJudulBuku(p.getBuku().getJudul());

        dto.setTanggalPinjam(p.getTanggalPinjam().toString());
        dto.setTanggalHarusKembali(p.getTanggalHarusKembali().toString());
        dto.setTanggalKembali(
            p.getTanggalKembali() != null ? p.getTanggalKembali().toString() : null
        );
        dto.setStatus(p.getStatus());

        return dto;
    }
}
