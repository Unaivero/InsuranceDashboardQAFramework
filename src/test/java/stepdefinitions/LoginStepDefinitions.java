package stepdefinitions;

import exceptions.PageException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import pages.DashboardPage;
import pages.LoginPage;
import utils.ErrorHandler;
import utils.LoggingUtil;
import utils.WebDriverUtil;

public class LoginStepDefinitions {

    private WebDriver driver;
    private LoginPage loginPage;
    private DashboardPage dashboardPage;
    private final Logger logger;

    public LoginStepDefinitions() {
        this.logger = LoggingUtil.getLogger(this.getClass());
        
        try {
            this.driver = WebDriverUtil.getDriver();
            ErrorHandler.validateDriver(driver);
            this.loginPage = new LoginPage(driver);
            logger.debug("LoginStepDefinitions initialized successfully");
        } catch (Exception e) {
            logger.error("Failed to initialize LoginStepDefinitions", e);
            throw ErrorHandler.wrapException("LoginStepDefinitions initialization", e);
        }
    }

    @Given("I am on the login page")
    public void i_am_on_the_login_page() {
        try {
            LoggingUtil.setTestStep("Navigate to login page");
            
            ErrorHandler.executeVoidWithRetry(() -> {
                loginPage.navigateToLoginPage();
            }, "navigateToLoginPage", 2, 1000);
            
            logger.info("Successfully navigated to login page");
        } catch (Exception e) {
            logger.error("Failed to navigate to login page", e);
            throw new PageException("LoginPage", "Failed to navigate to login page", e);
        }
    }

    @When("I enter {string} as username and {string} as password")
    public void i_enter_as_username_and_as_password(String username, String password) {
        try {
            ErrorHandler.validateNotEmpty(username, "username");
            ErrorHandler.validateNotEmpty(password, "password");
            
            LoggingUtil.setTestStep(String.format("Enter credentials - Username: %s", username));
            LoggingUtil.logTestData("username", username);
            LoggingUtil.logTestData("password", "[MASKED]"); // Don't log actual password
            
            ErrorHandler.executeVoidWithRetry(() -> {
                loginPage.enterUsername(username);
                loginPage.enterPassword(password);
            }, "enterCredentials", 2, 1000);
            
            logger.info("Successfully entered credentials for user: {}", username);
        } catch (Exception e) {
            logger.error("Failed to enter credentials for user: {}", username, e);
            throw new PageException("LoginPage", "Failed to enter credentials", e);
        }
    }

    @When("I click the login button")
    public void i_click_the_login_button() {
        try {
            LoggingUtil.setTestStep("Click login button");
            
            ErrorHandler.executeVoidWithRetry(() -> {
                loginPage.clickLoginButton();
            }, "clickLoginButton", 2, 1000);
            
            logger.info("Successfully clicked login button");
        } catch (Exception e) {
            logger.error("Failed to click login button", e);
            throw new PageException("LoginPage", "Failed to click login button", e);
        }
    }

    @Then("I should be redirected to the dashboard page")
    public void i_should_be_redirected_to_the_dashboard_page() {
        try {
            LoggingUtil.setTestStep("Verify dashboard redirection");
            
            // Initialize dashboard page with error handling
            dashboardPage = ErrorHandler.executeWithRetry(() -> {
                return new DashboardPage(driver);
            }, "initializeDashboardPage");
            
            // Verify dashboard is displayed with retry
            boolean isDashboardDisplayed = ErrorHandler.executeWithRetry(() -> {
                boolean isDisplayed = dashboardPage.isDashboardHeaderDisplayed();
                if (!isDisplayed) {
                    throw new PageException("DashboardPage", "Dashboard header not displayed - login may have failed");
                }
                return isDisplayed;
            }, "verifyDashboardHeader", 3, 2000);
            
            LoggingUtil.logAssertion("Dashboard redirection", isDashboardDisplayed, "Dashboard header displayed");
            Assertions.assertTrue(isDashboardDisplayed, "Not redirected to dashboard page - header not found");
            
            logger.info("Successfully verified dashboard redirection");
        } catch (Exception e) {
            logger.error("Failed to verify dashboard redirection", e);
            throw new PageException("DashboardPage", "Dashboard redirection verification failed", e);
        }
    }

    @Then("I should see an error message {string}")
    public void i_should_see_an_error_message(String expectedErrorMessageKey) {
        try {
            ErrorHandler.validateNotEmpty(expectedErrorMessageKey, "error message key");
            LoggingUtil.setTestStep("Verify error message: " + expectedErrorMessageKey);
            
            // Verify error message is displayed with retry
            boolean isErrorDisplayed = ErrorHandler.executeWithRetry(() -> {
                boolean isDisplayed = loginPage.isErrorMessageDisplayed();
                if (!isDisplayed) {
                    throw new PageException("LoginPage", "Error message not displayed");
                }
                return isDisplayed;
            }, "verifyErrorMessageDisplayed", 3, 1000);
            
            LoggingUtil.logAssertion("Error message displayed", isErrorDisplayed, "Error message visible");
            Assertions.assertTrue(isErrorDisplayed, "Error message is not displayed");
            
            // Verify error message text with retry
            String actualErrorMessage = ErrorHandler.executeWithRetry(() -> {
                return loginPage.getActualErrorMessageText();
            }, "getErrorMessageText");
            
            String expectedLocalizedErrorMessage = ErrorHandler.executeWithRetry(() -> {
                return loginPage.getLocalizedString(expectedErrorMessageKey);
            }, "getLocalizedErrorMessage");
            
            LoggingUtil.logAssertion("Error message text", 
                actualErrorMessage.equals(expectedLocalizedErrorMessage), 
                String.format("Expected: '%s', Actual: '%s'", expectedLocalizedErrorMessage, actualErrorMessage));
            
            Assertions.assertEquals(expectedLocalizedErrorMessage, actualErrorMessage, 
                String.format("Error message text mismatch. Expected: '%s', Actual: '%s'", 
                    expectedLocalizedErrorMessage, actualErrorMessage));
            
            logger.info("Successfully verified error message: {}", expectedErrorMessageKey);
        } catch (Exception e) {
            logger.error("Failed to verify error message: {}", expectedErrorMessageKey, e);
            throw new PageException("LoginPage", "Error message verification failed", e);
        }
    }
}
