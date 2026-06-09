package com.tup.reservasi.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tup.reservasi.dto.AccessIssueRequest;
import com.tup.reservasi.dto.AccessRecordResponse;
import com.tup.reservasi.service.AccessControlService;

/*
 * Penanggung jawab: Tadzkiroh Aziziyah Haqia.
 *
 * Arahan halaman dari class-diagram:
 * - Controller ini nanti menghubungkan halaman Satpam dengan behaviour:
 *   konfirmasiCheckIn(): AccessRecord
 *   konfirmasiCheckOut(): AccessRecord
 *   catatKendala()
 *   receiveNotification()
 * - Data yang perlu dikirim ke view:
 *   profil Satpam: shift, posJaga.
 *   daftar Reservation yang dapat check-in/check-out.
 *   daftar AccessRecord yang ditangani Satpam.
 *   daftar Notification untuk Satpam.
 * - File ini tetap komentar sampai fitur satpam mulai dikerjakan.
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
        return "satpam/dashboard";
    }

    @GetMapping("/akses")
    public String aksesRuangan(Model model, Authentication authentication) {
        String satpamId = authentication.getName();
        model.addAttribute("records", accessControlService.getRecordsBySatpam(satpamId)
                .stream()
                .map(AccessRecordResponse::from)
                .toList());
        model.addAttribute("belumCheckOut", accessControlService.getBelumCheckOut()
                .stream()
                .map(AccessRecordResponse::from)
                .toList());
        return "access/list";
    }

    @PostMapping("/akses/{reservationId}/check-in")
    public String konfirmasiCheckIn(
            @PathVariable String reservationId,
            Authentication authentication) {
        accessControlService.checkIn(reservationId, authentication.getName());
        return "redirect:/satpam/akses";
    }

    @PostMapping("/akses/{reservationId}/check-out")
    public String konfirmasiCheckOut(
            @PathVariable String reservationId,
            Authentication authentication) {
        accessControlService.checkOut(reservationId, authentication.getName());
        return "redirect:/satpam/akses";
    }

    @PostMapping("/akses/catat-kendala")
    public String catatKendala(
            @RequestParam String reservationId,
            @RequestParam String catatanPelanggaran,
            Authentication authentication) {
        AccessIssueRequest request = new AccessIssueRequest(
                reservationId,
                authentication.getName(),
                catatanPelanggaran);
        accessControlService.reportIssue(request);
        return "redirect:/satpam/akses";
    }
}
