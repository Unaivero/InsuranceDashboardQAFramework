package api.stepdefinitions;

import api.clients.PolicyApiClient;
import api.models.ApiResponse;
import api.models.PolicyApiModel;
import exceptions.FrameworkException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import testdata.TestDataUtil;
import utils.LoggingUtil;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * API Step Definitions for Policy Operations
 * 
 * Cucumber step definitions for testing Policy-related API endpoints
 * 
 * @author Insurance Dashboard QA Framework
 * @version 1.0
 */
public class PolicyApiStepDefinitions {
    
    private PolicyApiClient policyApiClient;
    private ApiResponse<?> lastApiResponse;
    private PolicyApiModel currentPolicy;
    private List<PolicyApiModel> policyList;
    private Exception lastException;
    
    public PolicyApiStepDefinitions() {
        this.policyApiClient = PolicyApiClient.getInstance();
    }
    
    // Authentication Steps
    @Given("I am authenticated as a {string} user for API access")
    public void i_am_authenticated_as_user_for_api_access(String userType) throws FrameworkException {
        LoggingUtil.logInfo("Authenticating as " + userType + " user for API access");
        
        // Get user credentials based on type
        String username, password;
        switch (userType.toLowerCase()) {
            case "admin":
                username = "admin@insurance.com";
                password = "admin123";
                break;
            case "agent":
                username = "agent@insurance.com";
                password = "agent123";
                break;
            case "manager":
                username = "manager@insurance.com";
                password = "manager123";
                break;
            default:
                username = "user@insurance.com";
                password = "user123";
                break;
        }
        
        try {
            policyApiClient.authenticate(username, password);
            LoggingUtil.logInfo("Successfully authenticated as " + userType + " user");
        } catch (Exception e) {
            LoggingUtil.logError("Failed to authenticate as " + userType + " user: " + e.getMessage());
            throw new FrameworkException("Authentication failed for " + userType + " user", e);
        }
    }
    
    // Policy Creation Steps
    @When("I create a new policy via API with the following details:")
    public void i_create_new_policy_via_api_with_details(Map<String, String> policyDetails) {
        LoggingUtil.logInfo("Creating new policy via API with details: " + policyDetails);
        
        try {
            PolicyApiModel policy = new PolicyApiModel();
            
            // Set policy details from the table
            if (policyDetails.containsKey("policyNumber")) {
                policy.setPolicyNumber(policyDetails.get("policyNumber"));
            }
            if (policyDetails.containsKey("policyType")) {
                policy.setPolicyType(policyDetails.get("policyType"));
            }
            if (policyDetails.containsKey("status")) {
                policy.setStatus(policyDetails.get("status"));
            }
            if (policyDetails.containsKey("customerName")) {
                policy.setCustomerName(policyDetails.get("customerName"));
            }
            if (policyDetails.containsKey("premiumAmount")) {
                policy.setPremiumAmount(new BigDecimal(policyDetails.get("premiumAmount")));
            }
            if (policyDetails.containsKey("deductibleAmount")) {
                policy.setDeductibleAmount(new BigDecimal(policyDetails.get("deductibleAmount")));
            }
            if (policyDetails.containsKey("coverageAmount")) {
                policy.setCoverageAmount(new BigDecimal(policyDetails.get("coverageAmount")));
            }
            if (policyDetails.containsKey("description")) {
                policy.setDescription(policyDetails.get("description"));
            }
            
            lastApiResponse = policyApiClient.createPolicy(policy);
            currentPolicy = (PolicyApiModel) lastApiResponse.getData();
            
            LoggingUtil.logInfo("Policy creation API call completed");
            
        } catch (Exception e) {
            lastException = e;
            LoggingUtil.logError("Failed to create policy via API: " + e.getMessage(), e);
        }
    }
    
    @When("I create a new {string} policy for customer {string} with premium {string}")
    public void i_create_new_policy_for_customer_with_premium(String policyType, String customerName, String premium) {
        LoggingUtil.logInfo("Creating new " + policyType + " policy for " + customerName + " with premium " + premium);
        
        try {
            PolicyApiModel policy = TestDataUtil.getInstance().createTestPolicy(policyType, "ACTIVE");
            policy.setCustomerName(customerName);
            policy.setPremiumAmount(new BigDecimal(premium));
            
            lastApiResponse = policyApiClient.createPolicy(policy);
            currentPolicy = (PolicyApiModel) lastApiResponse.getData();
            
            LoggingUtil.logInfo("Policy creation completed for customer: " + customerName);
            
        } catch (Exception e) {
            lastException = e;
            LoggingUtil.logError("Failed to create policy for customer " + customerName + ": " + e.getMessage(), e);
        }
    }
    
