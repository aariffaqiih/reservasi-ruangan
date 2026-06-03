package com.tup.reservasi.controller.rest;

/*
 * Penanggung jawab: Amelia Sofiana Makharomi.
 *
 * Arahan REST dari class-diagram:
 * - Endpoint final nanti melayani data User/Mahasiswa:
 *   profil User: id, nama, email, noHp.
 *   profil Mahasiswa: nim, prodi, angkatan.
 *   ubahProfil().
 *   lihatStatusReservasi().
 * - DTO terkait:
 *   RegistrationRequest, ProfileUpdateRequest, UserResponse.
 * - File ini tetap komentar sampai REST final dikerjakan.
 */

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tup.reservasi.dto.ProfileUpdateRequest;
import com.tup.reservasi.dto.RegistrationRequest;
import com.tup.reservasi.dto.UserResponse;
import com.tup.reservasi.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUserResponses();
    }

    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable String id) {
        return userService.getResponseById(id);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegistrationRequest request) {
        return new ResponseEntity<>(userService.register(request), HttpStatus.CREATED);
    }

    @GetMapping("/profile")
    public UserResponse getProfile(Authentication authentication) {
        return userService.getResponseById(authentication.getName());
    }

    @PutMapping("/profile")
    public UserResponse updateProfile(Authentication authentication, @Valid @RequestBody ProfileUpdateRequest request) {
        return userService.updateProfile(authentication.getName(), request);
    }
}
