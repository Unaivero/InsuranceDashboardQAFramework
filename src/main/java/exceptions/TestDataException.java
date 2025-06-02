package exceptions;

/**
 * Exception thrown when test data operations fail
 */
public class TestDataException extends FrameworkException {
    
    public TestDataException(String message) {
        super("TEST_DATA_ERROR", "TEST_DATA", message);
    }
    
    public TestDataException(String message, Throwable cause) {
        super("TEST_DATA_ERROR", "TEST_DATA", message, cause);
    }
    
    public TestDataException(String dataType, String operation, String details) {
        super("TEST_DATA_ERROR", "TEST_DATA", 
              String.format("Test data operation '%s' failed for type '%s': %s", operation, dataType, details));
    }
}
