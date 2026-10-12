package com.clonair.back.common.error;

import com.clonair.back.TestcontainersConfiguration;
import com.clonair.back.security.auth.AuthResponse;
import com.clonair.back.security.request.LoginRequest;
import com.clonair.back.security.request.RegisterRequest;
import com.clonair.back.security.jwt.JwtService;
import com.clonair.back.user.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.config.location=classpath:/application-test.properties")
@Import(TestcontainersConfiguration.class)
class ErrorHandlingIntegrationTest {
    @Autowired private TestRestTemplate restTemplate;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;
    @Autowired private JwtService jwtService;

    @Test
    void register_whenOwner_returns201AndDuplicateReturns409() throws Exception {
        RegisterRequest request = registration("OWNER");
        ResponseEntity<AuthResponse> created = restTemplate.postForEntity(
                "/v1/api/auth/register", request, AuthResponse.class);

        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(created.getBody()).isNotNull();
        assertThat(created.getBody().getToken()).isNotBlank();
        assertProblem(restTemplate.postForEntity("/v1/api/auth/register", request, String.class),
                409, "/v1/api/auth/register");
    }

    @ParameterizedTest
    @ValueSource(strings = {"ADMIN", "INVALID"})
    void register_whenRoleRejected_returns400(String role) throws Exception {
        assertProblem(restTemplate.postForEntity("/v1/api/auth/register", registration(role), String.class),
                400, "/v1/api/auth/register");
    }

    @Test
    void login_whenBadPasswordOrUnknownUser_returnsSame401() throws Exception {
        RegisterRequest request = registration("OWNER");
        restTemplate.postForEntity("/v1/api/auth/register", request, AuthResponse.class);

        JsonNode badPassword = assertProblem(restTemplate.postForEntity("/v1/api/auth/login",
                new LoginRequest(request.getUsername(), "wrong"), String.class), 401, "/v1/api/auth/login");
        JsonNode unknownUser = assertProblem(restTemplate.postForEntity("/v1/api/auth/login",
                new LoginRequest(UUID.randomUUID() + "@missing.test", "wrong"), String.class),
                401, "/v1/api/auth/login");

        assertThat(badPassword).isEqualTo(unknownUser);
    }

    @Test
    void login_whenMalformedJson_returns400() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        assertProblem(restTemplate.postForEntity("/v1/api/auth/login",
                new HttpEntity<>("{broken", headers), String.class), 400, "/v1/api/auth/login");
    }

    @Test
    void protectedEndpoint_whenAnonymous_returns401() throws Exception {
        assertProblem(restTemplate.getForEntity("/v1/api/user", String.class), 401, "/v1/api/user");
    }

    @ParameterizedTest
    @ValueSource(strings = {"/v1/api/property", "/v1/api/user", "/actuator/health"})
    void request_whenInvalidToken_returns401EvenOnPublicEndpoint(String path) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("invalid.jwt.token");
        assertProblem(restTemplate.exchange(path, HttpMethod.GET, new HttpEntity<>(headers), String.class),
                401, path);
    }

    @Test
    void users_whenOwner_returns403() throws Exception {
        assertProblem(restTemplate.exchange("/v1/api/user", HttpMethod.GET,
                authenticated(registration("OWNER")), String.class), 403, "/v1/api/user");
    }

    @Test
    void propertyRead_whenAnonymous_catalogIsPublicAndMissingResourceIs404() throws Exception {
        assertThat(restTemplate.getForEntity("/v1/api/property", String.class).getStatusCode())
                .isEqualTo(HttpStatus.OK);
        assertProblem(restTemplate.getForEntity("/v1/api/property/missing", String.class),
                404, "/v1/api/property/missing");
    }

    @Test
    void userRead_whenMissing_returns404() throws Exception {
        assertProblem(restTemplate.exchange("/v1/api/user/missing", HttpMethod.GET,
                authenticated(registration("USER")), String.class), 404, "/v1/api/user/missing");
    }

    @Test
    void health_whenAnonymous_returnsOnlyUpAndOtherActuatorEndpointsAreNotExposed() throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity("/actuator/health", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(objectMapper.readTree(response.getBody())).isEqualTo(objectMapper.readTree("{\"status\":\"UP\"}"));
        assertProblem(restTemplate.exchange("/actuator/env", HttpMethod.GET,
                authenticated(registration("OWNER")), String.class), 404, "/actuator/env");
    }

    @Test
    void errorEndpoint_whenAnonymous_isNotMaskedBySecurity() throws Exception {
        assertProblem(restTemplate.getForEntity("/error", String.class), 500, "/error");
    }

    private HttpEntity<Void> authenticated(RegisterRequest request) {
        restTemplate.postForEntity("/v1/api/auth/register", request, AuthResponse.class);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtService.getToken(userRepository.findByUsername(request.getUsername()).orElseThrow()));
        return new HttpEntity<>(headers);
    }

    private RegisterRequest registration(String role) {
        return RegisterRequest.builder().username(UUID.randomUUID() + "@errors.test")
                .password("Test1234!").firstname("Error").lastname("Test").country("Argentina").role(role).build();
    }

    private JsonNode assertProblem(ResponseEntity<String> response, int status, String path) throws Exception {
        assertThat(response.getStatusCode().value()).isEqualTo(status);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_PROBLEM_JSON);
        JsonNode body = objectMapper.readTree(response.getBody());
        assertThat(body.path("status").asInt()).isEqualTo(status);
        assertThat(body.path("type").asText()).isEqualTo("about:blank");
        assertThat(body.path("title").asText()).isEqualTo(HttpStatus.valueOf(status).getReasonPhrase());
        assertThat(body.path("detail").asText()).isNotBlank();
        assertThat(body.path("instance").asText()).isEqualTo(path);
        assertThat(body.has("trace")).isFalse();
        assertThat(body.has("exception")).isFalse();
        return body;
    }
}
