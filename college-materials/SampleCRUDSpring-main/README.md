# 📚 Sistem Perpustakaan — Project Pembelajaran Spring Boot

Project ini dirancang untuk membantu mahasiswa memahami konsep dasar
**Web Development dengan Spring Boot**, mencakup CRUD dan tiga jenis relasi database.

---

## 🎯 Tujuan Pembelajaran

| Materi | Implementasi dalam Project |
|---|---|
| CRUD Lengkap | Buku, Anggota, Kategori, Peminjaman |
| Koneksi MySQL | Spring Data JPA + Hibernate |
| Relasi One To One | Anggota ↔ Profil |
| Relasi One To Many | Anggota → Peminjaman, Buku → Peminjaman |
| Relasi Many To Many | Buku ↔ Kategori |
| Validasi Form | @NotBlank, @Email, @Pattern, @Min |
| Thymeleaf | th:each, th:field, th:if, th:href |
| Bootstrap 5 | Navbar, Tabel, Form, Badge, Alert |

---

## 🗂 Struktur Project

```
perpustakaan-app/
├── src/main/java/com/belajar/perpustakaan/
│   ├── PerpustakaanApplication.java       ← Entry point
│   │
│   ├── entity/                            ← Model / Tabel Database
│   │   ├── Anggota.java                   ← One To One → Profil
│   │   ├── Profil.java                    ← Sisi owning relasi
│   │   ├── Buku.java                      ← Many To Many ↔ Kategori
│   │   ├── Kategori.java                  ← Many To Many ↔ Buku
│   │   └── Peminjaman.java               ← Many To One ← Anggota, Buku
│   │
│   ├── repository/                        ← Akses Database
│   │   ├── AnggotaRepository.java
│   │   ├── BukuRepository.java
│   │   ├── KategoriRepository.java
│   │   ├── ProfilRepository.java
│   │   └── PeminjamanRepository.java
│   │
│   ├── service/                           ← Logika Bisnis
│   │   ├── AnggotaService.java
│   │   ├── BukuService.java
│   │   ├── KategoriService.java
│   │   └── PeminjamanService.java
│   │
│   └── controller/                        ← Handler Request HTTP
│       ├── HomeController.java
│       ├── BukuController.java
│       ├── AnggotaController.java
│       ├── KategoriController.java
│       └── PeminjamanController.java
│
├── src/main/resources/
│   ├── application.properties             ← Konfigurasi DB & Server
│   └── templates/                         ← Halaman HTML (Thymeleaf)
│       ├── index.html                     ← Dashboard
│       ├── buku/
│       │   ├── list.html
│       │   └── form.html
│       ├── anggota/
│       │   ├── list.html
│       │   ├── form.html
│       │   └── detail.html
│       ├── peminjaman/
│       │   ├── list.html
│       │   └── form.html
│       └── kategori/
│           └── list.html
│
├── docs/
│   └── database.sql                       ← SQL setup + data dummy
└── pom.xml                                ← Dependensi Maven
```

---

## 🗄 ERD (Entity Relationship Diagram)

```
┌─────────────┐       ┌─────────────┐
│   ANGGOTA   │ 1   1 │    PROFIL   │
│─────────────│───────│─────────────│
│ id (PK)     │       │ id (PK)     │
│ nama        │       │ alamat      │
│ email       │       │ tgl_lahir   │
│ no_hp       │       │ jenis_klmn  │
└─────────────┘       │ anggota_id  │ ← FK (UNIQUE)
        │             └─────────────┘
        │ 1
        │ (One To Many)
        │ *
┌───────────────────┐
│    PEMINJAMAN     │
│───────────────────│
│ id (PK)           │
│ anggota_id (FK)   │ ← Many To One → ANGGOTA
│ buku_id    (FK)   │ ← Many To One → BUKU
│ tgl_pinjam        │
│ tgl_harus_kembali │
│ tgl_kembali       │
│ status            │
└───────────────────┘
        * ↑
        │ (Many To One)
        │ 1
┌─────────────┐       ┌──────────────────┐       ┌─────────────┐
│    BUKU     │ *   * │   BUKU_KATEGORI  │ *   * │   KATEGORI  │
│─────────────│───────│──────────────────│───────│─────────────│
│ id (PK)     │       │ buku_id     (FK) │       │ id (PK)     │
│ judul       │       │ kategori_id (FK) │       │ nama        │
│ penulis     │       └──────────────────┘       │ deskripsi   │
│ isbn        │                                   └─────────────┘
│ tahun_terbit│
│ stok        │
└─────────────┘
```

---

## ⚙️ Cara Menjalankan Project

### 1. Persiapan Database

```bash
# Masuk ke MySQL
mysql -u root -p

# Jalankan script SQL
source /path/ke/docs/database.sql
```

