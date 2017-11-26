package eu.matrus.passmanager.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

@Data
class ExceptionResponse {
    private HttpStatus statusCode;
    private String errorMessage;
    private String debugMessage;
    private List<ValidationError> subErrors;

    ExceptionResponse(HttpStatus status) {
        this.statusCode = status;
    }

    ExceptionResponse(HttpStatus status, String message, Throwable ex) {
        this.statusCode = status;
        this.errorMessage = message;
        this.debugMessage = ex.getLocalizedMessage();
    }

    private void addValidationError(ValidationError subError) {
        if (subErrors == null) {
            subErrors = new ArrayList<>();
        }
        subErrors.add(subError);
    }

    private void addValidationError(String object, String field, Object rejectedValue, String message) {
        addValidationError(new ValidationError(object, field, rejectedValue, message));
    }

    private void addValidationError(FieldError fieldError) {
        this.addValidationError(
                fieldError.getObjectName(),
                fieldError.getField(),
                fieldError.getRejectedValue(),
                fieldError.getDefaultMessage());
    }

    void addValidationErrors(List<FieldError> fieldErrors) {
        fieldErrors.forEach(this::addValidationError);
    }
}

@Data
@AllArgsConstructor
class ValidationError {
    private String object;
    private String field;
    private Object rejectedValue;
    private String message;
}