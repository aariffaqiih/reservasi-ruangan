package com.tup.reservasi.controller;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/*
 * Penanggung jawab: Amelia Sofiana Makharomi.
 *
 * Arahan:
 * - Controller ini hanya starter untuk login dan redirect role.
 * - Role diarahkan ke dashboard sesuai turunan User pada class-diagram:
 *   Mahasiswa, Admin, dan Satpam.
 * - Controller fitur final tetap ada di MahasiswaController, AdminController,
 *   dan SatpamController agar jobdesk anggota tidak tercampur.
 */
@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication) {
        Set<String> authorities = authentication.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.toSet());

        if (authorities.contains("ROLE_MAHASISWA")) {
            return "redirect:/mahasiswa/dashboard";
        }
        if (authorities.contains("ROLE_ADMIN")) {
            return "redirect:/admin/dashboard";
        }
        if (authorities.contains("ROLE_SATPAM")) {
            return "redirect:/satpam/dashboard";
        }

        return "redirect:/login?error";
    }

    @GetMapping("/mahasiswa/dashboard")
    public String mahasiswaDashboard() {
        return "mahasiswa/dashboard";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/satpam/dashboard")
    public String satpamDashboard() {
        return "satpam/dashboard";
    }
}
