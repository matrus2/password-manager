package eu.matrus.passmanager.exceptions;

import lombok.Getter;

public class ResourceAlreadyExistsException extends RuntimeException {
    @Getter
    private String resourceId;

    public ResourceAlreadyExistsException(String resourceId, String message) {
        super(message);
        this.resourceId = resourceId;
    }
}
