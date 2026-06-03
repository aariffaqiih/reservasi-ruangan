# Peta File Pengembangan

Dokumen ini memetakan file starter agar implementasi fitur dapat dilakukan tanpa
membuat folder baru di tengah development.

## Acuan

Urutan acuan ketika ada perbedaan versi dokumentasi:

1. `src/main/resources/static/utilities/BLUEPRINT.txt` untuk target arsitektur terbaru.
2. `src/main/resources/static/utilities/class-diagram.png` untuk domain inti.
3. `src/main/resources/static/utilities/Reservasi_Ruang_SpringBoot_MVC_Blueprint.xlsx`
   untuk pembagian anggota dan traceability jobdesk.
4. Permintaan terbaru: struktur code mengikuti style dosen untuk koneksi database,
   sedangkan file fitur/domain disiapkan sebagai template kosong agar dikerjakan anggota.

## Modul Anggota

| Anggota | Fokus | Folder utama |
| --- | --- | --- |
| Amelia Sofiana Makharomi | `User`, `Mahasiswa`, autentikasi, profil | `entity`, `service`, `controller`, `templates/mahasiswa`, `templates/users` |
| Atha Muyassar | `Admin`, kontrak `Notifiable`, approval UI | `entity`, `controller`, `templates/admin`, `templates/approvals` |
| Tadzkiroh Aziziyah Haqia | `Satpam`, `AccessRecord`, `AccessControlService` | `entity`, `service`, `controller`, `templates/satpam`, `templates/access` |
| Ali Abdul Fattah 'Alim Kautsar | `Room`, availability, pengajuan reservasi | `entity`, `repository`, `service`, `templates/rooms`, `templates/reservations` |
| 'Aarif Rahmaan Jalaluddin Faqiih | `Reservation`, `Approval`, status, approval service | `entity`, `enums`, `repository`, `service`, `templates/reservations`, `templates/approvals` |
| Ajda Mutiara Zahra | `Notification`, `NotificationService` | `entity`, `repository`, `service`, `templates/notifications` |

## Folder Siap Pakai

- `src/main/java/com/tup/reservasi/config`
- `src/main/java/com/tup/reservasi/controller`
- `src/main/java/com/tup/reservasi/controller/rest`
- `src/main/java/com/tup/reservasi/dto`
- `src/main/java/com/tup/reservasi/entity`
- `src/main/java/com/tup/reservasi/enums`
- `src/main/java/com/tup/reservasi/exception`
- `src/main/java/com/tup/reservasi/repository`
- `src/main/java/com/tup/reservasi/service`
- `src/main/java/com/tup/reservasi/util`
- `src/main/resources/templates/fragments`
- `src/main/resources/templates/layout`
- `src/main/resources/templates/access`
- `src/main/resources/templates/admin`
- `src/main/resources/templates/approvals`
- `src/main/resources/templates/mahasiswa`
- `src/main/resources/templates/notifications`
- `src/main/resources/templates/reservations`
- `src/main/resources/templates/rooms`
- `src/main/resources/templates/satpam`
- `src/main/resources/templates/users`
- `src/main/resources/static/css`
- `src/main/resources/static/js/admin`
- `src/main/resources/static/js/mahasiswa`
- `src/main/resources/static/js/satpam`
- `src/test/java/com/tup/reservasi/entity`
- `src/test/java/com/tup/reservasi/exception`
- `src/test/java/com/tup/reservasi/repository`
- `src/test/java/com/tup/reservasi/service`
- `src/test/java/com/tup/reservasi/util`

## Aturan Database

Project sekarang mengikuti style koneksi database dosen: konfigurasi MySQL ada di
`application.properties`, driver MySQL dipakai langsung, dan Hibernate memakai
`spring.jpa.hibernate.ddl-auto=update` untuk kebutuhan belajar.

Yang aktif saat ini hanya starter autentikasi dengan tabel `users`. Entity,
repository, service, DTO, controller fitur, dan template fitur lainnya sengaja
masih berupa file template berkomentar agar implementasi tetap dikerjakan
anggota sesuai jobdesk.

<!--
  Penanggung jawab: seluruh anggota kelompok.
  Jobdesk: jaga peta file agar pembagian kerja tetap jelas.
-->
