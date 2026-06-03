package com.tup.reservasi.service;

/*
 * Penanggung jawab: Tadzkiroh Aziziyah Haqia.
 *
 * Arahan dari class-diagram:
 * - Service menyimpan/mengelola:
 *   accessRecords: List<AccessRecord>
 * - Behaviour yang perlu dibuat:
 *   checkIn(): AccessRecord
 *   checkOut(): AccessRecord
 *   reportIssue()
 * - Aturan yang perlu dipikirkan saat coding:
 *   checkIn membuat/mengisi AccessRecord dengan reservation, satpam, dan checkInTime.
 *   checkOut mengisi checkOutTime untuk AccessRecord yang sudah check-in.
 *   reportIssue mengisi catatanPelanggaran atau catatan kendala.
 *   proses akses hanya untuk Reservation yang sudah valid/disetujui.
 */
