package com.belajar.perpustakaan.controller.rest;

import com.belajar.perpustakaan.dto.ApiResponse;
import com.belajar.perpustakaan.dto.PeminjamanDTO;
import com.belajar.perpustakaan.entity.Peminjaman;
import com.belajar.perpustakaan.service.AnggotaService;
import com.belajar.perpustakaan.service.BukuService;
import com.belajar.perpustakaan.service.PeminjamanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * ==============================================
 * REST CONTROLLER: PeminjamanRestController
 * ==============================================
 * Base URL: /api/peminjaman
 *
 * Endpoint:
 *   GET    /api/peminjaman              → semua transaksi
 *   GET    /api/peminjaman/aktif        → yang masih dipinjam
 *   GET    /api/peminjaman/{id}         → detail satu transaksi
 *   POST   /api/peminjaman              → buat peminjaman baru
 *   PATCH  /api/peminjaman/{id}/kembalikan → proses pengembalian
 *   DELETE /api/peminjaman/{id}         → hapus record
 *
 * Catatan PATCH vs PUT:
 *   PUT   = replace seluruh resource (kirim semua field)
 *   PATCH = update sebagian resource (kirim field tertentu saja)
 *   → Pengembalian hanya mengubah status & tgl_kembali, jadi pakai PATCH
 */
@RestController
@RequestMapping("/api/peminjaman")
@RequiredArgsConstructor
public class PeminjamanRestController {

    private final PeminjamanService peminjamanService;
    private final AnggotaService anggotaService;
    private final BukuService bukuService;

    /**
     * GET /api/peminjaman
     * Ambil semua data peminjaman.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<PeminjamanDTO>>> semuaPeminjaman() {
        List<PeminjamanDTO> list = peminjamanService.semuaPeminjaman()
                .stream()
                .map(PeminjamanDTO::dari)
                .toList();
        return ResponseEntity.ok(ApiResponse.sukses("Data peminjaman", list));
    }

    /**
     * GET /api/peminjaman/aktif
     * Hanya tampilkan peminjaman yang statusnya DIPINJAM.
     */
    @GetMapping("/aktif")
    public ResponseEntity<ApiResponse<List<PeminjamanDTO>>> peminjamanAktif() {
        List<PeminjamanDTO> list = peminjamanService.peminjamanAktif()
                .stream()
                .map(PeminjamanDTO::dari)
                .toList();
        return ResponseEntity.ok(
            ApiResponse.sukses("Peminjaman aktif: " + list.size() + " transaksi", list)
        );
    }

    /**
     * GET /api/peminjaman/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PeminjamanDTO>> peminjamanById(@PathVariable Long id) {
        try {
            Peminjaman p = peminjamanService.cariById(id);
            return ResponseEntity.ok(ApiResponse.sukses("Data ditemukan", PeminjamanDTO.dari(p)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * POST /api/peminjaman
     * Buat transaksi peminjaman baru.
     *
     * Request Body:
     * {
     *   "anggotaId": 1,
     *   "bukuId": 3
     * }
     *
     * Otomatis:
     *   - tanggal_pinjam = hari ini
     *   - tanggal_harus_kembali = 7 hari ke depan
     *   - status = "DIPINJAM"
     *   - stok buku berkurang 1
     */
    @PostMapping
    public ResponseEntity<ApiResponse<PeminjamanDTO>> pinjam(@RequestBody Map<String, Object> body) {
        try {
            Long anggotaId = Long.valueOf(body.get("anggotaId").toString());
            Long bukuId    = Long.valueOf(body.get("bukuId").toString());

            Peminjaman peminjaman = new Peminjaman();
            peminjaman.setAnggota(anggotaService.cariById(anggotaId));
            peminjaman.setBuku(bukuService.cariById(bukuId));

            Peminjaman tersimpan = peminjamanService.pinjam(peminjaman);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.sukses("Peminjaman berhasil dicatat", PeminjamanDTO.dari(tersimpan)));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * PATCH /api/peminjaman/{id}/kembalikan
     * Proses pengembalian buku.
     *
     * Tidak perlu Request Body — cukup ID peminjaman di URL.
     * Service akan otomatis:
     *   - Set tanggal_kembali = hari ini
     *   - Cek apakah terlambat
     *   - Set status = "DIKEMBALIKAN" atau "TERLAMBAT"
     *   - Tambah stok buku +1
     */
    @PatchMapping("/{id}/kembalikan")
    public ResponseEntity<ApiResponse<PeminjamanDTO>> kembalikan(@PathVariable Long id) {
        try {
            Peminjaman p = peminjamanService.kembalikan(id);

            String pesan = p.getStatus().equals("TERLAMBAT")
                    ? "Buku dikembalikan tapi TERLAMBAT dari jadwal!"
                    : "Buku berhasil dikembalikan tepat waktu";

            return ResponseEntity.ok(ApiResponse.sukses(pesan, PeminjamanDTO.dari(p)));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * DELETE /api/peminjaman/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> hapus(@PathVariable Long id) {
        try {
            peminjamanService.cariById(id);
            peminjamanService.hapus(id);
            return ResponseEntity.ok(ApiResponse.sukses("Data peminjaman dihapus"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
