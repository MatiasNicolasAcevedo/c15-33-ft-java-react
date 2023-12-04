package com.clonair.back.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    
    @Bean
    public CorsConfigurationSource corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*"); // Permite todos los orígenes, cámbialo según tu configuración
        config.addAllowedMethod("*"); // Permite todos los métodos HTTP (GET, POST, etc.)
        config.addAllowedHeader("*"); // Permite todos los encabezados
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
