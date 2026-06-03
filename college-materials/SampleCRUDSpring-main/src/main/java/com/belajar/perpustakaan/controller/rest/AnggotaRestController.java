package com.belajar.perpustakaan.controller.rest;

import com.belajar.perpustakaan.dto.AnggotaDTO;
import com.belajar.perpustakaan.dto.ApiResponse;
import com.belajar.perpustakaan.entity.Anggota;
import com.belajar.perpustakaan.service.AnggotaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * ==============================================
 * REST CONTROLLER: AnggotaRestController
 * ==============================================
 * Base URL: /api/anggota
 *
 * Endpoint:
 *   GET    /api/anggota       → semua anggota
 *   GET    /api/anggota/{id}  → detail anggota
 *   POST   /api/anggota       → tambah anggota
 *   PUT    /api/anggota/{id}  → update anggota
 *   DELETE /api/anggota/{id}  → hapus anggota
 */
@RestController
@RequestMapping("/api/anggota")
@RequiredArgsConstructor
public class AnggotaRestController {

    private final AnggotaService anggotaService;

    /**
     * GET /api/anggota
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<AnggotaDTO>>> semuaAnggota() {
        List<AnggotaDTO> list = anggotaService.semuaAnggota()
                .stream()
                .map(AnggotaDTO::dari)
                .toList();
        return ResponseEntity.ok(ApiResponse.sukses("Berhasil mengambil data anggota", list));
    }

    /**
     * GET /api/anggota/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AnggotaDTO>> anggotaById(@PathVariable Long id) {
        try {
            Anggota anggota = anggotaService.cariById(id);
            return ResponseEntity.ok(ApiResponse.sukses("Anggota ditemukan", AnggotaDTO.dari(anggota)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * POST /api/anggota
     * Request Body:
     * {
     *   "nama": "Budi Santoso",
     *   "email": "budi@email.com",
     *   "noHp": "081234567890"
     * }
     */
    @PostMapping
    public ResponseEntity<ApiResponse<AnggotaDTO>> tambahAnggota(@RequestBody Map<String, String> body) {
        try {
            String nama  = body.get("nama");
            String email = body.get("email");
            String noHp  = body.get("noHp");

            if (nama == null || nama.isBlank() || email == null || email.isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("Nama dan email wajib diisi"));
            }

            if (anggotaService.emailSudahAda(email)) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error("Email '" + email + "' sudah terdaftar"));
            }

            Anggota anggota = new Anggota();
            anggota.setNama(nama);
            anggota.setEmail(email);
            anggota.setNoHp(noHp);

            Anggota tersimpan = anggotaService.simpan(anggota);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.sukses("Anggota berhasil didaftarkan", AnggotaDTO.dari(tersimpan)));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Gagal mendaftarkan anggota: " + e.getMessage()));
        }
    }

    /**
     * PUT /api/anggota/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AnggotaDTO>> updateAnggota(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        try {
            Anggota anggota = anggotaService.cariById(id);
            if (body.containsKey("nama"))  anggota.setNama(body.get("nama"));
            if (body.containsKey("email")) anggota.setEmail(body.get("email"));
            if (body.containsKey("noHp"))  anggota.setNoHp(body.get("noHp"));

            Anggota tersimpan = anggotaService.simpan(anggota);
            return ResponseEntity.ok(ApiResponse.sukses("Anggota berhasil diperbarui", AnggotaDTO.dari(tersimpan)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * DELETE /api/anggota/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> hapusAnggota(@PathVariable Long id) {
        try {
            anggotaService.cariById(id);
            anggotaService.hapus(id);
            return ResponseEntity.ok(ApiResponse.sukses("Anggota berhasil dihapus"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
