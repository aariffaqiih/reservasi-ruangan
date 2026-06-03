package com.example.crud.service;

import com.example.crud.model.User;
import com.example.crud.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    // =============================================
    // CREATE
    // =============================================
    public User save(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username '" + user.getUsername() + "' sudah digunakan!");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email '" + user.getEmail() + "' sudah terdaftar!");
        }
        return userRepository.save(user);
    }

    // =============================================
    // READ
    // =============================================
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByIdWithProfile(Long id) {
        return userRepository.findByIdWithProfile(id);
    }

    @Transactional(readOnly = true)
    public List<User> findAllWithDetails() {
        return userRepository.findAllWithDetails();
    }

    // =============================================
    // UPDATE
    // =============================================
    public User update(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User dengan ID " + id + " tidak ditemukan!"));

        // Cek username unik (kecuali milik sendiri)
        if (!user.getUsername().equals(userDetails.getUsername()) &&
                userRepository.existsByUsername(userDetails.getUsername())) {
            throw new RuntimeException("Username '" + userDetails.getUsername() + "' sudah digunakan!");
        }

        // Cek email unik (kecuali milik sendiri)
        if (!user.getEmail().equals(userDetails.getEmail()) &&
                userRepository.existsByEmail(userDetails.getEmail())) {
            throw new RuntimeException("Email '" + userDetails.getEmail() + "' sudah terdaftar!");
        }

        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        if (userDetails.getPassword() != null && !userDetails.getPassword().isBlank()) {
            user.setPassword(userDetails.getPassword());
        }

        return userRepository.save(user);
    }

    // =============================================
    // DELETE
    // =============================================
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User dengan ID " + id + " tidak ditemukan!");
        }
        userRepository.deleteById(id);
    }

    public long count() {
        return userRepository.count();
    }
}
