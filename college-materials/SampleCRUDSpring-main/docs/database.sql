-- ============================================================
-- SQL SETUP: Sistem Perpustakaan
-- Jalankan file ini di MySQL sebelum menjalankan aplikasi
-- ============================================================

-- Buat database
CREATE DATABASE IF NOT EXISTS db_perpustakaan
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE db_perpustakaan;

-- ============================================================
-- TABEL 1: anggota
-- ============================================================
CREATE TABLE IF NOT EXISTS anggota (
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    nama      VARCHAR(100) NOT NULL,
    email     VARCHAR(150) NOT NULL UNIQUE,
    no_hp     VARCHAR(15)  NOT NULL
);

-- ============================================================
-- TABEL 2: profil (ONE TO ONE dengan anggota)
-- Setiap anggota punya tepat satu profil.
-- Kolom anggota_id adalah FOREIGN KEY ke tabel anggota.
-- ============================================================
CREATE TABLE IF NOT EXISTS profil (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    alamat          TEXT         NOT NULL,
    tanggal_lahir   DATE,
    jenis_kelamin   CHAR(1),          -- 'L' atau 'P'
    anggota_id      BIGINT NOT NULL UNIQUE,   -- UNIQUE = memaksa relasi One to One
    FOREIGN KEY (anggota_id) REFERENCES anggota(id) ON DELETE CASCADE
);

-- ============================================================
-- TABEL 3: kategori
-- ============================================================
CREATE TABLE IF NOT EXISTS kategori (
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    nama      VARCHAR(100) NOT NULL UNIQUE,
    deskripsi TEXT
);

-- ============================================================
-- TABEL 4: buku
-- ============================================================
CREATE TABLE IF NOT EXISTS buku (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    judul        VARCHAR(255) NOT NULL,
    penulis      VARCHAR(150) NOT NULL,
    isbn         VARCHAR(20)  NOT NULL UNIQUE,
    tahun_terbit INT,
    stok         INT NOT NULL DEFAULT 0
);

-- ============================================================
-- TABEL PIVOT: buku_kategori (MANY TO MANY antara buku & kategori)
-- Tabel perantara ini menghubungkan buku dengan kategori.
-- Satu buku bisa ada di banyak baris, begitu pula kategori.
--
-- Contoh isi tabel:
--   buku_id=1, kategori_id=1  → Buku 1 masuk Kategori 1
--   buku_id=1, kategori_id=2  → Buku 1 juga masuk Kategori 2
--   buku_id=2, kategori_id=1  → Buku 2 masuk Kategori 1
-- ============================================================
CREATE TABLE IF NOT EXISTS buku_kategori (
    buku_id     BIGINT NOT NULL,
    kategori_id BIGINT NOT NULL,
    PRIMARY KEY (buku_id, kategori_id),   -- kombinasi keduanya harus unik
    FOREIGN KEY (buku_id)     REFERENCES buku(id)     ON DELETE CASCADE,
    FOREIGN KEY (kategori_id) REFERENCES kategori(id) ON DELETE CASCADE
);

-- ============================================================
-- TABEL 5: peminjaman (ONE TO MANY dari anggota dan buku)
-- Setiap record = satu transaksi peminjaman.
-- Kolom anggota_id dan buku_id adalah FOREIGN KEY.
-- ============================================================
CREATE TABLE IF NOT EXISTS peminjaman (
    id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    anggota_id           BIGINT       NOT NULL,
    buku_id              BIGINT       NOT NULL,
    tanggal_pinjam       DATE         NOT NULL,
    tanggal_harus_kembali DATE        NOT NULL,
    tanggal_kembali      DATE,                       -- NULL = belum dikembalikan
    status               VARCHAR(20)  NOT NULL DEFAULT 'DIPINJAM',
    FOREIGN KEY (anggota_id) REFERENCES anggota(id),
    FOREIGN KEY (buku_id)    REFERENCES buku(id)
);


-- ============================================================
-- DATA DUMMY
-- ============================================================

-- Data Kategori
INSERT INTO kategori (nama, deskripsi) VALUES
('Fiksi',       'Novel dan cerita imajinatif'),
('Non-Fiksi',   'Buku berdasarkan fakta dan kenyataan'),
('Sains',       'Ilmu pengetahuan alam dan teknologi'),
('Pemrograman', 'Buku tentang coding dan software'),
('Sejarah',     'Kisah dan peristiwa masa lalu'),
('Petualangan', 'Kisah perjalanan dan eksplorasi');

