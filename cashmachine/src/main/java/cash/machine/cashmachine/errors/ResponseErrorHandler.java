package cash.machine.cashmachine.errors;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ResponseErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ FeignException.class })
    public ResponseEntity<Object> handleConstraintViolation(
            FeignException ex,
            WebRequest request
    ) {
        return ResponseEntity.ok(ex.getLocalizedMessage());
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        return new ResponseEntity<>(HttpStatus.valueOf(500));
    }

}
