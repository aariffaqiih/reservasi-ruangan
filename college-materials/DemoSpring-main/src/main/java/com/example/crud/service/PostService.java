package com.example.crud.service;

import com.example.crud.model.Post;
import com.example.crud.model.User;
import com.example.crud.repository.PostRepository;
import com.example.crud.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // =============================================
    // CREATE
    // =============================================
    public Post save(Post post, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User dengan ID " + userId + " tidak ditemukan!"));

        post.setUser(user);
        return postRepository.save(post);
    }

    // =============================================
    // READ
    // =============================================
    @Transactional(readOnly = true)
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Post> findByUserId(Long userId) {
        return postRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Post> findByStatus(Post.PostStatus status) {
        return postRepository.findByStatus(status);
    }

    // =============================================
    // UPDATE
    // =============================================
    public Post update(Long id, Post postDetails) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post dengan ID " + id + " tidak ditemukan!"));

        post.setTitle(postDetails.getTitle());
        post.setContent(postDetails.getContent());
        post.setStatus(postDetails.getStatus());

        return postRepository.save(post);
    }

    // =============================================
    // DELETE
    // =============================================
    public void delete(Long id) {
        if (!postRepository.existsById(id)) {
            throw new RuntimeException("Post dengan ID " + id + " tidak ditemukan!");
        }
        postRepository.deleteById(id);
    }

    public long count() {
        return postRepository.count();
    }
}
