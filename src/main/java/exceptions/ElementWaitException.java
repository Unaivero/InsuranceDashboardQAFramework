package exceptions;

/**
 * Exception thrown when element wait operations fail
 */
public class ElementWaitException extends FrameworkException {
    
    private final long timeoutSeconds;
    private final String waitType;
    
    public ElementWaitException(String waitType, String elementInfo, long timeoutSeconds) {
        super("WAIT_TIMEOUT", "WAIT_UTIL", 
              String.format("Wait operation '%s' timed out after %d seconds for element: %s", 
                          waitType, timeoutSeconds, elementInfo));
        this.timeoutSeconds = timeoutSeconds;
        this.waitType = waitType;
    }
    
    public ElementWaitException(String waitType, String elementInfo, long timeoutSeconds, Throwable cause) {
        super("WAIT_TIMEOUT", "WAIT_UTIL", 
              String.format("Wait operation '%s' timed out after %d seconds for element: %s", 
                          waitType, timeoutSeconds, elementInfo), cause);
        this.timeoutSeconds = timeoutSeconds;
        this.waitType = waitType;
    }
    
    public long getTimeoutSeconds() {
        return timeoutSeconds;
    }
    
    public String getWaitType() {
        return waitType;
    }
}
