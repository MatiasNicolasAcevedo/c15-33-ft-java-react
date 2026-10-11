package com.clonair.back.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtServiceTest {

    private static final long ONE_DAY_MILLIS = 86_400_000L;

    @Test
    void getToken_whenGenerated_expiresAfterTwentyFourHours() {
        JwtService service = new JwtService(secret(32, (byte) 1), ONE_DAY_MILLIS);
        UserDetails user = user("owner@example.test");

        String token = service.getToken(user);

        long issuedAt = service.getClaim(token, Claims::getIssuedAt).getTime();
        long expiresAt = service.getClaim(token, Claims::getExpiration).getTime();
        assertThat(expiresAt - issuedAt).isEqualTo(ONE_DAY_MILLIS);
    }

    @Test
    void getUsernameFromToken_whenGenerated_returnsUsername() {
        JwtService service = new JwtService(secret(32, (byte) 1), ONE_DAY_MILLIS);
        UserDetails user = user("owner@example.test");

        String token = service.getToken(user);

        assertThat(service.getUsernameFromToken(token)).isEqualTo(user.getUsername());
    }

    @Test
    void isTokenValid_whenSameUser_returnsTrue() {
        JwtService service = new JwtService(secret(32, (byte) 1), ONE_DAY_MILLIS);
        UserDetails user = user("owner@example.test");

        String token = service.getToken(user);

        assertThat(service.isTokenValid(token, user)).isTrue();
    }

    @Test
    void isTokenValid_whenAnotherUser_returnsFalse() {
        JwtService service = new JwtService(secret(32, (byte) 1), ONE_DAY_MILLIS);
        String token = service.getToken(user("owner@example.test"));

        boolean valid = service.isTokenValid(token, user("other@example.test"));

        assertThat(valid).isFalse();
    }

    @Test
    void isTokenValid_whenSignedWithAnotherKey_throwsJwtException() {
        JwtService service = new JwtService(secret(32, (byte) 1), ONE_DAY_MILLIS);
        JwtService otherService = new JwtService(secret(32, (byte) 2), ONE_DAY_MILLIS);
        UserDetails user = user("owner@example.test");

        String token = otherService.getToken(user);

        assertThatThrownBy(() -> service.isTokenValid(token, user)).isInstanceOf(JwtException.class);
    }

    @Test
    void isTokenValid_whenExpired_throwsExpiredJwtException() {
        JwtService service = new JwtService(secret(32, (byte) 1), -ONE_DAY_MILLIS);
        UserDetails user = user("owner@example.test");

        String token = service.getToken(user);

        assertThatThrownBy(() -> service.isTokenValid(token, user)).isInstanceOf(ExpiredJwtException.class);
    }

    @Test
    void getToken_whenCustomExpiration_usesConfiguredDuration() {
        JwtService service = new JwtService(secret(32, (byte) 1), 3_600_000L);

        String token = service.getToken(user("owner@example.test"));

        long issuedAt = service.getClaim(token, Claims::getIssuedAt).getTime();
        long expiresAt = service.getClaim(token, Claims::getExpiration).getTime();
        assertThat(expiresAt - issuedAt).isEqualTo(3_600_000L);
    }

    @ParameterizedTest
    @ValueSource(ints = {32, 48, 64})
    void getToken_whenValidKeyLength_keepsHs256(int keyLength) {
        String secret = secret(keyLength, (byte) 1);
        JwtService service = new JwtService(secret, ONE_DAY_MILLIS);

        String token = service.getToken(user("owner@example.test"));

        String algorithm = Jwts.parserBuilder()
                .setSigningKey(Base64.getDecoder().decode(secret))
                .build().parseClaimsJws(token).getHeader().getAlgorithm();
        assertThat(algorithm).isEqualTo("HS256");
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 16, 24, 31})
    void constructor_whenDecodedSecretIsShort_throws(int keyLength) {
        String secret = secret(keyLength, (byte) 1);

        assertThatThrownBy(() -> new JwtService(secret, ONE_DAY_MILLIS))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("JWT_SECRET must decode to at least 32 bytes (256 bits)");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t"})
    void constructor_whenSecretIsMissing_throws(String secret) {
        assertThatThrownBy(() -> new JwtService(secret, ONE_DAY_MILLIS))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("JWT_SECRET is required and must be Base64 encoded");
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid!base64", "AQ==!", "a"})
    void constructor_whenSecretIsMalformed_throwsWithoutRevealingValue(String secret) {
        assertThatThrownBy(() -> new JwtService(secret, ONE_DAY_MILLIS))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("JWT_SECRET must be valid Base64");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    void isTokenValid_whenTokenIsMissing_returnsFalse(String token) {
        JwtService service = new JwtService(secret(32, (byte) 1), ONE_DAY_MILLIS);

        assertThat(service.isTokenValid(token, user("owner@example.test"))).isFalse();
        assertThat(service.getUsernameFromToken(token)).isNull();
    }

    private static UserDetails user(String username) {
        return new User(username, "unused", List.of());
    }

    private static String secret(int length, byte value) {
        byte[] bytes = new byte[length];
        Arrays.fill(bytes, value);
        return Base64.getEncoder().encodeToString(bytes);
    }
}
