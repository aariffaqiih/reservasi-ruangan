package com.tup.reservasi.auth;

/*
 * Penanggung jawab: Amelia Sofiana Makharomi.
 *
 * Arahan:
 * - Role starter mengikuti tiga turunan User pada class-diagram:
 *   Mahasiswa, Admin, dan Satpam.
 * - Jika nanti enum role final dibuat ulang, pastikan mapping Spring Security
 *   tetap sesuai dengan halaman /mahasiswa, /admin, dan /satpam.
 */
public enum UserRole {
    MAHASISWA,
    ADMIN,
    SATPAM
}
