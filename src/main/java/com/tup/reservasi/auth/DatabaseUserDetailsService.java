package com.tup.reservasi.auth;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/*
 * Penanggung jawab: Amelia Sofiana Makharomi.
 *
 * Arahan:
 * - File ini adalah penghubung login database ke Spring Security.
 * - Behaviour yang berkaitan dengan class-diagram:
 *   User.login(): boolean.
 * - Implementasi final boleh tetap memakai service ini selama data User/role
 *   sudah sinkron dengan entity domain yang dikerjakan tim.
 */
@Service
public class DatabaseUserDetailsService implements UserDetailsService {

    private final LoginUserRepository loginUserRepository;

    public DatabaseUserDetailsService(LoginUserRepository loginUserRepository) {
        this.loginUserRepository = loginUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LoginUser loginUser = loginUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username tidak ditemukan"));

        return User.withUsername(loginUser.getUsername())
                .password(loginUser.getPasswordHash())
                .roles(loginUser.getRole().name())
                .build();
    }
}
