package eu.matrus.passmanager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlingController {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity resourceNotFound(ResourceNotFoundException ex) {
        String message = ex.getResourceId() + " resource not found";
        return buildResponseEntity(new ExceptionResponse(HttpStatus.BAD_REQUEST, message, ex));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity invalidInput(MethodArgumentNotValidException ex) {
        ExceptionResponse apiException = new ExceptionResponse(HttpStatus.BAD_REQUEST);
        apiException.setErrorMessage("Validation Error");
        apiException.addValidationErrors(ex.getBindingResult().getFieldErrors());
        return buildResponseEntity(apiException);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity resourceExists(ResourceAlreadyExistsException ex) {
        String message = ex.getResourceId() + " resource already exists";
        return buildResponseEntity(new ExceptionResponse(HttpStatus.CONFLICT, message, ex));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity resourceAccessDenied(AccessDeniedException ex) {
        ExceptionResponse apiException = new ExceptionResponse(HttpStatus.FORBIDDEN);
        apiException.setErrorMessage("Access Denied Error");
        return buildResponseEntity(apiException);
    }

    private ResponseEntity buildResponseEntity(ExceptionResponse exceptionResponse) {
        return new ResponseEntity<>(exceptionResponse, exceptionResponse.getStatusCode());
    }
}