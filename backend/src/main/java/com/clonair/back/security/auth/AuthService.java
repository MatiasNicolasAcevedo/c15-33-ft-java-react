package com.clonair.back.security.auth;

import com.clonair.back.common.error.BadRequestException;
import com.clonair.back.common.error.ConflictException;
import org.springframework.dao.DataIntegrityViolationException;
import com.clonair.back.security.jwt.JwtService;
import com.clonair.back.user.User;
import com.clonair.back.user.UserRepository;
import com.clonair.back.security.request.LoginRequest;
import com.clonair.back.security.request.RegisterRequest;
import com.clonair.back.user.Role;
import java.util.List;
import static java.util.stream.Collectors.toList;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Esta clase Maneja la lógica de autenticación y registro de usuarios.
 * */
@Service
@RequiredArgsConstructor // Genera automáticamente un constructor que incluya todos los campos marcados como final en una clase.
public class AuthService {

    private final UserRepository userRepository; // Interactúa con la base de datos para operaciones relacionadas con usuarios.
    private final JwtService jwtService; // Gestiona la generación y validación de tokens JWT.
    private final PasswordEncoder passwordEncoder;  // Utilizado para cifrar las contraseñas antes de almacenarlas.
    private final AuthenticationManager authenticationManager; // Utilizado para autenticar a los usuarios.

    /**
     * Realiza la autenticación del usuario utilizando las credenciales proporcionadas,
     * busca al usuario en la base de datos,
     * genera un token JWT basado en la información del usuario autenticado
     * y lo devuelve encapsulado en un objeto AuthResponse.
     * */
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate( // AuthenticationManager se encarga de verificar si esas credenciales son válidas.
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())); // Se crea un objeto con las credenciales proporcionadas en la solicitud(request).

        // Se Busca al usuario en la base de datos basándose en el nombre de usuario proporcionado en la solicitud de inicio de sesión. Si no encuentra al usuario, lanza una excepción.
        UserDetails user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        String token = jwtService.getToken(user); // Utiliza el servicio jwtService para generar un token JWT basado en la información del usuario autenticado.
        List<String> role = user.getAuthorities().stream().map(
            (auth)->{return auth.getAuthority();}).toList();
        return AuthResponse.builder()  // Retorna un objeto AuthResponse que contiene el token JWT generado.
                .token(token)
                .role(role)
                .build();
    }

    /**
     * Este método se encarga de registrar nuevos usuarios en el sistema.
     * */
    public AuthResponse register(RegisterRequest request) {
        if (!"USER".equals(request.getRole()) && !"OWNER".equals(request.getRole())) {
            throw new BadRequestException("Registration role must be USER or OWNER");
        }
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new ConflictException("Username already registered");
        }
        Role role = Role.valueOf(request.getRole());

        User user = User.builder() // Se crea un User utilizando la información proporcionada en la request.
                .username(request.getUsername())
                .password(passwordEncoder.encode( request.getPassword())) // Utiliza el passwordEncoder para cifrar la contraseña antes de almacenarla en la base de datos.
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .country(request.getCountry())
                .role(role)
                .build();

        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            // La restricción única también protege registros concurrentes del mismo username.
            if (userRepository.findByUsername(request.getUsername()).isPresent()) {
                throw new ConflictException("Username already registered");
            }
            throw exception;
        }

        return AuthResponse.builder() // Retorna un objeto AuthResponse que encapsula el token JWT generado.
                .token(jwtService.getToken(user)) // Se genera un token JWT utilizando jwtService.
                .build();
    }

}

