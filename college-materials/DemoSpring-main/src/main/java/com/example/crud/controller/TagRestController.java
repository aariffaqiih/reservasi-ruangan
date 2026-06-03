package com.example.crud.controller;

import com.example.crud.model.Post;
import com.example.crud.model.Tag;
import com.example.crud.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * REST API Controller untuk Tag
 * Base URL: /api/tags
 */
@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagRestController {

    private final TagService tagService;

    // GET /api/tags
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAll() {
        List<Tag> tags = tagService.findAll();
        return ok("Berhasil mengambil data tags", tags);
    }

    // GET /api/tags/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable Long id) {
        Tag tag = tagService.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag tidak ditemukan!"));
        return ok("Berhasil mengambil data tag", tag);
    }

    // POST /api/tags
    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody Tag tag) {
        Tag saved = tagService.save(tag);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response(true, "Tag berhasil dibuat!", saved));
    }

    // PUT /api/tags/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id,
                                                       @Valid @RequestBody Tag tag) {
        Tag updated = tagService.update(id, tag);
        return ok("Tag berhasil diupdate!", updated);
    }

    // DELETE /api/tags/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        tagService.delete(id);
        return ok("Tag berhasil dihapus!", null);
    }

    // =============================================
    // MANY-TO-MANY ENDPOINTS
    // =============================================

    // POST /api/tags/post/{postId}/add/{tagId}
    // Tambah 1 tag ke post
    @PostMapping("/post/{postId}/add/{tagId}")
    public ResponseEntity<Map<String, Object>> addTagToPost(@PathVariable Long postId,
                                                             @PathVariable Long tagId) {
        Post post = tagService.addTagToPost(postId, tagId);
        return ok("Tag berhasil ditambahkan ke post!", post);
    }

    // DELETE /api/tags/post/{postId}/remove/{tagId}
    // Hapus 1 tag dari post
    @DeleteMapping("/post/{postId}/remove/{tagId}")
    public ResponseEntity<Map<String, Object>> removeTagFromPost(@PathVariable Long postId,
                                                                  @PathVariable Long tagId) {
        Post post = tagService.removeTagFromPost(postId, tagId);
        return ok("Tag berhasil dihapus dari post!", post);
    }

    // PUT /api/tags/post/{postId}/set
    // Set semua tag untuk sebuah post (kirim array tagId)
    // Body: [1, 2, 3]
    @PutMapping("/post/{postId}/set")
    public ResponseEntity<Map<String, Object>> setTagsForPost(@PathVariable Long postId,
                                                               @RequestBody Set<Long> tagIds) {
        Post post = tagService.setTagsForPost(postId, tagIds);
        return ok("Tags berhasil diset untuk post!", post);
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
