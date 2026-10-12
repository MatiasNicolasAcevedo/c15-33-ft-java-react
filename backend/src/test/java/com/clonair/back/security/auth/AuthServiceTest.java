package com.clonair.back.security.auth;

import com.clonair.back.common.error.ConflictException;
import com.clonair.back.security.jwt.JwtService;
import com.clonair.back.security.request.RegisterRequest;
import com.clonair.back.user.User;
import com.clonair.back.user.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock private UserRepository repository;
    @Mock private JwtService jwtService;
    @Mock private PasswordEncoder encoder;
    @Mock private AuthenticationManager authenticationManager;
    @InjectMocks private AuthService service;

    @Test
    void register_whenUsernameExists_returnsConflictWithoutSavingOrIssuingToken() {
        when(repository.findByUsername("duplicate@test.local")).thenReturn(Optional.of(new User()));

        assertThatThrownBy(() -> service.register(request())).isInstanceOf(ConflictException.class);
        verify(repository, never()).save(any());
        verifyNoInteractions(encoder, jwtService);
    }

    @Test
    void register_whenConcurrentRegistrationWins_returnsConflictWithoutIssuingToken() {
        when(repository.findByUsername("duplicate@test.local"))
                .thenReturn(Optional.empty(), Optional.of(new User()));
        when(encoder.encode(anyString())).thenReturn("encoded");
        when(repository.save(any())).thenThrow(new DataIntegrityViolationException("unique username"));

        assertThatThrownBy(() -> service.register(request())).isInstanceOf(ConflictException.class);
        verifyNoInteractions(jwtService);
    }

    @Test
    void register_whenOtherDatabaseConstraintFails_preservesInternalFailure() {
        when(repository.findByUsername("duplicate@test.local")).thenReturn(Optional.empty());
        when(encoder.encode(anyString())).thenReturn("encoded");
        DataIntegrityViolationException failure = new DataIntegrityViolationException("private SQL constraint");
        when(repository.save(any())).thenThrow(failure);

        assertThatThrownBy(() -> service.register(request())).isSameAs(failure);
        verifyNoInteractions(jwtService);
    }

    private RegisterRequest request() {
        return RegisterRequest.builder().username("duplicate@test.local").password("Test1234!")
                .firstname("Test").lastname("Owner").role("OWNER").build();
    }
}
