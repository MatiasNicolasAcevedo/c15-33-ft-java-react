package com.clonair.back.common.error;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Mantiene el mismo contrato en los errores despachados por el contenedor. */
@RestController
public class ProblemErrorController implements ErrorController {
    @RequestMapping("/error")
    public ResponseEntity<ProblemDetail> error(HttpServletRequest request) {
        Object code = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        HttpStatus status = code instanceof Integer ? HttpStatus.resolve((Integer) code) : null;
        if (status == null || !status.isError()) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        Object originalPath = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        String path = originalPath instanceof String ? (String) originalPath : request.getRequestURI();
        String detail = status.is5xxServerError() ? ApiProblems.INTERNAL_DETAIL : status.getReasonPhrase();
        return ResponseEntity.status(status).contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(ApiProblems.create(status, detail, path));
    }
}
