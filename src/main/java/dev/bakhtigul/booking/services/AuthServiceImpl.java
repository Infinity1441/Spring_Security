package dev.bakhtigul.booking.services;

import dev.bakhtigul.booking.config.security.JwtUtils;
import dev.bakhtigul.booking.config.security.SessionUser;
import dev.bakhtigul.booking.dto.auth.RefreshTokenRequest;
import dev.bakhtigul.booking.dto.auth.TokenResponse;
import dev.bakhtigul.booking.domains.auth.AuthUser;
import dev.bakhtigul.booking.domains.auth.AuthUserOtp;
import dev.bakhtigul.booking.enums.TokenType;
import dev.bakhtigul.booking.exceptions.ItemNotFoundException;
import dev.bakhtigul.booking.mappers.AuthUserMapper;
import dev.bakhtigul.booking.repositories.AuthUserOtpRepository;
import dev.bakhtigul.booking.dto.auth.AuthUserCreateDTO;
import dev.bakhtigul.booking.dto.auth.TokenRequest;
import dev.bakhtigul.booking.repositories.AuthUserRepository;
import dev.bakhtigul.booking.utils.BaseUtils;
import dev.bakhtigul.booking.utils.MailSenderService;
import org.jetbrains.annotations.NotNull;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final AuthUserMapper authUserMapper;
    private final AuthUserRepository authUserRepository;
    private final AuthUserOtpRepository authUserOtpRepository;
    private final PasswordEncoder passwordEncoder;
    private final BaseUtils utils;
    private final MailSenderService mailService;
    private final SessionUser sessionUser;
    private final JwtUtils jwtUtil;

    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            AuthUserMapper authUserMapper,
            AuthUserRepository authUserRepository,
            AuthUserOtpRepository authUserOtpRepository,
            PasswordEncoder passwordEncoder,
            BaseUtils utils,
            MailSenderService mailService,
            SessionUser sessionUser,
            JwtUtils jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.authUserMapper = authUserMapper;
        this.authUserRepository = authUserRepository;
        this.authUserOtpRepository = authUserOtpRepository;
        this.passwordEncoder = passwordEncoder;
        this.utils = utils;
        this.mailService = mailService;
        this.sessionUser = sessionUser;
        this.jwtUtil = jwtUtil;
    }


    @Override
    public TokenResponse generateToken(@NonNull TokenRequest tokenRequest) {
        String email = tokenRequest.email();
        String password = tokenRequest.password();
        this.authUserRepository.findByEmailLike(email).orElseThrow(() -> new ItemNotFoundException("User not found"));
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        this.authenticationManager.authenticate(authentication);
        return this.jwtUtil.generateToken(email);
    }
    @Override
    public TokenResponse refreshToken(@jakarta.validation.constraints.NotNull RefreshTokenRequest refreshTokenRequest) {

        String refreshToken = refreshTokenRequest.refreshToken();
        if (!this.jwtUtil.isValid(refreshToken, TokenType.REFRESH))
            throw new CredentialsExpiredException("Token is invalid");

        String email = this.jwtUtil.getUsername(refreshToken, TokenType.REFRESH);
        this.authUserRepository.findByEmail(email);
        TokenResponse tokenResponse = TokenResponse.builder().refreshToken(refreshToken).refreshTokenExpiry(this.jwtUtil.getExpiry(refreshToken, TokenType.REFRESH)).build();
        return this.jwtUtil.generateAccessToken(email, tokenResponse);
    }


    @Override
    public AuthUser create(@NotNull AuthUserCreateDTO dto) {
        AuthUser authUser = authUserMapper.toEntity(dto);
        authUser.setPassword(passwordEncoder.encode(dto.password()));
        authUser.setRole("USER");
        AuthUserOtp authUserOtp = AuthUserOtp.childBuilder()
                .createdBy(authUser.getId())
                .code(utils.activationCode(authUser.getId()))
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .build();

        String url = "http://localhost:8080/api/auth/activate/" + authUserOtp.getCode();

        Map<String, String> model = Map.of(
                "username", authUser.getUsername(),
                "to", authUser.getEmail(),
                "url", url);
        authUserOtpRepository.save(authUserOtp);
        mailService.sendActivationMail(model);
        return authUserRepository.save(authUser);
    }

    @Override
    /*@Transactional (noRollbackFor = RuntimeException.class)*/
    public boolean activateUserByOTP(@NonNull String code) {
        AuthUserOtp authUserOtp = authUserOtpRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("OTP code is invalid"));

        if (authUserOtp.getExpiresAt().isBefore(LocalDateTime.now())) {
            authUserOtp.setDeleted(true);
            authUserOtpRepository.save(authUserOtp);
            throw new RuntimeException("OTP code is invalid");
        }
        authUserRepository.activeUser(authUserOtp.getCreatedBy());
        return true;
    }

}
