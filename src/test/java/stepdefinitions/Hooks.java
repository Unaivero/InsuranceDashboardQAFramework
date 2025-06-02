package stepdefinitions;

import exceptions.FrameworkException;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import utils.ErrorHandler;
import utils.LoggingUtil;
import utils.WebDriverUtil;

public class Hooks {
    private static final Logger logger = LoggingUtil.getLogger(Hooks.class);

    @Before
    public void setUp(Scenario scenario) {
        String scenarioName = scenario.getName();
        logger.info("Starting test setup for scenario: {}", scenarioName);
        
        try {
            String browser = WebDriverUtil.getCurrentBrowser();
            String language = WebDriverUtil.getCurrentLanguage();
            
            // Set up logging context with error handling
            ErrorHandler.executeVoidWithRetry(() -> {
                LoggingUtil.setTestContext(scenarioName, browser, language);
                LoggingUtil.logScenarioStart(scenarioName);
            }, "setupLoggingContext");
            
            // Ensure WebDriver is initialized
            ErrorHandler.executeWithRetry(() -> {
                return WebDriverUtil.getDriver();
            }, "initializeWebDriver", 2, 3000);
            
            logger.info("Test setup completed successfully for scenario: {}", scenarioName);
        } catch (Exception e) {
            logger.error("Critical error during test setup for scenario: {}", scenarioName, e);
            
            // Try to clean up any partial initialization
            ErrorHandler.performGracefulCleanup("test setup failure", () -> {
                WebDriverUtil.quitDriver();
                LoggingUtil.clearContext();
            });
            
            throw new FrameworkException("Test setup failed for scenario: " + scenarioName, e);
        }
    }

    @After
    public void tearDown(Scenario scenario) {
        String scenarioName = scenario.getName();
        boolean scenarioPassed = !scenario.isFailed();
        
        logger.info("Starting test teardown for scenario: {} (Status: {})", 
                   scenarioName, scenarioPassed ? "PASSED" : "FAILED");
        
        try {
            if (scenario.isFailed()) {
                logger.error("Scenario failed: {}", scenarioName);
                handleFailureScreenshot(scenario, scenarioName);
            } else {
                logger.info("Scenario passed: {}", scenarioName);
            }
            
            // Log scenario completion with error handling
            ErrorHandler.executeVoidWithRetry(() -> {
                LoggingUtil.logScenarioEnd(scenarioName, scenarioPassed);
            }, "logScenarioEnd");
            
        } catch (Exception e) {
            logger.error("Error during scenario result processing for: {}", scenarioName, e);
            // Don't fail the teardown for logging issues
        }
        
        // Always attempt cleanup regardless of previous errors
        performTeardownCleanup(scenarioName);
        
        logger.info("Test teardown completed for scenario: {}", scenarioName);
    }

    private void handleFailureScreenshot(Scenario scenario, String scenarioName) {
        try {
            if (WebDriverUtil.isDriverInitialized()) {
                ErrorHandler.executeVoidWithRetry(() -> {
                    final byte[] screenshot = ((TakesScreenshot) WebDriverUtil.getDriver())
                        .getScreenshotAs(OutputType.BYTES);
                    String screenshotName = scenarioName + "_failure_screenshot";
                    scenario.attach(screenshot, "image/png", screenshotName);
                    LoggingUtil.logScreenshot("scenario failure", screenshotName);
                }, "captureFailureScreenshot", 2, 1000);
                
                logger.info("Failure screenshot captured for scenario: {}", scenarioName);
            } else {
                logger.warn("Cannot capture screenshot - WebDriver not initialized for scenario: {}", scenarioName);
            }
        } catch (Exception e) {
            logger.error("Failed to capture failure screenshot for scenario: {}", scenarioName, e);
            LoggingUtil.logError("screenshot capture", 
                "Failed to capture screenshot for scenario: " + scenarioName, e);
        }
    }

    private void performTeardownCleanup(String scenarioName) {
        // Quit driver with error handling
        ErrorHandler.performGracefulCleanup("WebDriver cleanup", () -> {
            WebDriverUtil.quitDriver();
            logger.debug("WebDriver cleaned up for scenario: {}", scenarioName);
        });
        
        // Clear logging context with error handling
        ErrorHandler.performGracefulCleanup("Logging context cleanup", () -> {
            LoggingUtil.clearContext();
            logger.debug("Logging context cleared for scenario: {}", scenarioName);
        });
    }
}
