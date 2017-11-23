package eu.matrus.passmanager.exceptions;

import lombok.Data;

@Data
public class ResourceNotFoundException extends RuntimeException {
    private String resourceId;

    public ResourceNotFoundException(String resourceId, String message) {
        super(message);
        this.resourceId = resourceId;
    }
}
