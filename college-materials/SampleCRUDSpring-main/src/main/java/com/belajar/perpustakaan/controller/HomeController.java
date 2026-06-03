package com.belajar.perpustakaan.controller;

import com.belajar.perpustakaan.repository.AnggotaRepository;
import com.belajar.perpustakaan.repository.BukuRepository;
import com.belajar.perpustakaan.repository.PeminjamanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller untuk halaman utama / dashboard
 */
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final BukuRepository bukuRepository;
    private final AnggotaRepository anggotaRepository;
    private final PeminjamanRepository peminjamanRepository;

    @GetMapping("/")
    public String dashboard(Model model) {
        // Kirim data statistik ke view
        model.addAttribute("totalBuku", bukuRepository.count());
        model.addAttribute("totalAnggota", anggotaRepository.count());
        model.addAttribute("totalPeminjaman", peminjamanRepository.findByStatus("DIPINJAM").size());
        return "index"; // → templates/index.html
    }
}
