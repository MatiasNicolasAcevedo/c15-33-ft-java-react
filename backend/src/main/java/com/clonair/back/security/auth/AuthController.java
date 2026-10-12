package com.clonair.back.security.auth;

import com.clonair.back.security.request.LoginRequest;
import com.clonair.back.security.request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador que gestiona las solicitudes HTTP relacionadas con la autenticación y el registro de usuarios.
 * */
@RestController // Indica que esta clase es un controlador REST y que las respuestas de los métodos se serializarán automáticamente a JSON.
@RequestMapping("v1/api/auth") // Establece la raíz de la URL para todas las solicitudes manejadas por este controlador.
@RequiredArgsConstructor // Genera un constructor que incluye todos los campos marcados como final.
public class AuthController {

    private final AuthService authService;

    /**
     * Maneja las solicitudes POST a /auth/login.
     * Toma un objeto LoginRequest desde el cuerpo de la solicitud (@RequestBody)
     * y llama al servicio authService.login(request).
     * Retorna una respuesta HTTP con el token de autenticación generado por el servicio.
     * */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

    /**
     * Maneja las solicitudes POST a /auth/register.
     * Toma un objeto RegisterRequest desde el cuerpo de la solicitud (@RequestBody)
     * y llama al servicio authService.register(request).
     * Retorna una respuesta HTTP con el token de autenticación generado por el servicio.
     * */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request){
        return ResponseEntity.status(201).body(authService.register(request));
    }

}
