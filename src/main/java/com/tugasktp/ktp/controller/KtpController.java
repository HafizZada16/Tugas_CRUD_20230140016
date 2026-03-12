package com.tugasktp.ktp.controller;

import com.tugasktp.ktp.dto.KtpRequestDto;
import com.tugasktp.ktp.dto.KtpResponseDto;
import com.tugasktp.ktp.service.KtpService;
import com.tugasktp.ktp.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ktp")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class KtpController {

    private final KtpService ktpService;

    // POST /ktp - Tambah data KTP baru
    @PostMapping
    public ResponseEntity<ApiResponse<KtpResponseDto>> createKtp(
            @Valid @RequestBody KtpRequestDto requestDto) {
        KtpResponseDto responseDto = ktpService.createKtp(requestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Data KTP berhasil ditambahkan", responseDto));
    }

    // GET /ktp - Ambil semua data KTP
    @GetMapping
    public ResponseEntity<ApiResponse<List<KtpResponseDto>>> getAllKtp() {
        List<KtpResponseDto> data = ktpService.getAllKtp();
        return ResponseEntity.ok(
                ApiResponse.success("Data KTP berhasil diambil", data));
    }

    // GET /ktp/{id} - Ambil data KTP berdasarkan id
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<KtpResponseDto>> getKtpById(
            @PathVariable Integer id) {
        KtpResponseDto responseDto = ktpService.getKtpById(id);
        return ResponseEntity.ok(
                ApiResponse.success("Data KTP berhasil diambil", responseDto));
    }

    // PUT /ktp/{id} - Perbarui data KTP berdasarkan id
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<KtpResponseDto>> updateKtp(
            @PathVariable Integer id,
            @Valid @RequestBody KtpRequestDto requestDto) {
        KtpResponseDto responseDto = ktpService.updateKtp(id, requestDto);
        return ResponseEntity.ok(
                ApiResponse.success("Data KTP berhasil diperbarui", responseDto));
    }

    // DELETE /ktp/{id} - Hapus data KTP berdasarkan id
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteKtp(
            @PathVariable Integer id) {
        ktpService.deleteKtp(id);
        return ResponseEntity.ok(
                ApiResponse.success("Data KTP berhasil dihapus"));
    }
}
