package com.clonair.back.property;

import com.clonair.back.image.Image;
import com.clonair.back.image.ImageService;
import com.clonair.back.location.Location;
import com.clonair.back.security.jwt.JwtService;
import com.clonair.back.user.Role;
import com.clonair.back.user.User;
import com.clonair.back.user.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PropertyServiceImpTest {

    @Mock
    private JwtService jwtService;
    @Mock
    private PropertyRepository propertyRepository;
    @Mock
    private ImageService imageService;
    @Mock
    private UserService userService;

    @InjectMocks
    private PropertyServiceImp propertyService;

    private User ownerUser;
    private User userUser;

    @BeforeEach
    void setUp() {
        ownerUser = User.builder()
                .id("owner-id")
                .username("owner@example.test")
                .password("encoded-password")
                .firstname("Owner")
                .lastname("Test")
                .country("Argentina")
                .role(Role.OWNER)
                .build();

        userUser = User.builder()
                .id("user-id")
                .username("user@example.test")
                .password("encoded-password")
                .firstname("User")
                .lastname("Test")
                .country("Argentina")
                .role(Role.USER)
                .build();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private void setSecurityContext(User user) {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private Property createProperty(String id, User user) {
        Property property = new Property();
        property.setId(id);
        property.setTitle("Test Property");
        property.setCategory(Category.APARTMENT);
        property.setDescription("Test description");
        property.setValue(100.0);
        property.setActive(true);
        property.setUser(user);
        Location location = new Location();
        location.setCountry("Argentina");
        location.setCity("Buenos Aires");
        property.setLocation(location);
        property.setBathroom(1);
        property.setBed(2);
        property.setBedroom(1);
        property.setImages(List.of());
        property.setServiceTypes(List.of(ServiceType.WIFI));
        return property;
    }

    private PropertyRequest createRequest() {
        return new PropertyRequest(
                "Test Property",
                "APARTMENT",
                "URBAN",
                "Test description",
                100.0,
                true,
                List.of(),
                "Argentina",
                "Buenos Aires",
                1,
                2,
                1,
                List.of("WIFI")
        );
    }

    @Nested
    class GetOne {

        @Test
        void getOne_whenIdDoesNotExist_throws() {
            when(propertyRepository.findById("missing-id")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> propertyService.getOne("missing-id"))
                    .isInstanceOf(Exception.class)
                    .hasMessage("Property not found");
        }

        @Test
        void getOne_whenPropertyExists_returnsResponse() throws Exception {
            Property property = createProperty("prop-1", ownerUser);
            when(propertyRepository.findById("prop-1")).thenReturn(Optional.of(property));

            PropertyResponse response = propertyService.getOne("prop-1");

            assertThat(response).isNotNull();
            assertThat(response.id()).isEqualTo("prop-1");
            assertThat(response.title()).isEqualTo("Test Property");
            assertThat(response.username()).isEqualTo("owner@example.test");
            assertThat(response.category()).isEqualTo(Category.APARTMENT);
        }
    }

    @Nested
    class Save {

        @Test
        void save_whenTokenMissing_throws() {
            PropertyRequest request = createRequest();

            assertThatThrownBy(() -> propertyService.save(null, request))
                    .isInstanceOf(Exception.class)
                    .hasMessage("Authorization token is missing or invalid");
        }

        @Test
        void save_whenTokenWithoutBearerPrefix_throws() {
            PropertyRequest request = createRequest();

            assertThatThrownBy(() -> propertyService.save("invalid-token", request))
                    .isInstanceOf(Exception.class)
                    .hasMessage("Authorization token is missing or invalid");
        }

        @Test
        void save_whenTokenInvalid_throws() {
            setSecurityContext(ownerUser);
            when(jwtService.isTokenValid(anyString(), any())).thenReturn(false);
            PropertyRequest request = createRequest();

            assertThatThrownBy(() -> propertyService.save("Bearer token", request))
                    .isInstanceOf(Exception.class)
                    .hasMessage("Invalid JWT token");
        }

        @Test
        void save_whenUserIsNotOwner_throws() {
            setSecurityContext(userUser);
            when(jwtService.isTokenValid(anyString(), any())).thenReturn(true);
            when(userService.findByUsername("user@example.test")).thenReturn(Optional.of(userUser));
            PropertyRequest request = createRequest();

            assertThatThrownBy(() -> propertyService.save("Bearer token", request))
                    .isInstanceOf(Exception.class)
                    .hasMessage("El usuario no es un OWNER");

            verify(propertyRepository, never()).save(any());
        }

        @Test
        void save_whenUserIsOwner_savesPropertyWithImages() throws Exception {
            setSecurityContext(ownerUser);
            when(jwtService.isTokenValid(anyString(), any())).thenReturn(true);
            when(userService.findByUsername("owner@example.test")).thenReturn(Optional.of(ownerUser));
            when(imageService.saveMulti(any(MultipartFile[].class))).thenReturn(List.of());
            when(propertyRepository.save(any(Property.class))).thenAnswer(invocation -> {
                Property saved = invocation.getArgument(0);
                saved.setId("new-prop-id");
                return saved;
            });
            PropertyRequest request = createRequest();

            propertyService.save("Bearer token", request);

            verify(propertyRepository).save(argThat(property ->
                    property.getUser() != null &&
                    property.getUser().getUsername().equals("owner@example.test") &&
                    property.getTitle().equals("Test Property")
            ));
        }
    }

    @Nested
    class Update {

        @Test
        void update_whenTokenMissing_throws() {
            PropertyRequest request = createRequest();

            assertThatThrownBy(() -> propertyService.update("prop-1", null, request))
                    .isInstanceOf(Exception.class)
                    .hasMessage("Authorization token is missing or invalid");
        }

        @Test
        void update_whenTokenInvalid_throws() {
            setSecurityContext(ownerUser);
            when(jwtService.isTokenValid(anyString(), any())).thenReturn(false);
            PropertyRequest request = createRequest();

            assertThatThrownBy(() -> propertyService.update("prop-1", "Bearer token", request))
                    .isInstanceOf(Exception.class)
                    .hasMessage("Invalid JWT token");
        }

        @Test
        void update_whenUserIsNotOwner_throws() {
            setSecurityContext(userUser);
            when(jwtService.isTokenValid(anyString(), any())).thenReturn(true);
            when(userService.findByUsername("user@example.test")).thenReturn(Optional.of(userUser));
            PropertyRequest request = createRequest();

            assertThatThrownBy(() -> propertyService.update("prop-1", "Bearer token", request))
                    .isInstanceOf(Exception.class)
                    .hasMessage("El usuario no es un OWNER");
        }

        @Test
        void update_whenPropertyNotFound_throws() {
            setSecurityContext(ownerUser);
            when(jwtService.isTokenValid(anyString(), any())).thenReturn(true);
            when(userService.findByUsername("owner@example.test")).thenReturn(Optional.of(ownerUser));
            when(propertyRepository.findById("missing-id")).thenReturn(Optional.empty());
            PropertyRequest request = createRequest();

            assertThatThrownBy(() -> propertyService.update("missing-id", "Bearer token", request))
                    .isInstanceOf(Exception.class)
                    .hasMessage("Property not found");
        }

        @Test
        void update_whenOwnerIsNotPropertyOwner_throws() {
            User otherOwner = User.builder()
                    .id("other-owner-id")
                    .username("other@example.test")
                    .password("encoded-password")
                    .role(Role.OWNER)
                    .build();
            setSecurityContext(otherOwner);
            when(jwtService.isTokenValid(anyString(), any())).thenReturn(true);
            when(userService.findByUsername("other@example.test")).thenReturn(Optional.of(otherOwner));

            Property existingProperty = createProperty("prop-1", ownerUser);
            when(propertyRepository.findById("prop-1")).thenReturn(Optional.of(existingProperty));
            PropertyRequest request = createRequest();

            assertThatThrownBy(() -> propertyService.update("prop-1", "Bearer token", request))
                    .isInstanceOf(Exception.class)
                    .hasMessage("No tiene permiso para editar esta propiedad");

            verify(propertyRepository, never()).save(any());
        }

        @Test
        void update_whenOwnerIsPropertyOwner_savesUpdatedProperty() throws Exception {
            setSecurityContext(ownerUser);
            when(jwtService.isTokenValid(anyString(), any())).thenReturn(true);
            when(userService.findByUsername("owner@example.test")).thenReturn(Optional.of(ownerUser));

            Property existingProperty = createProperty("prop-1", ownerUser);
            when(propertyRepository.findById("prop-1")).thenReturn(Optional.of(existingProperty));
            when(imageService.updatePropertyImages(any(Property.class), any(List.class)))
                    .thenReturn(List.of());
            when(propertyRepository.save(any(Property.class))).thenAnswer(invocation -> invocation.getArgument(0));
            PropertyRequest request = createRequest();

            propertyService.update("prop-1", "Bearer token", request);

            verify(propertyRepository).save(argThat(property ->
                    property.getTitle().equals("Test Property") &&
                    property.getUser().getUsername().equals("owner@example.test")
            ));
        }
    }

    @Nested
    class Delete {

        @Test
        void delete_whenTokenMissing_throws() {
            assertThatThrownBy(() -> propertyService.delete("prop-1", null))
                    .isInstanceOf(Exception.class)
                    .hasMessage("Authorization token is missing or invalid");
        }

        @Test
        void delete_whenTokenInvalid_throws() {
            setSecurityContext(ownerUser);
            when(jwtService.isTokenValid(anyString(), any())).thenReturn(false);

            assertThatThrownBy(() -> propertyService.delete("prop-1", "Bearer token"))
                    .isInstanceOf(Exception.class)
                    .hasMessage("Invalid JWT token");
        }

        @Test
        void delete_whenUserIsNotOwner_throws() {
            setSecurityContext(userUser);
            when(jwtService.isTokenValid(anyString(), any())).thenReturn(true);
            when(userService.findByUsername("user@example.test")).thenReturn(Optional.of(userUser));

            assertThatThrownBy(() -> propertyService.delete("prop-1", "Bearer token"))
                    .isInstanceOf(Exception.class)
                    .hasMessage("El usuario no tiene permiso para eliminar propiedades");
        }

        @Test
        void delete_whenOwnerIsNotPropertyOwner_throws() {
            User otherOwner = User.builder()
                    .id("other-owner-id")
                    .username("other@example.test")
                    .password("encoded-password")
                    .role(Role.OWNER)
                    .build();
            setSecurityContext(otherOwner);
            when(jwtService.isTokenValid(anyString(), any())).thenReturn(true);
            when(userService.findByUsername("other@example.test")).thenReturn(Optional.of(otherOwner));

            Property existingProperty = createProperty("prop-1", ownerUser);
            when(propertyRepository.findById("prop-1")).thenReturn(Optional.of(existingProperty));

            assertThatThrownBy(() -> propertyService.delete("prop-1", "Bearer token"))
                    .isInstanceOf(Exception.class)
                    .hasMessage("No tiene permiso para eliminar esta propiedad");

            verify(propertyRepository, never()).delete(any());
        }

        @Test
        void delete_whenOwnerIsPropertyOwner_deletesProperty() throws Exception {
            setSecurityContext(ownerUser);
            when(jwtService.isTokenValid(anyString(), any())).thenReturn(true);
            when(userService.findByUsername("owner@example.test")).thenReturn(Optional.of(ownerUser));

            Property existingProperty = createProperty("prop-1", ownerUser);
            when(propertyRepository.findById("prop-1")).thenReturn(Optional.of(existingProperty));

            propertyService.delete("prop-1", "Bearer token");

            verify(propertyRepository).delete(existingProperty);
        }
    }
}
