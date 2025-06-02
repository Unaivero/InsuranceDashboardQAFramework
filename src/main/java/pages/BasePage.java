package pages;

import exceptions.PageException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import utils.ConfigReader;
import utils.ErrorHandler;
import utils.LoggingUtil;
import utils.WebDriverUtil;
import utils.WaitUtil;

import java.time.Duration;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected ConfigReader configReader;
    protected Logger logger;
    protected String pageName;

    public BasePage(WebDriver driver) {
        // Validate driver parameter
        ErrorHandler.validateDriver(driver);
        
        this.driver = driver;
        this.logger = LoggingUtil.getLogger(this.getClass());
        this.pageName = this.getClass().getSimpleName();
        
        long timeout = initializeTimeout();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        
        try {
            PageFactory.initElements(driver, this); // Initialize WebElements annotated with @FindBy
        } catch (Exception e) {
            ErrorHandler.handlePageError(pageName, "PageFactory.initElements", "WebElement initialization", e);
        }
        
        // Initialize ConfigReader with error handling
        initializeConfigReader();
        
        LoggingUtil.setPageContext(pageName);
        logger.debug("Initialized page: {} with timeout: {}s, language: {}", pageName, timeout, getCurrentLanguage());
    }

    public BasePage() {
        this.driver = WebDriverUtil.getDriver();
        
        // Validate driver from utility
        ErrorHandler.validateDriver(this.driver);
        
        this.logger = LoggingUtil.getLogger(this.getClass());
        this.pageName = this.getClass().getSimpleName();
        
        long timeout = initializeTimeout();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        
        try {
            PageFactory.initElements(driver, this);
        } catch (Exception e) {
            ErrorHandler.handlePageError(pageName, "PageFactory.initElements", "WebElement initialization", e);
        }
        
        initializeConfigReader();
        
        LoggingUtil.setPageContext(pageName);
        logger.debug("Initialized page: {} with timeout: {}s, language: {}", pageName, timeout, getCurrentLanguage());
    }

    private long initializeTimeout() {
        long timeout = 10L; // Default timeout in seconds
        try {
            String timeoutStr = ConfigReader.getAppProperty("default.explicit.wait.timeout", "10");
            if (!timeoutStr.startsWith("App Key not found")) {
                timeout = Long.parseLong(timeoutStr);
            }
        } catch (Exception e) {
            logger.warn("Error parsing timeout from config, using default: {}s. Error: {}", timeout, e.getMessage());
        }
        return timeout;
    }

    private void initializeConfigReader() {
        try {
            String currentLanguage = System.getProperty("language", "en");
            this.configReader = new ConfigReader(currentLanguage);
        } catch (Exception e) {
            logger.warn("Failed to initialize ConfigReader, some localization features may not work: {}", e.getMessage());
            // Don't fail page initialization for config reader issues
        }
    }

    private String getCurrentLanguage() {
        try {
            return configReader != null ? configReader.getCurrentLanguage() : System.getProperty("language", "en");
        } catch (Exception e) {
            return "en"; // fallback
        }
    }

    // Enhanced common methods using WaitUtil for robust waiting
    protected void clickElement(WebElement element) {
        String elementInfo = getElementInfo(element);
        
        try {
            // Validate element first
            ErrorHandler.validateElement(element, elementInfo);
            
            // Use enhanced wait strategy for clicking with retry
            ErrorHandler.executeVoidWithRetry(() -> {
                WaitUtil.waitForElementClickable(element);
                LoggingUtil.logElementInteraction("click", elementInfo, null);
                element.click();
            }, "clickElement_" + elementInfo, 2, 1000);
            
            logger.debug("Successfully clicked element: {}", elementInfo);
        } catch (Exception e) {
            ErrorHandler.handlePageError(pageName, "click", elementInfo, e);
        }
    }

    protected void typeText(WebElement element, String text) {
        ErrorHandler.validateNotEmpty(text, "text to type");
        String elementInfo = getElementInfo(element);
        
        try {
            // Validate element first
            ErrorHandler.validateElement(element, elementInfo);
            
            // Use enhanced wait strategy for typing with retry
            ErrorHandler.executeVoidWithRetry(() -> {
                WaitUtil.waitForElementVisible(element);
                LoggingUtil.logElementInteraction("type", elementInfo, text);
                element.clear();
                element.sendKeys(text);
            }, "typeText_" + elementInfo, 2, 1000);
            
            logger.debug("Successfully typed text into element: {}", elementInfo);
        } catch (Exception e) {
            ErrorHandler.handlePageError(pageName, "type", elementInfo, e);
        }
    }

    protected String getElementText(WebElement element) {
        String elementInfo = getElementInfo(element);
        
        try {
            ErrorHandler.validateElement(element, elementInfo);
            
            return ErrorHandler.executeWithRetry(() -> {
                WaitUtil.waitForElementVisible(element);
                String text = element.getText();
                logger.debug("Retrieved text '{}' from element: {}", text, elementInfo);
                return text;
            }, "getElementText_" + elementInfo);
            
        } catch (Exception e) {
            ErrorHandler.handlePageError(pageName, "getText", elementInfo, e);
            return ""; // This line won't be reached due to exception, but satisfies compiler
        }
    }

    protected boolean isElementDisplayed(WebElement element) {
        String elementInfo = getElementInfo(element);
        
        try {
            // For display check, we use a shorter timeout and don't throw on failure
            WaitUtil.waitForElementVisible(element, 5); // Shorter wait for display check
            logger.debug("Element is displayed: {}", elementInfo);
            return element.isDisplayed();
        } catch (Exception e) {
            logger.debug("Element is not displayed: {}", elementInfo);
            return false;
        }
    }

    // Enhanced wait methods using WaitUtil
    protected void waitForElementToBeVisible(WebElement element) {
        WaitUtil.waitForElementVisible(element);
    }
    
    protected void waitForElementToBeVisible(WebElement element, long timeoutInSeconds) {
        WaitUtil.waitForElementVisible(element, timeoutInSeconds);
    }

    protected void waitForElementToBeClickable(WebElement element) {
        WaitUtil.waitForElementClickable(element);
    }

    protected void waitForElementToBeClickable(WebElement element, long timeoutInSeconds) {
        WaitUtil.waitForElementClickable(element, timeoutInSeconds);
    }

    // Additional robust wait methods
    protected void waitForElementToBePresent(By locator) {
        WaitUtil.waitForElementPresent(locator);
    }

    protected void waitForElementToBePresent(By locator, long timeoutInSeconds) {
        WaitUtil.waitForElementPresent(locator, timeoutInSeconds);
    }

    protected boolean waitForElementToBeInvisible(By locator) {
        return WaitUtil.waitForElementInvisible(locator);
    }

    protected boolean waitForElementToBeInvisible(By locator, long timeoutInSeconds) {
        return WaitUtil.waitForElementInvisible(locator, timeoutInSeconds);
    }

    protected boolean waitForElementText(WebElement element, String expectedText) {
        return WaitUtil.waitForElementText(element, expectedText);
    }

    protected boolean waitForElementText(WebElement element, String expectedText, long timeoutInSeconds) {
        return WaitUtil.waitForElementText(element, expectedText, timeoutInSeconds);
    }

    protected boolean waitForElementAttribute(WebElement element, String attribute, String value) {
        return WaitUtil.waitForElementAttribute(element, attribute, value);
    }

    protected boolean waitForElementAttribute(WebElement element, String attribute, String value, long timeoutInSeconds) {
        return WaitUtil.waitForElementAttribute(element, attribute, value, timeoutInSeconds);
    }

    // Page loading and AJAX wait methods
    protected void waitForPageToLoad() {
        WaitUtil.waitForPageToLoad();
    }

    protected boolean waitForAjaxToComplete() {
        return WaitUtil.waitForAjaxToComplete();
    }

    protected boolean waitForLoadingSpinnerToDisappear(By spinnerLocator) {
        return WaitUtil.waitForLoadingSpinnerToDisappear(spinnerLocator);
    }

    // Smart wait that combines multiple strategies
    protected boolean smartWaitForElement(By locator) {
        return WaitUtil.smartWait(locator);
    }

    protected boolean smartWaitForElement(By locator, long timeoutInSeconds) {
        return WaitUtil.smartWait(locator, timeoutInSeconds);
    }

    protected String getLocalizedString(String key) {
        try {
            ErrorHandler.validateNotEmpty(key, "localization key");
            
            if (configReader == null) {
                logger.warn("ConfigReader not initialized, cannot get localized string for key: {}", key);
                return key; // Return key as fallback
            }
            
            String value = configReader.getProperty(key);
            logger.debug("Retrieved localized string for key '{}': '{}'", key, value);
            return value;
        } catch (Exception e) {
            logger.warn("Failed to get localized string for key '{}': {}", key, e.getMessage());
            return key; // Return key as fallback
        }
    }

    public void navigateToUrl(String url) {
        try {
            ErrorHandler.validateNotEmpty(url, "URL");
            ErrorHandler.validateDriver(driver);
            
            ErrorHandler.executeVoidWithRetry(() -> {
                LoggingUtil.logBrowserOperation("navigate", url);
                driver.get(url);
            }, "navigateToUrl_" + url, 2, 2000);
            
            logger.info("Successfully navigated to URL: {}", url);
        } catch (Exception e) {
            ErrorHandler.handlePageError(pageName, "navigate", url, e);
        }
    }

    // Helper method to get element info for logging
    private String getElementInfo(WebElement element) {
        try {
            String tagName = element.getTagName();
            String id = element.getAttribute("id");
            String className = element.getAttribute("class");
            String name = element.getAttribute("name");
            
            StringBuilder info = new StringBuilder(tagName);
            if (id != null && !id.isEmpty()) {
                info.append("#").append(id);
            }
            if (className != null && !className.isEmpty()) {
                info.append(".").append(className.replace(" ", "."));
            }
            if (name != null && !name.isEmpty()) {
                info.append("[name='").append(name).append("']");
            }
            return info.toString();
        } catch (Exception e) {
            return "unknown-element";
        }
    }

    // Example: Get base URL from a config file (if you add it to ConfigReader)
    // public String getBaseUrl() {
    //     return configReader.getBaseUrl(); 
    // }
}
