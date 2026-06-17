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

import com.tup.reservasi.entity.Satpam;
import com.tup.reservasi.entity.User;
import com.tup.reservasi.service.UserService;

/*
 * Penanggung jawab: Tadzkiroh Aziziyah Haqia - 103112400242.
 * Modul: Satpam.
 */
@RestController
@RequestMapping("/api/satpam")
public class SatpamRestController {

    private final UserService userService;

    public SatpamRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<Satpam>> getAllSatpam() {
        List<Satpam> satpam = this.userService.getAllUsers().stream()
                .filter(Satpam.class::isInstance)
                .map(Satpam.class::cast)
                .toList();
        return ResponseEntity.ok(satpam);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Satpam> getSatpamById(@PathVariable Long id) {
        try {
            User user = this.userService.getUserById(id);
            if (user instanceof Satpam satpam) {
                return ResponseEntity.ok(satpam);
            }
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Satpam> createSatpam(@RequestBody Satpam satpam) {
        Satpam userCreated = (Satpam) this.userService.createUser(satpam);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Satpam> updateSatpam(@PathVariable Long id, @RequestBody Satpam satpam) {
        try {
            return ResponseEntity.ok(this.userService.updateSatpam(id, satpam));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSatpam(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
    }
}
