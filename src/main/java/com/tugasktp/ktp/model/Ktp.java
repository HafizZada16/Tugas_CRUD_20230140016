package com.tugasktp.ktp.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "ktp")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ktp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nomor_ktp", length = 16, nullable = false, unique = true)
    private String nomorKtp;

    @Column(name = "nama_lengkap", length = 100, nullable = false)
    private String namaLengkap;

    @Column(name = "alamat", length = 255, nullable = false)
    private String alamat;

    @Column(name = "tanggal_lahir", nullable = false)
    private LocalDate tanggalLahir;

    @Column(name = "jenis_kelamin", length = 10, nullable = false)
    private String jenisKelamin;
}
