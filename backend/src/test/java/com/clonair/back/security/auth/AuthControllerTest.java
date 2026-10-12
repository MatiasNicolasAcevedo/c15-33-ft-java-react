package com.clonair.back.security.auth;

import com.clonair.back.security.config.SecurityConfig;
import com.clonair.back.security.jwt.JwtAuthenticationFilter;
import com.clonair.back.security.jwt.JwtService;
import com.clonair.back.security.request.LoginRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class,
        properties = "spring.config.location=classpath:/application-test.properties")
@Import({SecurityConfig.class, JwtAuthenticationFilter.class, com.clonair.back.common.error.SecurityProblemHandler.class})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private UserDetailsService userDetailsService;
    @MockBean
    private AuthenticationProvider authenticationProvider;

    @Test
    void login_whenPublicRequest_delegatesAndReturnsTokenAndRole() throws Exception {
        LoginRequest request = new LoginRequest("owner@example.test", "Test1234!");
        when(authService.login(request)).thenReturn(
                new AuthResponse("test-token", List.of("OWNER")));

        mockMvc.perform(post("/v1/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"owner@example.test","password":"Test1234!"}
                                """))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").value("test-token"))
                .andExpect(jsonPath("$.role[0]").value("OWNER"));

        verify(authService).login(request);
    }
}
