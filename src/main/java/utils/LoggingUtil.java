package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * Centralized logging utility for the Insurance Dashboard QA Framework
 * Provides structured logging capabilities across all framework components
 */
public class LoggingUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(LoggingUtil.class);
    
    // MDC Keys for structured logging
    public static final String TEST_SCENARIO = "testScenario";
    public static final String TEST_STEP = "testStep";
    public static final String BROWSER = "browser";
    public static final String LANGUAGE = "language";
    public static final String PAGE_NAME = "pageName";
    public static final String ACTION = "action";
    public static final String ELEMENT = "element";
    
    /**
     * Set context for current test scenario
     */
    public static void setTestContext(String scenarioName, String browser, String language) {
        MDC.put(TEST_SCENARIO, scenarioName);
        MDC.put(BROWSER, browser);
        MDC.put(LANGUAGE, language);
        logger.info("Test context set - Scenario: {}, Browser: {}, Language: {}", 
                   scenarioName, browser, language);
    }
    
    /**
     * Set current test step
     */
    public static void setTestStep(String stepDescription) {
        MDC.put(TEST_STEP, stepDescription);
        logger.info("Test Step: {}", stepDescription);
    }
    
    /**
     * Set page context
     */
    public static void setPageContext(String pageName) {
        MDC.put(PAGE_NAME, pageName);
        logger.debug("Page context set: {}", pageName);
    }
    
    /**
     * Log page action
     */
    public static void logPageAction(String pageName, String action, String element) {
        MDC.put(PAGE_NAME, pageName);
        MDC.put(ACTION, action);
        MDC.put(ELEMENT, element);
        logger.info("Page Action - Page: {}, Action: {}, Element: {}", pageName, action, element);
    }
    
    /**
     * Log navigation
     */
    public static void logNavigation(String fromPage, String toPage, String method) {
        logger.info("Navigation - From: {} To: {} Method: {}", fromPage, toPage, method);
    }
    
    /**
     * Log test data usage
     */
    public static void logTestData(String dataType, String dataValue) {
        logger.debug("Test Data - Type: {}, Value: {}", dataType, dataValue);
    }
    
    /**
     * Log assertion
     */
    public static void logAssertion(String description, boolean passed, String details) {
        if (passed) {
            logger.info("Assertion PASSED - {}: {}", description, details);
        } else {
            logger.error("Assertion FAILED - {}: {}", description, details);
        }
    }
    
    /**
     * Log configuration loading
     */
    public static void logConfigLoad(String configType, String configPath, boolean success) {
        if (success) {
            logger.info("Configuration loaded successfully - Type: {}, Path: {}", configType, configPath);
        } else {
            logger.error("Failed to load configuration - Type: {}, Path: {}", configType, configPath);
        }
    }
    
    /**
     * Log browser operation
     */
    public static void logBrowserOperation(String operation, String details) {
        logger.debug("Browser Operation - {}: {}", operation, details);
    }
    
    /**
     * Log wait operation
     */
    public static void logWaitOperation(String waitType, String element, long timeoutSeconds, boolean success) {
        if (success) {
            logger.debug("Wait SUCCESS - Type: {}, Element: {}, Timeout: {}s", waitType, element, timeoutSeconds);
        } else {
            logger.warn("Wait TIMEOUT - Type: {}, Element: {}, Timeout: {}s", waitType, element, timeoutSeconds);
        }
    }
    
    /**
     * Log element interaction
     */
    public static void logElementInteraction(String interaction, String element, String value) {
        if (value != null && !value.isEmpty()) {
            logger.debug("Element Interaction - {}: {} with value: {}", interaction, element, value);
        } else {
            logger.debug("Element Interaction - {}: {}", interaction, element);
        }
    }
    
    /**
     * Log error with context
     */
    public static void logError(String operation, String details, Throwable throwable) {
        logger.error("Error during {} - Details: {}", operation, details, throwable);
    }
    
    /**
     * Log scenario start
     */
    public static void logScenarioStart(String scenarioName) {
        logger.info("=== SCENARIO START: {} ===", scenarioName);
    }
    
    /**
     * Log scenario end
     */
    public static void logScenarioEnd(String scenarioName, boolean passed) {
        if (passed) {
            logger.info("=== SCENARIO PASSED: {} ===", scenarioName);
        } else {
            logger.error("=== SCENARIO FAILED: {} ===", scenarioName);
        }
    }
    
    /**
     * Log screenshot capture
     */
    public static void logScreenshot(String reason, String filePath) {
        logger.info("Screenshot captured - Reason: {}, Path: {}", reason, filePath);
    }
    
    /**
     * Log report generation
     */
    public static void logReportGeneration(String reportType, String reportPath, boolean success) {
        if (success) {
            logger.info("Report generated successfully - Type: {}, Path: {}", reportType, reportPath);
        } else {
            logger.error("Failed to generate report - Type: {}, Path: {}", reportType, reportPath);
        }
    }
    
    /**
     * Clear MDC context (call at end of test)
     */
    public static void clearContext() {
        MDC.clear();
        logger.debug("MDC context cleared");
    }
    
    /**
     * Get logger for specific class
     */
    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }
    
    /**
     * Log performance metric
     */
    public static void logPerformance(String operation, long durationMs) {
        if (durationMs > 5000) { // Log slow operations (>5 seconds)
            logger.warn("SLOW OPERATION - {}: {}ms", operation, durationMs);
        } else {
            logger.debug("Performance - {}: {}ms", operation, durationMs);
        }
    }
    
    /**
     * Log test environment info
     */
    public static void logEnvironmentInfo(String baseUrl, String browser, String language, String environment) {
        logger.info("Test Environment - URL: {}, Browser: {}, Language: {}, Env: {}", 
                   baseUrl, browser, language, environment);
    }
    
    /**
     * Log retry operation success
     */
    public static void logRetrySuccess(String operation, int attemptNumber) {
        logger.info("Retry SUCCESS - Operation: {} succeeded on attempt {}", operation, attemptNumber);
    }
    
    /**
     * Log retry operation failure
     */
    public static void logRetryFailure(String operation, int maxAttempts, Throwable lastException) {
        logger.error("Retry FAILED - Operation: {} failed after {} attempts", operation, maxAttempts, lastException);
    }
    
    /**
     * Log retry attempt
     */
    public static void logRetryAttempt(String operation, int currentAttempt, int maxAttempts) {
        logger.debug("Retry ATTEMPT - Operation: {} attempt {}/{}", operation, currentAttempt, maxAttempts);
    }
}
