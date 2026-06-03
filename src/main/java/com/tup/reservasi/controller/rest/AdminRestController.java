package com.tup.reservasi.controller.rest;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tup.reservasi.dto.AdminRequest;
import com.tup.reservasi.entity.Admin;

import jakarta.validation.Valid;

/*
 * Penanggung jawab: Atha Muyassar.
 *
 * Arahan REST dari class-diagram:
 * - Endpoint final nanti melayani data Admin:
 *   unitKerja.
 *   verifikasiReservasi().
 *   setujuiReservasi().
 *   tolakReservasi().
 *   mintaRevisiData().
 * - DTO terkait:
 *   AdminRequest, ApprovalRequest, ApprovalResponse.
 * - Step 7 menyiapkan endpoint data Admin dasar.
 */

@RestController
@RequestMapping("/api/admins")
public class AdminRestController {

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAdmins() {
        return ResponseEntity.ok(List.of());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createAdmin(@Valid @RequestBody AdminRequest request) {
        Admin admin = new Admin(
                UUID.randomUUID().toString(),
                request.getNama(),
                request.getEmail(),
                request.getNoHp(),
                null,
                request.getUnitKerja());

        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(admin));
    }

    private Map<String, Object> toResponse(Admin admin) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("id", admin.getId());
        response.put("nama", admin.getNama());
        response.put("email", admin.getEmail());
        response.put("noHp", admin.getNoHp());
        response.put("unitKerja", admin.getUnitKerja());
        response.put("role", admin.getRole());
        response.put("dashboardPath", admin.getDashboardPath());
        return response;
    }
}
