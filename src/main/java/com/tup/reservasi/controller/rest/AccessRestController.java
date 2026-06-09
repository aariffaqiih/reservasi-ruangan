package com.tup.reservasi.controller.rest;

/*
 * Penanggung jawab: Tadzkiroh Aziziyah Haqia.
 *
 * Arahan REST dari class-diagram:
 * - Endpoint final nanti melayani behaviour AccessControlService:
 *   checkIn(): AccessRecord
 *   checkOut(): AccessRecord
 *   reportIssue()
 * - DTO terkait:
 *   AccessIssueRequest, AccessRecordResponse.
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

import com.tup.reservasi.dto.AccessIssueRequest;
import com.tup.reservasi.dto.AccessRecordResponse;
import com.tup.reservasi.exception.ReservationException;
import com.tup.reservasi.service.AccessControlService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/access")
public class AccessRestController {

    private final AccessControlService accessControlService;

    public AccessRestController(AccessControlService accessControlService) {
        this.accessControlService = accessControlService;
    }

    @GetMapping
    public List<AccessRecordResponse> getAllRecords() {
        return accessControlService.getAllRecords()
                .stream()
                .map(AccessRecordResponse::from)
                .toList();
    }

    @GetMapping("/belum-checkout")
    public List<AccessRecordResponse> getBelumCheckOut() {
        return accessControlService.getBelumCheckOut()
                .stream()
                .map(AccessRecordResponse::from)
                .toList();
    }

    @GetMapping("/kendala")
    public List<AccessRecordResponse> getRecordsWithKendala() {
        return accessControlService.getRecordsWithKendala()
                .stream()
                .map(AccessRecordResponse::from)
                .toList();
    }

    @GetMapping("/satpam/{satpamId}")
    public List<AccessRecordResponse> getRecordsBySatpam(@PathVariable String satpamId) {
        return accessControlService.getRecordsBySatpam(satpamId)
                .stream()
                .map(AccessRecordResponse::from)
                .toList();
    }

    @PostMapping("/{reservationId}/check-in")
    public AccessRecordResponse checkIn(
            @PathVariable String reservationId,
            Authentication authentication) {
        String satpamId = resolveSatpamId(authentication);
        return AccessRecordResponse.from(accessControlService.checkIn(reservationId, satpamId));
    }

    @PostMapping("/{reservationId}/check-out")
    public AccessRecordResponse checkOut(
            @PathVariable String reservationId,
            Authentication authentication) {
        String satpamId = resolveSatpamId(authentication);
        return AccessRecordResponse.from(accessControlService.checkOut(reservationId, satpamId));
    }

    @PostMapping("/report-issue")
    public AccessRecordResponse reportIssue(
            @Valid @RequestBody AccessIssueRequest request,
            Authentication authentication) {
        if (request.getSatpamId() == null || request.getSatpamId().isBlank()) {
            request.setSatpamId(resolveSatpamId(authentication));
        }
        return AccessRecordResponse.from(accessControlService.reportIssue(request));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(IllegalArgumentException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error(HttpStatus.BAD_REQUEST, exception.getMessage()));
    }

    @ExceptionHandler(ReservationException.class)
    public ResponseEntity<Map<String, Object>> handleConflict(ReservationException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error(HttpStatus.CONFLICT, exception.getMessage()));
    }

    private String resolveSatpamId(Authentication authentication) {
        if (authentication != null && authentication.getName() != null && !authentication.getName().isBlank()) {
            return authentication.getName();
        }
        throw new IllegalArgumentException("ID satpam tidak boleh kosong");
    }

    private Map<String, Object> error(HttpStatus status, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return body;
    }
}
