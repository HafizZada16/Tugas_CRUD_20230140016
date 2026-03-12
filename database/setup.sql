-- ===========================
-- Setup Database untuk Tugas CRUD KTP
-- ===========================

-- Buat schema jika belum ada
CREATE DATABASE IF NOT EXISTS `spring`
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE `spring`;

-- Tabel KTP
CREATE TABLE IF NOT EXISTS `ktp` (
    `id`              INT           NOT NULL AUTO_INCREMENT,
    `nomor_ktp`       VARCHAR(16)   NOT NULL UNIQUE,
    `nama_lengkap`    VARCHAR(100)  NOT NULL,
    `alamat`          VARCHAR(255)  NOT NULL,
    `tanggal_lahir`   DATE          NOT NULL,
    `jenis_kelamin`   VARCHAR(10)   NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
