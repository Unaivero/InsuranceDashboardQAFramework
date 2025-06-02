package exceptions;

/**
 * Exception thrown when WebDriver operations fail
 */
public class WebDriverException extends FrameworkException {
    
    public WebDriverException(String message) {
        super("WEBDRIVER_ERROR", "WEBDRIVER", message);
    }
    
    public WebDriverException(String message, Throwable cause) {
        super("WEBDRIVER_ERROR", "WEBDRIVER", message, cause);
    }
    
    public WebDriverException(String operation, String details, Throwable cause) {
        super("WEBDRIVER_ERROR", "WEBDRIVER", 
              String.format("WebDriver operation '%s' failed: %s", operation, details), cause);
    }
}
