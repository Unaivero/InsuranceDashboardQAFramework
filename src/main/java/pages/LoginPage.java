package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.ConfigReader;
import utils.WaitUtil;
import utils.EnhancedPageFactory;

public class LoginPage extends BasePage {

    // --- Locators --- 
    // These would ideally be more robust, e.g., using IDs or specific test attributes
    @FindBy(name = "username") // Assuming 'name' attribute for username field
    private WebElement usernameInput;

    @FindBy(name = "password") // Assuming 'name' attribute for password field
    private WebElement passwordInput;

    @FindBy(xpath = "//button[@type='submit']") // Assuming a submit button
    private WebElement loginButton;

    @FindBy(id = "errorMessage") // Assuming an element with id='errorMessage' for error messages
    public WebElement errorMessageText; // Made public for direct access in step def, consider getter

    // --- Constructor with Enhanced Page Factory --- 
    public LoginPage(WebDriver driver) {
        super(driver);
        // Use Enhanced PageFactory for robust element initialization
        EnhancedPageFactory.initElements(driver, this);
        
        // Wait for page to be ready
        waitForPageToBeReady();
    }
    
    public LoginPage() {
        super();
        // Use Enhanced PageFactory for robust element initialization
        EnhancedPageFactory.initElements(driver, this);
        
        // Wait for page to be ready
        waitForPageToBeReady();
    }

    /**
     * Wait for login page to be fully loaded and ready for interaction
     */
    private void waitForPageToBeReady() {
        try {
            // Wait for page to load completely
            WaitUtil.waitForPageToLoad();
            
            // Wait for essential elements to be present
            WaitUtil.waitForElementPresent(By.name("username"), 10);
            WaitUtil.waitForElementPresent(By.name("password"), 10);
            WaitUtil.waitForElementPresent(By.xpath("//button[@type='submit']"), 10);
            
            // Wait for any AJAX/JavaScript to complete
            WaitUtil.waitForAjaxToComplete();
            
            logger.debug("Login page is ready for interaction");
        } catch (Exception e) {
            logger.warn("Login page readiness check encountered issues: {}", e.getMessage());
        }
    }

    // --- Enhanced Page Methods with Robust Wait Strategies --- 
    @Step("Enter username: {username}")
    public void enterUsername(String username) {
        try {
            // Enhanced typing with multiple wait strategies
            WaitUtil.waitForElementVisible(usernameInput);
            WaitUtil.waitForElementClickable(usernameInput);
            
            typeText(usernameInput, username);
            
            // Verify text was entered correctly
            if (!WaitUtil.waitForElementValueText(By.name("username"), username, 5)) {
                logger.warn("Username value verification failed, retrying...");
                usernameInput.clear();
                typeText(usernameInput, username);
            }
        } catch (Exception e) {
            logger.error("Failed to enter username: {}", username, e);
            throw new RuntimeException("Could not enter username: " + username, e);
        }
    }

    @Step("Enter password: {password}")
    public void enterPassword(String password) {
        try {
            // Enhanced typing with multiple wait strategies
            WaitUtil.waitForElementVisible(passwordInput);
            WaitUtil.waitForElementClickable(passwordInput);
            
            typeText(passwordInput, password);
            
            // Note: Cannot verify password value due to security, but verify field is not empty
            if (passwordInput.getAttribute("value").isEmpty()) {
                logger.warn("Password field appears empty after typing, retrying...");
                passwordInput.clear();
                typeText(passwordInput, password);
            }
        } catch (Exception e) {
            logger.error("Failed to enter password", e);
            throw new RuntimeException("Could not enter password", e);
        }
    }

    @Step("Click login button")
    public void clickLoginButton() {
        try {
            // Enhanced clicking with multiple wait strategies
            WaitUtil.waitForElementVisible(loginButton);
            WaitUtil.waitForElementClickable(loginButton);
            
            // Wait for any animations to complete
            WaitUtil.waitForElementToStopMoving(loginButton);
            
            clickElement(loginButton);
            
            // Wait for page transition to begin
            WaitUtil.sleep(500); // Small delay to allow navigation to start
            
        } catch (Exception e) {
            logger.error("Failed to click login button", e);
            throw new RuntimeException("Could not click login button", e);
        }
    }

