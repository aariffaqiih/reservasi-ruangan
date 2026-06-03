package com.example.crud.repository;

import com.example.crud.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByUserId(Long userId);

    List<Post> findByStatus(Post.PostStatus status);

    @Query("SELECT p FROM Post p JOIN FETCH p.user WHERE p.user.id = :userId ORDER BY p.createdAt DESC")
    List<Post> findByUserIdWithUser(Long userId);

    long countByUserId(Long userId);
}
