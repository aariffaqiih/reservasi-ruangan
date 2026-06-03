package com.tup.reservasi.entity;

/*
 * Penanggung jawab: Amelia Sofiana Makharomi.
 *
 * Arahan test dari class-diagram:
 * - Uji atribut User:
 *   id, nama, email, noHp, passwordHash.
 * - Uji behaviour:
 *   login() mengembalikan boolean sesuai kredensial.
 *   logout() menghapus status sesi bila diimplementasikan di entity/service.
 *   ubahProfil() mengubah nama, email, noHp tanpa mengekspos passwordHash.
 * - Uji inheritance:
 *   Mahasiswa, Admin, dan Satpam mewarisi data User.
 */
