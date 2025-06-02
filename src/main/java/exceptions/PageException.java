package exceptions;

/**
 * Exception thrown when page operations fail
 */
public class PageException extends FrameworkException {
    
    public PageException(String pageName, String message) {
        super("PAGE_ERROR", pageName, message);
    }
    
    public PageException(String pageName, String message, Throwable cause) {
        super("PAGE_ERROR", pageName, message, cause);
    }
    
    public PageException(String pageName, String operation, String elementInfo, Throwable cause) {
        super("PAGE_ERROR", pageName, 
              String.format("Page operation '%s' failed on element '%s'", operation, elementInfo), cause);
    }
}
