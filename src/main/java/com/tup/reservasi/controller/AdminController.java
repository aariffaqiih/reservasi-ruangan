package com.tup.reservasi.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tup.reservasi.dto.ApprovalRequest;
import com.tup.reservasi.dto.ApprovalResponse;
import com.tup.reservasi.dto.ReservationResponse;
import com.tup.reservasi.service.ApprovalService;

/*
 * Penanggung jawab: Atha Muyassar.
 *
 * Arahan halaman dari class-diagram:
 * - Controller ini nanti menghubungkan halaman Admin dengan behaviour:
 *   verifikasiReservasi(): boolean
 *   setujuiReservasi(): Approval
 *   tolakReservasi(): Approval
 *   mintaRevisiData(): Approval
 *   receiveNotification()
 * - Data yang perlu dikirim ke view:
 *   profil Admin: unitKerja.
 *   daftar Reservation yang menunggu keputusan.
 *   daftar Approval yang sudah ditinjau Admin.
 *   daftar Notification untuk Admin.
 * - Step 7 mulai menyiapkan route halaman admin.
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
        return "admin/dashboard";
    }

    @GetMapping("/approvals")
    public String approvalList(Model model) {
        model.addAttribute("pendingReservations", approvalService.getPendingReservations()
                .stream()
                .map(ReservationResponse::from)
                .toList());
        model.addAttribute("approvals", approvalService.getApprovalHistory()
                .stream()
                .map(ApprovalResponse::from)
                .toList());
        return "approvals/list";
    }

    @PostMapping("/approvals/{reservationId}/approve")
    public String approveReservation(@PathVariable String reservationId,
            @RequestParam(required = false) String catatan,
            Authentication authentication) {
        approvalService.approveReservation(reservationId, authentication.getName(), request(catatan));
        return "redirect:/admin/approvals";
    }

    @PostMapping("/approvals/{reservationId}/reject")
    public String rejectReservation(@PathVariable String reservationId,
            @RequestParam(required = false) String catatan,
            Authentication authentication) {
        approvalService.rejectReservation(reservationId, authentication.getName(), request(catatan));
        return "redirect:/admin/approvals";
    }

    @PostMapping("/approvals/{reservationId}/revision")
    public String requestRevision(@PathVariable String reservationId,
            @RequestParam(required = false) String catatan,
            Authentication authentication) {
        approvalService.requestRevision(reservationId, authentication.getName(), request(catatan));
        return "redirect:/admin/approvals";
    }

    private ApprovalRequest request(String catatan) {
        ApprovalRequest request = new ApprovalRequest();
        request.setCatatan(catatan);
        return request;
    }
}
