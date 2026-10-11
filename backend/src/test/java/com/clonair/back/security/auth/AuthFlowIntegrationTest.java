package com.clonair.back.security.auth;

import com.clonair.back.TestcontainersConfiguration;
import com.clonair.back.security.jwt.JwtService;
import com.clonair.back.security.request.LoginRequest;
import com.clonair.back.security.request.RegisterRequest;
import com.clonair.back.user.Role;
import com.clonair.back.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.config.location=classpath:/application-test.properties")
@Import(TestcontainersConfiguration.class)
class AuthFlowIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void register_whenOwner_returnsTokenAndPersistsEncodedPassword() {
        String username = "register-owner@example.test";

        ResponseEntity<AuthResponse> response = registerOwner(username);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getToken()).isNotBlank();
        assertThat(jwtService.getUsernameFromToken(response.getBody().getToken())).isEqualTo(username);
        var user = userRepository.findByUsername(username).orElseThrow();
        assertThat(user.getRole()).isEqualTo(Role.OWNER);
        assertThat(user.getPassword()).isNotEqualTo("Test1234!");
        assertThat(passwordEncoder.matches("Test1234!", user.getPassword())).isTrue();
    }

    @Test
    void login_whenCorrectPassword_returnsTokenAndOwnerRole() {
        String username = "login-owner@example.test";
        assertThat(registerOwner(username).getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<AuthResponse> response = restTemplate.postForEntity("/v1/api/auth/login",
                new LoginRequest(username, "Test1234!"), AuthResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getToken()).isNotBlank();
        assertThat(jwtService.getUsernameFromToken(response.getBody().getToken())).isEqualTo(username);
        assertThat(response.getBody().getRole()).containsExactly("OWNER");
    }

    @Test
    void login_whenIncorrectPassword_isNot2xx() {
        String username = "bad-password-owner@example.test";
        assertThat(registerOwner(username).getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<String> response = restTemplate.postForEntity("/v1/api/auth/login",
                new LoginRequest(username, "wrong-password"), String.class);

        // Hoy responde 403; T-008 lo cambia a 401. Caracterizamos el rechazo sin fijar el código.
        assertThat(response.getStatusCode().is2xxSuccessful()).isFalse();
    }

    private ResponseEntity<AuthResponse> registerOwner(String username) {
        RegisterRequest request = RegisterRequest.builder()
                .username(username).password("Test1234!")
                .firstname("Test").lastname("Owner").country("Argentina").role("OWNER")
                .build();
        return restTemplate.postForEntity("/v1/api/auth/register", request, AuthResponse.class);
    }
}
