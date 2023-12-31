package dev.bakhtigul.booking.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springdoc.core.annotations.ParameterObject;

public record TokenRequest(

        @Pattern(regexp = "^[a-zA-Z0-9_!#$%&amp;'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "email is  invalid")
        @NotBlank String email,

        @NotBlank String password) {
}
