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
import java.util.Locale;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tup.reservasi.auth.UserRole;
import com.tup.reservasi.dto.ProfileUpdateRequest;
import com.tup.reservasi.dto.RegistrationRequest;
import com.tup.reservasi.dto.UserResponse;
import com.tup.reservasi.entity.Mahasiswa;
import com.tup.reservasi.entity.User;
import com.tup.reservasi.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public List<UserResponse> getAllUserResponses() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }
    public Optional<User> getById(String id) {
        return userRepository.findById(id);
    }
    public UserResponse getResponseById(String id) {
        return userRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("User tidak ditemukan"));
    }
    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public Optional<User> getByNoHp(String noHp) {
        return userRepository.findByNoHp(noHp);
    }
    @Transactional
    public UserResponse register(RegistrationRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request registrasi tidak boleh kosong");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email sudah digunakan");
        }
        if (request.getNoHp() != null && !request.getNoHp().isBlank()
                && userRepository.findByNoHp(request.getNoHp()).isPresent()) {
            throw new IllegalArgumentException("Nomor HP sudah digunakan");
        }

        UserRole role = parseRole(request.getRole());
        if (role != UserRole.MAHASISWA) {
            throw new IllegalArgumentException("Registrasi starter hanya mendukung role MAHASISWA");
        }

        Mahasiswa mahasiswa = new Mahasiswa(
                null,
                request.getNama(),
                request.getEmail(),
                request.getNoHp(),
                passwordEncoder.encode(request.getPassword()),
                request.getNim(),
                request.getProdi(),
                request.getAngkatan() == null ? 0 : request.getAngkatan());

        return toResponse(userRepository.save(mahasiswa));
    }
    public User simpan(User user) {
        return userRepository.save(user);
    }
    @Transactional
    public UserResponse updateProfile(String id, ProfileUpdateRequest request) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID user tidak boleh kosong");
        }
        if (request == null) {
            throw new IllegalArgumentException("Request profil tidak boleh kosong");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User tidak ditemukan"));

        userRepository.findByEmail(request.getEmail())
                .filter(existing -> !existing.getId().equals(user.getId()))
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Email sudah digunakan");
                });

        if (request.getNoHp() != null && !request.getNoHp().isBlank()) {
            userRepository.findByNoHp(request.getNoHp())
                    .filter(existing -> !existing.getId().equals(user.getId()))
                    .ifPresent(existing -> {
                        throw new IllegalArgumentException("Nomor HP sudah digunakan");
                    });
        }

        user.setNama(request.getNama());
        user.setEmail(request.getEmail());
        user.setNoHp(request.getNoHp());

        if (user instanceof Mahasiswa mahasiswa) {
            mahasiswa.setProdi(request.getProdi());
            if (request.getAngkatan() != null) {
                mahasiswa.setAngkatan(request.getAngkatan());
            }
        }

        return toResponse(userRepository.save(user));
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
    public UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }

        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setNama(user.getNama());
        response.setEmail(user.getEmail());
        response.setNoHp(user.getNoHp());

        if (user instanceof Mahasiswa mahasiswa) {
            response.setNim(mahasiswa.getNim());
            response.setProdi(mahasiswa.getProdi());
            response.setAngkatan(mahasiswa.getAngkatan());
        }

        return response;
    }
    private UserRole parseRole(String role) {
        if (role == null || role.isBlank()) {
            return UserRole.MAHASISWA;
        }
        return UserRole.valueOf(role.trim().toUpperCase(Locale.ROOT));
    }
}
