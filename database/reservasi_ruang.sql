-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 22 Jun 2026 pada 12.13
-- Versi server: 10.4.32-MariaDB
-- Versi PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `reservasi_ruang`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `access_records`
--

CREATE TABLE `access_records` (
  `record_id` bigint(20) NOT NULL,
  `catatan_pelanggaran` varchar(255) DEFAULT NULL,
  `check_in_time` datetime(6) DEFAULT NULL,
  `check_out_time` datetime(6) DEFAULT NULL,
  `reservation_id` bigint(20) DEFAULT NULL,
  `satpam_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `access_records`
--

INSERT INTO `access_records` (`record_id`, `catatan_pelanggaran`, `check_in_time`, `check_out_time`, `reservation_id`, `satpam_id`) VALUES
(1, NULL, '2026-06-01 07:55:00.000000', '2026-06-01 10:10:00.000000', 1, 14),
(2, NULL, '2026-06-02 12:58:00.000000', '2026-06-02 15:05:00.000000', 2, 15),
(3, 'Terdapat coret-coretan pada whiteboard, sudah diperingatkan.', '2026-06-03 09:02:00.000000', '2026-06-03 11:15:00.000000', 3, 14),
(4, NULL, '2026-06-04 14:05:00.000000', '2026-06-04 16:30:00.000000', 4, 15),
(5, NULL, '2026-06-05 10:01:00.000000', '2026-06-05 12:00:00.000000', 5, 16),
(6, NULL, '2026-06-22 10:05:00.000000', NULL, 18, 14);

-- --------------------------------------------------------

--
-- Struktur dari tabel `admins`
--

CREATE TABLE `admins` (
  `unit_kerja` varchar(100) DEFAULT NULL,
  `id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `admins`
--

INSERT INTO `admins` (`unit_kerja`, `id`) VALUES
('Bagian Akademik', 11),
('Kemahasiswaan', 12),
('Sarana dan Prasarana', 13);

-- --------------------------------------------------------

--
-- Struktur dari tabel `approvals`
--

CREATE TABLE `approvals` (
  `approval_id` bigint(20) NOT NULL,
  `catatan` varchar(255) DEFAULT NULL,
  `keputusan` enum('REVISI','SETUJUI','TOLAK') DEFAULT NULL,
  `reviewed_at` datetime(6) DEFAULT NULL,
  `admin_id` bigint(20) DEFAULT NULL,
  `reservation_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `approvals`
--

INSERT INTO `approvals` (`approval_id`, `catatan`, `keputusan`, `reviewed_at`, `admin_id`, `reservation_id`) VALUES
(1, 'Ruangan tersedia, silakan gunakan dengan tertib.', 'SETUJUI', '2026-05-29 10:00:00.000000', 11, 1),
(2, 'Disetujui untuk kegiatan kemahasiswaan.', 'SETUJUI', '2026-05-31 08:30:00.000000', 12, 2),
(3, 'Pastikan kondisi ruangan baik setelah digunakan.', 'SETUJUI', '2026-06-01 09:00:00.000000', 13, 3),
(4, 'Seminar disetujui. Mohon konfirmasi jumlah peserta H-1.', 'SETUJUI', '2026-06-02 11:00:00.000000', 11, 4),
(5, 'Ruang bisa digunakan.', 'SETUJUI', '2026-06-03 08:00:00.000000', 12, 5),
(6, 'Disetujui. Koordinasikan setup dengan bagian sarana.', 'SETUJUI', '2026-06-11 10:00:00.000000', 13, 6),
(7, 'Disetujui untuk rapat BEM.', 'SETUJUI', '2026-06-13 09:00:00.000000', 12, 7),
(8, 'Pelatihan disetujui.', 'SETUJUI', '2026-06-15 13:00:00.000000', 11, 8),
(9, 'Seminar riset disetujui. Koordinasi dengan dosen pembimbing.', 'SETUJUI', '2026-06-16 10:00:00.000000', 11, 9),
(10, 'Jadwal melampaui jam operasional. Gunakan fasilitas eksternal.', 'TOLAK', '2026-06-11 14:00:00.000000', 12, 14),
(11, 'Jam 07.00 sebelum jam buka gedung (08.00). Ajukan ulang.', 'TOLAK', '2026-06-13 10:00:00.000000', 13, 15),
(12, 'Sertakan daftar peserta dan surat dari dosen pembimbing.', 'REVISI', '2026-06-15 09:00:00.000000', 11, 16);

-- --------------------------------------------------------

--
-- Struktur dari tabel `mahasiswa`
--

CREATE TABLE `mahasiswa` (
  `angkatan` int(11) DEFAULT NULL,
  `nim` varchar(30) DEFAULT NULL,
  `prodi` varchar(100) DEFAULT NULL,
  `id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `mahasiswa`
--

INSERT INTO `mahasiswa` (`angkatan`, `nim`, `prodi`, `id`) VALUES
(2021, 'H1A021001', 'Bisnis Digital', 1),
(2021, 'H1A021002', 'Bisnis Digital', 2),
(2022, 'H1A022001', 'Sistem Informasi', 3),
(2022, 'H1A022002', 'Sistem Informasi', 4),
(2020, 'H1A020001', 'Teknik Informatika', 5),
(2023, 'H1A023001', 'Bisnis Digital', 6),
(2021, 'H1A021003', 'Teknik Informatika', 7),
(2022, 'H1A022003', 'Sistem Informasi', 8),
(2023, 'H1A023002', 'Teknik Informatika', 9),
(2020, 'H1A020002', 'Bisnis Digital', 10);

-- --------------------------------------------------------

--
-- Struktur dari tabel `notifications`
--

CREATE TABLE `notifications` (
  `notification_id` bigint(20) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `pesan` varchar(255) DEFAULT NULL,
  `status_baca` bit(1) DEFAULT NULL,
  `penerima_id` bigint(20) DEFAULT NULL,
  `reservation_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `notifications`
--

INSERT INTO `notifications` (`notification_id`, `created_at`, `pesan`, `status_baca`, `penerima_id`, `reservation_id`) VALUES
(1, '2026-05-29 10:01:00.000000', 'Reservasi Anda pada 01 Juni 2026 telah DISETUJUI.', b'1', 1, 1),
(2, '2026-05-31 08:31:00.000000', 'Reservasi Anda pada 02 Juni 2026 telah DISETUJUI.', b'1', 3, 2),
(3, '2026-06-01 09:01:00.000000', 'Reservasi Anda pada 03 Juni 2026 telah DISETUJUI.', b'1', 5, 3),
(4, '2026-06-02 11:01:00.000000', 'Reservasi Anda pada 04 Juni 2026 telah DISETUJUI.', b'1', 7, 4),
(5, '2026-06-03 08:01:00.000000', 'Reservasi Anda pada 05 Juni 2026 telah DISETUJUI.', b'1', 2, 5),
(6, '2026-06-11 10:01:00.000000', 'Reservasi Anda pada 25 Juni 2026 telah DISETUJUI.', b'1', 1, 6),
(7, '2026-06-13 09:01:00.000000', 'Reservasi Anda pada 26 Juni 2026 telah DISETUJUI.', b'0', 4, 7),
(8, '2026-06-15 13:01:00.000000', 'Reservasi Anda pada 27 Juni 2026 telah DISETUJUI.', b'0', 6, 8),
(9, '2026-06-16 10:01:00.000000', 'Reservasi Anda pada 28 Juni 2026 telah DISETUJUI.', b'0', 9, 9),
(10, '2026-06-20 08:01:00.000000', 'Reservasi Anda sedang menunggu persetujuan admin.', b'1', 2, 10),
(11, '2026-06-20 10:01:00.000000', 'Reservasi Anda sedang menunggu persetujuan admin.', b'0', 8, 11),
(12, '2026-06-21 09:31:00.000000', 'Reservasi Anda sedang menunggu persetujuan admin.', b'0', 10, 12),
(13, '2026-06-21 14:01:00.000000', 'Reservasi Anda sedang menunggu persetujuan admin.', b'0', 3, 13),
(14, '2026-06-11 14:01:00.000000', 'Reservasi Anda DITOLAK. Alasan: Jadwal melampaui jam operasional.', b'1', 4, 14),
(15, '2026-06-13 10:01:00.000000', 'Reservasi Anda DITOLAK. Alasan: Jam pengajuan sebelum jam buka gedung (08.00).', b'1', 7, 15),
(16, '2026-06-15 09:01:00.000000', 'Reservasi Anda memerlukan REVISI. Sertakan daftar peserta dan surat dosen pembimbing.', b'1', 5, 16),
(17, '2026-06-16 09:01:00.000000', 'Reservasi Anda pada 20 Juni 2026 telah DIBATALKAN.', b'1', 5, 16),
(18, '2026-06-19 10:01:00.000000', 'Reservasi Anda pada 22 Juni 2026 telah DIBATALKAN.', b'1', 6, 17),
(19, '2026-06-22 09:30:00.000000', 'Reservasi Anda hari ini (22 Juni 2026 pukul 10.00) segera dimulai.', b'1', 1, 18),
(20, '2026-06-20 08:02:00.000000', 'Pengajuan baru dari Budi Prasetyo (Orientasi Mahasiswa Baru) menunggu persetujuan Anda.', b'0', 11, 10),
(21, '2026-06-20 10:02:00.000000', 'Pengajuan baru dari Hana Lestari (Rapat Panitia KKN) menunggu persetujuan Anda.', b'0', 12, 11),
(22, '2026-06-21 09:32:00.000000', 'Pengajuan baru dari Julia Sari (Praktikum Basis Data) menunggu persetujuan Anda.', b'0', 13, 12),
(23, '2026-06-21 14:02:00.000000', 'Pengajuan baru dari Citra Dewi (Diskusi Tugas SI) menunggu persetujuan Anda.', b'0', 12, 13);

-- --------------------------------------------------------

--
-- Struktur dari tabel `reservations`
--

CREATE TABLE `reservations` (
  `reservation_id` bigint(20) NOT NULL,
  `cancelled_at` datetime(6) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `jam_mulai` time DEFAULT NULL,
  `jam_selesai` time DEFAULT NULL,
  `status` enum('ACTIVE','APPROVED','CANCELLED','COMPLETED','DRAFT','PENDING','REJECTED') DEFAULT NULL,
  `tanggal` date DEFAULT NULL,
  `tujuan` varchar(255) DEFAULT NULL,
  `mahasiswa_id` bigint(20) DEFAULT NULL,
  `room_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `reservations`
--

INSERT INTO `reservations` (`reservation_id`, `cancelled_at`, `created_at`, `jam_mulai`, `jam_selesai`, `status`, `tanggal`, `tujuan`, `mahasiswa_id`, `room_id`) VALUES
(1, NULL, '2026-05-28 09:00:00.000000', '08:00:00', '10:00:00', 'COMPLETED', '2026-06-01', 'Presentasi Tugas Akhir Kelompok', 1, 10503),
(2, NULL, '2026-05-30 10:15:00.000000', '13:00:00', '15:00:00', 'COMPLETED', '2026-06-02', 'Rapat Koordinasi Himpunan', 3, 40501),
(3, NULL, '2026-05-31 14:00:00.000000', '09:00:00', '11:00:00', 'COMPLETED', '2026-06-03', 'Workshop Pemrograman Python', 5, 20202),
(4, NULL, '2026-06-01 08:30:00.000000', '14:00:00', '16:00:00', 'COMPLETED', '2026-06-04', 'Seminar Kewirausahaan Mahasiswa', 7, 30202),
(5, NULL, '2026-06-02 11:00:00.000000', '10:00:00', '12:00:00', 'COMPLETED', '2026-06-05', 'Diskusi Penelitian Skripsi', 2, 20602),
(6, NULL, '2026-06-10 09:00:00.000000', '08:00:00', '17:00:00', 'APPROVED', '2026-06-25', 'Wisuda Prodi Bisnis Digital', 1, 30502),
(7, NULL, '2026-06-12 13:00:00.000000', '10:00:00', '12:00:00', 'APPROVED', '2026-06-26', 'Rapat BEM Fakultas', 4, 10403),
(8, NULL, '2026-06-14 10:30:00.000000', '13:00:00', '15:00:00', 'APPROVED', '2026-06-27', 'Pelatihan Desain Grafis', 6, 20403),
(9, NULL, '2026-06-15 08:00:00.000000', '09:00:00', '11:00:00', 'APPROVED', '2026-06-28', 'Seminar Riset Mahasiswa', 9, 30501),
(10, NULL, '2026-06-20 08:00:00.000000', '08:00:00', '12:00:00', 'PENDING', '2026-07-01', 'Orientasi Mahasiswa Baru 2026', 2, 10203),
(11, NULL, '2026-06-20 10:00:00.000000', '13:00:00', '15:00:00', 'PENDING', '2026-07-02', 'Rapat Panitia KKN Gelombang II', 8, 10103),
(12, NULL, '2026-06-21 09:30:00.000000', '09:00:00', '11:00:00', 'PENDING', '2026-07-03', 'Praktikum Basis Data', 10, 20601),
(13, NULL, '2026-06-21 14:00:00.000000', '14:00:00', '16:00:00', 'PENDING', '2026-07-04', 'Diskusi Tugas Kelompok SI', 3, 40201),
(14, NULL, '2026-06-10 15:00:00.000000', '08:00:00', '20:00:00', 'REJECTED', '2026-06-15', 'Konser Musik Kampus', 4, 20201),
(15, NULL, '2026-06-12 16:00:00.000000', '07:00:00', '09:00:00', 'REJECTED', '2026-06-18', 'Belajar Mandiri (jam non-aktif)', 7, 20102),
(16, '2026-06-16 09:00:00.000000', '2026-06-14 11:00:00.000000', '10:00:00', '12:00:00', 'CANCELLED', '2026-06-20', 'Seminar Tamu dari Industri', 5, 10502),
(17, '2026-06-19 10:00:00.000000', '2026-06-18 13:00:00.000000', '13:00:00', '15:00:00', 'CANCELLED', '2026-06-22', 'Pertemuan Kelompok Riset', 6, 30602),
(18, NULL, '2026-06-20 09:00:00.000000', '10:00:00', '12:00:00', 'ACTIVE', '2026-06-22', 'Diskusi Proposal Penelitian', 1, 20103),
(19, NULL, '2026-06-22 08:00:00.000000', '09:00:00', '11:00:00', 'DRAFT', '2026-07-10', 'Workshop IoT', 9, 10603),
(20, NULL, '2026-06-22 09:00:00.000000', '14:00:00', '16:00:00', 'DRAFT', '2026-07-15', 'Rapat Proyek Capstone', 10, 30302);

-- --------------------------------------------------------

--
-- Struktur dari tabel `rooms`
--

CREATE TABLE `rooms` (
  `room_id` bigint(20) NOT NULL,
  `gedung` varchar(100) NOT NULL,
  `kapasitas` int(11) NOT NULL,
  `nama_ruang` varchar(100) NOT NULL,
  `status_aktif` bit(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `rooms`
--

INSERT INTO `rooms` (`room_id`, `gedung`, `kapasitas`, `nama_ruang`, `status_aktif`) VALUES
(10102, 'DC', 50, 'Ruang Kelas', b'1'),
(10103, 'IoT', 40, 'Ruang Kelas', b'1'),
(10104, 'TT', 50, 'Ruang Kelas', b'1'),
(10202, 'DC', 45, 'Ruang Kelas', b'0'),
(10203, 'IoT', 30, 'Ruang Kelas', b'1'),
(10204, 'TT', 35, 'Ruang Kelas', b'1'),
(10302, 'DC', 40, 'Ruang Kelas', b'1'),
(10303, 'IoT', 30, 'Ruang Kelas', b'1'),
(10304, 'TT', 35, 'Ruang Kelas', b'1'),
(10402, 'DC', 50, 'Ruang Kelas', b'1'),
(10403, 'IoT', 45, 'Ruang Kelas', b'1'),
(10404, 'TT', 30, 'Ruang Kelas', b'1'),
(10502, 'DC', 40, 'Ruang Kelas', b'1'),
(10503, 'IoT', 35, 'Ruang Kelas', b'1'),
(10504, 'TT', 30, 'Ruang Kelas', b'1'),
(10602, 'DC', 30, 'Ruang Kelas', b'0'),
(10603, 'IoT', 50, 'Ruang Kelas', b'1'),
(10604, 'TT', 35, 'Ruang Kelas', b'1'),
(20101, 'Rektorat', 30, 'Ruang Kelas', b'0'),
(20102, 'DC', 35, 'Ruang Kelas', b'1'),
(20103, 'IoT', 40, 'Ruang Kelas', b'1'),
(20104, 'TT', 45, 'Ruang Kelas', b'1'),
(20201, 'Rektorat', 40, 'Ruang Kelas', b'1'),
(20202, 'DC', 30, 'Ruang Kelas', b'1'),
(20203, 'IoT', 45, 'Ruang Kelas', b'1'),
(20204, 'TT', 45, 'Ruang Kelas', b'1'),
(20301, 'Rektorat', 35, 'Ruang Kelas', b'1'),
(20302, 'DC', 30, 'Ruang Kelas', b'1'),
(20303, 'IoT', 45, 'Ruang Kelas', b'1'),
(20304, 'TT', 45, 'Ruang Kelas', b'1'),
(20401, 'Rektorat', 50, 'Ruang Kelas', b'0'),
(20402, 'DC', 45, 'Ruang Kelas', b'1'),
(20403, 'IoT', 35, 'Ruang Kelas', b'1'),
(20404, 'TT', 50, 'Ruang Kelas', b'1'),
(20501, 'Rektorat', 45, 'Ruang Kelas', b'0'),
(20502, 'DC', 40, 'Ruang Kelas', b'1'),
(20503, 'IoT', 50, 'Ruang Kelas', b'1'),
(20504, 'TT', 30, 'Ruang Kelas', b'1'),
(20601, 'Rektorat', 30, 'Ruang Kelas', b'1'),
(20602, 'DC', 40, 'Ruang Kelas', b'1'),
(20603, 'IoT', 50, 'Ruang Kelas', b'1'),
(20604, 'TT', 30, 'Ruang Kelas', b'1'),
(30101, 'Rektorat', 50, 'Ruang Kelas', b'1'),
(30102, 'DC', 40, 'Ruang Kelas', b'1'),
(30104, 'TT', 50, 'Ruang Kelas', b'1'),
(30105, 'DSP', 30, 'Ruang Kelas', b'1'),
(30201, 'Rektorat', 50, 'Ruang Kelas', b'1'),
(30202, 'DC', 30, 'Ruang Kelas', b'1'),
(30204, 'TT', 40, 'Ruang Kelas', b'1'),
(30205, 'DSP', 40, 'Ruang Kelas', b'1'),
(30301, 'Rektorat', 50, 'Ruang Kelas', b'1'),
(30302, 'DC', 35, 'Ruang Kelas', b'1'),
(30304, 'TT', 45, 'Ruang Kelas', b'1'),
(30305, 'DSP', 50, 'Ruang Kelas', b'1'),
(30401, 'Rektorat', 45, 'Ruang Kelas', b'1'),
(30402, 'DC', 35, 'Ruang Kelas', b'1'),
(30404, 'TT', 30, 'Ruang Kelas', b'1'),
(30405, 'DSP', 35, 'Ruang Kelas', b'1'),
(30501, 'Rektorat', 30, 'Ruang Kelas', b'1'),
(30502, 'DC', 45, 'Ruang Kelas', b'1'),
(30504, 'TT', 40, 'Ruang Kelas', b'1'),
(30505, 'DSP', 35, 'Ruang Kelas', b'1'),
(30601, 'Rektorat', 35, 'Ruang Kelas', b'1'),
(30602, 'DC', 50, 'Ruang Kelas', b'1'),
(30604, 'TT', 35, 'Ruang Kelas', b'1'),
(30605, 'DSP', 50, 'Ruang Kelas', b'1'),
(40101, 'Rektorat', 40, 'Ruang Kelas', b'1'),
(40105, 'DSP', 50, 'Ruang Kelas', b'1'),
(40201, 'Rektorat', 35, 'Ruang Kelas', b'1'),
(40205, 'DSP', 30, 'Ruang Kelas', b'1'),
(40301, 'Rektorat', 40, 'Ruang Kelas', b'1'),
(40305, 'DSP', 40, 'Ruang Kelas', b'1'),
(40401, 'Rektorat', 45, 'Ruang Kelas', b'0'),
(40405, 'DSP', 40, 'Ruang Kelas', b'1'),
(40501, 'Rektorat', 40, 'Ruang Kelas', b'1'),
(40505, 'DSP', 35, 'Ruang Kelas', b'1'),
(40601, 'Rektorat', 30, 'Ruang Kelas', b'1'),
(40605, 'DSP', 30, 'Ruang Kelas', b'0'),
(50105, 'DSP', 45, 'Ruang Kelas', b'1'),
(50205, 'DSP', 50, 'Ruang Kelas', b'1'),
(50305, 'DSP', 35, 'Ruang Kelas', b'1'),
(50405, 'DSP', 50, 'Ruang Kelas', b'1'),
(50505, 'DSP', 50, 'Ruang Kelas', b'1'),
(50605, 'DSP', 45, 'Ruang Kelas', b'1');

-- --------------------------------------------------------

--
-- Struktur dari tabel `satpam`
--

CREATE TABLE `satpam` (
  `pos_jaga` varchar(100) DEFAULT NULL,
  `shift` varchar(50) DEFAULT NULL,
  `id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `satpam`
--

INSERT INTO `satpam` (`pos_jaga`, `shift`, `id`) VALUES
('Pos Utama Rektorat', 'PAGI', 14),
('Pos Utama Rektorat', 'SIANG', 15),
('Pos Gedung DC', 'MALAM', 16);

-- --------------------------------------------------------

--
-- Struktur dari tabel `users`
--

CREATE TABLE `users` (
  `id` bigint(20) NOT NULL,
  `email` varchar(120) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `no_hp` varchar(20) DEFAULT NULL,
  `password_hash` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `users`
--

INSERT INTO `users` (`id`, `email`, `nama`, `no_hp`, `password_hash`) VALUES
(1, 'andi.saputra@student.telkomuniversity.ac.id', 'Andi Saputra', '081234567801', '$2a$12$dummyhashandi001'),
(2, 'budi.prasetyo@student.telkomuniversity.ac.id', 'Budi Prasetyo', '081234567802', '$2a$12$dummyhashbudi002'),
(3, 'citra.dewi@student.telkomuniversity.ac.id', 'Citra Dewi', '081234567803', '$2a$12$dummyhashcitra003'),
(4, 'dian.rahayu@student.telkomuniversity.ac.id', 'Dian Rahayu', '081234567804', '$2a$12$dummyhashdian004'),
(5, 'eko.nugroho@student.telkomuniversity.ac.id', 'Eko Nugroho', '081234567805', '$2a$12$dummyhasheko005'),
(6, 'fitri.handayani@student.telkomuniversity.ac.id', 'Fitri Handayani', '081234567806', '$2a$12$dummyhashfitri006'),
(7, 'galih.wibowo@student.telkomuniversity.ac.id', 'Galih Wibowo', '081234567807', '$2a$12$dummyhashgalih007'),
(8, 'hana.lestari@student.telkomuniversity.ac.id', 'Hana Lestari', '081234567808', '$2a$12$dummyhashhana008'),
(9, 'irfan.maulana@student.telkomuniversity.ac.id', 'Irfan Maulana', '081234567809', '$2a$12$dummyhashirfan009'),
(10, 'julia.sari@student.telkomuniversity.ac.id', 'Julia Sari', '081234567810', '$2a$12$dummyhashjulia010'),
(11, 'admin.akademik@telkomuniversity.ac.id', 'Bambang Sulistyo', '082111110011', '$2a$12$dummyhashbambang011'),
(12, 'admin.kemahasiswaan@telkomuniversity.ac.id', 'Siti Nurhaliza', '082111110012', '$2a$12$dummyhashsiti012'),
(13, 'admin.sarana@telkomuniversity.ac.id', 'Rudi Hartono', '082111110013', '$2a$12$dummyhashrudi013'),
(14, 'satpam.agus@telkomuniversity.ac.id', 'Agus Triyono', '083222220014', '$2a$12$dummyhashagus014'),
(15, 'satpam.joko@telkomuniversity.ac.id', 'Joko Susilo', '083222220015', '$2a$12$dummyhashjoko015'),
(16, 'satpam.budi@telkomuniversity.ac.id', 'Budi Santoso', '083222220016', '$2a$12$dummyhashbudis016');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `access_records`
--
ALTER TABLE `access_records`
  ADD PRIMARY KEY (`record_id`),
  ADD UNIQUE KEY `UKqvbar9m11wsnif8marciimhjl` (`reservation_id`),
  ADD KEY `FKagilhn88xmdqt0quhp467lnq2` (`satpam_id`);

--
-- Indeks untuk tabel `admins`
--
ALTER TABLE `admins`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `approvals`
--
ALTER TABLE `approvals`
  ADD PRIMARY KEY (`approval_id`),
  ADD UNIQUE KEY `UKcsu61tsk15413aja8tstwkyf2` (`reservation_id`),
  ADD KEY `FKqbpbaa25c2ijcp6qbfdhrnpvx` (`admin_id`);

--
-- Indeks untuk tabel `mahasiswa`
--
ALTER TABLE `mahasiswa`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKkvm6yjgxjs9vo3qhqsjog1a1p` (`nim`);

--
-- Indeks untuk tabel `notifications`
--
ALTER TABLE `notifications`
  ADD PRIMARY KEY (`notification_id`),
  ADD KEY `FKfnr99tgqpyiqye94yq9ibrkqf` (`penerima_id`),
  ADD KEY `FKeua0pytgljr86idnpkhuir1mu` (`reservation_id`);

--
-- Indeks untuk tabel `reservations`
--
ALTER TABLE `reservations`
  ADD PRIMARY KEY (`reservation_id`),
  ADD KEY `FKlv2j39e95g1ly0caudbxolfe4` (`mahasiswa_id`),
  ADD KEY `FKljt6q1tp205b0h26eiegc5mx6` (`room_id`);

--
-- Indeks untuk tabel `rooms`
--
ALTER TABLE `rooms`
  ADD PRIMARY KEY (`room_id`);

--
-- Indeks untuk tabel `satpam`
--
ALTER TABLE `satpam`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`),
  ADD UNIQUE KEY `UKnv2aaueu0jye4m5s9hwftk42n` (`no_hp`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `access_records`
--
ALTER TABLE `access_records`
  MODIFY `record_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT untuk tabel `approvals`
--
ALTER TABLE `approvals`
  MODIFY `approval_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT untuk tabel `notifications`
--
ALTER TABLE `notifications`
  MODIFY `notification_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- AUTO_INCREMENT untuk tabel `reservations`
--
ALTER TABLE `reservations`
  MODIFY `reservation_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT untuk tabel `rooms`
--
ALTER TABLE `rooms`
  MODIFY `room_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=50606;

--
-- AUTO_INCREMENT untuk tabel `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `access_records`
--
ALTER TABLE `access_records`
  ADD CONSTRAINT `FK6t7k2p3411j9fc8moti0ida7l` FOREIGN KEY (`reservation_id`) REFERENCES `reservations` (`reservation_id`),
  ADD CONSTRAINT `FKagilhn88xmdqt0quhp467lnq2` FOREIGN KEY (`satpam_id`) REFERENCES `satpam` (`id`);

--
-- Ketidakleluasaan untuk tabel `admins`
--
ALTER TABLE `admins`
  ADD CONSTRAINT `FKanhsicqm3lc8ya77tr7r0je18` FOREIGN KEY (`id`) REFERENCES `users` (`id`);

--
-- Ketidakleluasaan untuk tabel `approvals`
--
ALTER TABLE `approvals`
  ADD CONSTRAINT `FKl7lrh88tkc98o7jtiytsw0ovh` FOREIGN KEY (`reservation_id`) REFERENCES `reservations` (`reservation_id`),
  ADD CONSTRAINT `FKqbpbaa25c2ijcp6qbfdhrnpvx` FOREIGN KEY (`admin_id`) REFERENCES `admins` (`id`);

--
-- Ketidakleluasaan untuk tabel `mahasiswa`
--
ALTER TABLE `mahasiswa`
  ADD CONSTRAINT `FKrhp8pqsnd14pws4quc8w4mowr` FOREIGN KEY (`id`) REFERENCES `users` (`id`);

--
-- Ketidakleluasaan untuk tabel `notifications`
--
ALTER TABLE `notifications`
  ADD CONSTRAINT `FKeua0pytgljr86idnpkhuir1mu` FOREIGN KEY (`reservation_id`) REFERENCES `reservations` (`reservation_id`),
  ADD CONSTRAINT `FKfnr99tgqpyiqye94yq9ibrkqf` FOREIGN KEY (`penerima_id`) REFERENCES `users` (`id`);

--
-- Ketidakleluasaan untuk tabel `reservations`
--
ALTER TABLE `reservations`
  ADD CONSTRAINT `FKljt6q1tp205b0h26eiegc5mx6` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`room_id`),
  ADD CONSTRAINT `FKlv2j39e95g1ly0caudbxolfe4` FOREIGN KEY (`mahasiswa_id`) REFERENCES `mahasiswa` (`id`);

--
-- Ketidakleluasaan untuk tabel `satpam`
--
ALTER TABLE `satpam`
  ADD CONSTRAINT `FKcyk7u2lbj3sndxoh3mf05o4d1` FOREIGN KEY (`id`) REFERENCES `users` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
