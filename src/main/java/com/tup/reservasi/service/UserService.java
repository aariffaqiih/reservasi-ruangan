package com.tup.reservasi.service;

/*
 * Penanggung jawab: Amelia Sofiana Makharomi.
 *
 * Arahan dari class-diagram:
 * - Service ini menangani behaviour umum User:
 *   login(): boolean
 *   logout()
 *   ubahProfil()
 * - Data yang terkait:
 *   id, nama, email, noHp, passwordHash.
 * - Aturan yang perlu dipikirkan saat coding:
 *   perubahan profil tidak boleh mengubah passwordHash tanpa flow khusus.
 *   response profil tidak boleh mengirim passwordHash.
 *   untuk Mahasiswa, ikut kelola nim, prodi, dan angkatan.
 */
