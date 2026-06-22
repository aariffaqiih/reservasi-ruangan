package com.tup.data.repository;

import com.tup.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // SELECT * FROM users WHERE email = ?
    Optional<User> findByEmail(String email);

    // SELECT * FROM users WHERE name LIKE '%keyword%'
    List<User> findByNameContaining(String keyword);

    // CUSTOM QUERY JPQL
    @Query("SELECT u FROM User u WHERE u.phone = :phone")
    Optional<User> findUserByPhone(@Param("phone") String phone);

    // NATIVE QUERY (SQL murni)
    @Query(value = "SELECT * FROM users WHERE created_at >= :date", nativeQuery = true)
    List<User> findUsersCreatedAfter(@Param("date") java.time.LocalDateTime date);

}
