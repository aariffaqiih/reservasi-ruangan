package com.tup.reservasi.controller.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tup.reservasi.entity.Mahasiswa;
import com.tup.reservasi.entity.User;
import com.tup.reservasi.service.UserService;

/*
 * Penanggung jawab: Amelia Sofiana Makharomi - 103112400233.
 * Modul: User dan Mahasiswa.
 */
@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(this.userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(this.userService.getUserById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User userCreated = this.userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }

    @PostMapping("/mahasiswa")
    public ResponseEntity<Mahasiswa> createMahasiswa(@RequestBody Mahasiswa mahasiswa) {
        Mahasiswa userCreated = (Mahasiswa) this.userService.createUser(mahasiswa);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            return ResponseEntity.ok(this.userService.updateUser(id, user));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/mahasiswa/{id}")
    public ResponseEntity<Mahasiswa> updateMahasiswa(@PathVariable Long id, @RequestBody Mahasiswa mahasiswa) {
        try {
            return ResponseEntity.ok(this.userService.updateMahasiswa(id, mahasiswa));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countUsers() {
        return ResponseEntity.ok(this.userService.countUsers());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return this.userService.getUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/phone/{noHp}")
    public ResponseEntity<User> getUserByNoHp(@PathVariable String noHp) {
        return this.userService.getUserByNoHp(noHp)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/namecontain/{nama}")
    public ResponseEntity<List<User>> getUserByNamaContaining(@PathVariable String nama) {
        return ResponseEntity.ok(this.userService.getUserByNamaContaining(nama));
    }
}
