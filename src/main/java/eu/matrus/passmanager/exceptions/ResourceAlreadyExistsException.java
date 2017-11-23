package eu.matrus.passmanager.exceptions;


public class ResourceAlreadyExistsException extends RuntimeException {
    private String resourceId;

    public ResourceAlreadyExistsException(String resourceId, String message) {
        super(message);
        this.resourceId = resourceId;
    }
}
