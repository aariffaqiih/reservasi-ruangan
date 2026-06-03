package com.tup.reservasi.entity;

/*
 * Penanggung jawab: Tadzkiroh Aziziyah Haqia.
 *
 * Arahan dari class-diagram:
 * - Atribut yang perlu disiapkan:
 *   recordId: String
 *   reservation: Reservation
 *   satpam: Satpam
 *   checkInTime: LocalDateTime
 *   checkOutTime: LocalDateTime
 *   catatanPelanggaran: String
 * - Behaviour yang perlu dibuat:
 *   checkIn()
 *   checkOut()
 *   laporkanKendala()
 * - Catatan relasi:
 *   Setiap AccessRecord terhubung ke tepat 1 Reservation.
 *   Setiap AccessRecord ditangani oleh tepat 1 Satpam.
 */
