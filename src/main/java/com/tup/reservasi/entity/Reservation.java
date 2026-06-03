package com.tup.reservasi.entity;

/*
 * Penanggung jawab: 'Aarif Rahmaan Jalaluddin Faqiih.
 *
 * Arahan dari class-diagram:
 * - Atribut yang perlu disiapkan:
 *   reservationId: String
 *   mahasiswa: Mahasiswa
 *   room: Room
 *   tanggal: LocalDate
 *   jamMulai: LocalTime
 *   jamSelesai: LocalTime
 *   tujuan: String
 *   status: ReservationStatus
 *   createdAt: LocalDateTime
 *   cancelledAt: LocalDateTime
 *   approval: Approval
 *   accessRecord: AccessRecord
 * - Behaviour yang perlu dibuat:
 *   ajukan()
 *   ubahStatus()
 *   batalkan()
 *   validasiWaktu(): boolean
 *   isCanBeCancelled(): boolean
 * - Catatan relasi:
 *   Setiap Reservation milik tepat 1 Mahasiswa dan tepat 1 Room.
 *   Reservation memiliki 0..1 Approval.
 *   Reservation memiliki 0..1 AccessRecord.
 */
