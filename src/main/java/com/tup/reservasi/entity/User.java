package com.tup.reservasi.entity;

/*
 * Penanggung jawab: Amelia Sofiana Makharomi.
 *
 * Arahan dari class-diagram:
 * - Jadikan User sebagai parent/abstract entity untuk Mahasiswa, Admin, dan Satpam.
 * - Atribut yang perlu disiapkan:
 *   id: String
 *   nama: String
 *   email: String
 *   noHp: String
 *   passwordHash: String
 * - Behaviour yang perlu dibuat:
 *   login(): boolean
 *   logout()
 *   ubahProfil()
 * - Catatan relasi:
 *   Mahasiswa extends User.
 *   Admin extends User.
 *   Satpam extends User.
 */
