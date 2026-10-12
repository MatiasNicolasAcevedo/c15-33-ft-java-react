package com.clonair.back.property;

import com.clonair.back.security.config.SecurityConfig;
import com.clonair.back.security.jwt.JwtAuthenticationFilter;
import com.clonair.back.security.jwt.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PropertyController.class,
        properties = "spring.config.location=classpath:/application-test.properties")
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
class PropertyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PropertyService propertyService;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private UserDetailsService userDetailsService;
    @MockBean
    private AuthenticationProvider authenticationProvider;

    @Test
    void delete_whenIdProvided_delegatesAndReturns200() throws Exception {
        mockMvc.perform(delete("/v1/api/property/abc")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer x"))
                .andExpect(status().isOk());

        verify(propertyService).delete("abc", "Bearer x");
    }

    @Test
    void delete_whenIdMissing_doesNotCallService() throws Exception {
        mockMvc.perform(delete("/v1/api/property")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer x"))
                .andExpect(status().is4xxClientError());

        verifyNoInteractions(propertyService);
    }
}
