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

import com.tup.reservasi.dto.ReservationRequest;
import com.tup.reservasi.entity.Reservation;
import com.tup.reservasi.service.ReservationService;

/*
 * Penanggung jawab: Aarif Rahmaan Jalaluddin Faqiih - 103112430182.
 * Modul: Reservation.
 */
@RestController
@RequestMapping("/api/reservations")
public class ReservationRestController {

    private final ReservationService reservationService;

    public ReservationRestController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        return ResponseEntity.ok(this.reservationService.getAllReservations());
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long reservationId) {
        try {
            return ResponseEntity.ok(this.reservationService.getReservationById(reservationId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<List<Reservation>> getReservationHistory(@PathVariable Long userId) {
        return ResponseEntity.ok(this.reservationService.getReservationHistory(userId));
    }

    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody ReservationRequest reservation) {
        try {
            Reservation reservationCreated = this.reservationService.createReservation(reservation.toReservation());
            return ResponseEntity.status(HttpStatus.CREATED).body(reservationCreated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{reservationId}")
    public ResponseEntity<Reservation> updateReservation(@PathVariable Long reservationId,
            @RequestBody ReservationRequest reservation) {
        try {
            return ResponseEntity.ok(this.reservationService.updateReservation(reservationId, reservation.toReservation()));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{reservationId}/cancel")
    public ResponseEntity<Boolean> cancelReservation(@PathVariable Long reservationId,
            @RequestParam(required = false) String alasan) {
        try {
            return ResponseEntity.ok(this.reservationService.cancelReservation(reservationId, alasan));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long reservationId) {
        try {
            this.reservationService.deleteReservation(reservationId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
