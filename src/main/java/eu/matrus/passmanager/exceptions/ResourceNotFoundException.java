package eu.matrus.passmanager.exceptions;


public class ResourceNotFoundException extends RuntimeException {
    private String resourceId;

    public ResourceNotFoundException(String resourceId, String message) {
        super(message);
        this.resourceId = resourceId;
    }
}
