package com.clonair.back.user;

import com.clonair.back.property.Property;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImpTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImp userService;

    @Test
    void getOne_whenIdDoesNotExist_throws() {
        when(userRepository.findById("missing-id")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getOne("missing-id", "unused-token"))
                .isInstanceOf(Exception.class).hasMessage("User not found");
    }

    @Test
    void getByUsername_whenUserExists_mapsUserAndPropertyIds() throws Exception {
        Property property = new Property();
        property.setId("property-id");
        User user = User.builder()
                .id("user-id").username("owner@example.test").password("encoded-password")
                .firstname("Test").lastname("Owner").country("Argentina").role(Role.OWNER)
                .properties(List.of(property)).contact("test-contact").description("test-description")
                .build();
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        UserResponse response = userService.getByUsername(user.getUsername(), "unused-token");

        assertThat(response).isEqualTo(new UserResponse("user-id", "owner@example.test",
                "Test", "Owner", "Argentina", Role.OWNER, List.of("property-id"),
                "test-contact", "test-description"));
    }
}
