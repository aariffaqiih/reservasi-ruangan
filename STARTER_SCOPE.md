# Starter Scope

<!--
  Penanggung jawab: seluruh anggota kelompok.
  Jobdesk: jaga batas starter agar implementasi fitur tetap sesuai pembagian kerja.
-->

Starter ini hanya menyiapkan autentikasi berbasis database untuk tiga role:

- `MAHASISWA`
- `ADMIN`
- `SATPAM`

## Yang Sudah Dibuat

- database MySQL `reservasi_ruang`
- migration tabel autentikasi `users`
- schema database domain lengkap sesuai blueprint
- data dummy relasional untuk seluruh tabel domain
- seed tiga akun login starter dengan password BCrypt
- form login HTML mentah
- redirect dashboard berdasarkan role
- pembatasan akses halaman antar-role
- halaman placeholder mahasiswa, admin, dan satpam
- scaffold package backend lengkap untuk entity, repository, service, DTO, controller, exception, dan utilitas
- scaffold template HTML per-role, fragment Thymeleaf bersama, JavaScript per-role, dan slot test per modul
- peta kerja anggota pada `DEVELOPMENT_MAP.md`

## Akun Starter

| Role | Username | Password default |
| --- | --- | --- |
| Mahasiswa | `mhs` | `mhs` |
| Admin | `adm` | `adm` |
| Satpam | `stm` | `stm` |

Password default hanya untuk development awal dan dapat diganti melalui environment variable pada `application.properties`.

## Sengaja Belum Dibuat

Domain dan fitur berikut tetap menjadi pekerjaan anggota sesuai workbook:

- entity domain `User`, `Mahasiswa`, `Admin`, dan `Satpam`
- profil user dan registrasi mahasiswa
- ruang dan ketersediaan ruang
- reservasi dan status reservasi
- approval
- access record, check-in, dan check-out
- notifikasi
- REST API fitur bisnis
- UI selain login dan placeholder role

Schema database sudah disiapkan agar setiap owner modul dapat langsung mengembangkan entity, repository, service, dan controller pada tabel domain yang relevan. Keberadaan tabel dan data dummy tidak berarti fitur bisnisnya sudah diimplementasikan pada source code.

## Menjalankan Dengan MySQL

1. Jalankan `database/create_database.sql` melalui phpMyAdmin.
2. Pastikan MySQL Server aktif.
3. Atur `DB_USERNAME` dan `DB_PASSWORD` jika berbeda dari default lokal.
4. Jalankan `.\mvnw.cmd spring-boot:run`.

Flyway akan membuat dan memigrasikan tabel domain saat aplikasi dijalankan.

## Aturan Pengembangan

- Jangan mengedit migration Flyway `V1` sampai `V6` yang sudah diterapkan.
- Tambahkan migration `V7__nama_perubahan.sql` dan seterusnya untuk perubahan database baru.
- Isi placeholder milik masing-masing anggota tanpa memindahkan file jika tidak diperlukan.
- Gunakan `DEVELOPMENT_MAP.md` sebagai peta struktur starter.
