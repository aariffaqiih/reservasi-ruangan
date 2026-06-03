package com.tup.reservasi.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.tup.reservasi.entity.Mahasiswa;
import com.tup.reservasi.repository.UserRepository;

/*
 * Penanggung jawab: Amelia Sofiana Makharomi.
 *
 * Arahan:
 * - File ini hanya membuat tiga akun starter untuk role Mahasiswa, Admin, dan Satpam.
 * - Data ini membantu menguji isolasi halaman role sebelum fitur domain dibuat.
 * - Saat entity User/Mahasiswa/Admin/Satpam final sudah dibuat,
 *   sepakati apakah data starter tetap dipakai atau diganti seeding final.
 */
@Component
public class StarterUserSeeder implements ApplicationRunner {

    private final LoginUserRepository loginUserRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final boolean enabled;
    private final String mahasiswaUsername;
    private final String mahasiswaPassword;
    private final String adminUsername;
    private final String adminPassword;
    private final String satpamUsername;
    private final String satpamPassword;

    public StarterUserSeeder(
            LoginUserRepository loginUserRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.starter-users.enabled}") boolean enabled,
            @Value("${app.starter-users.mahasiswa.username}") String mahasiswaUsername,
            @Value("${app.starter-users.mahasiswa.password}") String mahasiswaPassword,
            @Value("${app.starter-users.admin.username}") String adminUsername,
            @Value("${app.starter-users.admin.password}") String adminPassword,
            @Value("${app.starter-users.satpam.username}") String satpamUsername,
            @Value("${app.starter-users.satpam.password}") String satpamPassword) {
        this.loginUserRepository = loginUserRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.enabled = enabled;
        this.mahasiswaUsername = mahasiswaUsername;
        this.mahasiswaPassword = mahasiswaPassword;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
        this.satpamUsername = satpamUsername;
        this.satpamPassword = satpamPassword;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!enabled) {
            return;
        }

        createIfMissing(mahasiswaUsername, mahasiswaPassword, UserRole.MAHASISWA);
        createIfMissing(adminUsername, adminPassword, UserRole.ADMIN);
        createIfMissing(satpamUsername, satpamPassword, UserRole.SATPAM);
        createMahasiswaDomainIfMissing();
    }

    private void createIfMissing(String username, String password, UserRole role) {
        if (loginUserRepository.findByUsername(username).isEmpty()) {
            loginUserRepository.save(new LoginUser(username, passwordEncoder.encode(password), role));
        }
    }
    private void createMahasiswaDomainIfMissing() {
        if (userRepository.findById(mahasiswaUsername).isEmpty()) {
            Mahasiswa mahasiswa = new Mahasiswa(
                    mahasiswaUsername,
                    "Mahasiswa Starter",
                    "mhs@starter.local",
                    "080000000001",
                    passwordEncoder.encode(mahasiswaPassword),
                    mahasiswaUsername,
                    "Teknik Informatika",
                    2026);
            userRepository.save(mahasiswa);
        }
    }
}
