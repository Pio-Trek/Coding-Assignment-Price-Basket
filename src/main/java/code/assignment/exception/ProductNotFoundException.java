package code.assignment.exception;

/**
 * Exception when the item does not exist in the current stock.
 */

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String message) {
        super(message);
    }
}
