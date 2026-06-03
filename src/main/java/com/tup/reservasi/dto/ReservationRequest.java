package com.tup.reservasi.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/*
 * Penanggung jawab: Ali Abdul Fattah 'Alim Kautsar.
 *
 * Arahan DTO request:
 * - Data untuk ajukan reservasi:
 *   mahasiswaId: String
 *   roomId: String
 *   tanggal: LocalDate
 *   jamMulai: LocalTime
 *   jamSelesai: LocalTime
 *   tujuan: String
 * - Behaviour terkait:
 *   Mahasiswa.ajukanReservasi(), Reservation.ajukan(), Reservation.validasiWaktu(),
 *   ReservationService.createReservation(), ReservationService.validateAvailability().
 */

public class ReservationRequest {

    @NotBlank(message = "ID mahasiswa tidak boleh kosong")
    private String mahasiswaId;

    @NotBlank(message = "ID ruang tidak boleh kosong")
    private String roomId;

    @NotNull(message = "Tanggal reservasi tidak boleh kosong")
    @FutureOrPresent(message = "Tanggal reservasi tidak boleh di masa lalu")
    private LocalDate tanggal;

    @NotNull(message = "Jam mulai tidak boleh kosong")
    private LocalTime jamMulai;

    @NotNull(message = "Jam selesai tidak boleh kosong")
    private LocalTime jamSelesai;

    @NotBlank(message = "Tujuan reservasi tidak boleh kosong")
    @Size(max = 255, message = "Tujuan reservasi maksimal 255 karakter")
    private String tujuan;

    public ReservationRequest() {
    }

    public ReservationRequest(String mahasiswaId, String roomId, LocalDate tanggal, LocalTime jamMulai,
            LocalTime jamSelesai, String tujuan) {
        this.mahasiswaId = mahasiswaId;
        this.roomId = roomId;
        this.tanggal = tanggal;
        this.jamMulai = jamMulai;
        this.jamSelesai = jamSelesai;
        this.tujuan = tujuan;
    }

    public boolean validasiWaktu() {
        return jamMulai != null && jamSelesai != null && jamMulai.isBefore(jamSelesai);
    }

    public String getMahasiswaId() {
        return mahasiswaId;
    }

    public void setMahasiswaId(String mahasiswaId) {
        this.mahasiswaId = mahasiswaId;
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

    public String getTujuan() {
        return tujuan;
    }

    public void setTujuan(String tujuan) {
        this.tujuan = tujuan;
    }
}
