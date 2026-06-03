package com.example.crud.controller;

import com.example.crud.model.Profile;
import com.example.crud.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST API Controller untuk Profile
 * Base URL: /api/profiles
 */
@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileRestController {

    private final ProfileService profileService;

    // GET /api/profiles
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAll() {
        List<Profile> profiles = profileService.findAll();
        return ok("Berhasil mengambil data profiles", profiles);
    }

    // GET /api/profiles/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable Long id) {
        Profile profile = profileService.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile dengan ID " + id + " tidak ditemukan!"));
        return ok("Berhasil mengambil data profile", profile);
    }

    // GET /api/profiles/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getByUserId(@PathVariable Long userId) {
        Profile profile = profileService.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile untuk User ID " + userId + " tidak ditemukan!"));
        return ok("Berhasil mengambil profile user", profile);
    }

    // POST /api/profiles?userId={userId}
    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Profile profile,
                                                       @RequestParam Long userId) {
        Profile saved = profileService.save(profile, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response(true, "Profile berhasil dibuat!", saved));
    }

    // PUT /api/profiles/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id,
                                                       @RequestBody Profile profile) {
        Profile updated = profileService.update(id, profile);
        return ok("Profile berhasil diupdate!", updated);
    }

    // DELETE /api/profiles/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        profileService.delete(id);
        return ok("Profile berhasil dihapus!", null);
    }

    private ResponseEntity<Map<String, Object>> ok(String message, Object data) {
        return ResponseEntity.ok(response(true, message, data));
    }

    private Map<String, Object> response(boolean success, String message, Object data) {
        Map<String, Object> res = new HashMap<>();
        res.put("success", success);
        res.put("message", message);
        res.put("data", data);
        return res;
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleError(RuntimeException e) {
        Map<String, Object> res = new HashMap<>();
        res.put("success", false);
        res.put("message", e.getMessage());
        res.put("data", null);
        return ResponseEntity.badRequest().body(res);
    }
}
