package utils;

import exceptions.WebDriverException;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.slf4j.Logger;

import java.time.Duration;

public class WebDriverUtil {
    private static ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private static final Logger logger = LoggingUtil.getLogger(WebDriverUtil.class);

    public static WebDriver getDriver() {
        if (driverThreadLocal.get() == null) {
            try {
                initializeDriver(); // This will set the driver for the current thread
            } catch (Exception e) {
                ErrorHandler.handleWebDriverError("getDriver", "Failed to initialize driver", e);
            }
        }
        return driverThreadLocal.get();
    }

    private static void initializeDriver() {
        // Read browser type from a config file or system property (default to Chrome)
        String browserType = System.getProperty("browser", "chrome").toLowerCase();
        String language = System.getProperty("language", "en");
        
        logger.info("Initializing WebDriver - Browser: {}, Language: {}", browserType, language);
        
        try {
            WebDriver driver = ErrorHandler.executeWithRetry(
                () -> createDriverInstance(browserType),
                "createDriverInstance_" + browserType,
                2, // 2 retries for driver creation
                2000 // 2 second delay between retries
            );
            
            driverThreadLocal.set(driver);
            
            // Configure the newly created driver for the current thread
            configureDriver(driver, browserType, language);
            
        } catch (Exception e) {
            ErrorHandler.handleWebDriverError("initializeDriver", 
                String.format("Browser: %s, Language: %s", browserType, language), e);
        }
    }

    private static WebDriver createDriverInstance(String browserType) {
        switch (browserType) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                WebDriver firefoxDriver = new FirefoxDriver();
                LoggingUtil.logBrowserOperation("initialize", "Firefox driver created successfully");
                return firefoxDriver;
                
            case "edge":
                WebDriverManager.edgedriver().setup();
                WebDriver edgeDriver = new EdgeDriver();
                LoggingUtil.logBrowserOperation("initialize", "Edge driver created successfully");
                return edgeDriver;
                
            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = getChromeOptions();
                WebDriver chromeDriver = new ChromeDriver(options);
                LoggingUtil.logBrowserOperation("initialize", 
                    "Chrome driver created successfully with options: " + options.getArguments());
                return chromeDriver;
        }
    }

    private static void configureDriver(WebDriver driver, String browserType, String language) {
        try {
            ErrorHandler.validateDriver(driver);
            
            // Set timeouts with error handling
            ErrorHandler.executeVoidWithRetry(
                () -> driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)),
                "setImplicitWait"
            );
            
            ErrorHandler.executeVoidWithRetry(
                () -> driver.manage().window().maximize(),
                "maximizeWindow"
            );
            
            String sessionId = driver.toString();
            logger.info("WebDriver configured successfully - Session: {}, Implicit wait: 10s, Window: maximized", sessionId);
            
            // Log environment info with error handling
            try {
                String baseUrl = ConfigReader.getAppProperty("app.base.url", "URL_NOT_CONFIGURED");
                LoggingUtil.logEnvironmentInfo(baseUrl, browserType, language, "test");
            } catch (Exception e) {
                logger.warn("Failed to log environment info: {}", e.getMessage());
            }
            
        } catch (Exception e) {
            ErrorHandler.handleWebDriverError("configureDriver", 
                String.format("Session configuration failed for browser: %s", browserType), e);
        }
    }

    private static ChromeOptions getChromeOptions() {
        try {
            ChromeOptions options = new ChromeOptions();
            
            // Basic stability options
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-extensions");
            options.addArguments("--disable-web-security");
            options.addArguments("--allow-running-insecure-content");
            
            // Performance options
            options.addArguments("--disable-background-timer-throttling");
            options.addArguments("--disable-backgrounding-occluded-windows");
            options.addArguments("--disable-renderer-backgrounding");
            
            // Check for headless mode
            String headless = System.getProperty("headless", "false");
            if ("true".equalsIgnoreCase(headless)) {
                options.addArguments("--headless");
                options.addArguments("--window-size=1920,1080");
                logger.info("Chrome running in headless mode");
            }
            
            // Set custom window size if specified
            String windowSize = System.getProperty("window.size");
            if (windowSize != null && !windowSize.isEmpty()) {
                options.addArguments("--window-size=" + windowSize);
                logger.info("Chrome window size set to: {}", windowSize);
            }
            
            return options;
        } catch (Exception e) {
            logger.error("Failed to create Chrome options", e);
            throw new WebDriverException("Failed to create Chrome options", e);
        }
    }

    public static void quitDriver() {
        WebDriver currentDriver = driverThreadLocal.get();
        if (currentDriver != null) {
            String sessionId = currentDriver.toString();
            try {
                LoggingUtil.logBrowserOperation("quit", "Closing WebDriver session: " + sessionId);
                
                ErrorHandler.executeVoidWithRetry(
                    () -> currentDriver.quit(),
                    "quitDriver",
                    2, // 2 retries
                    1000 // 1 second delay
                );
                
                logger.info("WebDriver session closed successfully: {}", sessionId);
            } catch (Exception e) {
                logger.error("Error closing WebDriver session {}: {}", sessionId, e.getMessage());
                // Don't throw here - we want to clean up ThreadLocal regardless
            } finally {
                ErrorHandler.performGracefulCleanup("ThreadLocal cleanup", () -> {
                    driverThreadLocal.remove();
                    logger.debug("ThreadLocal WebDriver cleaned up for session: {}", sessionId);
                });
            }
        } else {
            logger.debug("No WebDriver session to quit");
        }
    }

    /**
     * Get current browser name
     */
    public static String getCurrentBrowser() {
        return System.getProperty("browser", "chrome");
    }

    /**
     * Get current language
     */
    public static String getCurrentLanguage() {
        return System.getProperty("language", "en");
    }

    /**
     * Check if driver is initialized and valid
     */
    public static boolean isDriverInitialized() {
        WebDriver driver = driverThreadLocal.get();
        if (driver == null) {
            return false;
        }
        
        try {
            // Validate driver is still functional
            ErrorHandler.validateDriver(driver);
            return true;
        } catch (Exception e) {
            logger.warn("Driver exists but is not functional: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Force reinitialize driver (useful for recovery scenarios)
     */
    public static void reinitializeDriver() {
        logger.info("Force reinitializing WebDriver");
        try {
            quitDriver(); // Clean up existing driver
        } catch (Exception e) {
            logger.warn("Error during driver cleanup before reinitialize: {}", e.getMessage());
        }
        
        // Clear ThreadLocal to force new initialization
        driverThreadLocal.remove();
        
        // Get driver will trigger initialization
        getDriver();
    }
}