    // Policy Retrieval Steps
    @When("I retrieve all policies via API")
    public void i_retrieve_all_policies_via_api() {
        LoggingUtil.logInfo("Retrieving all policies via API");
        
        try {
            lastApiResponse = policyApiClient.getAllPolicies();
            policyList = (List<PolicyApiModel>) lastApiResponse.getData();
            
            LoggingUtil.logInfo("Retrieved " + (policyList != null ? policyList.size() : 0) + " policies");
            
        } catch (Exception e) {
            lastException = e;
            LoggingUtil.logError("Failed to retrieve all policies: " + e.getMessage(), e);
        }
    }
    
    @When("I retrieve policies with pagination page {int} and size {int}")
    public void i_retrieve_policies_with_pagination(int page, int size) {
        LoggingUtil.logInfo("Retrieving policies with pagination - page: " + page + ", size: " + size);
        
        try {
            lastApiResponse = policyApiClient.getAllPolicies(page, size);
            policyList = (List<PolicyApiModel>) lastApiResponse.getData();
            
            LoggingUtil.logInfo("Retrieved " + (policyList != null ? policyList.size() : 0) + " policies with pagination");
            
        } catch (Exception e) {
            lastException = e;
            LoggingUtil.logError("Failed to retrieve policies with pagination: " + e.getMessage(), e);
        }
    }
    
    @When("I retrieve policy by ID {string}")
    public void i_retrieve_policy_by_id(String policyId) {
        LoggingUtil.logInfo("Retrieving policy by ID: " + policyId);
        
        try {
            lastApiResponse = policyApiClient.getPolicyById(policyId);
            currentPolicy = (PolicyApiModel) lastApiResponse.getData();
            
            LoggingUtil.logInfo("Retrieved policy with ID: " + policyId);
            
        } catch (Exception e) {
            lastException = e;
            LoggingUtil.logError("Failed to retrieve policy by ID " + policyId + ": " + e.getMessage(), e);
        }
    }
    
    @When("I retrieve policy by policy number {string}")
    public void i_retrieve_policy_by_policy_number(String policyNumber) {
        LoggingUtil.logInfo("Retrieving policy by policy number: " + policyNumber);
        
        try {
            lastApiResponse = policyApiClient.getPolicyByNumber(policyNumber);
            currentPolicy = (PolicyApiModel) lastApiResponse.getData();
            
            LoggingUtil.logInfo("Retrieved policy with number: " + policyNumber);
            
        } catch (Exception e) {
            lastException = e;
            LoggingUtil.logError("Failed to retrieve policy by number " + policyNumber + ": " + e.getMessage(), e);
        }
    }
    
    @When("I search for policies with status {string}")
    public void i_search_for_policies_with_status(String status) {
        LoggingUtil.logInfo("Searching for policies with status: " + status);
        
        try {
            lastApiResponse = policyApiClient.getPoliciesByStatus(status);
            policyList = (List<PolicyApiModel>) lastApiResponse.getData();
            
            LoggingUtil.logInfo("Found " + (policyList != null ? policyList.size() : 0) + " policies with status: " + status);
            
        } catch (Exception e) {
            lastException = e;
            LoggingUtil.logError("Failed to search policies by status " + status + ": " + e.getMessage(), e);
        }
    }
    
    @When("I search for policies with type {string}")
    public void i_search_for_policies_with_type(String policyType) {
        LoggingUtil.logInfo("Searching for policies with type: " + policyType);
        
        try {
            lastApiResponse = policyApiClient.getPoliciesByType(policyType);
            policyList = (List<PolicyApiModel>) lastApiResponse.getData();
            
            LoggingUtil.logInfo("Found " + (policyList != null ? policyList.size() : 0) + " policies with type: " + policyType);
            
        } catch (Exception e) {
            lastException = e;
            LoggingUtil.logError("Failed to search policies by type " + policyType + ": " + e.getMessage(), e);
        }
    }
    
    // Policy Update Steps
    @When("I update the policy status to {string}")
    public void i_update_policy_status_to(String newStatus) {
        LoggingUtil.logInfo("Updating policy status to: " + newStatus);
        
        try {
            if (currentPolicy == null || currentPolicy.getId() == null) {
                throw new FrameworkException("No current policy or policy ID available for update");
            }
            
            lastApiResponse = policyApiClient.updatePolicyStatus(currentPolicy.getId(), newStatus);
            currentPolicy = (PolicyApiModel) lastApiResponse.getData();
            
            LoggingUtil.logInfo("Policy status updated to: " + newStatus);
            
        } catch (Exception e) {
            lastException = e;
            LoggingUtil.logError("Failed to update policy status to " + newStatus + ": " + e.getMessage(), e);
        }
    }
    
