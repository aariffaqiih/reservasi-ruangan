package com.example.crud.controller;

import com.example.crud.model.Post;
import com.example.crud.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST API Controller untuk Post
 * Base URL: /api/posts
 */
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostRestController {

    private final PostService postService;

    // GET /api/posts
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAll() {
        List<Post> posts = postService.findAll();
        return ok("Berhasil mengambil data posts", posts);
    }

    // GET /api/posts/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable Long id) {
        Post post = postService.findById(id)
                .orElseThrow(() -> new RuntimeException("Post dengan ID " + id + " tidak ditemukan!"));
        return ok("Berhasil mengambil data post", post);
    }

    // GET /api/posts/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getByUserId(@PathVariable Long userId) {
        List<Post> posts = postService.findByUserId(userId);
        return ok("Berhasil mengambil posts user", posts);
    }

    // POST /api/posts?userId={userId}
    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody Post post,
                                                       @RequestParam Long userId) {
        Post saved = postService.save(post, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response(true, "Post berhasil dibuat!", saved));
    }

    // PUT /api/posts/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id,
                                                       @Valid @RequestBody Post post) {
        Post updated = postService.update(id, post);
        return ok("Post berhasil diupdate!", updated);
    }

    // DELETE /api/posts/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        postService.delete(id);
        return ok("Post berhasil dihapus!", null);
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
