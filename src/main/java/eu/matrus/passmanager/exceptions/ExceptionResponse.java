package eu.matrus.passmanager.exceptions;

import lombok.Data;

@Data
class ExceptionResponse {
    private String errorCode;
    private String errorMessage;
}