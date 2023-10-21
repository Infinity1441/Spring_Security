package dev.bakhtigul.booking.repositories;

import dev.bakhtigul.booking.domains.auth.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {
    @Transactional
    @Modifying
    @Query("update AuthUser a set a.active = true where a.createdBy = :createdBy")
    void activeUser(@Param("createdBy") Long createdBy);
    @Query("select a from AuthUser a where upper(a.username) = upper(?1) and a.deleted = false ")
    Optional<AuthUser> findByUsername(String username);
    Optional<AuthUser> findByEmailLike(String email);

    @Query("select a from AuthUser a where upper(a.email) = upper(?1) and a.deleted = false ")
    AuthUser findByEmail(String email);
}