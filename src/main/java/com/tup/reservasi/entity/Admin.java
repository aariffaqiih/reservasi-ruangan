package com.tup.reservasi.entity;

/*
 * Penanggung jawab: Atha Muyassar.
 *
 * Arahan dari class-diagram:
 * - Admin harus extends User.
 * - Admin mengimplementasikan behaviour Notifiable.
 * - Atribut khusus Admin:
 *   unitKerja: String
 * - Behaviour yang perlu dibuat:
 *   verifikasiReservasi(): boolean
 *   setujuiReservasi(): Approval
 *   tolakReservasi(): Approval
 *   mintaRevisiData(): Approval
 *   receiveNotification()
 * - Catatan relasi:
 *   Satu Admin menangani 0..* Approval.
 *   Setiap Approval ditangani oleh tepat 1 Admin.
 */