    @Step("Perform login with username: {username}")
    public void performLogin(String username, String password) {
        try {
            enterUsername(username);
            enterPassword(password);
            clickLoginButton();
            
            // Wait for login process to complete
            WaitUtil.waitForAjaxToComplete();
            
        } catch (Exception e) {
            logger.error("Login process failed for user: {}", username, e);
            throw new RuntimeException("Login failed for user: " + username, e);
        }
    }

    // Enhanced login method that expects successful navigation to dashboard
    public DashboardPage loginExpectingSuccess(String username, String password) {
        try {
            performLogin(username, password);
            
            // Wait for navigation away from login page
            WaitUtil.waitForUrlContains("dashboard", 15);
            
            // Wait for dashboard page to load
            WaitUtil.waitForPageToLoad();
            
            return new DashboardPage(driver);
        } catch (Exception e) {
            logger.error("Expected successful login failed for user: {}", username, e);
            throw new RuntimeException("Expected successful login failed for user: " + username, e);
        }
    }

    @Step("Get actual error message text")
    public String getActualErrorMessageText() {
        try {
            // Wait for error message to appear with shorter timeout
            WaitUtil.waitForElementVisible(errorMessageText, 5);
            return getElementText(errorMessageText);
        } catch (Exception e) {
            logger.debug("No error message found or visible");
            return "";
        }
    }

    public boolean isErrorMessageDisplayed() {
        try {
            return WaitUtil.waitForElementVisible(errorMessageText, 3) != null;
        } catch (Exception e) {
            logger.debug("Error message not displayed");
            return false;
        }
    }
    
    @Step("Navigate to Login Page")
    public void navigateToLoginPage(){
        try {
            String baseUrl = ConfigReader.getAppProperty("app.base.url");
            String loginPath = ConfigReader.getAppProperty("app.login.path");
            
            if (baseUrl.endsWith("/")) {
                baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
            }
            if (loginPath.startsWith("/")) {
                loginPath = loginPath.substring(1);
            }
            
            String fullUrl = baseUrl + "/" + loginPath;
            navigateToUrl(fullUrl);
            
            // Wait for navigation to complete and page to be ready
            WaitUtil.waitForUrl(fullUrl, 10);
            waitForPageToBeReady();
            
        } catch (Exception e) {
            logger.error("Failed to navigate to login page", e);
            throw new RuntimeException("Could not navigate to login page", e);
        }
    }

    // Enhanced validation methods
    @Step("Verify login page is displayed")
    public boolean isLoginPageDisplayed() {
        try {
            // Check multiple indicators that login page is displayed
            boolean usernameVisible = WaitUtil.waitForElementVisible(usernameInput, 5) != null;
            boolean passwordVisible = WaitUtil.waitForElementVisible(passwordInput, 5) != null;
            boolean loginButtonVisible = WaitUtil.waitForElementVisible(loginButton, 5) != null;
            
            return usernameVisible && passwordVisible && loginButtonVisible;
        } catch (Exception e) {
            logger.debug("Login page display check failed", e);
            return false;
        }
    }

    @Step("Wait for login form to be interactive")
    public void waitForLoginFormInteractive() {
        try {
            // Wait for all form elements to be clickable
            WaitUtil.waitForElementClickable(usernameInput);
            WaitUtil.waitForElementClickable(passwordInput);
            WaitUtil.waitForElementClickable(loginButton);
            
            logger.debug("Login form is interactive and ready for user input");
        } catch (Exception e) {
            logger.error("Login form is not interactive", e);
            throw new RuntimeException("Login form is not ready for interaction", e);
        }
    }

    @Step("Clear login form")
    public void clearLoginForm() {
        try {
            if (isElementDisplayed(usernameInput)) {
                usernameInput.clear();
            }
            if (isElementDisplayed(passwordInput)) {
                passwordInput.clear();
            }
            logger.debug("Login form cleared");
        } catch (Exception e) {
            logger.warn("Could not clear login form", e);
        }
    }
}
