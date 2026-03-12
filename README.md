# Tugas CRUD KTP — Spring Boot & MySQL

> **Nama:** Hafiz Zada Maulana  
> **NIM:** 20230140016  
> **Mata Kuliah:** Software Deployment

Aplikasi manajemen data KTP (Kartu Tanda Penduduk) berbasis **REST API Spring Boot** dengan frontend **HTML/CSS/JavaScript + jQuery Ajax**.

---

## 📸 Tampilan Aplikasi

> **Petunjuk:** Ganti placeholder di bawah ini dengan screenshot hasil tampilan aplikasi kamu.

### Light Mode

<img width="1920" height="1045" alt="image" src="https://github.com/user-attachments/assets/3244b644-eb5c-45ee-a394-b4b41b831cd5" />


### Dark Mode

<img width="1920" height="1045" alt="image" src="https://github.com/user-attachments/assets/19b6b08c-dd0a-46d8-aadd-2e99652b346e" />


### Tambah Data KTP

<img width="1920" height="1045" alt="image" src="https://github.com/user-attachments/assets/e231f228-25bb-42ae-8738-2630cf14752f" />


### Tabel Data KTP

<img width="1919" height="1079" alt="image" src="https://github.com/user-attachments/assets/74fb3acc-6da2-49a2-92b7-41d57523b8c2" />


---

## 🗂️ Struktur Project

```
Tugas_CRUD_KTP/
├── src/main/java/com/tugasktp/ktp/
│   ├── TugasCrudKtpApplication.java    # Main class
│   ├── controller/
│   │   └── KtpController.java          # REST Controller (5 endpoint)
│   ├── service/
│   │   ├── KtpService.java             # Interface service
│   │   └── impl/
│   │       └── KtpServiceImpl.java     # Implementasi logika bisnis
│   ├── repository/
│   │   └── KtpRepository.java          # Spring Data JPA Repository
│   ├── model/
│   │   └── Ktp.java                    # JPA Entity
│   ├── dto/
│   │   ├── KtpRequestDto.java          # DTO untuk request (input)
│   │   └── KtpResponseDto.java         # DTO untuk response (output)
│   ├── mapper/
│   │   └── KtpMapper.java              # Konversi Entity ↔ DTO
│   ├── util/
│   │   └── ApiResponse.java            # Wrapper response standar
│   └── exception/
│       ├── ResourceNotFoundException.java
│       ├── DuplicateNomorKtpException.java
│       └── GlobalExceptionHandler.java
├── src/main/resources/
│   └── application.properties          # Konfigurasi database & server
├── frontend/
│   ├── index.html                      # Halaman utama
│   ├── style.css                       # Styling (Light & Dark mode)
│   └── app.js                          # jQuery Ajax CRUD
├── database/
│   └── setup.sql                       # Script SQL setup database
└── pom.xml                             # Maven dependencies
```

---

## 🗄️ Schema Database

**Database:** `spring`  
**Tabel:** `ktp`

| Kolom           | Tipe         | Keterangan                  |
| --------------- | ------------ | --------------------------- |
| `id`            | INT          | Primary Key, Auto Increment |
| `nomor_ktp`     | VARCHAR(16)  | Unik, 16 digit NIK          |
| `nama_lengkap`  | VARCHAR(100) | Nama lengkap pemilik        |
| `alamat`        | VARCHAR(255) | Alamat lengkap              |
| `tanggal_lahir` | DATE         | Format: YYYY-MM-DD          |
| `jenis_kelamin` | VARCHAR(10)  | `Laki-laki` / `Perempuan`   |

---

## ⚙️ Setup & Instalasi

### Prasyarat

- Java 17+
- Maven (atau jalankan via VS Code / IntelliJ)
- MySQL Server
- MySQL Workbench (opsional)

### Langkah 1 — Setup Database

Buka MySQL Workbench, jalankan file `database/setup.sql`:

```sql
-- Script akan otomatis membuat:
-- database 'spring' dan tabel 'ktp'
```

