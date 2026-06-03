package com.belajar.perpustakaan.controller;

import com.belajar.perpustakaan.entity.Buku;
import com.belajar.perpustakaan.service.BukuService;
import com.belajar.perpustakaan.service.KategoriService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

/**
 * ==============================================
 * CONTROLLER: BukuController
 * ==============================================
 * Menangani semua request HTTP yang berkaitan dengan Buku.
 *
 * Pola URL:
 *   GET  /buku                      → tampilkan semua buku
 *   GET  /buku?keyword=java         → cari buku by judul
 *   GET  /buku?keyword=java&cari=penulis → cari buku by penulis
 *   GET  /buku/tambah               → form tambah buku
 *   POST /buku/simpan               → proses simpan
 *   GET  /buku/edit/{id}            → form edit buku
 *   POST /buku/update               → proses update
 *   GET  /buku/hapus/{id}           → hapus buku
 */
@Controller
@RequestMapping("/buku")
@RequiredArgsConstructor
public class BukuController {

    private final BukuService bukuService;
    private final KategoriService kategoriService;

    /**
     * GET /buku
     * GET /buku?keyword=clean              → cari judul mengandung "clean"
     * GET /buku?keyword=martin&cari=penulis → cari penulis mengandung "martin"
     *
     * @RequestParam(required = false) = parameter URL opsional.
     *   Jika URL: /buku           → keyword = null → tampilkan semua
     *   Jika URL: /buku?keyword=x → keyword = "x" → tampilkan hasil pencarian
     *
     * defaultValue = nilai default jika parameter tidak ada di URL
     */
    @GetMapping
    public String daftarBuku(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "judul") String cari,
            Model model) {

        List<Buku> bukuList;

        if (keyword != null && !keyword.isBlank()) {
            // Ada keyword → lakukan pencarian sesuai filter
            if ("penulis".equals(cari)) {
                bukuList = bukuService.cariByPenulis(keyword);
            } else {
                // default: cari by judul
                bukuList = bukuService.cariByJudul(keyword);
            }
            // Kirim kembali ke view agar form pencarian tetap terisi
            model.addAttribute("keyword", keyword);
            model.addAttribute("cari", cari);
            model.addAttribute("infoHasil",
                    "Ditemukan " + bukuList.size() + " buku untuk pencarian \"" + keyword + "\"");
        } else {
            // Tidak ada keyword → tampilkan semua buku
            bukuList = bukuService.semuaBuku();
        }

        model.addAttribute("bukuList", bukuList);
        model.addAttribute("pageTitle", "Daftar Buku");
        return "buku/list";
    }

    /**
     * GET /buku/tambah
     * Tampilkan form untuk menambah buku baru.
     */
    @GetMapping("/tambah")
    public String formTambah(Model model) {
        model.addAttribute("buku", new Buku());
        model.addAttribute("kategoriList", kategoriService.semuaKategori());
        model.addAttribute("pageTitle", "Tambah Buku");
        return "buku/form";
    }

    /**
     * POST /buku/simpan
     * Proses data dari form tambah buku.
     */
    @PostMapping("/simpan")
    public String simpanBuku(@Valid @ModelAttribute("buku") Buku buku,
                             BindingResult result,
                             @RequestParam(value = "kategoriIds", required = false) List<Long> kategoriIds,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("kategoriList", kategoriService.semuaKategori());
            model.addAttribute("pageTitle", "Tambah Buku");
            return "buku/form";
        }

        if (kategoriIds != null && !kategoriIds.isEmpty()) {
            buku.setKategoriList(kategoriIds.stream()
                    .map(kategoriService::cariById)
                    .toList());
        }

        bukuService.simpan(buku);
        redirectAttributes.addFlashAttribute("sukses", "Buku berhasil ditambahkan!");
        return "redirect:/buku";
    }

    /**
     * GET /buku/edit/{id}
     */
    @GetMapping("/edit/{id}")
    public String formEdit(@PathVariable Long id, Model model) {
        model.addAttribute("buku", bukuService.cariById(id));
        model.addAttribute("kategoriList", kategoriService.semuaKategori());
        model.addAttribute("pageTitle", "Edit Buku");
        return "buku/form";
    }

    /**
     * POST /buku/update
     */
    @PostMapping("/update")
    public String updateBuku(@Valid @ModelAttribute("buku") Buku buku,
                             BindingResult result,
                             @RequestParam(value = "kategoriIds", required = false) List<Long> kategoriIds,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("kategoriList", kategoriService.semuaKategori());
            model.addAttribute("pageTitle", "Edit Buku");
            return "buku/form";
        }

        if (kategoriIds != null && !kategoriIds.isEmpty()) {
            buku.setKategoriList(kategoriIds.stream()
                    .map(kategoriService::cariById)
                    .toList());
        }

        bukuService.simpan(buku);
        redirectAttributes.addFlashAttribute("sukses", "Buku berhasil diperbarui!");
        return "redirect:/buku";
    }

    /**
     * GET /buku/hapus/{id}
     */
    @GetMapping("/hapus/{id}")
    public String hapusBuku(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bukuService.hapus(id);
            redirectAttributes.addFlashAttribute("sukses", "Buku berhasil dihapus!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Gagal menghapus buku: " + e.getMessage());
        }
        return "redirect:/buku";
    }
}