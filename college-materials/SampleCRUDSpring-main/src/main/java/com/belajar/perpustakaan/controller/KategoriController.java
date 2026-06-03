package com.belajar.perpustakaan.controller;

import com.belajar.perpustakaan.entity.Kategori;
import com.belajar.perpustakaan.service.KategoriService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/kategori")
@RequiredArgsConstructor
public class KategoriController {

    private final KategoriService kategoriService;

    @GetMapping
    public String daftarKategori(Model model) {
        model.addAttribute("kategoriList", kategoriService.semuaKategori());
        model.addAttribute("kategori", new Kategori()); // untuk form inline
        model.addAttribute("pageTitle", "Kelola Kategori");
        return "kategori/list";
    }

    @PostMapping("/simpan")
    public String simpan(@Valid @ModelAttribute("kategori") Kategori kategori,
                          BindingResult result,
                          RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Nama kategori tidak boleh kosong!");
            return "redirect:/kategori";
        }
        kategoriService.simpan(kategori);
        redirectAttributes.addFlashAttribute("sukses", "Kategori berhasil disimpan!");
        return "redirect:/kategori";
    }

    @GetMapping("/hapus/{id}")
    public String hapus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            kategoriService.hapus(id);
            redirectAttributes.addFlashAttribute("sukses", "Kategori berhasil dihapus!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Gagal menghapus: " + e.getMessage());
        }
        return "redirect:/kategori";
    }
}
