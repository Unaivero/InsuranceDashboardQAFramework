package api.clients;

import api.models.ApiResponse;
import api.models.PolicyApiModel;
import exceptions.FrameworkException;
import io.restassured.response.Response;
import utils.LoggingUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Policy API Client
 * 
 * Handles all Policy-related API operations including:
 * - CRUD operations for policies
 * - Policy search and filtering
 * - Policy status management
 * - Bulk operations
 * 
 * @author Insurance Dashboard QA Framework
 * @version 1.0
 */
public class PolicyApiClient extends BaseApiClient {
    
    private static PolicyApiClient instance;
    
    /**
     * Private constructor for singleton pattern
     */
    private PolicyApiClient() {
        super();
        initializeClient();
    }
    
    /**
     * Get singleton instance
     */
    public static synchronized PolicyApiClient getInstance() {
        if (instance == null) {
            instance = new PolicyApiClient();
        }
        return instance;
    }
    
    @Override
    protected void initializeClient() {
        LoggingUtil.logInfo("Initializing Policy API Client");
        // Add any policy-specific initialization here
        addHeader("X-API-Client", "PolicyApiClient");
    }
    
    /**
     * Get all policies
     */
    public ApiResponse<List<PolicyApiModel>> getAllPolicies() throws FrameworkException {
        LoggingUtil.logInfo("Fetching all policies");
        
        Response response = executeGetRequest(POLICIES_ENDPOINT);
        validateResponse(response, HTTP_OK);
        
        return parseResponse(response, List.class);
    }
    
    /**
     * Get policies with pagination
     */
    public ApiResponse<List<PolicyApiModel>> getAllPolicies(int page, int size) throws FrameworkException {
        LoggingUtil.logInfo("Fetching policies with pagination - page: " + page + ", size: " + size);
        
        String endpoint = POLICIES_ENDPOINT + "?page=" + page + "&size=" + size;
        Response response = executeGetRequest(endpoint);
        validateResponse(response, HTTP_OK);
        
        return parseResponse(response, List.class);
    }
    
    /**
     * Get policy by ID
     */
    public ApiResponse<PolicyApiModel> getPolicyById(String policyId) throws FrameworkException {
        LoggingUtil.logInfo("Fetching policy by ID: " + policyId);
        
        if (policyId == null || policyId.trim().isEmpty()) {
            throw new FrameworkException("Policy ID cannot be null or empty");
        }
        
        String endpoint = POLICIES_ENDPOINT + "/" + policyId;
        Response response = executeGetRequest(endpoint);
        validateResponse(response, HTTP_OK);
        
        return parseResponse(response, PolicyApiModel.class);
    }
    
    /**
     * Get policy by policy number
     */
    public ApiResponse<PolicyApiModel> getPolicyByNumber(String policyNumber) throws FrameworkException {
        LoggingUtil.logInfo("Fetching policy by number: " + policyNumber);
        
        if (policyNumber == null || policyNumber.trim().isEmpty()) {
            throw new FrameworkException("Policy number cannot be null or empty");
        }
        
        String endpoint = POLICIES_ENDPOINT + "/search?policyNumber=" + policyNumber;
        Response response = executeGetRequest(endpoint);
        validateResponse(response, HTTP_OK);
        
        return parseResponse(response, PolicyApiModel.class);
    }
    
    /**
     * Create new policy
     */
    public ApiResponse<PolicyApiModel> createPolicy(PolicyApiModel policy) throws FrameworkException {
        LoggingUtil.logInfo("Creating new policy: " + policy.getPolicyNumber());
        
        if (policy == null) {
            throw new FrameworkException("Policy object cannot be null");
        }
        
        // Validate policy before sending
        policy.validate();
        
        Response response = executePostRequest(POLICIES_ENDPOINT, policy);
        validateResponse(response, HTTP_CREATED);
        
        return parseResponse(response, PolicyApiModel.class);
    }
    
    /**
     * Update existing policy
     */
    public ApiResponse<PolicyApiModel> updatePolicy(String policyId, PolicyApiModel policy) throws FrameworkException {
        LoggingUtil.logInfo("Updating policy: " + policyId);
        
        if (policyId == null || policyId.trim().isEmpty()) {
            throw new FrameworkException("Policy ID cannot be null or empty");
        }
        
        if (policy == null) {
            throw new FrameworkException("Policy object cannot be null");
        }
        
        // Validate policy before sending
        policy.validate();
        
        String endpoint = POLICIES_ENDPOINT + "/" + policyId;
        Response response = executePutRequest(endpoint, policy);
        validateResponse(response, HTTP_OK);
        
        return parseResponse(response, PolicyApiModel.class);
    }
    
    /**
     * Delete policy
     */
    public ApiResponse<Void> deletePolicy(String policyId) throws FrameworkException {
        LoggingUtil.logInfo("Deleting policy: " + policyId);
        
        if (policyId == null || policyId.trim().isEmpty()) {
            throw new FrameworkException("Policy ID cannot be null or empty");
        }
        
        String endpoint = POLICIES_ENDPOINT + "/" + policyId;
        Response response = executeDeleteRequest(endpoint);
        validateResponse(response, HTTP_NO_CONTENT);
        
        return parseResponse(response, Void.class);
    }
    