-- Data Buku
INSERT INTO buku (judul, penulis, isbn, tahun_terbit, stok) VALUES
('Laskar Pelangi',          'Andrea Hirata',     '978-979-1652-00-8', 2005, 5),
('Bumi Manusia',            'Pramoedya Ananta',  '978-979-407-313-5', 1980, 3),
('Clean Code',              'Robert C. Martin',  '978-0-13-235088-4', 2008, 4),
('Python untuk Pemula',     'Budi Raharjo',      '978-602-7950-12-3', 2019, 6),
('Sapiens',                 'Yuval Noah Harari', '978-0-06-231609-7', 2011, 2),
('Harry Potter dan Batu Bertuah', 'J.K. Rowling','978-979-22-8755-1', 1997, 8),
('The Pragmatic Programmer','David Thomas',      '978-0-13-595705-9', 1999, 3),
('Pulang',                  'Tere Liye',         '978-602-7870-42-4', 2012, 5);

-- Relasi Buku - Kategori (MANY TO MANY)
-- Laskar Pelangi: Fiksi + Petualangan
INSERT INTO buku_kategori (buku_id, kategori_id) VALUES (1, 1), (1, 6);
-- Bumi Manusia: Fiksi + Sejarah
INSERT INTO buku_kategori (buku_id, kategori_id) VALUES (2, 1), (2, 5);
-- Clean Code: Pemrograman + Non-Fiksi
INSERT INTO buku_kategori (buku_id, kategori_id) VALUES (3, 4), (3, 2);
-- Python untuk Pemula: Pemrograman + Sains
INSERT INTO buku_kategori (buku_id, kategori_id) VALUES (4, 4), (4, 3);
-- Sapiens: Sejarah + Non-Fiksi
INSERT INTO buku_kategori (buku_id, kategori_id) VALUES (5, 5), (5, 2);
-- Harry Potter: Fiksi + Petualangan
INSERT INTO buku_kategori (buku_id, kategori_id) VALUES (6, 1), (6, 6);
-- The Pragmatic Programmer: Pemrograman
INSERT INTO buku_kategori (buku_id, kategori_id) VALUES (7, 4);
-- Pulang: Fiksi
INSERT INTO buku_kategori (buku_id, kategori_id) VALUES (8, 1);

-- Data Anggota
INSERT INTO anggota (nama, email, no_hp) VALUES
('Budi Santoso',    'budi@email.com',   '081234567890'),
('Siti Rahayu',     'siti@email.com',   '082345678901'),
('Ahmad Fauzi',     'ahmad@email.com',  '083456789012'),
('Dewi Lestari',    'dewi@email.com',   '084567890123'),
('Raka Pratama',    'raka@email.com',   '085678901234');

-- Data Profil (ONE TO ONE dengan Anggota)
INSERT INTO profil (alamat, tanggal_lahir, jenis_kelamin, anggota_id) VALUES
('Jl. Merdeka No. 10, Bandung',    '2000-05-15', 'L', 1),
('Jl. Sudirman No. 25, Jakarta',   '2001-08-22', 'P', 2),
('Jl. Diponegoro No. 5, Surabaya', '1999-12-01', 'L', 3),
('Jl. Gajah Mada No. 18, Medan',   '2002-03-10', 'P', 4),
('Jl. Imam Bonjol No. 7, Semarang','2000-11-30', 'L', 5);

-- Data Peminjaman
INSERT INTO peminjaman (anggota_id, buku_id, tanggal_pinjam, tanggal_harus_kembali, tanggal_kembali, status) VALUES
(1, 1, '2026-05-10', '2026-05-17', '2026-05-16', 'DIKEMBALIKAN'),
(2, 3, '2026-05-15', '2026-05-22', NULL, 'DIPINJAM'),
(3, 4, '2026-05-18', '2026-05-25', NULL, 'DIPINJAM'),
(1, 6, '2026-05-20', '2026-05-27', NULL, 'DIPINJAM'),
(4, 2, '2026-04-30', '2026-05-07', '2026-05-10', 'TERLAMBAT');

-- ============================================================
-- VERIFIKASI DATA
-- ============================================================
SELECT 'ANGGOTA'   AS tabel, COUNT(*) AS jumlah FROM anggota   UNION ALL
SELECT 'PROFIL',            COUNT(*)            FROM profil     UNION ALL
SELECT 'BUKU',              COUNT(*)            FROM buku       UNION ALL
SELECT 'KATEGORI',          COUNT(*)            FROM kategori   UNION ALL
SELECT 'BUKU_KATEGORI',     COUNT(*)            FROM buku_kategori UNION ALL
SELECT 'PEMINJAMAN',        COUNT(*)            FROM peminjaman;
