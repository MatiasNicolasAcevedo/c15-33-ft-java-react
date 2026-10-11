package com.clonair.back.security.jwt;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    @Test
    void getToken_whenGenerated_expiresAfterTwentyFourHours() {
        JwtService service = new JwtService();
        User user = new User("owner@example.test", "unused", java.util.List.of());

        String token = service.getToken(user);

        long issuedAt = service.getClaim(token, Claims::getIssuedAt).getTime();
        long expiresAt = service.getClaim(token, Claims::getExpiration).getTime();
        assertThat(expiresAt - issuedAt).isEqualTo(86_400_000L);
    }
}
