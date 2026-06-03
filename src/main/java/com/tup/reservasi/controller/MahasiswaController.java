package com.tup.reservasi.controller;

/*
 * Penanggung jawab: Amelia Sofiana Makharomi.
 *
 * Arahan halaman dari class-diagram:
 * - Controller ini nanti menghubungkan halaman Mahasiswa dengan behaviour:
 *   ajukanReservasi(): Reservation
 *   batalkanReservasi(): boolean
 *   lihatStatusReservasi(): List<Reservation>
 *   receiveNotification()
 * - Data yang perlu dikirim ke view:
 *   profil Mahasiswa: nim, prodi, angkatan.
 *   daftar Reservation milik Mahasiswa.
 *   daftar Notification milik Mahasiswa bila fitur notifikasi sudah aktif.
 * - File ini tetap komentar sampai fitur mahasiswa mulai dikerjakan.
 */

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MahasiswaController {

    @GetMapping("/mahasiswa")
    public String mahasiswaHome() {
        return "mahasiswa/dashboard";
    }
}