package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class DashboardPage extends BasePage {

    // --- Locators --- 
    @FindBy(xpath = "//h1[contains(text(),'Dashboard')] | //h1[contains(text(),'Panel')]" ) // Example: Welcome message or header
    private WebElement dashboardHeader;
    
    @FindBy(linkText = "Policies") // Assuming a link to the policy list
    private WebElement policiesLink;
    
    @FindBy(id = "welcomeMessage")
    private WebElement welcomeMessageLabel;

    // --- Constructor --- 
    public DashboardPage(WebDriver driver) {
        super(driver);
    }
    
    public DashboardPage() {
        super();
    }

    // --- Page Methods --- 
    @Step("Check if Dashboard header is displayed")
    public boolean isDashboardHeaderDisplayed() {
        return isElementDisplayed(dashboardHeader);
    }

    @Step("Get welcome message from Dashboard")
    public String getWelcomeMessage() {
        // The actual text might be dynamic, so we fetch it and then get its localized version if needed
        // Or, if the element itself contains the key, use that.
        // For now, assuming the element contains a key or text that needs to be localized.
        return getLocalizedString(getElementText(welcomeMessageLabel));
    }
    
    @Step("Navigate to Policy List page from Dashboard")
    public PolicyListPage navigateToPolicyList() {
        clickElement(policiesLink);
        return new PolicyListPage(driver);
    }
}
