package com.tup.reservasi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tup.reservasi.service.ApprovalService;

/*
 * Penanggung jawab: Atha Muyassar - 103112430185.
 * Modul: Admin.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ApprovalService approvalService;

    public AdminController(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    @GetMapping
    public String adminHome() {
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("pendingReservations", this.approvalService.getPendingReservations());
        model.addAttribute("approvals", this.approvalService.getAllApprovals());
        return "admin/dashboard";
    }

    @GetMapping("/approvals")
    public String approvalList(Model model) {
        model.addAttribute("pendingReservations", this.approvalService.getPendingReservations());
        model.addAttribute("approvals", this.approvalService.getAllApprovals());
        return "approvals/list";
    }

    @PostMapping("/approvals/{reservationId}/approve")
    public String approveReservation(@PathVariable Long reservationId,
            @RequestParam Long adminId,
            @RequestParam(required = false) String catatan,
            RedirectAttributes redirectAttrs) {
        try {
            this.approvalService.approveReservation(adminId, reservationId, catatan);
            redirectAttrs.addFlashAttribute("sukses", "Reservasi disetujui");
        } catch (RuntimeException e) {
            redirectAttrs.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/approvals";
    }

    @PostMapping("/approvals/{reservationId}/reject")
    public String rejectReservation(@PathVariable Long reservationId,
            @RequestParam Long adminId,
            @RequestParam(required = false) String catatan,
            RedirectAttributes redirectAttrs) {
        try {
            this.approvalService.rejectReservation(adminId, reservationId, catatan);
            redirectAttrs.addFlashAttribute("sukses", "Reservasi ditolak");
        } catch (RuntimeException e) {
            redirectAttrs.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/approvals";
    }

    @PostMapping("/approvals/{reservationId}/revision")
    public String requestRevision(@PathVariable Long reservationId,
            @RequestParam Long adminId,
            @RequestParam(required = false) String catatan,
            RedirectAttributes redirectAttrs) {
        try {
            this.approvalService.requestRevision(adminId, reservationId, catatan);
            redirectAttrs.addFlashAttribute("sukses", "Reservasi diminta revisi");
        } catch (RuntimeException e) {
            redirectAttrs.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/approvals";
    }
}
