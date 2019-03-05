package code.assignment.exception;

/**
 * Exception when a special offer is not found.
 */

public class OfferNotFoundException extends RuntimeException {

    public OfferNotFoundException(String message) {
        super(message);
    }
}
