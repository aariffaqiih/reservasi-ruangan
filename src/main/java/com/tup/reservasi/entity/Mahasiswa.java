package com.tup.reservasi.entity;

/*
 * Penanggung jawab: Amelia Sofiana Makharomi.
 *
 * Arahan dari class-diagram:
 * - Mahasiswa harus extends User.
 * - Atribut khusus Mahasiswa:
 *   nim: String
 *   prodi: String
 *   angkatan: int
 * - Behaviour yang perlu dibuat:
 *   ajukanReservasi(): Reservation
 *   batalkanReservasi(): boolean
 *   lihatStatusReservasi(): List<Reservation>
 *   receiveNotification()
 * - Catatan relasi:
 *   Satu Mahasiswa dapat memiliki 0..* Reservation.
 *   Mahasiswa juga menerima Notification melalui kontrak Notifiable jika dipakai final.
 */
