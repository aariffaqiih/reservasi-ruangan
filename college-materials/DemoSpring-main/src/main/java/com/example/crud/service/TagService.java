package com.example.crud.service;

import com.example.crud.model.Post;
import com.example.crud.model.Tag;
import com.example.crud.repository.PostRepository;
import com.example.crud.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class TagService {

    private final TagRepository tagRepository;
    private final PostRepository postRepository;

    public Tag save(Tag tag) {
        if (tagRepository.existsByName(tag.getName())) {
            throw new RuntimeException("Tag '" + tag.getName() + "' sudah ada!");
        }
        return tagRepository.save(tag);
    }

    @Transactional(readOnly = true)
    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Tag> findById(Long id) {
        return tagRepository.findById(id);
    }

    public Tag update(Long id, Tag tagDetails) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag dengan ID " + id + " tidak ditemukan!"));
        if (!tag.getName().equals(tagDetails.getName()) &&
                tagRepository.existsByName(tagDetails.getName())) {
            throw new RuntimeException("Tag '" + tagDetails.getName() + "' sudah ada!");
        }
        tag.setName(tagDetails.getName());
        tag.setColor(tagDetails.getColor());
        return tagRepository.save(tag);
    }

    public void delete(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag dengan ID " + id + " tidak ditemukan!"));
        // Lepas relasi many-to-many dari semua post sebelum hapus
        for (Post post : new HashSet<>(tag.getPosts())) {
            post.getTags().remove(tag);
            postRepository.save(post);
        }
        tagRepository.deleteById(id);
    }

    public Post addTagToPost(Long postId, Long tagId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post tidak ditemukan!"));
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("Tag tidak ditemukan!"));
        post.getTags().add(tag);
        return postRepository.save(post);
    }

    public Post removeTagFromPost(Long postId, Long tagId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post tidak ditemukan!"));
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("Tag tidak ditemukan!"));
        post.getTags().remove(tag);
        return postRepository.save(post);
    }

    public Post setTagsForPost(Long postId, Set<Long> tagIds) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post tidak ditemukan!"));
        Set<Tag> tags = tagIds == null || tagIds.isEmpty()
                ? new HashSet<>()
                : new HashSet<>(tagRepository.findAllById(tagIds));
        post.setTags(tags);
        return postRepository.save(post);
    }

    // Hitung jumlah post per tag via query — hindari lazy loading di template
    @Transactional(readOnly = true)
    public Map<Long, Long> getPostCountPerTag() {
        List<Tag> tags = tagRepository.findAll();
        Map<Long, Long> countMap = new HashMap<>();
        for (Tag tag : tags) {
            countMap.put(tag.getId(), tagRepository.countPostsByTagId(tag.getId()));
        }
        return countMap;
    }

    public long count() {
        return tagRepository.count();
    }
}
