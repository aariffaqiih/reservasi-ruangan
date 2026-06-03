package com.belajar.perpustakaan.controller.rest;

import com.belajar.perpustakaan.dto.ApiResponse;
import com.belajar.perpustakaan.entity.Kategori;
import com.belajar.perpustakaan.service.KategoriService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller untuk Kategori.
 * Base URL: /api/kategori
 */
@RestController
@RequestMapping("/api/kategori")
@RequiredArgsConstructor
public class KategoriRestController {

    private final KategoriService kategoriService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Kategori>>> semuaKategori() {
        // Untuk Kategori kita bisa langsung return entity
        // karena tidak ada circular reference yang berbahaya
        // (bukuList pakai @JsonIgnore di bawah - atau kita return fields pilihan)
        List<Kategori> list = kategoriService.semuaKategori();
        // Hindari circular: set bukuList ke null sebelum return
        list.forEach(k -> k.setBukuList(null));
        return ResponseEntity.ok(ApiResponse.sukses("Data kategori", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Kategori>> kategoriById(@PathVariable Long id) {
        try {
            Kategori kat = kategoriService.cariById(id);
            kat.setBukuList(null); // hindari circular reference
            return ResponseEntity.ok(ApiResponse.sukses("Kategori ditemukan", kat));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * POST /api/kategori
     * Request Body:
     * {
     *   "nama": "Fiksi",
     *   "deskripsi": "Buku fiksi dan novel"
     * }
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Kategori>> tambahKategori(@RequestBody Map<String, String> body) {
        try {
            Kategori kategori = new Kategori();
            kategori.setNama(body.get("nama"));
            kategori.setDeskripsi(body.get("deskripsi"));

            Kategori tersimpan = kategoriService.simpan(kategori);
            tersimpan.setBukuList(null);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.sukses("Kategori berhasil ditambahkan", tersimpan));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Gagal: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> hapusKategori(@PathVariable Long id) {
        try {
            kategoriService.cariById(id);
            kategoriService.hapus(id);
            return ResponseEntity.ok(ApiResponse.sukses("Kategori berhasil dihapus"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
