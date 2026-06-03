package com.tup.reservasi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/*
 * Penanggung jawab: Ali Abdul Fattah 'Alim Kautsar.
 *
 * Arahan DTO request:
 * - Data untuk membatalkan reservasi:
 *   reservationId: String
 *   alasanPembatalan: String bila dibutuhkan UI final.
 * - Behaviour terkait:
 *   Mahasiswa.batalkanReservasi(), Reservation.batalkan(),
 *   Reservation.isCanBeCancelled(), ReservationService.cancelReservation().
 */

public class ReservationCancelRequest {

    @NotBlank(message = "ID reservasi tidak boleh kosong")
    private String reservationId;

    @Size(max = 255, message = "Alasan pembatalan maksimal 255 karakter")
    private String alasanPembatalan;

    public ReservationCancelRequest() {
    }

    public ReservationCancelRequest(String reservationId, String alasanPembatalan) {
        this.reservationId = reservationId;
        this.alasanPembatalan = alasanPembatalan;
    }

    public boolean hasAlasanPembatalan() {
        return alasanPembatalan != null && !alasanPembatalan.trim().isEmpty();
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getAlasanPembatalan() {
        return alasanPembatalan;
    }

    public void setAlasanPembatalan(String alasanPembatalan) {
        this.alasanPembatalan = alasanPembatalan;
    }
}
