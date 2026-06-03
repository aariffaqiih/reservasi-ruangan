package com.belajar.perpustakaan.dto;

import com.belajar.perpustakaan.entity.Anggota;
import lombok.Data;

/**
 * DTO untuk Anggota - menghindari circular reference dengan Peminjaman.
 * Menyertakan data profil langsung (flatten One To One).
 */
@Data
public class AnggotaDTO {

    private Long id;
    private String nama;
    private String email;
    private String noHp;

    // Data dari Profil (One To One) - di-flatten langsung ke DTO
    private String alamat;
    private String tanggalLahir;
    private String jenisKelamin;

    // Statistik
    private int totalPeminjaman;

    public static AnggotaDTO dari(Anggota anggota) {
        AnggotaDTO dto = new AnggotaDTO();
        dto.setId(anggota.getId());
        dto.setNama(anggota.getNama());
        dto.setEmail(anggota.getEmail());
        dto.setNoHp(anggota.getNoHp());

        // Ambil data profil jika ada (One To One)
        if (anggota.getProfil() != null) {
            dto.setAlamat(anggota.getProfil().getAlamat());
            dto.setJenisKelamin(anggota.getProfil().getJenisKelamin());
            if (anggota.getProfil().getTanggalLahir() != null) {
                dto.setTanggalLahir(anggota.getProfil().getTanggalLahir().toString());
            }
        }

        // Hitung total peminjaman (One To Many)
        if (anggota.getPeminjaman() != null) {
            dto.setTotalPeminjaman(anggota.getPeminjaman().size());
        }

        return dto;
    }
}
