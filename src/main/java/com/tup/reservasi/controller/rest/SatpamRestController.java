package com.tup.reservasi.controller.rest;

/*
 * Penanggung jawab: Tadzkiroh Aziziyah Haqia.
 *
 * Arahan REST dari class-diagram:
 * - Endpoint final nanti melayani data Satpam:
 *   shift, posJaga.
 *   konfirmasiCheckIn().
 *   konfirmasiCheckOut().
 *   catatKendala().
 * - DTO terkait:
 *   SatpamRequest, AccessIssueRequest, AccessRecordResponse.
 * - File ini tetap komentar sampai REST final dikerjakan.
 */

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import com.tup.reservasi.dto.SatpamRequest;
import com.tup.reservasi.entity.Satpam;
import com.tup.reservasi.exception.ReservationException;
import com.tup.reservasi.repository.UserRepository;
import com.tup.reservasi.service.AccessControlService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/satpam")
public class SatpamRestController {

    private final UserRepository userRepository;
    private final AccessControlService accessControlService;

    public SatpamRestController(UserRepository userRepository, AccessControlService accessControlService) {
        this.userRepository = userRepository;
        this.accessControlService = accessControlService;
    }

    @GetMapping
    public List<Map<String, Object>> getAllSatpam() {
        return userRepository.findAll().stream()
                .filter(u -> u instanceof Satpam)
                .map(u -> toResponse((Satpam) u))
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getSatpamById(@PathVariable String id) {
        return userRepository.findById(id)
                .filter(u -> u instanceof Satpam)
                .map(u -> ResponseEntity.ok(toResponse((Satpam) u)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createSatpam(@Valid @RequestBody SatpamRequest request) {
        Satpam satpam = new Satpam(
                UUID.randomUUID().toString(),
                request.getNama(),
                request.getEmail(),
                request.getNoHp(),
                null,
                request.getShift(),
                request.getPosJaga());
        Satpam saved = (Satpam) userRepository.save(satpam);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(saved));
    }

    @GetMapping("/{satpamId}/records")
    public List<AccessRecordResponse> getRecordsBySatpam(@PathVariable String satpamId) {
        return accessControlService.getRecordsBySatpam(satpamId)
                .stream()
                .map(AccessRecordResponse::from)
                .toList();
    }

    @PostMapping("/{satpamId}/check-in/{reservationId}")
    public AccessRecordResponse konfirmasiCheckIn(
            @PathVariable String satpamId,
            @PathVariable String reservationId) {
        return AccessRecordResponse.from(accessControlService.checkIn(reservationId, satpamId));
    }

    @PostMapping("/{satpamId}/check-out/{reservationId}")
    public AccessRecordResponse konfirmasiCheckOut(
            @PathVariable String satpamId,
            @PathVariable String reservationId) {
        return AccessRecordResponse.from(accessControlService.checkOut(reservationId, satpamId));
    }

    @PostMapping("/{satpamId}/catat-kendala")
    public AccessRecordResponse catatKendala(
            @PathVariable String satpamId,
            @Valid @RequestBody AccessIssueRequest request,
            Authentication authentication) {
        request.setSatpamId(satpamId);
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

    private Map<String, Object> toResponse(Satpam satpam) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("id", satpam.getId());
        response.put("nama", satpam.getNama());
        response.put("email", satpam.getEmail());
        response.put("noHp", satpam.getNoHp());
        response.put("shift", satpam.getShift());
        response.put("posJaga", satpam.getPosJaga());
        response.put("role", satpam.getRole());
        return response;
    }

    private Map<String, Object> error(HttpStatus status, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return body;
    }
}
