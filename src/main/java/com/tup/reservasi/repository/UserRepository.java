package com.tup.reservasi.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tup.reservasi.entity.User;

/*
 * Penanggung jawab: Amelia Sofiana Makharomi - 103112400233.
 * Modul: User dan Mahasiswa.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByNoHp(String noHp);

    List<User> findByNamaContaining(String nama);
}
