package com.tup.reservasi.service;

/*
 * Penanggung jawab: Ali Abdul Fattah 'Alim Kautsar.
 *
 * Arahan dari class-diagram:
 * - Service menyimpan/mengelola:
 *   reservations: List<Reservation>
 *   rooms: List<Room>
 * - Behaviour yang perlu dibuat:
 *   createReservation(): Reservation
 *   validateAvailability(): boolean
 *   cancelReservation(): boolean
 *   getReservationHistory(): List<Reservation>
 * - Aturan yang perlu dipikirkan saat coding:
 *   cek Room aktif sebelum reservasi dibuat.
 *   cek tanggal, jamMulai, jamSelesai agar tidak bentrok.
 *   gunakan Reservation.validasiWaktu() untuk validasi jam.
 *   gunakan Reservation.isCanBeCancelled() sebelum membatalkan.
 */
