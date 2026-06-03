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

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tup.reservasi.entity.User;
import com.tup.reservasi.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public Optional<User> getByNoHp(String noHp) {
        return userRepository.findByNoHp(noHp);
    }
    public User simpan(User user) {
        return userRepository.save(user);
    }
    public boolean login() {
        return true;
    }
    public void logout() {
        System.out.println("User logout");
    }
    public void ubahProfil(User user) {
        userRepository.save(user);
    }
}