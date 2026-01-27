package com.acme.jga.advice;

import com.acme.jga.config.AppGenericProperties;
import com.acme.jga.domain.exceptions.FunctionalErrors;
import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.exceptions.Scope;
import com.acme.jga.domain.validation.ValidationException;
import com.acme.jga.rest.dtos.shared.ApiError;
import com.acme.jga.rest.dtos.shared.ApiErrorDetail;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static com.acme.jga.domain.shared.StreamUtil.ofNullableList;

@RequiredArgsConstructor
@ControllerAdvice
@Slf4j
public class AppControllerAdvice {
    private final AppGenericProperties appProperties;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleInternal(Exception exception, HttpServletRequest request) {
        String dumpFileName = generateDumpFileName();
        try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw);
             BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get(appProperties.getErrorPath() + "/" + dumpFileName))) {
            exception.printStackTrace(pw);
            String httpRequestDump = dumpHttpRequest(request);
            bufferedWriter.write(httpRequestDump);
            bufferedWriter.newLine();
            bufferedWriter.write(sw.toString());
            bufferedWriter.flush();
        } catch (IOException e) {
            log.error("An error occurred writing dump file {}", dumpFileName);
        }
        ApiError apiError = new ApiError(Scope.INTERNAL.name(),
                HttpStatus.INTERNAL_SERVER_ERROR.name(),
                "Internal server error, dump file [" + dumpFileName + "]",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                Collections.emptyList());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(apiError);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiError> handleValidationException(Exception ex) {
        List<ApiErrorDetail> apiErrorDetails = ofNullableList(((ValidationException) ex).getValidationErrors()).map(validationError -> ApiErrorDetail.builder()
                .code(validationError.getValidationRule())
                .field(validationError.getFieldName())
                .message(validationError.getMessage())
                .build()).toList();
        final ApiError apiError = ApiError.builder()
                .scope(Scope.REQUEST.name())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .code(HttpStatus.BAD_REQUEST.name())
                .details(apiErrorDetails)
                .message("ArgumentNotValid").build();
        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler(FunctionalException.class)
    public ResponseEntity<ApiError> handleFunctional(FunctionalException exception, HttpServletRequest request) {
        int targetStatus = HttpStatus.BAD_REQUEST.value();
        if (FunctionalErrors.NOT_FOUND.name().equals(exception.getCode())) {
            targetStatus = HttpStatus.NOT_FOUND.value();
        }
        ApiError apiError = new ApiError(exception.getScope(),
                exception.getCode(),
                exception.getMessage(),
                targetStatus,
                Collections.emptyList());
        return ResponseEntity.status(targetStatus).body(apiError);
    }

    /**
     *
     * Generate dump file name for exception and error reports.
     *
     * @return Dump file name.
     */
    private String generateDumpFileName() {
        String prefix = appProperties.getModuleName() + "_dump_";
        String suffix = ".txt";
        return prefix + System.currentTimeMillis() + suffix;
    }

    /**
     * Dump HttpServletRequest parameters & headers.
     *
     * @param request HttpServletRequest.
     * @return Dump
     */
    private String dumpHttpRequest(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("Request URI=").append(request.getRequestURI());
        sb.append("\n").append("Request Method=").append(request.getMethod());
        sb.append("\n").append("Server Name=").append(request.getServerName());
        sb.append("\n").append("Remote Address=").append(request.getRemoteAddr());
        sb.append("\n").append("Headers");
        request.getHeaderNames().asIterator().forEachRemaining(name -> {
            sb.append("\n").append(name).append("=").append(request.getHeader(name));
        });
        request.getParameterNames().asIterator().forEachRemaining(name -> {
            sb.append("\n").append("param: [").append(name).append("]=[").append(request.getParameter(name)).append("]");
        });
        return sb.toString();
    }
}
