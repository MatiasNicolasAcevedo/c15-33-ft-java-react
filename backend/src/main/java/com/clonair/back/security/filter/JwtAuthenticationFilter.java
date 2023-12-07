package com.clonair.back.security.filter;

import com.clonair.back.security.service.JwtService;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Esta clase es responsable de interceptar las solicitudes entrantes,
 * validar los tokens JWT y establecer la autenticación en el contexto de seguridad si el token es válido.
 * */
@Component // @Component marca esta clase como un componente para ser escaneado y manejado por Spring como un bean.
@RequiredArgsConstructor // Genera automáticamente un constructor que incluya todos los campos marcados como final en una clase.
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService; // Service que maneja la generación, validación y extracción de tokens JWT.
    private final UserDetailsService userDetailsService; // Se utiliza para cargar los detalles del usuario a partir del nombre de usuario extraído del token JWT.

    /** FALTA EXPLICACION */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String token = getTokenFromRequest(request);
        final String username;

        if (token != null) {
            username = jwtService.getUsernameFromToken(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtService.isTokenValid(token, userDetails)) { // Llamada a isTokenValid con el token y userDetails
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }

        filterChain.doFilter(request, response);
    }


    /**
     * Este método extrae el token JWT del encabezado de autorización de la solicitud.
     * Busca si el encabezado comienza con "Bearer " y luego devuelve el token.
     * */
    private String getTokenFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

}
