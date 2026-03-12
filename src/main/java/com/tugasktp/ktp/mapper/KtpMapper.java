package com.tugasktp.ktp.mapper;

import com.tugasktp.ktp.dto.KtpRequestDto;
import com.tugasktp.ktp.dto.KtpResponseDto;
import com.tugasktp.ktp.model.Ktp;

public class KtpMapper {

    // Konversi RequestDto -> Entity (untuk create)
    public static Ktp toEntity(KtpRequestDto dto) {
        return Ktp.builder()
                .nomorKtp(dto.getNomorKtp())
                .namaLengkap(dto.getNamaLengkap())
                .alamat(dto.getAlamat())
                .tanggalLahir(dto.getTanggalLahir())
                .jenisKelamin(dto.getJenisKelamin())
                .build();
    }

    // Konversi Entity -> ResponseDto (untuk response API)
    public static KtpResponseDto toResponseDto(Ktp ktp) {
        return KtpResponseDto.builder()
                .id(ktp.getId())
                .nomorKtp(ktp.getNomorKtp())
                .namaLengkap(ktp.getNamaLengkap())
                .alamat(ktp.getAlamat())
                .tanggalLahir(ktp.getTanggalLahir())
                .jenisKelamin(ktp.getJenisKelamin())
                .build();
    }

    // Update field Entity dari RequestDto (untuk update)
    public static void updateEntity(Ktp ktp, KtpRequestDto dto) {
        ktp.setNomorKtp(dto.getNomorKtp());
        ktp.setNamaLengkap(dto.getNamaLengkap());
        ktp.setAlamat(dto.getAlamat());
        ktp.setTanggalLahir(dto.getTanggalLahir());
        ktp.setJenisKelamin(dto.getJenisKelamin());
    }

    // Private constructor agar tidak bisa diinstansiasi
    private KtpMapper() {}
}
