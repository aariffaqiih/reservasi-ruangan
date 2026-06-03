package com.tup.reservasi.auth;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/*
 * Penanggung jawab: Amelia Sofiana Makharomi.
 *
 * Arahan:
 * - File ini adalah repository starter untuk login.
 * - Query aktif saat ini cukup mencari user berdasarkan username.
 * - Jika nanti User domain final digabung dengan autentikasi,
 *   sesuaikan repository agar tetap mendukung login dan data profil User.
 */
public interface LoginUserRepository extends JpaRepository<LoginUser, Long> {

    Optional<LoginUser> findByUsername(String username);
}
