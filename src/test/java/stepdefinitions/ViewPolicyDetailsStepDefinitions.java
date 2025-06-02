package stepdefinitions;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import pages.PolicyDetailsPage;
import pages.PolicyListPage;
import utils.WebDriverUtil;
import utils.LoggingUtil;

/**
 * Enhanced step definitions for Policy Details viewing functionality
 * Updated to use the enhanced PolicyDetailsPage capabilities
 */
public class ViewPolicyDetailsStepDefinitions {

    private WebDriver driver;
    private PolicyListPage policyListPage;
    private PolicyDetailsPage policyDetailsPage;

    public ViewPolicyDetailsStepDefinitions() {
        this.driver = WebDriverUtil.getDriver();
        // Assuming PolicyListPage is already the current page due to Background steps
        this.policyListPage = new PolicyListPage(driver);
        // PolicyDetailsPage will be instantiated after clicking a policy
    }

    @When("I click on policy with ID {string}")
    public void i_click_on_policy_with_id(String policyId) {
        LoggingUtil.setTestStep("Clicking on policy with ID: " + policyId);
        LoggingUtil.logTestData("policyId", policyId);
        
        policyDetailsPage = policyListPage.clickPolicyById(policyId);
        
        // Wait for the page to load completely
        policyDetailsPage.waitForPageToLoad();
    }

    @Then("I should be on the policy detail page for {string}")
    public void i_should_be_on_the_policy_detail_page_for(String expectedPolicyId) {
        LoggingUtil.setTestStep("Verifying policy detail page for ID: " + expectedPolicyId);
        
        Assertions.assertTrue(policyDetailsPage.isPolicyDetailsPageDisplayed(),
                "Policy details page is not displayed.");
        
        // Verify we're on the correct policy details page
        Assertions.assertTrue(policyDetailsPage.verifyPolicyDetails(expectedPolicyId),
                "Not on the policy detail page for ID: " + expectedPolicyId + 
                ". Current ID: " + policyDetailsPage.getPolicyId());
                
        LoggingUtil.logTestData("verifiedPolicyId", expectedPolicyId);
    }

    @Then("I should see the policy information")
    public void i_should_see_the_policy_information() {
        LoggingUtil.setTestStep("Verifying policy information is displayed");
        
        Assertions.assertTrue(policyDetailsPage.isPolicyInformationDisplayed(),
                "Policy information (ID, Status, Type) is not fully displayed.");
        
        // Additional verification that key fields contain data
        Assertions.assertFalse(policyDetailsPage.getPolicyId().isEmpty(), 
                "Policy ID is empty.");
        Assertions.assertFalse(policyDetailsPage.getPolicyStatus().isEmpty(), 
                "Policy status is empty.");
        Assertions.assertFalse(policyDetailsPage.getPolicyType().isEmpty(), 
                "Policy type is empty.");
                
        // Log the policy information for debugging
        String policyInfo = policyDetailsPage.getAllPolicyInformation();
        LoggingUtil.logTestData("policyInformation", policyInfo);
    }

    @Then("I should see policy details including status, type, and coverage information")
    public void i_should_see_policy_details_including_status_type_and_coverage_information() {
        LoggingUtil.setTestStep("Verifying comprehensive policy details");
        
        // Verify all major sections of policy information are displayed
        Assertions.assertTrue(policyDetailsPage.isPolicyInformationDisplayed(),
                "Basic policy information is not displayed.");
        
        // Verify specific fields contain data
        String policyInfo = policyDetailsPage.getAllPolicyInformation();
        Assertions.assertFalse(policyInfo.isEmpty(), 
                "Policy information is empty.");
        
        // Verify key fields are not empty
        Assertions.assertFalse(policyDetailsPage.getPolicyNumber().isEmpty(), 
                "Policy number is empty.");
        Assertions.assertFalse(policyDetailsPage.getCoverageAmount().isEmpty(), 
                "Coverage amount is empty.");
        Assertions.assertFalse(policyDetailsPage.getInsuredName().isEmpty(), 
                "Insured name is empty.");
                
        // Log comprehensive policy details
        LoggingUtil.logTestData("comprehensivePolicyDetails", policyInfo);
    }

    @Then("I should see policy documents section")
    public void i_should_see_policy_documents_section() {
        LoggingUtil.setTestStep("Verifying policy documents section");
        
        Assertions.assertTrue(policyDetailsPage.isPolicyDocumentsSectionDisplayed(),
                "Policy documents section is not displayed.");
                
        // Log document count for visibility
        int documentCount = policyDetailsPage.getPolicyDocumentsCount();
        LoggingUtil.logTestData("policyDocumentsCount", String.valueOf(documentCount));
    }

    @Then("I should see claims history table")
    public void i_should_see_claims_history_table() {
        LoggingUtil.setTestStep("Verifying claims history table");
        
        Assertions.assertTrue(policyDetailsPage.isClaimsTableDisplayed(),
                "Claims history table is not displayed.");
                
        // Log claims count for visibility
        int claimsCount = policyDetailsPage.getClaimsCount();
        LoggingUtil.logTestData("claimsCount", String.valueOf(claimsCount));
    }

    @When("I click the edit policy button")
    public void i_click_the_edit_policy_button() {
        LoggingUtil.setTestStep("Clicking edit policy button");
        
        policyDetailsPage.clickEditPolicy();
        LoggingUtil.logPageAction("PolicyDetailsPage", "click", "editButton");
    }

    @When("I click the download policy button")
    public void i_click_the_download_policy_button() {
        LoggingUtil.setTestStep("Clicking download policy button");
        
        policyDetailsPage.clickDownloadPolicy();
        LoggingUtil.logPageAction("PolicyDetailsPage", "click", "downloadButton");
    }

    @When("I navigate back to the policy list")
    public void i_navigate_back_to_the_policy_list() {
        LoggingUtil.setTestStep("Navigating back to policy list");
        
        policyListPage = policyDetailsPage.navigateBackToPolicies();
        Assertions.assertTrue(policyListPage.isPolicyListPageDisplayed(),
                "Not successfully navigated back to policy list page.");
                
        LoggingUtil.logNavigation("PolicyDetailsPage", "PolicyListPage", "backLink");
    }

    @Then("I should be on the policy listing page")
    public void i_should_be_on_the_policy_listing_page() {
        LoggingUtil.setTestStep("Verifying on policy listing page");
        
        Assertions.assertTrue(policyListPage.isPolicyListPageDisplayed(),
                "Not on the policy listing page.");
    }
}
