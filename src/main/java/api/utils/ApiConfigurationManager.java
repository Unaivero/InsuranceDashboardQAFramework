package api.utils;

import utils.ConfigReader;
import utils.LoggingUtil;
import exceptions.ConfigurationException;

/**
 * API Configuration Manager
 * 
 * Manages all API-related configuration including:
 * - Base URLs and endpoints
 * - Timeout configurations
 * - Authentication settings
 * - Retry policies
 * 
 * @author Insurance Dashboard QA Framework
 * @version 1.0
 */
public class ApiConfigurationManager {
    
    private static ApiConfigurationManager instance;
    private final ConfigReader configReader;
    
    // Configuration keys
    private static final String API_BASE_URL = "api.base.url";
    private static final String API_VERSION = "api.version";
    private static final String API_CONNECTION_TIMEOUT = "api.connection.timeout";
    private static final String API_READ_TIMEOUT = "api.read.timeout";
    private static final String API_WRITE_TIMEOUT = "api.write.timeout";
    private static final String API_MAX_RESPONSE_TIME = "api.max.response.time";
    private static final String API_MAX_RETRY_ATTEMPTS = "api.max.retry.attempts";
    private static final String API_RETRY_DELAY = "api.retry.delay.ms";
    private static final String API_AUTH_TYPE = "api.auth.type";
    private static final String API_AUTH_HEADER = "api.auth.header";
    private static final String API_AUTH_TOKEN_PREFIX = "api.auth.token.prefix";
    private static final String API_SSL_VERIFICATION = "api.ssl.verification";
    private static final String API_CERTIFICATE_PATH = "api.certificate.path";
    private static final String API_EXPECTED_CONTENT_TYPE = "api.expected.content.type";
    
    // Default values
    private static final String DEFAULT_BASE_URL = "http://localhost:8080/api";
    private static final String DEFAULT_VERSION = "v1";
    private static final int DEFAULT_CONNECTION_TIMEOUT = 30;
    private static final int DEFAULT_READ_TIMEOUT = 60;
    private static final int DEFAULT_WRITE_TIMEOUT = 60;
    private static final int DEFAULT_MAX_RESPONSE_TIME = 5000;
    private static final int DEFAULT_MAX_RETRY_ATTEMPTS = 3;
    private static final int DEFAULT_RETRY_DELAY = 1000;
    private static final String DEFAULT_AUTH_TYPE = "bearer";
    private static final String DEFAULT_AUTH_HEADER = "Authorization";
    private static final String DEFAULT_AUTH_TOKEN_PREFIX = "Bearer";
    private static final boolean DEFAULT_SSL_VERIFICATION = false;
    private static final String DEFAULT_CONTENT_TYPE = "application/json";
    
    /**
     * Private constructor for singleton pattern
     */
    private ApiConfigurationManager() {
        this.configReader = ConfigReader.getInstance();
        LoggingUtil.logInfo("API Configuration Manager initialized");
    }
    
