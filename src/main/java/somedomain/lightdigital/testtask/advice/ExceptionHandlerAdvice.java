package somedomain.lightdigital.testtask.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author Ilya Avkhimenya
 */
@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<String> handleConflict(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
