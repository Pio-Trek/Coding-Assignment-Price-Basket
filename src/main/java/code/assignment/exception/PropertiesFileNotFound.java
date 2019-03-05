package code.assignment.exception;

/**
 * Exception when properties file cannot be found.
 */

public class PropertiesFileNotFound extends RuntimeException {

    public PropertiesFileNotFound(String message) {
        super(message);
    }
}
