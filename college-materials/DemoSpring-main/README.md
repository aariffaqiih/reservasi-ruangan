# 🚀 SpringBoot CRUD - MySQL + Relasi JPA

Project Spring Boot CRUD lengkap dengan REST API dan Web View (Thymeleaf).

## 📦 Teknologi
- **Spring Boot** 3.2.0
- **Spring Data JPA** + Hibernate
- **MySQL** Database
- **Thymeleaf** (View Engine)
- **Bootstrap 5** + **Font Awesome 6** + **jQuery 3.7**
- **Lombok**
- **Validation**

---

## 🗂️ Struktur Tabel & Relasi

```
users (tabel utama)
 ├── profiles  → ONE-TO-ONE  (profiles.user_id FK → users.id)
 └── posts     → ONE-TO-MANY (posts.user_id    FK → users.id)
```

### Tabel `users`
| Kolom | Tipe | Keterangan |
|-------|------|------------|
| id | BIGINT PK | Auto increment |
| username | VARCHAR(50) | Unique |
| email | VARCHAR(100) | Unique |
| password | VARCHAR(255) | |
| created_at | DATETIME | |
| updated_at | DATETIME | |

### Tabel `profiles` (One-to-One dengan users)
| Kolom | Tipe | Keterangan |
|-------|------|------------|
| id | BIGINT PK | Auto increment |
| user_id | BIGINT FK | UNIQUE → users.id |
| full_name | VARCHAR(100) | |
| phone | VARCHAR(20) | |
| address | TEXT | |
| birth_date | DATE | |
| gender | VARCHAR(10) | |
| bio | TEXT | |
| avatar_url | VARCHAR(255) | |
| created_at | DATETIME | |
| updated_at | DATETIME | |

### Tabel `posts` (One-to-Many dari users)
| Kolom | Tipe | Keterangan |
|-------|------|------------|
| id | BIGINT PK | Auto increment |
| user_id | BIGINT FK | → users.id |
| title | VARCHAR(255) | |
| content | TEXT | |
| status | ENUM | DRAFT, PUBLISHED, ARCHIVED |
| created_at | DATETIME | |
| updated_at | DATETIME | |

---

## ⚙️ Setup & Menjalankan

### 1. Buat Database MySQL
```sql
CREATE DATABASE db_crud CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. Konfigurasi `application.properties`
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/db_crud?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

### 3. Jalankan Aplikasi
```bash
./mvnw spring-boot:run
```

### 4. Buka Browser
```
http://localhost:8080
```

---

## 🌐 Web Pages (Thymeleaf + Bootstrap)

| URL | Deskripsi |
|-----|-----------|
| `/` | Dashboard utama |
| `/users` | List semua user |
| `/users/create` | Form tambah user |
| `/users/edit/{id}` | Form edit user |
| `/users/{id}/detail` | Detail user + profile + posts |
| `/profiles` | List semua profile |
| `/profiles/create` | Form tambah profile |
| `/posts` | List semua post |
| `/posts/create` | Form tambah post |

---

## 🔌 REST API Endpoints

### Users — `/api/users`
| Method | URL | Deskripsi |
|--------|-----|-----------|
| GET | `/api/users` | Ambil semua user |
| GET | `/api/users/{id}` | Ambil user by ID |
| POST | `/api/users` | Tambah user baru |
| PUT | `/api/users/{id}` | Update user |
| DELETE | `/api/users/{id}` | Hapus user |

### Profiles — `/api/profiles`
| Method | URL | Deskripsi |
|--------|-----|-----------|
| GET | `/api/profiles` | Ambil semua profile |
| GET | `/api/profiles/{id}` | Ambil profile by ID |
| GET | `/api/profiles/user/{userId}` | Ambil profile by user |
| POST | `/api/profiles?userId={id}` | Buat profile untuk user |
| PUT | `/api/profiles/{id}` | Update profile |
| DELETE | `/api/profiles/{id}` | Hapus profile |

### Posts — `/api/posts`
| Method | URL | Deskripsi |
|--------|-----|-----------|
| GET | `/api/posts` | Ambil semua post |
| GET | `/api/posts/{id}` | Ambil post by ID |
| GET | `/api/posts/user/{userId}` | Ambil posts by user |
| POST | `/api/posts?userId={id}` | Buat post untuk user |
| PUT | `/api/posts/{id}` | Update post |
| DELETE | `/api/posts/{id}` | Hapus post |

---

## 📝 Contoh Request API (Postman / cURL)

### Create User
```bash
POST /api/users
Content-Type: application/json

{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "password123"
}
```

### Create Profile untuk User ID 1
```bash
POST /api/profiles?userId=1
Content-Type: application/json

{
  "fullName": "John Doe",
  "phone": "081234567890",
  "gender": "Laki-laki",
  "address": "Jl. Sudirman No. 1, Jakarta"
}
```

### Create Post untuk User ID 1
```bash
POST /api/posts?userId=1
Content-Type: application/json

{
  "title": "Post Pertama Saya",
  "content": "Ini adalah konten post pertama",
  "status": "PUBLISHED"
}
```

---

## 🏗️ Struktur Package
```
src/main/java/com/example/crud/
├── CrudApplication.java          # Main class
├── controller/
│   ├── WebController.java        # MVC controller (Thymeleaf)
│   ├── UserRestController.java   # REST /api/users
│   ├── ProfileRestController.java # REST /api/profiles
│   └── PostRestController.java   # REST /api/posts
├── model/
│   ├── User.java                 # Entity User
│   ├── Profile.java              # Entity Profile (One-to-One)
│   └── Post.java                 # Entity Post (Many-to-One)
├── repository/
│   ├── UserRepository.java
│   ├── ProfileRepository.java
│   └── PostRepository.java
└── service/
    ├── UserService.java
    ├── ProfileService.java
    └── PostService.java

src/main/resources/
├── application.properties
└── templates/
    ├── layout.html               # Template induk (sidebar, navbar)
    ├── dashboard.html
    ├── users/
    │   ├── list.html
    │   ├── form.html
    │   └── detail.html
    ├── profiles/
    │   ├── list.html
    │   └── form.html
    └── posts/
        ├── list.html
        └── form.html
```
