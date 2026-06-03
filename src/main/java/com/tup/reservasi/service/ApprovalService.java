package com.tup.reservasi.service;

/*
 * Penanggung jawab: 'Aarif Rahmaan Jalaluddin Faqiih.
 *
 * Arahan dari class-diagram:
 * - Service menyimpan/mengelola:
 *   approvals: List<Approval>
 * - Behaviour yang perlu dibuat:
 *   verifyReservation(): boolean
 *   approveReservation(): Approval
 *   rejectReservation(): Approval
 *   requestRevision(): Approval
 * - Aturan yang perlu dipikirkan saat coding:
 *   verifyReservation() memeriksa data Reservation sebelum keputusan.
 *   approve/reject/revision mengisi keputusan, catatan, reviewedAt, dan Admin.
 *   perubahan Approval harus ikut mengubah status Reservation.
 */
