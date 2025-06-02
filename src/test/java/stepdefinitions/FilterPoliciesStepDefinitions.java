package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import pages.DashboardPage;
import pages.LoginPage;
import pages.PolicyListPage;
import utils.WebDriverUtil;

import java.util.List;

public class FilterPoliciesStepDefinitions {

    private WebDriver driver;
    private LoginPage loginPage;
    private DashboardPage dashboardPage;
    private PolicyListPage policyListPage;

    public FilterPoliciesStepDefinitions() {
        this.driver = WebDriverUtil.getDriver();
        this.loginPage = new LoginPage(driver);
        this.dashboardPage = new DashboardPage(driver);
        this.policyListPage = new PolicyListPage(driver);
    }

    @Given("I am logged in as {string} with password {string}")
    public void i_am_logged_in_as_with_password(String username, String password) {
        loginPage.navigateToLoginPage(); // Ensure we start at the login page
        // loginPage.performLogin(username, password); // Use the existing method
        // dashboardPage = new DashboardPage(driver); // Assume login navigates to dashboard
        dashboardPage = loginPage.loginExpectingSuccess(username, password);
        Assertions.assertTrue(dashboardPage.isDashboardHeaderDisplayed(), "Login was not successful or not redirected to dashboard.");
    }

    @Given("I am on the policy listing page")
    public void i_am_on_the_policy_listing_page() {
        // Assuming dashboardPage is already instantiated from the login step
        policyListPage = dashboardPage.navigateToPolicyList();
        Assertions.assertTrue(policyListPage.isPolicyListPageDisplayed(), "Not navigated to policy listing page.");
    }

    @When("I filter policies by status {string}")
    public void i_filter_policies_by_status(String status) {
        String statusKey;
        switch (status.toLowerCase()) {
            case "active":
                statusKey = "policy.status.active";
                break;
            case "expired":
                statusKey = "policy.status.expired";
                break;
            default:
                throw new IllegalArgumentException("Unsupported status: " + status);
        }
        policyListPage.filterByStatus(statusKey);
    }

    @Then("I should see only {string} policies in the list")
    public void i_should_see_only_policies_in_the_list(String expectedStatus) {
        List<String> displayedStatuses = policyListPage.getDisplayedPolicyStatuses();
        String localizedExpectedStatus = policyListPage.getLocalizedString("policy.status." + expectedStatus.toLowerCase());

        Assertions.assertFalse(displayedStatuses.isEmpty(), "No policies found in the list after filtering.");

        for (String actualStatus : displayedStatuses) {
            Assertions.assertEquals(localizedExpectedStatus, actualStatus,
                    "Policy with status '" + actualStatus + "' found, but expected only '" + localizedExpectedStatus + "' policies.");
        }
    }
}
