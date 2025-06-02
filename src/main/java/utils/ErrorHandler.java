package utils;

import exceptions.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Supplier;

/**
 * Centralized error handling utility for the framework
 * Provides consistent error handling patterns and recovery mechanisms
 */
public class ErrorHandler {
    
    private static final Logger logger = LoggingUtil.getLogger(ErrorHandler.class);
    private static final int DEFAULT_RETRY_COUNT = 3;
    private static final long DEFAULT_RETRY_DELAY_MS = 1000;
    
    /**
     * Execute an operation with retry mechanism
     */
    public static <T> T executeWithRetry(Supplier<T> operation, String operationName) {
        return executeWithRetry(operation, operationName, DEFAULT_RETRY_COUNT, DEFAULT_RETRY_DELAY_MS);
    }
    
    /**
     * Execute an operation with custom retry configuration
     */
    public static <T> T executeWithRetry(Supplier<T> operation, String operationName, 
                                       int maxRetries, long retryDelayMs) {
        Exception lastException = null;
        
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                logger.debug("Executing operation '{}' - Attempt {}/{}", operationName, attempt, maxRetries);
                T result = operation.get();
                
                if (attempt > 1) {
                    logger.info("Operation '{}' succeeded on attempt {}/{}", operationName, attempt, maxRetries);
                    LoggingUtil.logRetrySuccess(operationName, attempt);
                }
                
                return result;
            } catch (Exception e) {
                lastException = e;
                logger.warn("Operation '{}' failed on attempt {}/{}: {}", 
                           operationName, attempt, maxRetries, e.getMessage());
                
                if (attempt < maxRetries) {
                    try {
                        Thread.sleep(retryDelayMs);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new FrameworkException("Operation interrupted during retry delay", ie);
                    }
                } else {
                    logger.error("Operation '{}' failed after {} attempts", operationName, maxRetries);
                    LoggingUtil.logRetryFailure(operationName, maxRetries, e);
                }
            }
        }
        
        // All retries exhausted
        throw new FrameworkException(
            String.format("Operation '%s' failed after %d attempts", operationName, maxRetries), 
            lastException
        );
    }
    
    /**
     * Execute a void operation with retry mechanism
     */
    public static void executeVoidWithRetry(Runnable operation, String operationName) {
        executeVoidWithRetry(operation, operationName, DEFAULT_RETRY_COUNT, DEFAULT_RETRY_DELAY_MS);
    }
    
    /**
     * Execute a void operation with custom retry configuration
     */
    public static void executeVoidWithRetry(Runnable operation, String operationName, 
                                          int maxRetries, long retryDelayMs) {
        executeWithRetry(() -> {
            operation.run();
            return null;
        }, operationName, maxRetries, retryDelayMs);
    }
    
    /**
     * Handle WebDriver exceptions with context
     */
    public static void handleWebDriverError(String operation, String details, Exception originalException) {
        String errorMessage = String.format("WebDriver operation '%s' failed: %s", operation, details);
        logger.error(errorMessage, originalException);
        LoggingUtil.logError(operation, details, originalException);
        
        // Take screenshot if driver is available
        try {
            takeErrorScreenshot(operation);
        } catch (Exception screenshotException) {
            logger.warn("Failed to take error screenshot: {}", screenshotException.getMessage());
        }
        
        throw new WebDriverException(operation, details, originalException);
    }
    
    /**
     * Handle page operation errors with context
     */
    public static void handlePageError(String pageName, String operation, String elementInfo, Exception originalException) {
        String errorMessage = String.format("Page '%s' operation '%s' failed on element '%s'", 
                                           pageName, operation, elementInfo);
        logger.error(errorMessage, originalException);
        LoggingUtil.logError(operation, elementInfo, originalException);
        
        // Take screenshot for page errors
        try {
            takeErrorScreenshot(String.format("%s_%s", pageName, operation));
        } catch (Exception screenshotException) {
            logger.warn("Failed to take error screenshot: {}", screenshotException.getMessage());
        }
        
        throw new PageException(pageName, operation, elementInfo, originalException);
    }
    
    /**
     * Handle configuration errors
     */
    public static void handleConfigurationError(String configType, String filePath, Exception originalException) {
        String errorMessage = String.format("Failed to load %s configuration from: %s", configType, filePath);
        logger.error(errorMessage, originalException);
        LoggingUtil.logConfigLoad(configType, filePath, false);
        
        throw new ConfigurationException(configType, filePath, originalException);
    }
    
    /**
     * Handle element wait timeout errors
     */
    public static void handleWaitTimeoutError(String waitType, String elementInfo, long timeoutSeconds, Exception originalException) {
        String errorMessage = String.format("Wait operation '%s' timed out after %d seconds for element: %s", 
                                           waitType, timeoutSeconds, elementInfo);
        logger.error(errorMessage, originalException);
        LoggingUtil.logWaitOperation(waitType, elementInfo, timeoutSeconds, false);
        
        // Take screenshot for wait timeouts
        try {
            takeErrorScreenshot(String.format("wait_timeout_%s", waitType));
        } catch (Exception screenshotException) {
            logger.warn("Failed to take error screenshot: {}", screenshotException.getMessage());
        }
        
        throw new ElementWaitException(waitType, elementInfo, timeoutSeconds, originalException);
    }
    
    /**
     * Handle test data errors
     */
    public static void handleTestDataError(String dataType, String operation, String details) {
        String errorMessage = String.format("Test data operation '%s' failed for type '%s': %s", 
                                           operation, dataType, details);
        logger.error(errorMessage);
        
        throw new TestDataException(dataType, operation, details);
    }
    
    /**
     * Log and wrap unexpected exceptions
     */
    public static RuntimeException wrapException(String operation, Exception originalException) {
        String errorMessage = String.format("Unexpected error during operation '%s': %s", 
                                           operation, originalException.getMessage());
        logger.error(errorMessage, originalException);
        LoggingUtil.logError(operation, "Unexpected error", originalException);
        
        if (originalException instanceof RuntimeException) {
            return (RuntimeException) originalException;
        }
        
        return new FrameworkException(errorMessage, originalException);
    }
    
    /**
     * Get full stack trace as string
     */
    public static String getStackTrace(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        return stringWriter.toString();
    }
    
    /**
     * Take screenshot on error
     */
    private static void takeErrorScreenshot(String errorContext) {
        try {
            if (WebDriverUtil.isDriverInitialized()) {
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                String screenshotName = String.format("error_%s_%s", errorContext, timestamp);
                
                // This would need a screenshot utility - placeholder for now
                logger.debug("Error screenshot would be taken: {}", screenshotName);
                LoggingUtil.logScreenshot("error", screenshotName);
            }
        } catch (Exception e) {
            logger.warn("Failed to take error screenshot: {}", e.getMessage());
        }
    }
    
    /**
     * Validate required parameters
     */
    public static void validateNotNull(Object value, String parameterName) {
        if (value == null) {
            throw new FrameworkException(String.format("Required parameter '%s' cannot be null", parameterName));
        }
    }
    
    /**
     * Validate string parameters
     */
    public static void validateNotEmpty(String value, String parameterName) {
        validateNotNull(value, parameterName);
        if (value.trim().isEmpty()) {
            throw new FrameworkException(String.format("Required parameter '%s' cannot be empty", parameterName));
        }
    }
    
    /**
     * Validate WebElement
     */
    public static void validateElement(WebElement element, String elementName) {
        validateNotNull(element, elementName);
        try {
            // This will throw if element is stale
            element.isDisplayed();
        } catch (Exception e) {
            throw new PageException("UNKNOWN", 
                String.format("Element '%s' is stale or invalid", elementName), e);
        }
    }
    
    /**
     * Validate WebDriver
     */
    public static void validateDriver(WebDriver driver) {
        validateNotNull(driver, "WebDriver");
        try {
            // Simple validation - get current URL
            driver.getCurrentUrl();
        } catch (Exception e) {
            throw new WebDriverException("WebDriver validation failed - driver may be closed or invalid", e);
        }
    }
    
    /**
     * Handle graceful cleanup on errors
     */
    public static void performGracefulCleanup(String context, Runnable cleanupAction) {
        try {
            logger.debug("Performing graceful cleanup for context: {}", context);
            cleanupAction.run();
            logger.debug("Graceful cleanup completed for context: {}", context);
        } catch (Exception e) {
            logger.warn("Graceful cleanup failed for context '{}': {}", context, e.getMessage());
            // Don't throw - this is cleanup, we don't want to mask the original error
        }
    }
}
