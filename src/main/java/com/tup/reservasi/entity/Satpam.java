package com.tup.reservasi.entity;

/*
 * Penanggung jawab: Tadzkiroh Aziziyah Haqia.
 *
 * Arahan dari class-diagram:
 * - Satpam harus extends User.
 * - Satpam mengimplementasikan behaviour Notifiable.
 * - Atribut khusus Satpam:
 *   shift: String
 *   posJaga: String
 * - Behaviour yang perlu dibuat:
 *   konfirmasiCheckIn(): AccessRecord
 *   konfirmasiCheckOut(): AccessRecord
 *   catatKendala()
 *   receiveNotification()
 * - Catatan relasi:
 *   Satu Satpam dapat menangani 0..* AccessRecord.
 *   Setiap AccessRecord ditangani oleh tepat 1 Satpam.
 */