    /**
     * Get singleton instance
     */
    public static synchronized ApiConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ApiConfigurationManager();
        }
        return instance;
    }
    
    /**
     * Get API base URL
     */
    public String getBaseUrl() {
        try {
            return configReader.getProperty(API_BASE_URL, DEFAULT_BASE_URL);
        } catch (Exception e) {
            LoggingUtil.logWarning("Failed to read API base URL, using default: " + DEFAULT_BASE_URL);
            return DEFAULT_BASE_URL;
        }
    }
    
    /**
     * Get API version
     */
    public String getApiVersion() {
        try {
            return configReader.getProperty(API_VERSION, DEFAULT_VERSION);
        } catch (Exception e) {
            LoggingUtil.logWarning("Failed to read API version, using default: " + DEFAULT_VERSION);
            return DEFAULT_VERSION;
        }
    }
    
    /**
     * Get connection timeout in seconds
     */
    public int getConnectionTimeout() {
        try {
            String timeoutStr = configReader.getProperty(API_CONNECTION_TIMEOUT, String.valueOf(DEFAULT_CONNECTION_TIMEOUT));
            return Integer.parseInt(timeoutStr);
        } catch (Exception e) {
            LoggingUtil.logWarning("Failed to read connection timeout, using default: " + DEFAULT_CONNECTION_TIMEOUT);
            return DEFAULT_CONNECTION_TIMEOUT;
        }
    }
    
    /**
     * Get read timeout in seconds
     */
    public int getReadTimeout() {
        try {
            String timeoutStr = configReader.getProperty(API_READ_TIMEOUT, String.valueOf(DEFAULT_READ_TIMEOUT));
            return Integer.parseInt(timeoutStr);
        } catch (Exception e) {
            LoggingUtil.logWarning("Failed to read read timeout, using default: " + DEFAULT_READ_TIMEOUT);
            return DEFAULT_READ_TIMEOUT;
        }
    }
    
    /**
     * Get write timeout in seconds
     */
    public int getWriteTimeout() {
        try {
            String timeoutStr = configReader.getProperty(API_WRITE_TIMEOUT, String.valueOf(DEFAULT_WRITE_TIMEOUT));
            return Integer.parseInt(timeoutStr);
        } catch (Exception e) {
            LoggingUtil.logWarning("Failed to read write timeout, using default: " + DEFAULT_WRITE_TIMEOUT);
            return DEFAULT_WRITE_TIMEOUT;
        }
    }
    
    /**
     * Get maximum response time in milliseconds
     */
    public int getMaxResponseTime() {
        try {
            String responseTimeStr = configReader.getProperty(API_MAX_RESPONSE_TIME, String.valueOf(DEFAULT_MAX_RESPONSE_TIME));
            return Integer.parseInt(responseTimeStr);
        } catch (Exception e) {
            LoggingUtil.logWarning("Failed to read max response time, using default: " + DEFAULT_MAX_RESPONSE_TIME);
            return DEFAULT_MAX_RESPONSE_TIME;
        }
    }
    
    /**
     * Get maximum retry attempts
     */
    public int getMaxRetryAttempts() {
        try {
            String attemptsStr = configReader.getProperty(API_MAX_RETRY_ATTEMPTS, String.valueOf(DEFAULT_MAX_RETRY_ATTEMPTS));
            return Integer.parseInt(attemptsStr);
        } catch (Exception e) {
            LoggingUtil.logWarning("Failed to read max retry attempts, using default: " + DEFAULT_MAX_RETRY_ATTEMPTS);
            return DEFAULT_MAX_RETRY_ATTEMPTS;
        }
    }
    
    /**
     * Get retry delay in milliseconds
     */
    public int getRetryDelay() {
        try {
            String delayStr = configReader.getProperty(API_RETRY_DELAY, String.valueOf(DEFAULT_RETRY_DELAY));
            return Integer.parseInt(delayStr);
        } catch (Exception e) {
            LoggingUtil.logWarning("Failed to read retry delay, using default: " + DEFAULT_RETRY_DELAY);
            return DEFAULT_RETRY_DELAY;
        }
    }
    
    /**
     * Get authentication type
     */
    public String getAuthType() {
        try {
            return configReader.getProperty(API_AUTH_TYPE, DEFAULT_AUTH_TYPE);
        } catch (Exception e) {
            LoggingUtil.logWarning("Failed to read auth type, using default: " + DEFAULT_AUTH_TYPE);
            return DEFAULT_AUTH_TYPE;
        }
    }
    
    /**
     * Get authentication header name
     */
    public String getAuthHeader() {
        try {
            return configReader.getProperty(API_AUTH_HEADER, DEFAULT_AUTH_HEADER);
        } catch (Exception e) {
            LoggingUtil.logWarning("Failed to read auth header, using default: " + DEFAULT_AUTH_HEADER);
            return DEFAULT_AUTH_HEADER;
        }
    }
    
    /**
     * Get authentication token prefix
     */
    public String getAuthTokenPrefix() {
        try {
            return configReader.getProperty(API_AUTH_TOKEN_PREFIX, DEFAULT_AUTH_TOKEN_PREFIX);
        } catch (Exception e) {
            LoggingUtil.logWarning("Failed to read auth token prefix, using default: " + DEFAULT_AUTH_TOKEN_PREFIX);
            return DEFAULT_AUTH_TOKEN_PREFIX;
        }
    }
    
    /**
     * Check if SSL verification is enabled
     */
    public boolean isSslVerificationEnabled() {
        try {
            String sslStr = configReader.getProperty(API_SSL_VERIFICATION, String.valueOf(DEFAULT_SSL_VERIFICATION));
            return Boolean.parseBoolean(sslStr);
        } catch (Exception e) {
            LoggingUtil.logWarning("Failed to read SSL verification setting, using default: " + DEFAULT_SSL_VERIFICATION);
            return DEFAULT_SSL_VERIFICATION;
        }
    }
    
    /**
     * Get certificate path
     */
    public String getCertificatePath() {
        try {
            return configReader.getProperty(API_CERTIFICATE_PATH, "");
        } catch (Exception e) {
            LoggingUtil.logWarning("Failed to read certificate path, using empty string");
            return "";
        }
    }
    
    /**
     * Get expected content type
     */
    public String getExpectedContentType() {
        try {
            return configReader.getProperty(API_EXPECTED_CONTENT_TYPE, DEFAULT_CONTENT_TYPE);
        } catch (Exception e) {
            LoggingUtil.logWarning("Failed to read expected content type, using default: " + DEFAULT_CONTENT_TYPE);
            return DEFAULT_CONTENT_TYPE;
        }
    }
    
    /**
     * Get full API URL with version
     */
    public String getFullApiUrl() {
        String baseUrl = getBaseUrl();
        String version = getApiVersion();
        
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        
        return baseUrl + "/" + version;
    }
    
    /**
     * Get endpoint URL
     */
    public String getEndpointUrl(String endpoint) {
        String baseUrl = getBaseUrl();
        
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        
        if (!endpoint.startsWith("/")) {
            endpoint = "/" + endpoint;
        }
        
        return baseUrl + endpoint;
    }
    
    /**
     * Validate configuration
     */
    public void validateConfiguration() throws ConfigurationException {
        LoggingUtil.logInfo("Validating API configuration...");
        
        // Validate base URL
        String baseUrl = getBaseUrl();
        if (baseUrl == null || baseUrl.trim().isEmpty()) {
            throw new ConfigurationException("API base URL cannot be null or empty");
        }
        
        if (!baseUrl.startsWith("http://") && !baseUrl.startsWith("https://")) {
            throw new ConfigurationException("API base URL must start with http:// or https://");
        }
        
        // Validate timeouts
        if (getConnectionTimeout() <= 0) {
            throw new ConfigurationException("Connection timeout must be greater than 0");
        }
        
        if (getReadTimeout() <= 0) {
            throw new ConfigurationException("Read timeout must be greater than 0");
        }
        
        if (getWriteTimeout() <= 0) {
            throw new ConfigurationException("Write timeout must be greater than 0");
        }
        
        if (getMaxResponseTime() <= 0) {
            throw new ConfigurationException("Max response time must be greater than 0");
        }
        
        // Validate retry settings
        if (getMaxRetryAttempts() < 0) {
            throw new ConfigurationException("Max retry attempts cannot be negative");
        }
        
        if (getRetryDelay() < 0) {
            throw new ConfigurationException("Retry delay cannot be negative");
        }
        
        // Validate authentication settings
        String authType = getAuthType();
        if (authType == null || authType.trim().isEmpty()) {
            throw new ConfigurationException("Auth type cannot be null or empty");
        }
        
        String authHeader = getAuthHeader();
        if (authHeader == null || authHeader.trim().isEmpty()) {
            throw new ConfigurationException("Auth header cannot be null or empty");
        }
        
        LoggingUtil.logInfo("API configuration validation completed successfully");
    }
    
    /**
     * Get configuration summary
     */
    public String getConfigurationSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("=== API Configuration Summary ===\n");
        summary.append("Base URL: ").append(getBaseUrl()).append("\n");
        summary.append("API Version: ").append(getApiVersion()).append("\n");
        summary.append("Connection Timeout: ").append(getConnectionTimeout()).append("s\n");
        summary.append("Read Timeout: ").append(getReadTimeout()).append("s\n");
        summary.append("Write Timeout: ").append(getWriteTimeout()).append("s\n");
        summary.append("Max Response Time: ").append(getMaxResponseTime()).append("ms\n");
        summary.append("Max Retry Attempts: ").append(getMaxRetryAttempts()).append("\n");
        summary.append("Retry Delay: ").append(getRetryDelay()).append("ms\n");
        summary.append("Auth Type: ").append(getAuthType()).append("\n");
        summary.append("Auth Header: ").append(getAuthHeader()).append("\n");
        summary.append("SSL Verification: ").append(isSslVerificationEnabled()).append("\n");
        summary.append("Expected Content Type: ").append(getExpectedContentType()).append("\n");
        summary.append("================================");
        
        return summary.toString();
    }
    
    /**
     * Print configuration summary to logs
     */
    public void logConfigurationSummary() {
        LoggingUtil.logInfo(getConfigurationSummary());
    }
}
