package com.example.crud.service;

import com.example.crud.model.Profile;
import com.example.crud.model.User;
import com.example.crud.repository.ProfileRepository;
import com.example.crud.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    // =============================================
    // CREATE
    // =============================================
    public Profile save(Profile profile, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User dengan ID " + userId + " tidak ditemukan!"));

        if (profileRepository.existsByUserId(userId)) {
            throw new RuntimeException("User ini sudah memiliki profil!");
        }

        profile.setUser(user);
        return profileRepository.save(profile);
    }

    // =============================================
    // READ
    // =============================================
    @Transactional(readOnly = true)
    public List<Profile> findAll() {
        return profileRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Profile> findById(Long id) {
        return profileRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Profile> findByUserId(Long userId) {
        return profileRepository.findByUserId(userId);
    }

    // =============================================
    // UPDATE
    // =============================================
    public Profile update(Long id, Profile profileDetails) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile dengan ID " + id + " tidak ditemukan!"));

        profile.setFullName(profileDetails.getFullName());
        profile.setPhone(profileDetails.getPhone());
        profile.setAddress(profileDetails.getAddress());
        profile.setBirthDate(profileDetails.getBirthDate());
        profile.setGender(profileDetails.getGender());
        profile.setAvatarUrl(profileDetails.getAvatarUrl());
        profile.setBio(profileDetails.getBio());

        return profileRepository.save(profile);
    }

    // =============================================
    // DELETE
    // =============================================
    public void delete(Long id) {
        if (!profileRepository.existsById(id)) {
            throw new RuntimeException("Profile dengan ID " + id + " tidak ditemukan!");
        }
        profileRepository.deleteById(id);
    }
}
