package dev.bakhtigul.booking.config.security;

import dev.bakhtigul.booking.domains.auth.AuthUser;
import dev.bakhtigul.booking.repositories.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class SessionUser {
    private final AuthUserRepository userRepository;

    public AuthUser user() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
//        Object principal = authentication.getPrincipal();
//        if (principal instanceof UserDetails ud)
//            return ud;
//        return null;
        String name = authentication.getName();
        if (authentication.isAuthenticated()) {
            AuthUser authUser = userRepository.findByEmail(name);
            System.out.println(authUser);
            return authUser;
        }
        return null;
    }

    public Long id() {
        AuthUser user = user();
        if (Objects.isNull(user))
            //throw new RuntimeException("Unauthenticated");
            return -1L;
        return user.getId();
    }
}
