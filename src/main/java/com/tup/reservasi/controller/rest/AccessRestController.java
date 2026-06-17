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

import com.tup.reservasi.entity.AccessRecord;
import com.tup.reservasi.service.AccessControlService;

/*
 * Penanggung jawab: Tadzkiroh Aziziyah Haqia - 103112400242.
 * Modul: AccessRecord dan AccessControlService.
 */
@RestController
@RequestMapping("/api/access")
public class AccessRestController {

    private final AccessControlService accessControlService;

    public AccessRestController(AccessControlService accessControlService) {
        this.accessControlService = accessControlService;
    }

    @GetMapping
    public ResponseEntity<List<AccessRecord>> getAllRecords() {
        return ResponseEntity.ok(this.accessControlService.getAllRecords());
    }

    @GetMapping("/{recordId}")
    public ResponseEntity<AccessRecord> getRecordById(@PathVariable Long recordId) {
        try {
            return ResponseEntity.ok(this.accessControlService.getRecordById(recordId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<AccessRecord> createRecord(@RequestBody AccessRecord record) {
        AccessRecord recordCreated = this.accessControlService.createRecord(record);
        return ResponseEntity.status(HttpStatus.CREATED).body(recordCreated);
    }

    @PutMapping("/{recordId}")
    public ResponseEntity<AccessRecord> updateRecord(@PathVariable Long recordId,
            @RequestBody AccessRecord record) {
        try {
            return ResponseEntity.ok(this.accessControlService.updateRecord(recordId, record));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/check-in")
    public ResponseEntity<AccessRecord> checkIn(@RequestParam Long satpamId,
            @RequestParam Long reservationId) {
        try {
            AccessRecord recordCreated = this.accessControlService.checkIn(satpamId, reservationId);
            return ResponseEntity.status(HttpStatus.CREATED).body(recordCreated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{recordId}/check-out")
    public ResponseEntity<AccessRecord> checkOut(@PathVariable Long recordId,
            @RequestParam Long satpamId) {
        try {
            return ResponseEntity.ok(this.accessControlService.checkOut(satpamId, recordId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{recordId}/kendala")
    public ResponseEntity<Void> reportIssue(@PathVariable Long recordId,
            @RequestParam Long satpamId,
            @RequestParam String deskripsi) {
        try {
            this.accessControlService.reportIssue(satpamId, recordId, deskripsi);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{recordId}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long recordId) {
        try {
            this.accessControlService.deleteRecord(recordId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
