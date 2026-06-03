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

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tup.reservasi.entity.User;
import com.tup.reservasi.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}