package com.example.crud.repository;

import com.example.crud.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(String name);

    boolean existsByName(String name);

    // Cari tag berdasarkan nama (partial match)
    List<Tag> findByNameContainingIgnoreCase(String keyword);

    // Hitung berapa post yang pakai tag ini
    @Query("SELECT COUNT(p) FROM Post p JOIN p.tags t WHERE t.id = :tagId")
    long countPostsByTagId(Long tagId);
}
