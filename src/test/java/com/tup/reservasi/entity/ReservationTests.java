package com.tup.reservasi.entity;

/*
 * Penanggung jawab: 'Aarif Rahmaan Jalaluddin Faqiih.
 *
 * Arahan test dari class-diagram:
 * - Uji atribut Reservation:
 *   reservationId, mahasiswa, room, tanggal, jamMulai, jamSelesai,
 *   tujuan, status, createdAt, cancelledAt, approval, accessRecord.
 * - Uji behaviour:
 *   ajukan() mengatur status awal.
 *   ubahStatus() mengubah status sesuai alur.
 *   batalkan() mengisi cancelledAt.
 *   validasiWaktu() menolak jam selesai sebelum/sama dengan jam mulai.
 *   isCanBeCancelled() sesuai status reservasi.
 */
