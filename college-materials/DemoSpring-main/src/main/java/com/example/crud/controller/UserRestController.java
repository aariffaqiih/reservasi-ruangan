package com.example.crud.controller;

import com.example.crud.model.User;
import com.example.crud.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST API Controller untuk User
 * Base URL: /api/users
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    // GET /api/users
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAll() {
        List<User> users = userService.findAll();
        return ok("Berhasil mengambil data users", users);
    }

    // GET /api/users/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable Long id) {
        User user = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("User dengan ID " + id + " tidak ditemukan!"));
        return ok("Berhasil mengambil data user", user);
    }

    // POST /api/users
    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody User user) {
        User saved = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response(true, "User berhasil dibuat!", saved));
    }

    // PUT /api/users/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id,
                                                       @Valid @RequestBody User user) {
        User updated = userService.update(id, user);
        return ok("User berhasil diupdate!", updated);
    }

    // DELETE /api/users/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        userService.delete(id);
        return ok("User berhasil dihapus!", null);
    }

    // =============================================
    // Helper methods
    // =============================================
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

    // Global exception handler for this controller
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleError(RuntimeException e) {
        Map<String, Object> res = new HashMap<>();
        res.put("success", false);
        res.put("message", e.getMessage());
        res.put("data", null);
        return ResponseEntity.badRequest().body(res);
    }
}