    @When("I update the policy premium amount to {string}")
    public void i_update_policy_premium_amount_to(String newPremium) {
        LoggingUtil.logInfo("Updating policy premium amount to: " + newPremium);
        
        try {
            if (currentPolicy == null || currentPolicy.getId() == null) {
                throw new FrameworkException("No current policy or policy ID available for update");
            }
            
            currentPolicy.setPremiumAmount(new BigDecimal(newPremium));
            lastApiResponse = policyApiClient.updatePolicy(currentPolicy.getId(), currentPolicy);
            currentPolicy = (PolicyApiModel) lastApiResponse.getData();
            
            LoggingUtil.logInfo("Policy premium amount updated to: " + newPremium);
            
        } catch (Exception e) {
            lastException = e;
            LoggingUtil.logError("Failed to update policy premium to " + newPremium + ": " + e.getMessage(), e);
        }
    }
    
    // Policy Deletion Steps
    @When("I delete the current policy")
    public void i_delete_current_policy() {
        LoggingUtil.logInfo("Deleting current policy");
        
        try {
            if (currentPolicy == null || currentPolicy.getId() == null) {
                throw new FrameworkException("No current policy or policy ID available for deletion");
            }
            
            lastApiResponse = policyApiClient.deletePolicy(currentPolicy.getId());
            
            LoggingUtil.logInfo("Policy deleted successfully");
            
        } catch (Exception e) {
            lastException = e;
            LoggingUtil.logError("Failed to delete current policy: " + e.getMessage(), e);
        }
    }
    
    @When("I delete policy with ID {string}")
    public void i_delete_policy_with_id(String policyId) {
        LoggingUtil.logInfo("Deleting policy with ID: " + policyId);
        
        try {
            lastApiResponse = policyApiClient.deletePolicy(policyId);
            
            LoggingUtil.logInfo("Policy deleted successfully with ID: " + policyId);
            
        } catch (Exception e) {
            lastException = e;
            LoggingUtil.logError("Failed to delete policy with ID " + policyId + ": " + e.getMessage(), e);
        }
    }
    
    // Validation Steps
    @Then("the API response should be successful")
    public void the_api_response_should_be_successful() {
        LoggingUtil.logInfo("Validating API response is successful");
        
        assertNotNull(lastApiResponse, "API response should not be null");
        assertTrue(lastApiResponse.isSuccess(), "API response should indicate success");
        assertNull(lastException, "No exception should have occurred");
        
        LoggingUtil.logInfo("API response validation passed - response is successful");
    }
    
    @Then("the API response should indicate failure")
    public void the_api_response_should_indicate_failure() {
        LoggingUtil.logInfo("Validating API response indicates failure");
        
        assertTrue(lastApiResponse == null || !lastApiResponse.isSuccess() || lastException != null, 
                  "API response should indicate failure or exception should have occurred");
        
        LoggingUtil.logInfo("API response validation passed - response indicates failure");
    }
    
    @Then("the API response should contain {int} policies")
    public void the_api_response_should_contain_policies(int expectedCount) {
        LoggingUtil.logInfo("Validating API response contains " + expectedCount + " policies");
        
        assertNotNull(lastApiResponse, "API response should not be null");
        assertTrue(lastApiResponse.isSuccess(), "API response should be successful");
        assertNotNull(policyList, "Policy list should not be null");
        assertEquals(expectedCount, policyList.size(), "Policy list should contain " + expectedCount + " policies");
        
        LoggingUtil.logInfo("API response validation passed - contains " + expectedCount + " policies");
    }
    
    @Then("the API response should contain at least {int} policies")
    public void the_api_response_should_contain_at_least_policies(int minCount) {
        LoggingUtil.logInfo("Validating API response contains at least " + minCount + " policies");
        
        assertNotNull(lastApiResponse, "API response should not be null");
        assertTrue(lastApiResponse.isSuccess(), "API response should be successful");
        assertNotNull(policyList, "Policy list should not be null");
        assertTrue(policyList.size() >= minCount, 
                  "Policy list should contain at least " + minCount + " policies, but found " + policyList.size());
        
        LoggingUtil.logInfo("API response validation passed - contains at least " + minCount + " policies");
    }
    
    @Then("the returned policy should have policy number {string}")
    public void the_returned_policy_should_have_policy_number(String expectedPolicyNumber) {
        LoggingUtil.logInfo("Validating returned policy has policy number: " + expectedPolicyNumber);
        
        assertNotNull(lastApiResponse, "API response should not be null");
        assertTrue(lastApiResponse.isSuccess(), "API response should be successful");
        assertNotNull(currentPolicy, "Current policy should not be null");
        assertEquals(expectedPolicyNumber, currentPolicy.getPolicyNumber(), 
                    "Policy number should match expected value");
        
        LoggingUtil.logInfo("Policy number validation passed");
    }
    
