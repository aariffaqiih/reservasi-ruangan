package com.belajar.perpustakaan.controller;

import com.belajar.perpustakaan.entity.Anggota;
import com.belajar.perpustakaan.entity.Profil;
import com.belajar.perpustakaan.service.AnggotaService;
import com.belajar.perpustakaan.repository.ProfilRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;

/**
 * ==============================================
 * CONTROLLER: AnggotaController
 * ==============================================
 * Form anggota menggabungkan dua object (Anggota + Profil) dalam
 * satu halaman, sehingga lebih mudah menggunakan @RequestParam
 * daripada @ModelAttribute agar tidak konflik dengan BindingResult.
 */
@Controller
@RequestMapping("/anggota")
@RequiredArgsConstructor
public class AnggotaController {

    private final AnggotaService anggotaService;
    private final ProfilRepository profilRepository;

    @GetMapping
    public String daftarAnggota(Model model) {
        model.addAttribute("anggotaList", anggotaService.semuaAnggota());
        model.addAttribute("pageTitle", "Daftar Anggota");
        return "anggota/list";
    }

    @GetMapping("/tambah")
    public String formTambah(Model model) {
        model.addAttribute("anggota", new Anggota());
        model.addAttribute("profil", new Profil());
        model.addAttribute("pageTitle", "Tambah Anggota");
        return "anggota/form";
    }

    @PostMapping("/simpan")
    public String simpanAnggota(@RequestParam String nama,
                                @RequestParam String email,
                                @RequestParam String noHp,
                                @RequestParam(required = false) String profilAlamat,
                                @RequestParam(required = false) String profilTanggalLahir,
                                @RequestParam(required = false) String profilJenisKelamin,
                                RedirectAttributes redirectAttributes) {

        // Validasi dasar
        if (nama.isBlank() || email.isBlank() || noHp.isBlank()) {
            redirectAttributes.addFlashAttribute("error", "Nama, email, dan No HP wajib diisi!");
            return "redirect:/anggota/tambah";
        }

        // Simpan data Anggota
        Anggota anggota = new Anggota();
        anggota.setNama(nama);
        anggota.setEmail(email);
        anggota.setNoHp(noHp);
        Anggota anggotaSimpan = anggotaService.simpan(anggota);

        // Simpan Profil jika alamat diisi (One To One)
        if (profilAlamat != null && !profilAlamat.isBlank()) {
            Profil profil = new Profil();
            profil.setAlamat(profilAlamat);
            profil.setJenisKelamin(profilJenisKelamin);
            if (profilTanggalLahir != null && !profilTanggalLahir.isBlank()) {
                profil.setTanggalLahir(LocalDate.parse(profilTanggalLahir));
            }
            profil.setAnggota(anggotaSimpan);
            profilRepository.save(profil);
        }

        redirectAttributes.addFlashAttribute("sukses", "Anggota berhasil didaftarkan!");
        return "redirect:/anggota";
    }

    @GetMapping("/detail/{id}")
    public String detailAnggota(@PathVariable Long id, Model model) {
        Anggota anggota = anggotaService.cariById(id);
        model.addAttribute("anggota", anggota);
        model.addAttribute("pageTitle", "Detail Anggota");
        return "anggota/detail";
    }

    @GetMapping("/edit/{id}")
    public String formEdit(@PathVariable Long id, Model model) {
        Anggota anggota = anggotaService.cariById(id);
        model.addAttribute("anggota", anggota);
        model.addAttribute("profil", anggota.getProfil() != null ? anggota.getProfil() : new Profil());
        model.addAttribute("pageTitle", "Edit Anggota");
        return "anggota/form";
    }

    @PostMapping("/update")
    public String updateAnggota(@RequestParam Long id,
                                @RequestParam String nama,
                                @RequestParam String email,
                                @RequestParam String noHp,
                                @RequestParam(required = false) Long profilId,
                                @RequestParam(required = false) String profilAlamat,
                                @RequestParam(required = false) String profilTanggalLahir,
                                @RequestParam(required = false) String profilJenisKelamin,
                                RedirectAttributes redirectAttributes) {

        // Update data Anggota
        Anggota anggota = anggotaService.cariById(id);
        anggota.setNama(nama);
        anggota.setEmail(email);
        anggota.setNoHp(noHp);
        anggotaService.simpan(anggota);

        // Update atau buat Profil
        if (profilAlamat != null && !profilAlamat.isBlank()) {
            Profil profil = profilId != null
                    ? profilRepository.findById(profilId).orElse(new Profil())
                    : new Profil();
            profil.setAlamat(profilAlamat);
            profil.setJenisKelamin(profilJenisKelamin);
            if (profilTanggalLahir != null && !profilTanggalLahir.isBlank()) {
                profil.setTanggalLahir(LocalDate.parse(profilTanggalLahir));
            }
            profil.setAnggota(anggota);
            profilRepository.save(profil);
        }

        redirectAttributes.addFlashAttribute("sukses", "Data anggota berhasil diperbarui!");
        return "redirect:/anggota";
    }

    @GetMapping("/hapus/{id}")
    public String hapusAnggota(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            anggotaService.hapus(id);
            redirectAttributes.addFlashAttribute("sukses", "Anggota berhasil dihapus!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Gagal menghapus: " + e.getMessage());
        }
        return "redirect:/anggota";
    }
}