package com.tup.reservasi.controller.rest;

/* ---------------------------------------------------------------
 * Penanggung jawab: Ali Abdul Fattah 'Alim Kautsar.
 *
 * REST controller untuk entitas Reservation. Menyajikan endpoint CRUD serta
 * operasi khusus: cek ketersediaan ruang dan pembatalan reservasi.
 * Semua logika bisnis didelegasikan ke {@link com.tup.reservasi.service.ReservationService}.
 * --------------------------------------------------------------- */

import com.tup.reservasi.dto.ReservationAvailabilityRequest;
import com.tup.reservasi.dto.ReservationCancelRequest;
import com.tup.reservasi.dto.ReservationRequest;
import com.tup.reservasi.dto.ReservationResponse;
import com.tup.reservasi.entity.Reservation;
import com.tup.reservasi.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reservations")
public class ReservationRestController {

    private final ReservationService reservationService;

    public ReservationRestController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // ---------------------------------------------------------------------
    // CREATE – buat reservasi baru
    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody ReservationRequest request) {
        Reservation reservation = reservationService.createReservation(request);
        return new ResponseEntity<>(ReservationResponse.from(reservation), HttpStatus.CREATED);
    }

    // ---------------------------------------------------------------------
    // CHECK AVAILABILITY – cek apakah ruang tersedia pada rentang waktu
    @PostMapping("/availability")
    public ResponseEntity<Boolean> checkAvailability(@RequestBody ReservationAvailabilityRequest request) {
        boolean available = reservationService.validateAvailability(
                request.getRoomId(),
                request.getTanggal(),
                request.getJamMulai(),
                request.getJamSelesai()
        );
        return ResponseEntity.ok(available);
    }

    // ---------------------------------------------------------------------
    // CANCEL – batalkan reservasi yang sudah dibuat
    @PostMapping("/cancel")
    public ResponseEntity<Void> cancelReservation(@RequestBody ReservationCancelRequest request) {
        reservationService.cancelReservation(request);
        return ResponseEntity.ok().build();
    }

    // ---------------------------------------------------------------------
    // READ – satu reservasi berdasarkan ID
    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationResponse> getReservation(@PathVariable String reservationId) {
        Optional<Reservation> opt = reservationService.getReservationById(reservationId);
        return opt.map(r -> ResponseEntity.ok(ReservationResponse.from(r)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ---------------------------------------------------------------------
    // READ – semua reservasi (riwayat umum)
    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getAllReservations() {
        List<ReservationResponse> list = reservationService.getReservationHistory()
                .stream()
                .map(ReservationResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    // ---------------------------------------------------------------------
    // READ – riwayat reservasi per mahasiswa (optional query param)
    @GetMapping("/history")
    public ResponseEntity<List<ReservationResponse>> getReservationHistory(@RequestParam(required = false) String mahasiswaId) {
        List<Reservation> source = (mahasiswaId != null && !mahasiswaId.isBlank())
                ? reservationService.getReservationHistory(mahasiswaId)
                : reservationService.getReservationHistory();
        List<ReservationResponse> list = source.stream()
                .map(ReservationResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }
}
