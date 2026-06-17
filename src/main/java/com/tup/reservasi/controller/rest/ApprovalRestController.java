package com.tup.reservasi.controller.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tup.reservasi.entity.Approval;
import com.tup.reservasi.entity.Reservation;
import com.tup.reservasi.service.ApprovalService;

/*
 * Penanggung jawab: Aarif Rahmaan Jalaluddin Faqiih - 103112430182.
 * Modul: Approval dan ApprovalService.
 */
@RestController
@RequestMapping("/api/approvals")
public class ApprovalRestController {

    private final ApprovalService approvalService;

    public ApprovalRestController(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    @GetMapping
    public ResponseEntity<List<Approval>> getAllApprovals() {
        return ResponseEntity.ok(this.approvalService.getAllApprovals());
    }

    @GetMapping("/{approvalId}")
    public ResponseEntity<Approval> getApprovalById(@PathVariable Long approvalId) {
        try {
            return ResponseEntity.ok(this.approvalService.getApprovalById(approvalId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/pending")
    public ResponseEntity<List<Reservation>> getPendingReservations() {
        return ResponseEntity.ok(this.approvalService.getPendingReservations());
    }

    @PostMapping
    public ResponseEntity<Approval> createApproval(@RequestBody Approval approval) {
        Approval approvalCreated = this.approvalService.createApproval(approval);
        return ResponseEntity.status(HttpStatus.CREATED).body(approvalCreated);
    }

    @PutMapping("/{approvalId}")
    public ResponseEntity<Approval> updateApproval(@PathVariable Long approvalId,
            @RequestBody Approval approval) {
        try {
            return ResponseEntity.ok(this.approvalService.updateApproval(approvalId, approval));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{reservationId}/approve")
    public ResponseEntity<Approval> approveReservation(@PathVariable Long reservationId,
            @RequestParam Long adminId,
            @RequestBody Approval approval) {
        String catatan = approval.getCatatan();
        try {
            Approval approvalCreated = this.approvalService.approveReservation(adminId, reservationId, catatan);
            return ResponseEntity.status(HttpStatus.CREATED).body(approvalCreated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{reservationId}/reject")
    public ResponseEntity<Approval> rejectReservation(@PathVariable Long reservationId,
            @RequestParam Long adminId,
            @RequestBody Approval approval) {
        String catatan = approval.getCatatan();
        try {
            Approval approvalCreated = this.approvalService.rejectReservation(adminId, reservationId, catatan);
            return ResponseEntity.status(HttpStatus.CREATED).body(approvalCreated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{reservationId}/revision")
    public ResponseEntity<Approval> requestRevision(@PathVariable Long reservationId,
            @RequestParam Long adminId,
            @RequestBody Approval approval) {
        String catatan = approval.getCatatan();
        try {
            Approval approvalCreated = this.approvalService.requestRevision(adminId, reservationId, catatan);
            return ResponseEntity.status(HttpStatus.CREATED).body(approvalCreated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{approvalId}")
    public ResponseEntity<Void> deleteApproval(@PathVariable Long approvalId) {
        try {
            this.approvalService.deleteApproval(approvalId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
