package appbarber.api;


import appbarber.enums.ApiError;
import appbarber.exception.ConflictException;
import appbarber.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.BindException;
import java.time.OffsetDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {


    private ResponseEntity<ApiError> build(HttpStatus status, String msg, HttpServletRequest req) {
        return
                ResponseEntity.status(status).body(
                new ApiError(req.getRequestURI(), status.value(), status.getReasonPhrase(),  OffsetDateTime.now())
        );
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(NotFoundException ex, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), req);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiError> handleConflict(ConflictException ex, HttpServletRequest req) {
        return build(HttpStatus.CONFLICT, ex.getMessage(), req);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class, IllegalArgumentException.class})
    public ResponseEntity<ApiError> handleBadRequest(Exception ex, HttpServletRequest req) {
        String msg = ex.getMessage();
        return build(HttpStatus.BAD_REQUEST, msg, req);
    }
}
