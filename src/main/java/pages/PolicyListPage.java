package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.stream.Collectors;

public class PolicyListPage extends BasePage {

    // --- Locators --- 
    @FindBy(id = "policyStatusFilter") // Assuming a dropdown filter for policy status
    private WebElement statusFilterDropdown;

    @FindBy(xpath = "//table[@id='policyTable']/tbody/tr") // Assuming policies are in a table
    private List<WebElement> policyRows;

    // Assuming each row has a cell for status and a cell for policy ID (e.g., with a link)
    // These XPaths are relative to each policyRow
    private String policyStatusCellRelativeXpath = ".//td[3]"; // Example: 3rd cell is status
    private String policyIdLinkRelativeXpath = ".//td[1]/a"; // Example: 1st cell has a link with Policy ID

    // --- Constructor --- 
    public PolicyListPage(WebDriver driver) {
        super(driver);
    }
    
    public PolicyListPage() {
        super();
    }

    // --- Page Methods --- 
    @Step("Filter policies by status key: {statusKey}")
    public void filterByStatus(String statusKey) {
        // 'statusKey' should be a key like "policy.status.active" or "policy.status.expired"
        // The actual visible text in the dropdown will be its localized value.
        String localizedStatus = getLocalizedString(statusKey);
        Select statusSelect = new Select(statusFilterDropdown);
        waitForElementToBeClickable(statusFilterDropdown); // Ensure dropdown is ready
        statusSelect.selectByVisibleText(localizedStatus);
    }

    @Step("Get displayed policy statuses")
    public List<String> getDisplayedPolicyStatuses() {
        waitForElementToBeVisible(policyRows.get(0)); // Wait for at least one row to be visible
        return policyRows.stream()
                .map(row -> row.findElement(By.xpath(policyStatusCellRelativeXpath)).getText())
                .collect(Collectors.toList());
    }

    @Step("Click policy by ID: {policyId}")
    public PolicyDetailsPage clickPolicyById(String policyId) {
        waitForElementToBeVisible(policyRows.get(0)); // Wait for rows
        for (WebElement row : policyRows) {
            WebElement idLink = row.findElement(By.xpath(policyIdLinkRelativeXpath));
            if (getElementText(idLink).equals(policyId)) {
                clickElement(idLink);
                return new PolicyDetailsPage(driver);
            }
        }
        throw new RuntimeException("Policy with ID " + policyId + " not found in the list.");
    }
    
    @Step("Check if Policy List page is displayed")
    public boolean isPolicyListPageDisplayed(){
        // Add a check for a unique element on the policy list page
        return isElementDisplayed(statusFilterDropdown);
    }
}
