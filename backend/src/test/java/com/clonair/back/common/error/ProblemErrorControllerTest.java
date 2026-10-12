package com.clonair.back.common.error;

import jakarta.servlet.RequestDispatcher;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

class ProblemErrorControllerTest {
    @ParameterizedTest
    @ValueSource(ints = {400, 404, 405, 500, 503})
    void dispatchedError_keepsStatusAndOriginalPathWithoutLeakingException(int status) {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/error");
        request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, status);
        request.setAttribute(RequestDispatcher.ERROR_REQUEST_URI, "/original");
        request.setAttribute(RequestDispatcher.ERROR_EXCEPTION, new RuntimeException("private database URL"));
        var response = new ProblemErrorController().error(request);

        assertThat(response.getStatusCode().value()).isEqualTo(status);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_PROBLEM_JSON);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getInstance().toString()).isEqualTo("/original");
        assertThat(response.getBody().getDetail()).doesNotContain("private");
        if (status >= 500) assertThat(response.getBody().getDetail()).isEqualTo(ApiProblems.INTERNAL_DETAIL);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 200, 999})
    void directErrorOrInvalidStatus_returnsGeneric500(int status) {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/error");
        if (status != 0) request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, status);
        var response = new ProblemErrorController().error(request);

        assertThat(response.getStatusCode().value()).isEqualTo(500);
        assertThat(response.getBody().getInstance().toString()).isEqualTo("/error");
        assertThat(response.getBody().getDetail()).isEqualTo(ApiProblems.INTERNAL_DETAIL);
    }
}
