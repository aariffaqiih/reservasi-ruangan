package com.tup.reservasi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tup.reservasi.service.AccessControlService;

/*
 * Penanggung jawab: Tadzkiroh Aziziyah Haqia - 103112400242.
 * Modul: Satpam dan AccessControlService.
 */
@Controller
@RequestMapping("/satpam")
public class SatpamController {

    private final AccessControlService accessControlService;

    public SatpamController(AccessControlService accessControlService) {
        this.accessControlService = accessControlService;
    }

    @GetMapping
    public String satpamHome() {
        return "redirect:/satpam/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("records", this.accessControlService.getAllRecords());
        model.addAttribute("belumCheckOut", this.accessControlService.getBelumCheckOut());
        return "satpam/dashboard";
    }

    @GetMapping("/akses")
    public String aksesRuangan(Model model) {
        model.addAttribute("records", this.accessControlService.getAllRecords());
        model.addAttribute("belumCheckOut", this.accessControlService.getBelumCheckOut());
        return "access/list";
    }

    @PostMapping("/akses/check-in")
    public String konfirmasiCheckIn(@RequestParam Long satpamId,
            @RequestParam Long reservationId,
            RedirectAttributes redirectAttrs) {
        try {
            this.accessControlService.checkIn(satpamId, reservationId);
            redirectAttrs.addFlashAttribute("sukses", "Check-in berhasil");
        } catch (RuntimeException e) {
            redirectAttrs.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/satpam/akses";
    }

    @PostMapping("/akses/{recordId}/check-out")
    public String konfirmasiCheckOut(@PathVariable Long recordId,
            @RequestParam Long satpamId,
            RedirectAttributes redirectAttrs) {
        try {
            this.accessControlService.checkOut(satpamId, recordId);
            redirectAttrs.addFlashAttribute("sukses", "Check-out berhasil");
        } catch (RuntimeException e) {
            redirectAttrs.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/satpam/akses";
    }

    @PostMapping("/akses/{recordId}/kendala")
    public String catatKendala(@PathVariable Long recordId,
            @RequestParam Long satpamId,
            @RequestParam String catatanPelanggaran,
            RedirectAttributes redirectAttrs) {
        try {
            this.accessControlService.reportIssue(satpamId, recordId, catatanPelanggaran);
            redirectAttrs.addFlashAttribute("sukses", "Kendala berhasil dicatat");
        } catch (RuntimeException e) {
            redirectAttrs.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/satpam/akses";
    }
}
