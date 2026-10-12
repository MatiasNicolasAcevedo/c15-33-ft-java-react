package com.clonair.back.common.error;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GlobalExceptionHandlerTest {
    private MockMvc mvc;
    private final FailureController controller = new FailureController();

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    @ParameterizedTest
    @MethodSource("failures")
    void failure_whenThrown_returnsStatusAndProblemWithoutInternalDetails(Exception exception, int code, String detail)
            throws Exception {
        controller.failure = exception;

        mvc.perform(get("/failure"))
                .andExpect(status().is(code))
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(code))
                .andExpect(jsonPath("$.type").value("about:blank"))
                .andExpect(jsonPath("$.detail").value(detail))
                .andExpect(jsonPath("$.instance").value("/failure"))
                .andExpect(jsonPath("$.trace").doesNotExist())
                .andExpect(jsonPath("$.exception").doesNotExist());
    }

    static Stream<Arguments> failures() {
        return Stream.of(
                Arguments.of(new NotFoundException("Property not found"), 404, "Property not found"),
                Arguments.of(new ForbiddenException("Not the owner"), 403, "Not the owner"),
                Arguments.of(new ConflictException("Username already registered"), 409, "Username already registered"),
                Arguments.of(new BadRequestException("Invalid role"), 400, "Invalid role"),
                Arguments.of(new IllegalArgumentException("internal enum class"), 400, "Invalid request value"),
                Arguments.of(new BadCredentialsException("secret username"), 401, ApiProblems.UNAUTHORIZED_DETAIL),
                Arguments.of(new AccessDeniedException("internal rule"), 403, ApiProblems.FORBIDDEN_DETAIL),
                Arguments.of(new Exception("jdbc password=private"), 500, ApiProblems.INTERNAL_DETAIL));
    }

    @Test
    void authenticationFailure_returnsBearerChallenge() throws Exception {
        controller.failure = new BadCredentialsException("private");
        mvc.perform(get("/failure")).andExpect(header().string("WWW-Authenticate", "Bearer"));
    }

    @Test
    void mvcErrors_preserveClientStatusAndProblemContract() throws Exception {
        mvc.perform(post("/number").contentType(MediaType.APPLICATION_JSON).content("{invalid"))
                .andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));
        mvc.perform(get("/number"))
                .andExpect(status().isMethodNotAllowed()).andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));
        mvc.perform(post("/number").contentType(MediaType.TEXT_PLAIN).content("x"))
                .andExpect(status().isUnsupportedMediaType()).andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));
        mvc.perform(get("/parameter").param("value", "invalid"))
                .andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));
        mvc.perform(get("/parameter"))
                .andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));
    }

    @RestController
    static class FailureController {
        Exception failure;
        @GetMapping("/failure")
        void fail() throws Exception { throw failure; }
        @PostMapping("/number")
        void number(@RequestBody NumberRequest number) {}
        @GetMapping("/parameter")
        void parameter(@RequestParam int value) {}
    }
    record NumberRequest(int value) {}
}
