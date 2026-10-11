package com.clonair.back.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Se encarga de manejar la creación, validación y extracción de información de los tokens JWT.
 * Los "Claims" en esta clase representan la información contenida dentro de un token JWT.
 * */
@Service
public class JwtService {

    private final Key signingKey;
    private final long expirationMillis;

    public JwtService(@Value("${jwt.secret}") String secret,
                      @Value("${jwt.expiration}") long expirationMillis) {
        if (!StringUtils.hasText(secret)) {
            throw new IllegalArgumentException("JWT_SECRET is required and must be Base64 encoded");
        }
        byte[] keyBytes;
        try {
            keyBytes = Base64.getDecoder().decode(secret);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("JWT_SECRET must be valid Base64");
        }
        if (keyBytes.length < 32) {
            throw new IllegalArgumentException("JWT_SECRET must decode to at least 32 bytes (256 bits)");
        }
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
        this.expirationMillis = expirationMillis;
    }

    /**
     * Método que recibe un objeto UserDetails y devuelve un token JWT.
     * Este método llama a otro método interno, que genera el token con cualquier extraClaims adicional proporcionado.
     * */
    public String getToken(UserDetails user) { //
        return getToken(new HashMap<>(), user);
    }

    /**
     * Este método crea un token JWT utilizando la librería io.jsonwebtoken.
     * Toma un mapa de Claims adicionales (opcional) y los detalles del usuario (UserDetails).
     * */
    private String getToken(Map<String,Object> extraClaims, UserDetails user) {
        long issuedAt = System.currentTimeMillis();
        return Jwts
                .builder()
                .setClaims(extraClaims) // Establece los Claims adicionales proporcionados.
                .setSubject(user.getUsername()) // Establece el sujeto del token como el nombre de usuario del objeto UserDetails.
                .setIssuedAt(new Date(issuedAt)) // Establece la fecha de emisión del token como el momento actual.
                .setExpiration(new Date(issuedAt + expirationMillis)) // Duración configurable en milisegundos; por defecto 24 horas.
                .signWith(signingKey, SignatureAlgorithm.HS256) // Firma el token con el algoritmo de hash HS256 utilizando la clave secreta.
                .compact(); // Devuelve el token JWT compactado como una cadena.
    }

    /**
     * Este método extrae el nombre de usuario (subject) del token JWT.
     * Utiliza el método genérico getClaim para obtener el sujeto del token (Claims::getSubject).
     * Devuelve el nombre de usuario almacenado en el token.
     * */
    public String getUsernameFromToken(String token) {
        if (StringUtils.hasText(token)) {
            return getClaim(token, Claims::getSubject);
        }
        return null;
    }

    /**
     * Este método verifica la validez del token JWT.
     * Obtiene el nombre de usuario del token utilizando getUsernameFromToken.
     * Compara este nombre de usuario con el nombre de usuario del objeto UserDetails proporcionado como argumento.
     * Verifica si el token ha expirado utilizando isTokenExpired.
     * Devuelve true si el token es válido (el nombre de usuario coincide y el token no ha expirado).
     * */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        if (StringUtils.hasText(token)) {
            final String username = getUsernameFromToken(token);
            return (username != null && username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        }
        return false; // Retorna falso para tokens nulos o vacíos
    }

/* REVISAR
    public boolean isTokenValid(String token) {
        return (!isTokenExpired(token));
    }
*/
    /**
     * Este método devuelve todos los Claims almacenados en el token JWT.
     * Utiliza la librería io.jsonwebtoken para analizar el token,
     * verifica la firma con la clave correspondiente
     * y devuelve el cuerpo (body) del token, que contiene todos los Claims.
     * */
    private Claims getAllClaims(String token) {
        if (StringUtils.hasText(token)) {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }
        return null;
    }

    /**
     * Este método genérico obtiene un Claim específico del token.
     * Utiliza el método getAllClaims para obtener todos los Claims del token
     * y luego aplica la función claimsResolver para obtener el Claim específico deseado.
     * Es genérico en su implementación, lo que permite obtener cualquier Claim del token.
     * */
    public <T> T getClaim(String token, Function<Claims,T> claimsResolver) {
        final Claims claims=getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Este método obtiene la fecha(Date) de expiración del token.
     * Utiliza el método getClaim con Claims::getExpiration,
     * para obtener la fecha de expiración almacenada en el token.
     * */
    private Date getExpiration(String token){
        return getClaim(token, Claims::getExpiration);
    }

    /**
     * Este método verifica si el token ha expirado.
     * Utiliza getExpiration para obtener la fecha de expiración del token
     * y compara esa fecha con la fecha actual (new Date()).
     * Devuelve true si el token ha expirado.
     * */
    private boolean isTokenExpired(String token){
        return getExpiration(token).before(new Date());
    }

}
