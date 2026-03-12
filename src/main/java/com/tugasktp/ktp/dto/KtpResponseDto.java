package com.tugasktp.ktp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KtpResponseDto {

    private Integer id;

    private String nomorKtp;

    private String namaLengkap;

    private String alamat;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate tanggalLahir;

    private String jenisKelamin;
}
