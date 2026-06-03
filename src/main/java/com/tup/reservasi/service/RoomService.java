package com.tup.reservasi.service;

/*
 * Penanggung jawab: Ali Abdul Fattah 'Alim Kautsar.
 *
 * Arahan dari class-diagram:
 * - Service ini membantu behaviour Room:
 *   aktifkan()
 *   nonaktifkan()
 *   ubahStatusAktif()
 *   getInfoRuang(): String
 * - Data yang terkait:
 *   roomId, namaRuang, gedung, kapasitas, statusAktif.
 * - Aturan yang perlu dipikirkan saat coding:
 *   Room nonaktif tidak boleh dipakai untuk Reservation baru.
 *   perubahan status aktif perlu dicek terhadap reservasi yang sedang berjalan.
 */
