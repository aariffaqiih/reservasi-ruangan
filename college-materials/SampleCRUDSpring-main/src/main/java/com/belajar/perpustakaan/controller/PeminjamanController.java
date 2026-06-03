package com.belajar.perpustakaan.controller;

import com.belajar.perpustakaan.entity.Peminjaman;
import com.belajar.perpustakaan.service.AnggotaService;
import com.belajar.perpustakaan.service.BukuService;
import com.belajar.perpustakaan.service.PeminjamanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/peminjaman")
@RequiredArgsConstructor
public class PeminjamanController {

    private final PeminjamanService peminjamanService;
    private final AnggotaService anggotaService;
    private final BukuService bukuService;

    @GetMapping
    public String daftarPeminjaman(Model model) {
        model.addAttribute("peminjamanList", peminjamanService.semuaPeminjaman());
        model.addAttribute("pageTitle", "Daftar Peminjaman");
        return "peminjaman/list";
    }

    @GetMapping("/tambah")
    public String formTambah(Model model) {
        model.addAttribute("peminjaman", new Peminjaman());
        model.addAttribute("anggotaList", anggotaService.semuaAnggota());
        model.addAttribute("bukuList", bukuService.bukuTersedia()); // hanya buku yang stoknya ada
        model.addAttribute("pageTitle", "Form Peminjaman");
        return "peminjaman/form";
    }

    @PostMapping("/simpan")
    public String simpanPeminjaman(@RequestParam Long anggotaId,
                                    @RequestParam Long bukuId,
                                    RedirectAttributes redirectAttributes) {
        try {
            Peminjaman peminjaman = new Peminjaman();
            peminjaman.setAnggota(anggotaService.cariById(anggotaId));
            peminjaman.setBuku(bukuService.cariById(bukuId));

            peminjamanService.pinjam(peminjaman);
            redirectAttributes.addFlashAttribute("sukses", "Peminjaman berhasil dicatat!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/peminjaman";
    }

    /**
     * Proses pengembalian buku.
     * Mahasiswa bisa pelajari cara update status tanpa form penuh.
     */
    @GetMapping("/kembalikan/{id}")
    public String kembalikanBuku(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Peminjaman p = peminjamanService.kembalikan(id);
            String pesan = p.getStatus().equals("TERLAMBAT")
                    ? "Buku dikembalikan tapi TERLAMBAT!"
                    : "Buku berhasil dikembalikan tepat waktu!";
            redirectAttributes.addFlashAttribute("sukses", pesan);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/peminjaman";
    }

    @GetMapping("/hapus/{id}")
    public String hapusPeminjaman(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        peminjamanService.hapus(id);
        redirectAttributes.addFlashAttribute("sukses", "Data peminjaman dihapus!");
        return "redirect:/peminjaman";
    }
}
