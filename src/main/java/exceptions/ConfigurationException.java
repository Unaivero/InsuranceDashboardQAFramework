package exceptions;

/**
 * Exception thrown when configuration operations fail
 */
public class ConfigurationException extends FrameworkException {
    
    public ConfigurationException(String message) {
        super("CONFIG_ERROR", "CONFIGURATION", message);
    }
    
    public ConfigurationException(String message, Throwable cause) {
        super("CONFIG_ERROR", "CONFIGURATION", message, cause);
    }
    
    public ConfigurationException(String configType, String filePath, Throwable cause) {
        super("CONFIG_ERROR", "CONFIGURATION", 
              String.format("Failed to load %s configuration from: %s", configType, filePath), cause);
    }
}
