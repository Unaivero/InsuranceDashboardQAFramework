package utils;

import exceptions.ConfigurationException;
import org.slf4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class ConfigReader {
    private static Properties appProperties;
    private static final String APP_PROPERTIES_PATH = "config/config.properties";
    private static final Logger staticLogger = LoggingUtil.getLogger(ConfigReader.class);

    // Static initializer block to load app properties once
    static {
        appProperties = new Properties();
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream(APP_PROPERTIES_PATH)) {
            if (input == null) {
                staticLogger.error("Unable to find application properties file: {}", APP_PROPERTIES_PATH);
                LoggingUtil.logConfigLoad("application", APP_PROPERTIES_PATH, false);
                throw new ConfigurationException("application", APP_PROPERTIES_PATH, 
                    new IOException("Application properties file not found: " + APP_PROPERTIES_PATH));
            } else {
                appProperties.load(new InputStreamReader(input, StandardCharsets.UTF_8));
                staticLogger.info("Application properties loaded successfully from: {}", APP_PROPERTIES_PATH);
                LoggingUtil.logConfigLoad("application", APP_PROPERTIES_PATH, true);
            }
        } catch (IOException ex) {
            staticLogger.error("Error loading application configuration properties from: {}", APP_PROPERTIES_PATH, ex);
            LoggingUtil.logConfigLoad("application", APP_PROPERTIES_PATH, false);
            throw new ConfigurationException("application", APP_PROPERTIES_PATH, ex);
        }
    }
    private Properties properties;
    private static final String DEFAULT_LANGUAGE = "en";
    private String currentLanguage;
    private final Logger logger;

    public ConfigReader(String language) {
        this.logger = LoggingUtil.getLogger(this.getClass());
        
        // Validate language parameter
        ErrorHandler.validateNotEmpty(language, "language");
        
        this.currentLanguage = language.trim();
        logger.debug("Initializing ConfigReader with language: {}", this.currentLanguage);
        loadProperties();
    }

    private void loadProperties() {
        properties = new Properties();
        String propertiesFileName = "config/messages_" + currentLanguage + ".properties";
        
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(propertiesFileName)) {
            if (input == null) {
                logger.warn("Language properties file not found: {}", propertiesFileName);
                
                // Fallback to default language if specific language file not found
                if (!currentLanguage.equals(DEFAULT_LANGUAGE)) {
                    logger.info("Falling back to default language ({})", DEFAULT_LANGUAGE);
                    currentLanguage = DEFAULT_LANGUAGE;
                    propertiesFileName = "config/messages_" + currentLanguage + ".properties";
                    
                    try (InputStream defaultInput = getClass().getClassLoader().getResourceAsStream(propertiesFileName)) {
                        if (defaultInput == null) {
                            String errorMsg = "Default language properties file not found: " + propertiesFileName;
                            logger.error(errorMsg);
                            LoggingUtil.logConfigLoad("language", propertiesFileName, false);
                            throw new ConfigurationException("language", propertiesFileName, 
                                new IOException(errorMsg));
                        }
                        properties.load(new InputStreamReader(defaultInput, StandardCharsets.UTF_8));
                        logger.info("Default language properties loaded successfully: {}", propertiesFileName);
                        LoggingUtil.logConfigLoad("language", propertiesFileName, true);
                    }
                } else {
                    String errorMsg = "Default language properties file not found: " + propertiesFileName;
                    logger.error(errorMsg);
                    LoggingUtil.logConfigLoad("language", propertiesFileName, false);
                    throw new ConfigurationException("language", propertiesFileName, 
                        new IOException(errorMsg));
                }
            } else {
                properties.load(new InputStreamReader(input, StandardCharsets.UTF_8));
                logger.info("Language properties loaded successfully: {}", propertiesFileName);
                LoggingUtil.logConfigLoad("language", propertiesFileName, true);
            }
        } catch (IOException ex) {
            logger.error("Error loading language properties from: {}", propertiesFileName, ex);
            LoggingUtil.logConfigLoad("language", propertiesFileName, false);
            throw new ConfigurationException("language", propertiesFileName, ex);
        }
    }

    public String getProperty(String key) {
        ErrorHandler.validateNotEmpty(key, "property key");
        
        String value = properties.getProperty(key);
        if (value == null) {
            String errorMsg = String.format("Property not found for key: %s in language: %s", key, currentLanguage);
            logger.warn(errorMsg);
            throw new ConfigurationException(String.format("Missing property key '%s' in language '%s'", key, currentLanguage));
        }
        
        logger.debug("Retrieved property '{}' = '{}'", key, value);
        return value;
    }

    /**
     * Get property with default value if not found
     */
    public String getProperty(String key, String defaultValue) {
        ErrorHandler.validateNotEmpty(key, "property key");
        
        String value = properties.getProperty(key, defaultValue);
        if (value.equals(defaultValue)) {
            logger.debug("Property '{}' not found, using default: '{}'", key, defaultValue);
        } else {
            logger.debug("Retrieved property '{}' = '{}'", key, value);
        }
        return value;
    }

    public String getCurrentLanguage() {
        return currentLanguage;
    }

    // Example: Method to get base URL from a general config file
    // public String getBaseUrl() {
    //     return properties.getProperty("baseUrl");
    // }

    /**
     * Retrieves a property value from the application configuration file (config.properties).
     *
     * @param key The key of the property to retrieve.
     * @return The property value as a String, or throws ConfigurationException if key not found.
     */
    public static String getAppProperty(String key) {
        ErrorHandler.validateNotEmpty(key, "app property key");
        
        if (appProperties == null) {
            throw new ConfigurationException("Application properties not loaded - static initialization failed");
        }
        
        String value = appProperties.getProperty(key);
        if (value == null) {
            throw new ConfigurationException(String.format("Application property key '%s' not found", key));
        }
        
        return value;
    }

    /**
     * Get app property with default value
     */
    public static String getAppProperty(String key, String defaultValue) {
        ErrorHandler.validateNotEmpty(key, "app property key");
        
        if (appProperties == null) {
            staticLogger.warn("Application properties not loaded, returning default value for key: {}", key);
            return defaultValue;
        }
        
        return appProperties.getProperty(key, defaultValue);
    }
}
