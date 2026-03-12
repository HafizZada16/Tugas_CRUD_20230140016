package com.tugasktp.ktp.repository;

import com.tugasktp.ktp.model.Ktp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KtpRepository extends JpaRepository<Ktp, Integer> {

    // Cek apakah nomorKtp sudah ada (untuk validasi duplikat saat create)
    boolean existsByNomorKtp(String nomorKtp);

    // Cek apakah nomorKtp sudah dipakai oleh data lain (untuk validasi duplikat saat update)
    boolean existsByNomorKtpAndIdNot(String nomorKtp, Integer id);
}
