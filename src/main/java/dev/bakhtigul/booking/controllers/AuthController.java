package dev.bakhtigul.booking.controllers;

import dev.bakhtigul.booking.dto.auth.AuthUserCreateDTO;
import dev.bakhtigul.booking.dto.auth.RefreshTokenRequest;
import dev.bakhtigul.booking.dto.auth.TokenRequest;
import dev.bakhtigul.booking.dto.auth.TokenResponse;
import dev.bakhtigul.booking.domains.auth.AuthUser;
import dev.bakhtigul.booking.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/access/token")
    public ResponseEntity<TokenResponse> accessToken(@RequestBody TokenRequest tokenRequest) {
        return ResponseEntity.ok(authService.generateToken(tokenRequest));
    }
    @PostMapping("/refresh/token")
    public ResponseEntity<TokenResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthUser> register(@NonNull @Valid @RequestBody AuthUserCreateDTO dto) {
        return ResponseEntity.ok(authService.create(dto));
    }

    @GetMapping("/activate/{code}")
    public ResponseEntity<Boolean> activate(@PathVariable String code) {
        return ResponseEntity.ok(authService.activateUserByOTP(code));
    }

}
