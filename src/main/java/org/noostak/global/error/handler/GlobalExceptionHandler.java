package org.noostak.global.error.handler;

import org.noostak.global.error.core.BaseException;
import org.noostak.global.error.core.ErrorCode;
import org.noostak.global.error.core.ErrorResponse;
import org.noostak.global.utils.GlobalLogger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponse> handleMissingRequestHeaderException(MissingRequestHeaderException e){
        return getErrorResponse(e,GlobalErrorCode.MISSING_HEADER);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceException(NoResourceFoundException e) {
        return getErrorResponse(e,GlobalErrorCode.RESOURCE_NOT_FOUND);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse>
        handleMethodNotAllowedException(HttpRequestMethodNotSupportedException e) {
        return getErrorResponse(e,GlobalErrorCode.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException e) {
        return getErrorResponse(e,e.getErrorCode());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
        return getErrorResponse(e,GlobalErrorCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return getErrorResponse(e,GlobalErrorCode.INTERNAL_SERVER_ERROR);
    }

    private static ResponseEntity<ErrorResponse> getErrorResponse(Exception e, GlobalErrorCode errorCode) {
        GlobalLogger.error(e.toString());

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.of(errorCode));
    }

    private static ResponseEntity<ErrorResponse> getErrorResponse(Exception e, ErrorCode errorCode) {
        GlobalLogger.error(e.toString());

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.of(errorCode, e.getMessage()));
    }
}
