package com.tup.reservasi.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/*
 * Penanggung jawab: Ali Abdul Fattah 'Alim Kautsar.
 *
 * Arahan DTO request:
 * - Data untuk cek availability:
 *   roomId: String
 *   tanggal: LocalDate
 *   jamMulai: LocalTime
 *   jamSelesai: LocalTime
 * - Behaviour terkait:
 *   ReservationService.validateAvailability(): boolean.
 */

public class ReservationAvailabilityRequest {

    @NotBlank(message = "ID ruang tidak boleh kosong")
    private String roomId;

    @NotNull(message = "Tanggal reservasi tidak boleh kosong")
    @FutureOrPresent(message = "Tanggal reservasi tidak boleh di masa lalu")
    private LocalDate tanggal;

    @NotNull(message = "Jam mulai tidak boleh kosong")
    private LocalTime jamMulai;

    @NotNull(message = "Jam selesai tidak boleh kosong")
    private LocalTime jamSelesai;

    public ReservationAvailabilityRequest() {
    }

    public ReservationAvailabilityRequest(String roomId, LocalDate tanggal, LocalTime jamMulai,
            LocalTime jamSelesai) {
        this.roomId = roomId;
        this.tanggal = tanggal;
        this.jamMulai = jamMulai;
        this.jamSelesai = jamSelesai;
    }

    public boolean validasiWaktu() {
        return jamMulai != null && jamSelesai != null && jamMulai.isBefore(jamSelesai);
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public LocalDate getTanggal() {
        return tanggal;
    }

    public void setTanggal(LocalDate tanggal) {
        this.tanggal = tanggal;
    }

    public LocalTime getJamMulai() {
        return jamMulai;
    }

    public void setJamMulai(LocalTime jamMulai) {
        this.jamMulai = jamMulai;
    }

    public LocalTime getJamSelesai() {
        return jamSelesai;
    }

    public void setJamSelesai(LocalTime jamSelesai) {
        this.jamSelesai = jamSelesai;
    }
}
