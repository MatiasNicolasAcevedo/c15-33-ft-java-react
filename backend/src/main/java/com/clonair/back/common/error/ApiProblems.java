package com.clonair.back.common.error;

import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public final class ApiProblems {
    public static final String UNAUTHORIZED_DETAIL = "Authentication is required or credentials are invalid";
    public static final String FORBIDDEN_DETAIL = "You do not have permission to perform this action";
    public static final String INTERNAL_DETAIL = "An unexpected error occurred";

    private ApiProblems() {}

    public static ProblemDetail create(HttpStatus status, String detail, String path) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setInstance(URI.create(path));
        return problem;
    }
}
