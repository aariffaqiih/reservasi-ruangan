-- =============================================
-- DATABASE SETUP
-- Jalankan script ini sebelum start aplikasi
-- =============================================

CREATE DATABASE IF NOT EXISTS db_crud
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE db_crud;

-- Tabel akan dibuat otomatis oleh Hibernate (ddl-auto=update)
-- Script ini hanya untuk membuat database

-- =============================================
-- CONTOH DATA (opsional, jalankan setelah app start)
-- =============================================

-- INSERT users
-- INSERT INTO users (username, email, password, created_at, updated_at)
-- VALUES
--   ('admin', 'admin@example.com', 'admin123', NOW(), NOW()),
--   ('budi', 'budi@example.com', 'budi123', NOW(), NOW()),
--   ('sari', 'sari@example.com', 'sari123', NOW(), NOW());

-- INSERT profiles (FK ke users)
-- INSERT INTO profiles (user_id, full_name, phone, gender, address, created_at, updated_at)
-- VALUES
--   (1, 'Administrator', '081234567890', 'Laki-laki', 'Jakarta', NOW(), NOW()),
--   (2, 'Budi Santoso', '082345678901', 'Laki-laki', 'Bandung', NOW(), NOW());

-- INSERT posts (FK ke users)
-- INSERT INTO posts (user_id, title, content, status, created_at, updated_at)
-- VALUES
--   (1, 'Hello World', 'Konten pertama saya', 'PUBLISHED', NOW(), NOW()),
--   (2, 'Belajar Spring Boot', 'Spring Boot sangat mudah', 'DRAFT', NOW(), NOW()),
--   (2, 'Tutorial JPA', 'JPA memudahkan akses database', 'PUBLISHED', NOW(), NOW());