    /**
     * Search policies by criteria
     */
    public ApiResponse<List<PolicyApiModel>> searchPolicies(Map<String, String> searchCriteria) throws FrameworkException {
        LoggingUtil.logInfo("Searching policies with criteria: " + searchCriteria);
        
        StringBuilder queryString = new StringBuilder();
        if (searchCriteria != null && !searchCriteria.isEmpty()) {
            queryString.append("?");
            searchCriteria.forEach((key, value) -> 
                queryString.append(key).append("=").append(value).append("&"));
            // Remove last &
            queryString.setLength(queryString.length() - 1);
        }
        
        String endpoint = POLICIES_ENDPOINT + "/search" + queryString.toString();
        Response response = executeGetRequest(endpoint);
        validateResponse(response, HTTP_OK);
        
        return parseResponse(response, List.class);
    }
    
    /**
     * Get policies by status
     */
    public ApiResponse<List<PolicyApiModel>> getPoliciesByStatus(String status) throws FrameworkException {
        LoggingUtil.logInfo("Fetching policies by status: " + status);
        
        if (status == null || status.trim().isEmpty()) {
            throw new FrameworkException("Status cannot be null or empty");
        }
        
        Map<String, String> searchCriteria = new HashMap<>();
        searchCriteria.put("status", status);
        
        return searchPolicies(searchCriteria);
    }
    
    /**
     * Get policies by type
     */
    public ApiResponse<List<PolicyApiModel>> getPoliciesByType(String policyType) throws FrameworkException {
        LoggingUtil.logInfo("Fetching policies by type: " + policyType);
        
        if (policyType == null || policyType.trim().isEmpty()) {
            throw new FrameworkException("Policy type cannot be null or empty");
        }
        
        Map<String, String> searchCriteria = new HashMap<>();
        searchCriteria.put("policyType", policyType);
        
        return searchPolicies(searchCriteria);
    }
    
    /**
     * Get policies by customer
     */
    public ApiResponse<List<PolicyApiModel>> getPoliciesByCustomer(String customerId) throws FrameworkException {
        LoggingUtil.logInfo("Fetching policies by customer: " + customerId);
        
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new FrameworkException("Customer ID cannot be null or empty");
        }
        
        Map<String, String> searchCriteria = new HashMap<>();
        searchCriteria.put("customerId", customerId);
        
        return searchPolicies(searchCriteria);
    }
    
    /**
     * Update policy status
     */
    public ApiResponse<PolicyApiModel> updatePolicyStatus(String policyId, String newStatus) throws FrameworkException {
        LoggingUtil.logInfo("Updating policy status - ID: " + policyId + ", Status: " + newStatus);
        
        if (policyId == null || policyId.trim().isEmpty()) {
            throw new FrameworkException("Policy ID cannot be null or empty");
        }
        
        if (newStatus == null || newStatus.trim().isEmpty()) {
            throw new FrameworkException("Status cannot be null or empty");
        }
        
        Map<String, String> statusUpdate = new HashMap<>();
        statusUpdate.put("status", newStatus);
        
        String endpoint = POLICIES_ENDPOINT + "/" + policyId + "/status";
        Response response = executePutRequest(endpoint, statusUpdate);
        validateResponse(response, HTTP_OK);
        
        return parseResponse(response, PolicyApiModel.class);
    }
    
    /**
     * Get policy claims
     */
    public ApiResponse<List<String>> getPolicyClaims(String policyId) throws FrameworkException {
        LoggingUtil.logInfo("Fetching claims for policy: " + policyId);
        
        if (policyId == null || policyId.trim().isEmpty()) {
            throw new FrameworkException("Policy ID cannot be null or empty");
        }
        
        String endpoint = POLICIES_ENDPOINT + "/" + policyId + "/claims";
        Response response = executeGetRequest(endpoint);
        validateResponse(response, HTTP_OK);
        
        return parseResponse(response, List.class);
    }
    
    /**
     * Bulk create policies
     */
    public ApiResponse<List<PolicyApiModel>> createPoliciesBulk(List<PolicyApiModel> policies) throws FrameworkException {
        LoggingUtil.logInfo("Creating policies in bulk - count: " + (policies != null ? policies.size() : 0));
        
        if (policies == null || policies.isEmpty()) {
            throw new FrameworkException("Policies list cannot be null or empty");
        }
        
        // Validate all policies before sending
        for (PolicyApiModel policy : policies) {
            policy.validate();
        }
        
        Response response = executePostRequest(POLICIES_ENDPOINT + "/bulk", policies);
        validateResponse(response, HTTP_CREATED);
        
        return parseResponse(response, List.class);
    }
    
    /**
     * Get active policies count
     */
    public ApiResponse<Integer> getActivePoliciesCount() throws FrameworkException {
        LoggingUtil.logInfo("Fetching active policies count");
        
        Response response = executeGetRequest(POLICIES_ENDPOINT + "/count/active");
        validateResponse(response, HTTP_OK);
        
        return parseResponse(response, Integer.class);
    }
    
    /**
     * Get policies statistics
     */
    public ApiResponse<Map<String, Object>> getPoliciesStatistics() throws FrameworkException {
        LoggingUtil.logInfo("Fetching policies statistics");
        
        Response response = executeGetRequest(POLICIES_ENDPOINT + "/statistics");
        validateResponse(response, HTTP_OK);
        
        return parseResponse(response, Map.class);
    }
    
    /**
     * Validate policy number availability
     */
    public ApiResponse<Boolean> isPolicyNumberAvailable(String policyNumber) throws FrameworkException {
        LoggingUtil.logInfo("Checking policy number availability: " + policyNumber);
        
        if (policyNumber == null || policyNumber.trim().isEmpty()) {
            throw new FrameworkException("Policy number cannot be null or empty");
        }
        
        String endpoint = POLICIES_ENDPOINT + "/validate/number?policyNumber=" + policyNumber;
        Response response = executeGetRequest(endpoint);
        validateResponse(response, HTTP_OK);
        
        return parseResponse(response, Boolean.class);
    }
}
