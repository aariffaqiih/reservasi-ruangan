package com.tup.reservasi.repository;

/*
 * Penanggung jawab: Amelia Sofiana Makharomi.
 *
 * Arahan repository:
 * - Siapkan akses data untuk User, Mahasiswa, Admin, dan Satpam saat entity final dibuat.
 * - Query yang kemungkinan dibutuhkan:
 *   cari berdasarkan id: String.
 *   cari berdasarkan email.
 *   cari berdasarkan noHp bila login/profil memerlukan.
 *   cari Mahasiswa berdasarkan nim.
 * - Jangan buat query yang mengembalikan passwordHash ke layer response.
 */

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tup.reservasi.entity.User;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    Optional<User> findByNoHp(String noHp);

}