    @Then("the returned policy should have status {string}")
    public void the_returned_policy_should_have_status(String expectedStatus) {
        LoggingUtil.logInfo("Validating returned policy has status: " + expectedStatus);
        
        assertNotNull(lastApiResponse, "API response should not be null");
        assertTrue(lastApiResponse.isSuccess(), "API response should be successful");
        assertNotNull(currentPolicy, "Current policy should not be null");
        assertEquals(expectedStatus, currentPolicy.getStatus(), 
                    "Policy status should match expected value");
        
        LoggingUtil.logInfo("Policy status validation passed");
    }
    
    @Then("the returned policy should have customer name {string}")
    public void the_returned_policy_should_have_customer_name(String expectedCustomerName) {
        LoggingUtil.logInfo("Validating returned policy has customer name: " + expectedCustomerName);
        
        assertNotNull(lastApiResponse, "API response should not be null");
        assertTrue(lastApiResponse.isSuccess(), "API response should be successful");
        assertNotNull(currentPolicy, "Current policy should not be null");
        assertEquals(expectedCustomerName, currentPolicy.getCustomerName(), 
                    "Customer name should match expected value");
        
        LoggingUtil.logInfo("Customer name validation passed");
    }
    
    @Then("the returned policy should have premium amount {string}")
    public void the_returned_policy_should_have_premium_amount(String expectedPremium) {
        LoggingUtil.logInfo("Validating returned policy has premium amount: " + expectedPremium);
        
        assertNotNull(lastApiResponse, "API response should not be null");
        assertTrue(lastApiResponse.isSuccess(), "API response should be successful");
        assertNotNull(currentPolicy, "Current policy should not be null");
        assertNotNull(currentPolicy.getPremiumAmount(), "Premium amount should not be null");
        
        BigDecimal expected = new BigDecimal(expectedPremium);
        assertEquals(0, expected.compareTo(currentPolicy.getPremiumAmount()), 
                    "Premium amount should match expected value");
        
        LoggingUtil.logInfo("Premium amount validation passed");
    }
    
    @Then("all returned policies should have status {string}")
    public void all_returned_policies_should_have_status(String expectedStatus) {
        LoggingUtil.logInfo("Validating all returned policies have status: " + expectedStatus);
        
        assertNotNull(lastApiResponse, "API response should not be null");
        assertTrue(lastApiResponse.isSuccess(), "API response should be successful");
        assertNotNull(policyList, "Policy list should not be null");
        
        for (PolicyApiModel policy : policyList) {
            assertEquals(expectedStatus, policy.getStatus(), 
                        "All policies should have status " + expectedStatus);
        }
        
        LoggingUtil.logInfo("All policies status validation passed");
    }
    
    @Then("all returned policies should have type {string}")
    public void all_returned_policies_should_have_type(String expectedType) {
        LoggingUtil.logInfo("Validating all returned policies have type: " + expectedType);
        
        assertNotNull(lastApiResponse, "API response should not be null");
        assertTrue(lastApiResponse.isSuccess(), "API response should be successful");
        assertNotNull(policyList, "Policy list should not be null");
        
        for (PolicyApiModel policy : policyList) {
            assertEquals(expectedType, policy.getPolicyType(), 
                        "All policies should have type " + expectedType);
        }
        
        LoggingUtil.logInfo("All policies type validation passed");
    }
    
    @And("the API response should have pagination information")
    public void the_api_response_should_have_pagination_information() {
        LoggingUtil.logInfo("Validating API response has pagination information");
        
        assertNotNull(lastApiResponse, "API response should not be null");
        assertTrue(lastApiResponse.isSuccess(), "API response should be successful");
        assertNotNull(lastApiResponse.getPagination(), "Pagination information should be present");
        
        LoggingUtil.logInfo("Pagination information validation passed");
    }
    
    @And("the current policy should be set")
    public void the_current_policy_should_be_set() {
        LoggingUtil.logInfo("Validating current policy is set");
        
        assertNotNull(currentPolicy, "Current policy should be set");
        assertNotNull(currentPolicy.getId(), "Current policy should have an ID");
        
        LoggingUtil.logInfo("Current policy validation passed");
    }
    
    // Cleanup Steps
    @And("I clear the API test data")
    public void i_clear_api_test_data() {
        LoggingUtil.logInfo("Clearing API test data");
        
        lastApiResponse = null;
        currentPolicy = null;
        policyList = null;
        lastException = null;
        
        LoggingUtil.logInfo("API test data cleared");
    }
}