Atau buka file `docs/database.sql` di MySQL Workbench / phpMyAdmin dan jalankan.

### 2. Konfigurasi Aplikasi

Edit file `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/db_perpustakaan
spring.datasource.username=root
spring.datasource.password=ISI_PASSWORD_MYSQL_KAMU
```

### 3. Jalankan Aplikasi

**Via IntelliJ IDEA:**
- Klik kanan `PerpustakaanApplication.java` → Run

**Via Maven (Terminal):**
```bash
mvn spring-boot:run
```

### 4. Buka di Browser

```
http://localhost:8080
```

---

## 🔗 URL / Routing Lengkap

| Method | URL | Fungsi |
|---|---|---|
| GET | `/` | Dashboard |
| GET | `/buku` | Daftar semua buku |
| GET | `/buku/tambah` | Form tambah buku |
| POST | `/buku/simpan` | Simpan buku baru |
| GET | `/buku/edit/{id}` | Form edit buku |
| POST | `/buku/update` | Update buku |
| GET | `/buku/hapus/{id}` | Hapus buku |
| GET | `/anggota` | Daftar semua anggota |
| GET | `/anggota/tambah` | Form daftar anggota |
| POST | `/anggota/simpan` | Simpan anggota baru |
| GET | `/anggota/detail/{id}` | Detail + riwayat anggota |
| GET | `/anggota/edit/{id}` | Form edit anggota |
| POST | `/anggota/update` | Update anggota |
| GET | `/anggota/hapus/{id}` | Hapus anggota |
| GET | `/peminjaman` | Daftar semua peminjaman |
| GET | `/peminjaman/tambah` | Form peminjaman baru |
| POST | `/peminjaman/simpan` | Catat peminjaman |
| GET | `/peminjaman/kembalikan/{id}` | Proses pengembalian |
| GET | `/peminjaman/hapus/{id}` | Hapus record |
| GET | `/kategori` | Daftar + form tambah kategori |
| POST | `/kategori/simpan` | Simpan kategori |
| GET | `/kategori/hapus/{id}` | Hapus kategori |

---

## 💡 Poin Penting untuk Dipelajari

### Relasi One To One (Anggota ↔ Profil)

```java
// Di class Anggota:
@OneToOne(mappedBy = "anggota", cascade = CascadeType.ALL)
private Profil profil;

// Di class Profil:
@OneToOne
@JoinColumn(name = "anggota_id")  // ← FK ada di tabel profil
private Anggota anggota;
```

### Relasi One To Many (Anggota → Peminjaman)

```java
// Di class Anggota (sisi "one"):
@OneToMany(mappedBy = "anggota", cascade = CascadeType.ALL)
private List<Peminjaman> peminjaman;

// Di class Peminjaman (sisi "many"):
@ManyToOne
@JoinColumn(name = "anggota_id")  // ← FK ada di tabel peminjaman
private Anggota anggota;
```

### Relasi Many To Many (Buku ↔ Kategori)

```java
// Di class Buku (sisi yang mendefinisikan tabel pivot):
@ManyToMany
@JoinTable(
    name = "buku_kategori",
    joinColumns = @JoinColumn(name = "buku_id"),
    inverseJoinColumns = @JoinColumn(name = "kategori_id")
)
private List<Kategori> kategoriList;

// Di class Kategori (sisi referensi):
@ManyToMany(mappedBy = "kategoriList")
private List<Buku> bukuList;
```

### Alur Data di Spring Boot

```
Browser → HTTP Request
    ↓
Controller (menerima request, memanggil service)
    ↓
Service (logika bisnis: validasi, kalkulasi, dll)
    ↓
Repository (query ke database)
    ↓
Database MySQL
    ↓ (data kembali ke atas)
Controller → mengirim data ke Thymeleaf (Model)
    ↓
Thymeleaf → render HTML
    ↓
Browser ← HTTP Response (halaman HTML)
```

---

## ❓ Pertanyaan Diskusi untuk Mahasiswa

1. Mengapa kita butuh tabel `profil` terpisah dari `anggota`? Kapan relasi One-to-One berguna?
2. Apa yang terjadi pada data `peminjaman` jika anggota dihapus? (Petunjuk: lihat `cascade`)
3. Mengapa ada tabel `buku_kategori`? Apa yang akan terjadi jika kita coba simpan relasi Many-to-Many tanpa tabel pivot?
4. Apa perbedaan `@OneToMany` dan `@ManyToOne`? Siapa yang menyimpan Foreign Key?
5. Apa kegunaan `mappedBy` dalam anotasi relasi?

---

*Project ini dibuat untuk keperluan pembelajaran di kelas. Dipersilakan dimodifikasi sesuai kebutuhan.*
