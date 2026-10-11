package com.clonair.back.security;

import com.clonair.back.TestcontainersConfiguration;
import com.clonair.back.image.Image;
import com.clonair.back.image.ImageRepository;
import com.clonair.back.property.Property;
import com.clonair.back.property.PropertyRepository;
import com.clonair.back.security.auth.AuthResponse;
import com.clonair.back.security.jwt.JwtService;
import com.clonair.back.security.request.LoginRequest;
import com.clonair.back.security.request.RegisterRequest;
import com.clonair.back.user.Role;
import com.clonair.back.user.User;
import com.clonair.back.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.config.location=classpath:/application-test.properties")
@Import(TestcontainersConfiguration.class)
class AuthorizationIntegrationTest {

    @Autowired private TestRestTemplate restTemplate;
    @Autowired private UserRepository userRepository;
    @Autowired private PropertyRepository propertyRepository;
    @Autowired private ImageRepository imageRepository;
    @Autowired private JwtService jwtService;
    @Autowired private PasswordEncoder passwordEncoder;

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"ADMIN", "INVALID", "", "owner", " OWNER "})
    void register_whenRoleIsNotEligible_rejectsWithoutPersisting(String role) {
        RegisterRequest request = registration(role);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "/v1/api/auth/register", request, String.class);

        // T-008 define los códigos de error; aquí verificamos el rechazo y sus efectos.
        assertThat(response.getStatusCode().is2xxSuccessful()).isFalse();
        assertThat(userRepository.findByUsername(request.getUsername())).isEmpty();
    }

    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"USER", "OWNER"})
    void register_whenRoleIsEligible_persistsRoleAndReturnsValidToken(Role role) {
        RegisterRequest request = registration(role.name());

        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
                "/v1/api/auth/register", request, AuthResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        assertThat(user.getRole()).isEqualTo(role);
        assertThat(jwtService.isTokenValid(response.getBody().getToken(), user)).isTrue();
    }

    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"USER", "OWNER"})
    void users_whenNonAdmin_forbidsListingAndDeletionWithoutDeletingUsers(Role role) {
        User caller = createUser(role);
        User victim = createUser(Role.USER);
        HttpEntity<Void> request = authenticated(caller);

        ResponseEntity<String> listing = restTemplate.exchange(
                "/v1/api/user", HttpMethod.GET, request, String.class);
        ResponseEntity<String> deletion = restTemplate.exchange(
                "/v1/api/user/" + victim.getId(), HttpMethod.DELETE, request, String.class);
        ResponseEntity<String> selfDeletion = restTemplate.exchange(
                "/v1/api/user/" + caller.getId(), HttpMethod.DELETE, request, String.class);

        assertThat(listing.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(deletion.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(selfDeletion.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(userRepository.existsById(victim.getId())).isTrue();
        assertThat(userRepository.existsById(caller.getId())).isTrue();
    }

    @Test
    void users_whenAdminFromRepository_allowsListingAndDeletion() {
        User admin = createUser(Role.ADMIN);
        User victim = createUser(Role.USER);
        HttpEntity<Void> request = authenticated(admin);

        ResponseEntity<String> listing = restTemplate.exchange(
                "/v1/api/user", HttpMethod.GET, request, String.class);
        ResponseEntity<String> deletion = restTemplate.exchange(
                "/v1/api/user/" + victim.getId(), HttpMethod.DELETE, request, String.class);

        assertThat(listing.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(listing.getBody()).contains(victim.getUsername());
        assertThat(deletion.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(userRepository.existsById(victim.getId())).isFalse();
    }

    @Test
    void propertyReads_whenAnonymous_returnsCatalogDetailAndOwnerCatalog() {
        // Property hace cascade de persist al dueño: usar uno nuevo evita una entidad detached.
        User owner = newUser(Role.OWNER);
        Property property = new Property();
        property.setTitle("Public authorization test property");
        property.setUser(owner);
        property.setImages(List.of());
        property = propertyRepository.save(property);

        for (String path : List.of("/v1/api/property", "/v1/api/property/" + property.getId(),
                "/v1/api/property/user/" + owner.getId())) {
            ResponseEntity<String> response = restTemplate.getForEntity(path, String.class);

            assertThat(response.getStatusCode()).as(path).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).contains(property.getId());
        }
    }

    @Test
    void imageReads_whenAnonymous_returnsContentAndUrl() {
        Image image = new Image();
        image.setName("public-test.jpg");
        image.setMime("image/jpeg");
        image.setContent(new byte[]{1, 2, 3});
        image = imageRepository.save(image);

        ResponseEntity<byte[]> content = restTemplate.getForEntity(
                "/v1/api/img/" + image.getId(), byte[].class);
        ResponseEntity<String> url = restTemplate.getForEntity(
                "/v1/api/img/" + image.getId() + "/url", String.class);

        assertThat(content.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(content.getBody()).containsExactly(1, 2, 3);
        assertThat(url.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(url.getBody()).isEqualTo("data:image/jpeg;base64,AQID");
    }

    @ParameterizedTest
    @ValueSource(strings = {"/v1/api/property", "/v1/api/img"})
    void writes_whenAnonymous_rejectsAllMutationMethods(String path) {
        for (HttpMethod method : List.of(HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE)) {
            String target = method == HttpMethod.POST ? path : path + "/missing-id";

            ResponseEntity<String> response = restTemplate.exchange(
                    target, method, HttpEntity.EMPTY, String.class);

            assertThat(response.getStatusCode()).as("%s %s", method, target)
                    .isEqualTo(HttpStatus.FORBIDDEN);
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"http://localhost:5173", "http://localhost:8081"})
    void preflight_whenConfiguredOrigin_allowsAuthorizationHeaderOnProtectedEndpoint(String origin) {
        ResponseEntity<String> response = preflight(origin, "/v1/api/user/missing-id", HttpMethod.DELETE);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getHeaders().getAccessControlAllowOrigin()).isEqualTo(origin);
        assertThat(response.getHeaders().getAccessControlAllowMethods()).contains(HttpMethod.DELETE);
        assertThat(response.getHeaders().getAccessControlAllowHeaders()).contains("authorization");
    }

    @ParameterizedTest
    @ValueSource(strings = {"http://evil.example", "http://localhost.evil.example:5173", "http://localhost:5174"})
    void preflight_whenUnconfiguredOrigin_rejectsWithoutAllowOriginHeader(String origin) {
        // Auth es público: el rechazo tiene que venir de CORS, no de falta de autenticación.
        ResponseEntity<String> response = preflight(origin, "/v1/api/auth/login", HttpMethod.POST);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getHeaders().getAccessControlAllowOrigin()).isNull();
    }

    private ResponseEntity<String> preflight(String origin, String path, HttpMethod method) {
        // HttpURLConnection descarta Origin; el cliente HTTP del JDK sí lo transmite.
        TestRestTemplate corsClient = new TestRestTemplate();
        corsClient.getRestTemplate().setRequestFactory(new JdkClientHttpRequestFactory());
        HttpHeaders headers = new HttpHeaders();
        headers.setOrigin(origin);
        headers.setAccessControlRequestMethod(method);
        headers.setAccessControlRequestHeaders(List.of("authorization"));
        return corsClient.exchange(restTemplate.getRootUri() + path, HttpMethod.OPTIONS,
                new HttpEntity<>(headers), String.class);
    }

    private RegisterRequest registration(String role) {
        return RegisterRequest.builder().username(UUID.randomUUID() + "@authorization.test")
                .password("Test1234!").firstname("Authorization").lastname("Test")
                .country("Argentina").role(role).build();
    }

    private User createUser(Role role) {
        return userRepository.save(newUser(role));
    }

    private User newUser(Role role) {
        return User.builder()
                .username(UUID.randomUUID() + "@authorization.test")
                .password(passwordEncoder.encode("Test1234!"))
                .firstname("Authorization").lastname("Test").role(role).build();
    }

    private HttpEntity<Void> authenticated(User user) {
        ResponseEntity<AuthResponse> login = restTemplate.postForEntity("/v1/api/auth/login",
                new LoginRequest(user.getUsername(), "Test1234!"), AuthResponse.class);
        assertThat(login.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(login.getBody()).isNotNull();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(login.getBody().getToken());
        return new HttpEntity<>(headers);
    }
}
