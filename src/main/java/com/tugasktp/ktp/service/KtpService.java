package com.tugasktp.ktp.service;

import com.tugasktp.ktp.dto.KtpRequestDto;
import com.tugasktp.ktp.dto.KtpResponseDto;

import java.util.List;

public interface KtpService {

    // Ambil semua data KTP
    List<KtpResponseDto> getAllKtp();

    // Ambil data KTP berdasarkan id
    KtpResponseDto getKtpById(Integer id);

    // Tambah data KTP baru
    KtpResponseDto createKtp(KtpRequestDto requestDto);

    // Perbarui data KTP berdasarkan id
    KtpResponseDto updateKtp(Integer id, KtpRequestDto requestDto);

    // Hapus data KTP berdasarkan id
    void deleteKtp(Integer id);
}
