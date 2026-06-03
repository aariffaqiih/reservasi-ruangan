package com.belajar.perpustakaan.controller.rest;

import com.belajar.perpustakaan.dto.ApiResponse;
import com.belajar.perpustakaan.dto.BukuDTO;
import com.belajar.perpustakaan.entity.Buku;
import com.belajar.perpustakaan.entity.Kategori;
import com.belajar.perpustakaan.service.BukuService;
import com.belajar.perpustakaan.service.KategoriService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * ==============================================
 * REST CONTROLLER: BukuRestController
 * ==============================================
 * Menyediakan API endpoint untuk resource Buku.
 *
 * Perbedaan @Controller vs @RestController:
 *   @Controller     → return nama template HTML (Thymeleaf)
 *   @RestController → return data JSON (otomatis serialisasi)
 *
 * @RestController = @Controller + @ResponseBody
 *
 * Base URL: /api/buku
 *
 * Endpoint yang tersedia:
 *   GET    /api/buku           → ambil semua buku
 *   GET    /api/buku/{id}      → ambil buku by ID
 *   GET    /api/buku/tersedia  → ambil buku yang stok > 0
 *   POST   /api/buku           → tambah buku baru
 *   PUT    /api/buku/{id}      → update buku
 *   DELETE /api/buku/{id}      → hapus buku
 *
 * Cara test: gunakan Postman, Insomnia, atau curl
 */
@RestController
@RequestMapping("/api/buku")
@RequiredArgsConstructor
public class BukuRestController {

    private final BukuService bukuService;
    private final KategoriService kategoriService;

    /**
     * GET /api/buku
     * Ambil semua buku.
     *
     * Response: 200 OK
     * {
     *   "success": true,
     *   "message": "...",
     *   "data": [ { id, judul, penulis, ... }, ... ]
     * }
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<BukuDTO>>> semuaBuku() {
        List<BukuDTO> bukuList = bukuService.semuaBuku()
                .stream()
                .map(BukuDTO::dari)
                .toList();

        return ResponseEntity.ok(
            ApiResponse.sukses("Berhasil mengambil data buku", bukuList)
        );
    }

    /**
     * GET /api/buku/{id}
     * Ambil detail satu buku berdasarkan ID.
     *
     * Response 200: buku ditemukan
     * Response 404: buku tidak ditemukan
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BukuDTO>> bukuById(@PathVariable Long id) {
        try {
            Buku buku = bukuService.cariById(id);
            return ResponseEntity.ok(
                ApiResponse.sukses("Buku ditemukan", BukuDTO.dari(buku))
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * GET /api/buku/tersedia
     * Ambil buku yang stoknya masih ada (stok > 0).
     */
    @GetMapping("/tersedia")
    public ResponseEntity<ApiResponse<List<BukuDTO>>> bukuTersedia() {
        List<BukuDTO> bukuList = bukuService.bukuTersedia()
                .stream()
                .map(BukuDTO::dari)
                .toList();

        return ResponseEntity.ok(
            ApiResponse.sukses("Buku tersedia: " + bukuList.size() + " judul", bukuList)
        );
    }

    /**
     * POST /api/buku
     * Tambah buku baru.
     *
     * Request Body (JSON):
     * {
     *   "judul": "Clean Code",
     *   "penulis": "Robert C. Martin",
     *   "isbn": "978-0-13-235088-4",
     *   "tahunTerbit": 2008,
     *   "stok": 5,
     *   "kategoriIds": [1, 2]
     * }
     *
     * Response 201: buku berhasil dibuat
     * Response 400: data tidak valid
     */
    @PostMapping
    public ResponseEntity<ApiResponse<BukuDTO>> tambahBuku(@RequestBody Map<String, Object> body) {
        try {
            Buku buku = new Buku();
            buku.setJudul((String) body.get("judul"));
            buku.setPenulis((String) body.get("penulis"));
            buku.setIsbn((String) body.get("isbn"));

            if (body.get("tahunTerbit") != null) {
                buku.setTahunTerbit((Integer) body.get("tahunTerbit"));
            }
            if (body.get("stok") != null) {
                buku.setStok((Integer) body.get("stok"));
            }

            // Set kategori jika ada (Many To Many)
            if (body.get("kategoriIds") != null) {
                List<Integer> ids = (List<Integer>) body.get("kategoriIds");
                List<Kategori> kategoriList = ids.stream()
                        .map(id -> kategoriService.cariById(Long.valueOf(id)))
                        .toList();
                buku.setKategoriList(kategoriList);
            }

            Buku tersimpan = bukuService.simpan(buku);

            // HTTP 201 Created = standar untuk resource baru berhasil dibuat
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.sukses("Buku berhasil ditambahkan", BukuDTO.dari(tersimpan)));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Gagal menambahkan buku: " + e.getMessage()));
        }
    }

    /**
     * PUT /api/buku/{id}
     * Update data buku yang sudah ada.
     *
     * Request Body (JSON) - kirim field yang ingin diupdate:
     * {
     *   "judul": "Judul Baru",
     *   "stok": 10
     * }
     *
     * Response 200: berhasil diupdate
     * Response 404: buku tidak ditemukan
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BukuDTO>> updateBuku(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body) {
        try {
            Buku buku = bukuService.cariById(id);

            // Update hanya field yang dikirim (partial update)
            if (body.containsKey("judul"))       buku.setJudul((String) body.get("judul"));
            if (body.containsKey("penulis"))     buku.setPenulis((String) body.get("penulis"));
            if (body.containsKey("isbn"))        buku.setIsbn((String) body.get("isbn"));
            if (body.containsKey("tahunTerbit")) buku.setTahunTerbit((Integer) body.get("tahunTerbit"));
            if (body.containsKey("stok"))        buku.setStok((Integer) body.get("stok"));

            Buku tersimpan = bukuService.simpan(buku);
            return ResponseEntity.ok(
                ApiResponse.sukses("Buku berhasil diperbarui", BukuDTO.dari(tersimpan))
            );

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * DELETE /api/buku/{id}
     * Hapus buku berdasarkan ID.
     *
     * Response 200: berhasil dihapus
     * Response 404: buku tidak ditemukan
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> hapusBuku(@PathVariable Long id) {
        try {
            bukuService.cariById(id); // validasi dulu apakah ada
            bukuService.hapus(id);
            return ResponseEntity.ok(ApiResponse.sukses("Buku berhasil dihapus"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
