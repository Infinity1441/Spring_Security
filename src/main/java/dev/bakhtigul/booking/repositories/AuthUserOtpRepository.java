package dev.bakhtigul.booking.repositories;

import dev.bakhtigul.booking.domains.auth.AuthUserOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AuthUserOtpRepository extends JpaRepository<AuthUserOtp, Long> {
    @Query("select a from AuthUserOtp a where a.code = ?1 and a.deleted = false")
    Optional<AuthUserOtp> findByCode(String code);
}