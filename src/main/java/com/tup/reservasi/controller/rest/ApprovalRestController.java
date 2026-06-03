package com.tup.reservasi.controller.rest;

/*
 * Penanggung jawab: 'Aarif Rahmaan Jalaluddin Faqiih.
 *
 * Arahan REST dari class-diagram:
 * - Endpoint final nanti melayani behaviour ApprovalService:
 *   verifyReservation(): boolean
 *   approveReservation(): Approval
 *   rejectReservation(): Approval
 *   requestRevision(): Approval
 * - DTO terkait:
 *   ApprovalRequest, ApprovalResponse.
 * - File ini tetap komentar sampai REST final dikerjakan.
 */

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tup.reservasi.dto.ApprovalRequest;
import com.tup.reservasi.dto.ApprovalResponse;
import com.tup.reservasi.dto.ReservationResponse;
import com.tup.reservasi.exception.ReservationException;
import com.tup.reservasi.service.ApprovalService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/approvals")
public class ApprovalRestController {

    private final ApprovalService approvalService;

    public ApprovalRestController(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    @GetMapping("/pending")
    public List<ReservationResponse> getPendingApprovals() {
        return approvalService.getPendingReservations()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @GetMapping
    public List<ApprovalResponse> getApprovalHistory() {
        return approvalService.getApprovalHistory()
                .stream()
                .map(ApprovalResponse::from)
                .toList();
    }

    @PostMapping("/{reservationId}/approve")
    public ApprovalResponse approveReservation(
            @PathVariable String reservationId,
            @Valid @RequestBody(required = false) ApprovalRequest request,
            Authentication authentication) {
        return ApprovalResponse.from(approvalService.approveReservation(
                reservationId,
                resolveAdminId(request, authentication),
                request));
    }

    @PostMapping("/{reservationId}/reject")
    public ApprovalResponse rejectReservation(
            @PathVariable String reservationId,
            @Valid @RequestBody(required = false) ApprovalRequest request,
            Authentication authentication) {
        return ApprovalResponse.from(approvalService.rejectReservation(
                reservationId,
                resolveAdminId(request, authentication),
                request));
    }

    @PostMapping("/{reservationId}/revision")
    public ApprovalResponse requestRevision(
            @PathVariable String reservationId,
            @Valid @RequestBody(required = false) ApprovalRequest request,
            Authentication authentication) {
        return ApprovalResponse.from(approvalService.requestRevision(
                reservationId,
                resolveAdminId(request, authentication),
                request));
    }

    @PostMapping("/{reservationId}/request-revision")
    public ApprovalResponse requestRevisionAlias(
            @PathVariable String reservationId,
            @Valid @RequestBody(required = false) ApprovalRequest request,
            Authentication authentication) {
        return requestRevision(reservationId, request, authentication);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(IllegalArgumentException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error(HttpStatus.BAD_REQUEST, exception.getMessage()));
    }

    @ExceptionHandler(ReservationException.class)
    public ResponseEntity<Map<String, Object>> handleConflict(ReservationException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error(HttpStatus.CONFLICT, exception.getMessage()));
    }

    private String resolveAdminId(ApprovalRequest request, Authentication authentication) {
        if (request != null && request.getAdminId() != null && !request.getAdminId().isBlank()) {
            return request.getAdminId();
        }
        if (authentication != null && authentication.getName() != null && !authentication.getName().isBlank()) {
            return authentication.getName();
        }
        throw new IllegalArgumentException("ID admin tidak boleh kosong");
    }

    private Map<String, Object> error(HttpStatus status, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return body;
    }
}
