package dev.bakhtigul.booking.services;

import dev.bakhtigul.booking.dto.auth.TokenResponse;
import dev.bakhtigul.booking.domains.auth.AuthUser;
import dev.bakhtigul.booking.dto.auth.AuthUserCreateDTO;
import dev.bakhtigul.booking.dto.auth.RefreshTokenRequest;
import dev.bakhtigul.booking.dto.auth.TokenRequest;
import org.springframework.lang.NonNull;

public interface AuthService {

    TokenResponse generateToken(@NonNull TokenRequest tokenRequest);

    TokenResponse refreshToken(@jakarta.validation.constraints.NotNull RefreshTokenRequest refreshTokenRequest);

    AuthUser create(@NonNull AuthUserCreateDTO dto);

    boolean activateUserByOTP(String code);
}
