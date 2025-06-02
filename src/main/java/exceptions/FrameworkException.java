package exceptions;

/**
 * Base exception class for the Insurance Dashboard QA Framework
 * All framework-specific exceptions should extend this class
 */
public class FrameworkException extends RuntimeException {
    
    private final String errorCode;
    private final String component;
    
    public FrameworkException(String message) {
        super(message);
        this.errorCode = "FRAMEWORK_ERROR";
        this.component = "UNKNOWN";
    }
    
    public FrameworkException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "FRAMEWORK_ERROR";
        this.component = "UNKNOWN";
    }
    
    public FrameworkException(String errorCode, String component, String message) {
        super(message);
        this.errorCode = errorCode;
        this.component = component;
    }
    
    public FrameworkException(String errorCode, String component, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.component = component;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public String getComponent() {
        return component;
    }
    
    @Override
    public String toString() {
        return String.format("[%s][%s] %s", errorCode, component, getMessage());
    }
}
