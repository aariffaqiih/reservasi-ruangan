package com.belajar.perpustakaan.dto;

import com.belajar.perpustakaan.entity.Buku;
import lombok.Data;
import java.util.List;

/**
 * ==============================================
 * DTO: BukuDTO
 * ==============================================
 * DTO (Data Transfer Object) digunakan untuk REST API
 * agar tidak terjadi infinite loop saat serialisasi JSON.
 *
 * Masalah tanpa DTO:
 *   Buku → kategoriList → Kategori → bukuList → Buku → ... (infinite loop!)
 *
 * Solusi dengan DTO:
 *   Kita pilih field mana saja yang ingin dikirim ke client.
 *
 * DTO juga berguna untuk:
 *   - Menyembunyikan field sensitif
 *   - Menggabungkan data dari beberapa entity
 *   - Mengontrol format data yang dikirim
 */
@Data
public class BukuDTO {

    private Long id;
    private String judul;
    private String penulis;
    private String isbn;
    private Integer tahunTerbit;
    private Integer stok;
    private List<String> kategoriList; // hanya nama kategori, bukan object penuh

    /**
     * Konversi dari Entity Buku ke BukuDTO.
     * Method static memudahkan pemanggilan: BukuDTO.dari(buku)
     */
    public static BukuDTO dari(Buku buku) {
        BukuDTO dto = new BukuDTO();
        dto.setId(buku.getId());
        dto.setJudul(buku.getJudul());
        dto.setPenulis(buku.getPenulis());
        dto.setIsbn(buku.getIsbn());
        dto.setTahunTerbit(buku.getTahunTerbit());
        dto.setStok(buku.getStok());

        // Ambil hanya nama kategori (hindari circular reference)
        if (buku.getKategoriList() != null) {
            dto.setKategoriList(
                buku.getKategoriList().stream()
                    .map(k -> k.getNama())
                    .toList()
            );
        }
        return dto;
    }
}
