package dev.bakhtigul.booking.mappers;

import dev.bakhtigul.booking.domains.auth.AuthUser;
import dev.bakhtigul.booking.dto.auth.AuthUserCreateDTO;
import org.mapstruct.Mapper;
import org.springframework.lang.NonNull;

@Mapper(componentModel = "spring")
public interface AuthUserMapper {
    AuthUser toEntity(@NonNull AuthUserCreateDTO dto);

}
