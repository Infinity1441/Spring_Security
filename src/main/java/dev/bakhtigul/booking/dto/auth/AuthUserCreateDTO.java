package dev.bakhtigul.booking.dto.auth;

import dev.bakhtigul.booking.domains.auth.AuthUser;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * A DTO for the {@link AuthUser} entity
 */

public record AuthUserCreateDTO(
        @NotBlank String username,
        @NotBlank String password,
        @NotBlank
        @Email(regexp = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-]+)(\\.[a-zA-Z]{2,5}){1,2}$") String email) implements Serializable {
}