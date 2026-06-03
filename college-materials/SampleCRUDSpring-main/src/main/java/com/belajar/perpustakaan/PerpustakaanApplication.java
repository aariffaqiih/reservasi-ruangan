package com.belajar.perpustakaan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ==============================================
 * ENTRY POINT - Titik awal aplikasi Spring Boot
 * ==============================================
 * Anotasi @SpringBootApplication = gabungan dari:
 *   - @Configuration
 *   - @EnableAutoConfiguration
 *   - @ComponentScan
 */
@SpringBootApplication
public class PerpustakaanApplication {

    public static void main(String[] args) {
        SpringApplication.run(PerpustakaanApplication.class, args);
        System.out.println("===========================================");
        System.out.println("  Aplikasi Perpustakaan berhasil berjalan!");
        System.out.println("  Buka: http://localhost:8080");
        System.out.println("===========================================");
    }
}
