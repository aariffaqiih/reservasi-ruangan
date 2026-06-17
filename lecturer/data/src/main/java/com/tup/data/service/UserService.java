package com.tup.data.service;

import com.tup.data.entity.User;
import com.tup.data.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return this.userRepository.findById(id).orElseThrow(()->new RuntimeException("User tidak ditemukan"));
    }

    public Optional<User> getUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public Optional<User> getUserByPhone(String phone) {
        return this.userRepository.findUserByPhone(phone);
    }

    public List<User> getUserByNameContaining(String name){
        return this.userRepository.findByNameContaining(name);
    }

    public User createUser(User user) {
        return this.userRepository.save(user);
    }

    public User updateUser(Long id, User updatedData) {
        User userExisting=getUserById(id);
        userExisting.setName(updatedData.getName());
        userExisting.setPhone(updatedData.getPhone());
        return this.userRepository.save(userExisting);
    }

    public void deleteUser(Long id) {
        User userExisting=getUserById(id);
        this.userRepository.delete(userExisting);
    }

    public Long countUsers() {
        return this.userRepository.count();
    }

    public List<User> getUsersCreatedAfter(LocalDateTime date) {
        return userRepository.findUsersCreatedAfter(date);
    }
}
