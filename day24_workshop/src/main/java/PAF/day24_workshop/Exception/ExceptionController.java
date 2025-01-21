package PAF.day24_workshop.Exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import PAF.day24_workshop.Models.exception.ApiError;
import PAF.day24_workshop.Models.exception.LimitException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice
public class ExceptionController {
    
    @ExceptionHandler(LimitException.class)
        public ResponseEntity<ApiError> handleException(LimitException ex, HttpServletRequest request,
            HttpServletResponse response) {

        ApiError apiError = new ApiError(404, ex.getMessage(), new Date());

        return new ResponseEntity<ApiError>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
