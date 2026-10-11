package com.clonair.back.security.config;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.cors.DefaultCorsProcessor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CorsConfigTest {

    @ParameterizedTest
    @ValueSource(strings = {"", " ", ", ,"})
    void preflight_whenOriginsAreEmpty_deniesCrossOriginRequests(String origins) throws Exception {
        var source = new CorsConfig().corsConfigurationSource(origins);
        var request = new MockHttpServletRequest("OPTIONS", "/v1/api/auth/login");
        request.addHeader("Origin", "http://localhost:5173");
        request.addHeader("Access-Control-Request-Method", "POST");
        var response = new MockHttpServletResponse();

        boolean allowed = new DefaultCorsProcessor().processRequest(
                source.getCorsConfiguration(request), request, response);

        assertThat(allowed).isFalse();
        assertThat(response.getStatus()).isEqualTo(403);
        assertThat(response.getHeader("Access-Control-Allow-Origin")).isNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"*", "http://localhost:5173,*", "https://*.example.test"})
    void configuration_whenWildcardOrigin_refusesToEnableUnrestrictedCors(String origins) {
        assertThatThrownBy(() -> new CorsConfig().corsConfigurationSource(origins))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("CORS_ALLOWED_ORIGINS");
    }
}
