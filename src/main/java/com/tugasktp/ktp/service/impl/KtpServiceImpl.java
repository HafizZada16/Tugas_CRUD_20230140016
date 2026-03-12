package com.tugasktp.ktp.service.impl;

import com.tugasktp.ktp.dto.KtpRequestDto;
import com.tugasktp.ktp.dto.KtpResponseDto;
import com.tugasktp.ktp.exception.DuplicateNomorKtpException;
import com.tugasktp.ktp.exception.ResourceNotFoundException;
import com.tugasktp.ktp.mapper.KtpMapper;
import com.tugasktp.ktp.model.Ktp;
import com.tugasktp.ktp.repository.KtpRepository;
import com.tugasktp.ktp.service.KtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KtpServiceImpl implements KtpService {

    private final KtpRepository ktpRepository;

    @Override
    @Transactional(readOnly = true)
    public List<KtpResponseDto> getAllKtp() {
        return ktpRepository.findAll()
                .stream()
                .map(KtpMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public KtpResponseDto getKtpById(Integer id) {
        Ktp ktp = ktpRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Data KTP dengan id " + id + " tidak ditemukan"));
        return KtpMapper.toResponseDto(ktp);
    }

    @Override
    @Transactional
    public KtpResponseDto createKtp(KtpRequestDto requestDto) {
        // Validasi: nomorKtp tidak boleh duplikat
        if (ktpRepository.existsByNomorKtp(requestDto.getNomorKtp())) {
            throw new DuplicateNomorKtpException(
                    "Nomor KTP " + requestDto.getNomorKtp() + " sudah terdaftar");
        }

        Ktp ktp = KtpMapper.toEntity(requestDto);
        Ktp savedKtp = ktpRepository.save(ktp);
        return KtpMapper.toResponseDto(savedKtp);
    }

    @Override
    @Transactional
    public KtpResponseDto updateKtp(Integer id, KtpRequestDto requestDto) {
        // Cek data ada
        Ktp ktp = ktpRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Data KTP dengan id " + id + " tidak ditemukan"));

        // Validasi: nomorKtp tidak boleh duplikat dengan data lain
        if (ktpRepository.existsByNomorKtpAndIdNot(requestDto.getNomorKtp(), id)) {
            throw new DuplicateNomorKtpException(
                    "Nomor KTP " + requestDto.getNomorKtp() + " sudah digunakan oleh data lain");
        }

        KtpMapper.updateEntity(ktp, requestDto);
        Ktp updatedKtp = ktpRepository.save(ktp);
        return KtpMapper.toResponseDto(updatedKtp);
    }

    @Override
    @Transactional
    public void deleteKtp(Integer id) {
        // Cek data ada sebelum dihapus
        if (!ktpRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Data KTP dengan id " + id + " tidak ditemukan");
        }
        ktpRepository.deleteById(id);
    }
}
