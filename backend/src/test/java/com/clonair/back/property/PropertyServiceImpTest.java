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

            Image mockImage1 = new Image();
            mockImage1.setId("img-1");
            Image mockImage2 = new Image();
            mockImage2.setId("img-2");
            when(imageService.saveMulti(any(MultipartFile[].class))).thenReturn(List.of(mockImage1, mockImage2));

            when(propertyRepository.save(any(Property.class))).thenAnswer(invocation -> {
                Property saved = invocation.getArgument(0);
                saved.setId("new-prop-id");
                return saved;
            });

            MultipartFile mockFile1 = mock(MultipartFile.class);
            MultipartFile mockFile2 = mock(MultipartFile.class);
            PropertyRequest request = new PropertyRequest(
                    "Test Property", "APARTMENT", "URBAN", "Test description",
                    100.0, true, List.of(mockFile1, mockFile2),
                    "Argentina", "Buenos Aires", 1, 2, 1, List.of("WIFI")
            );

            propertyService.save("Bearer token", request);

            // Verify imageService.saveMulti was called with the uploaded files
            verify(imageService).saveMulti(argThat(files ->
                    files.length == 2 && files[0] == mockFile1 && files[1] == mockFile2
            ));

            // Verify the property saved has the OWNER set by identity, images list, and bidirectional association
            verify(propertyRepository).save(argThat(property -> {
                // OWNER must be the exact user object (identity)
                if (property.getUser() != ownerUser) {
                    return false;
                }
                // Property must have exactly 2 images
                List<Image> propImages = property.getImages();
                if (propImages == null || propImages.size() != 2) {
                    return false;
                }
                // Each image must point back to this same property (identity)
                for (Image img : propImages) {
                    if (img.getProperty() != property) {
                        return false;
                    }
                }
                // Images must be the ones returned by saveMulti
                if (propImages.get(0) != mockImage1 || propImages.get(1) != mockImage2) {
                    return false;
                }
                return true;
            }));
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

            // Request with values DIFFERENT from the existing property
            PropertyRequest request = new PropertyRequest(
                    "Updated Title", "HOUSE", "BEACH", "Updated description",
                    250.0, false, List.of(),
                    "Spain", "Madrid", 3, 5, 4, List.of("SWIMMING_POOL", "PARKING_LOT")
            );

            propertyService.update("prop-1", "Bearer token", request);

            verify(propertyRepository).save(argThat(property -> {
                // Verify all fields are updated to the new values
                if (!"Updated Title".equals(property.getTitle())) return false;
                if (property.getCategory() != Category.HOUSE) return false;
                if (!"Updated description".equals(property.getDescription())) return false;
                if (property.getValue() != 250.0) return false;
                if (property.isActive() != false) return false;
                if (!"Spain".equals(property.getLocation().getCountry())) return false;
                if (!"Madrid".equals(property.getLocation().getCity())) return false;
                if (property.getBathroom() != 3) return false;
                if (property.getBed() != 5) return false;
                if (property.getBedroom() != 4) return false;
                if (property.getServiceTypes().size() != 2) return false;
                if (property.getSubCategory() != SubCategory.BEACH) return false;
                // ID and OWNER must be preserved
                if (!"prop-1".equals(property.getId())) return false;
                if (property.getUser() != ownerUser) return false;
                return true;
            }));
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