### Langkah 2 — Konfigurasi Koneksi

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3308/spring?useSSL=false&serverTimezone=Asia/Jakarta
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD   # ← sesuaikan
```

> Ganti `3308` dengan port MySQL kamu (biasanya `3306`).

### Langkah 3 — Jalankan Backend

**Via VS Code:**

1. Buka `TugasCrudKtpApplication.java`
2. Klik tombol ▶️ **Run Java**

**Via Terminal (jika Maven terinstall):**

```bash
mvn spring-boot:run
```

Tunggu hingga muncul:

```
Started TugasCrudKtpApplication in X.XXX seconds
```

### Langkah 4 — Buka Frontend

Double-click `frontend/index.html` atau buka via Live Server di VS Code.

---

## 🌐 API Endpoints

Base URL: `http://localhost:8080`

### POST `/ktp` — Tambah KTP Baru

**Request Body:**

```json
{
    "nomorKtp": "3201010101010001",
    "namaLengkap": "Budi Santoso",
    "alamat": "Jl. Merdeka No. 1, Jakarta",
    "tanggalLahir": "2000-01-01",
    "jenisKelamin": "Laki-laki"
}
```

**Response (201 Created):**

```json
{
    "success": true,
    "message": "Data KTP berhasil ditambahkan",
    "data": {
        "id": 1,
        "nomorKtp": "3201010101010001",
        "namaLengkap": "Budi Santoso",
        "alamat": "Jl. Merdeka No. 1, Jakarta",
        "tanggalLahir": "2000-01-01",
        "jenisKelamin": "Laki-laki"
    }
}
```

---

### GET `/ktp` — Ambil Semua Data KTP

**Response (200 OK):**

```json
{
  "success": true,
  "message": "Data KTP berhasil diambil",
  "data": [ { "id": 1, ... }, { "id": 2, ... } ]
}
```

---

### GET `/ktp/{id}` — Ambil KTP Berdasarkan ID

**Response (200 OK):**

```json
{
  "success": true,
  "message": "Data KTP berhasil diambil",
  "data": { "id": 1, "nomorKtp": "...", ... }
}
```

**Response (404 Not Found):**

```json
{
    "success": false,
    "message": "Data KTP dengan id 99 tidak ditemukan",
    "data": null
}
```

---

### PUT `/ktp/{id}` — Update KTP

**Request Body:** sama seperti POST  
**Response (200 OK):**

```json
{
  "success": true,
  "message": "Data KTP berhasil diperbarui",
  "data": { "id": 1, ... }
}
```

---

### DELETE `/ktp/{id}` — Hapus KTP

**Response (200 OK):**

```json
{
    "success": true,
    "message": "Data KTP berhasil dihapus",
    "data": null
}
```

---

## ❌ Error Handling

| HTTP Status               | Kasus                | Contoh Message                            |
| ------------------------- | -------------------- | ----------------------------------------- |
| 400 Bad Request           | Validasi field gagal | `"nomorKtp": "harus 16 digit angka"`      |
| 404 Not Found             | Data tidak ditemukan | `"Data KTP dengan id 99 tidak ditemukan"` |
| 409 Conflict              | Nomor KTP duplikat   | `"Nomor KTP 3201... sudah terdaftar"`     |
| 500 Internal Server Error | Error server         | `"Terjadi kesalahan pada server"`         |

---

## 🎨 Fitur Frontend

- ✅ Form tambah / edit data KTP (tanpa refresh halaman)
- ✅ Tabel data KTP dengan nomor urut otomatis
- ✅ Dropdown **Jenis Kelamin** (Laki-laki / Perempuan)
- ✅ **Date picker** untuk Tanggal Lahir
- ✅ Tombol **Edit** (mengisi form otomatis) dan **Hapus** (dengan konfirmasi modal)
- ✅ **Toast notification** sukses / error di pojok kanan atas
- ✅ Pesan error per field dari validasi backend
- ✅ **Toggle Dark / Light Mode** — preferensi tersimpan di browser
- ✅ Tombol refresh data tanpa reload halaman

---

## 🛠️ Teknologi yang Digunakan

| Layer             | Teknologi                   |
| ----------------- | --------------------------- |
| Backend Framework | Spring Boot 3.2.3           |
| Database          | MySQL                       |
| ORM               | Spring Data JPA (Hibernate) |
| Validasi          | Jakarta Bean Validation     |
| Boilerplate       | Lombok                      |
| Frontend          | HTML5, CSS3, JavaScript     |
| Ajax              | jQuery 3.7.1                |
| Font              | Google Fonts (Inter)        |
