package api.clients;

import api.models.ApiResponse;
import api.models.ClaimApiModel;
import exceptions.FrameworkException;
import io.restassured.response.Response;
import utils.LoggingUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Claims API Client
 * 
 * Handles all Claims-related API operations including:
 * - Claims management (CRUD)
 * - Claims workflow operations
 * - Status tracking and updates
 * - Document management
 * 
 * @author Insurance Dashboard QA Framework
 * @version 1.0
 */
public class ClaimsApiClient extends BaseApiClient {
    
    private static ClaimsApiClient instance;
    
    /**
     * Private constructor for singleton pattern
     */
    private ClaimsApiClient() {
        super();
        initializeClient();
    }
    
    /**
     * Get singleton instance
     */
    public static synchronized ClaimsApiClient getInstance() {
        if (instance == null) {
            instance = new ClaimsApiClient();
        }
        return instance;
    }
    
    @Override
    protected void initializeClient() {
        LoggingUtil.logInfo("Initializing Claims API Client");
        addHeader("X-API-Client", "ClaimsApiClient");
    }
    
    /**
     * Get all claims
     */
    public ApiResponse<List<ClaimApiModel>> getAllClaims() throws FrameworkException {
        LoggingUtil.logInfo("Fetching all claims");
        
        Response response = executeGetRequest(CLAIMS_ENDPOINT);
        validateResponse(response, HTTP_OK);
        
        return parseResponse(response, List.class);
    }
    
    /**
     * Get claims with pagination
     */
    public ApiResponse<List<ClaimApiModel>> getAllClaims(int page, int size) throws FrameworkException {
        LoggingUtil.logInfo("Fetching claims with pagination - page: " + page + ", size: " + size);
        
        String endpoint = CLAIMS_ENDPOINT + "?page=" + page + "&size=" + size;
        Response response = executeGetRequest(endpoint);
        validateResponse(response, HTTP_OK);
        
        return parseResponse(response, List.class);
    }
    
    /**
     * Get claim by ID
     */
    public ApiResponse<ClaimApiModel> getClaimById(String claimId) throws FrameworkException {
        LoggingUtil.logInfo("Fetching claim by ID: " + claimId);
        
        if (claimId == null || claimId.trim().isEmpty()) {
            throw new FrameworkException("Claim ID cannot be null or empty");
        }
        
        String endpoint = CLAIMS_ENDPOINT + "/" + claimId;
        Response response = executeGetRequest(endpoint);
        validateResponse(response, HTTP_OK);
        
        return parseResponse(response, ClaimApiModel.class);
    }
    
    /**
     * Get claim by claim number
     */
    public ApiResponse<ClaimApiModel> getClaimByNumber(String claimNumber) throws FrameworkException {
        LoggingUtil.logInfo("Fetching claim by number: " + claimNumber);
        
        if (claimNumber == null || claimNumber.trim().isEmpty()) {
            throw new FrameworkException("Claim number cannot be null or empty");
        }
        
        String endpoint = CLAIMS_ENDPOINT + "/search?claimNumber=" + claimNumber;
        Response response = executeGetRequest(endpoint);
        validateResponse(response, HTTP_OK);
        
        return parseResponse(response, ClaimApiModel.class);
    }
    
    /**
     * Create new claim
     */
    public ApiResponse<ClaimApiModel> createClaim(ClaimApiModel claim) throws FrameworkException {
        LoggingUtil.logInfo("Creating new claim: " + claim.getClaimNumber());
        
        if (claim == null) {
            throw new FrameworkException("Claim object cannot be null");
        }
        
        // Validate claim before sending
        claim.validate();
        
        Response response = executePostRequest(CLAIMS_ENDPOINT, claim);
        validateResponse(response, HTTP_CREATED);
        
        return parseResponse(response, ClaimApiModel.class);
    }
    
    /**
     * Update existing claim
     */
    public ApiResponse<ClaimApiModel> updateClaim(String claimId, ClaimApiModel claim) throws FrameworkException {
        LoggingUtil.logInfo("Updating claim: " + claimId);
        
        if (claimId == null || claimId.trim().isEmpty()) {
            throw new FrameworkException("Claim ID cannot be null or empty");
        }
        
        if (claim == null) {
            throw new FrameworkException("Claim object cannot be null");
        }
        
        // Validate claim before sending
        claim.validate();
        
        String endpoint = CLAIMS_ENDPOINT + "/" + claimId;
        Response response = executePutRequest(endpoint, claim);
        validateResponse(response, HTTP_OK);
        
        return parseResponse(response, ClaimApiModel.class);
    }
    
    /**
     * Delete claim
     */
    public ApiResponse<Void> deleteClaim(String claimId) throws FrameworkException {
        LoggingUtil.logInfo("Deleting claim: " + claimId);
        
        if (claimId == null || claimId.trim().isEmpty()) {
            throw new FrameworkException("Claim ID cannot be null or empty");
        }
        
        String endpoint = CLAIMS_ENDPOINT + "/" + claimId;
        Response response = executeDeleteRequest(endpoint);
        validateResponse(response, HTTP_NO_CONTENT);
        
        return parseResponse(response, Void.class);
    }
    
    /**
     * Search claims by criteria
     */
    public ApiResponse<List<ClaimApiModel>> searchClaims(Map<String, String> searchCriteria) throws FrameworkException {
        LoggingUtil.logInfo("Searching claims with criteria: " + searchCriteria);
        
        StringBuilder queryString = new StringBuilder();
        if (searchCriteria != null && !searchCriteria.isEmpty()) {
            queryString.append("?");
            searchCriteria.forEach((key, value) -> 
                queryString.append(key).append("=").append(value).append("&"));
            // Remove last &
            queryString.setLength(queryString.length() - 1);
        }
        
        String endpoint = CLAIMS_ENDPOINT + "/search" + queryString.toString();
        Response response = executeGetRequest(endpoint);
        validateResponse(response, HTTP_OK);
        
        return parseResponse(response, List.class);
    }
    
    /**
     * Get claims by status
     */
    public ApiResponse<List<ClaimApiModel>> getClaimsByStatus(String status) throws FrameworkException {
        LoggingUtil.logInfo("Fetching claims by status: " + status);
        
        if (status == null || status.trim().isEmpty()) {
            throw new FrameworkException("Status cannot be null or empty");
        }
        
        Map<String, String> searchCriteria = new HashMap<>();
        searchCriteria.put("status", status);
        
        return searchClaims(searchCriteria);
    }
    
    /**
     * Get claims by type
     */
    public ApiResponse<List<ClaimApiModel>> getClaimsByType(String claimType) throws FrameworkException {
        LoggingUtil.logInfo("Fetching claims by type: " + claimType);
        
        if (claimType == null || claimType.trim().isEmpty()) {
            throw new FrameworkException("Claim type cannot be null or empty");
        }
        
        Map<String, String> searchCriteria = new HashMap<>();
        searchCriteria.put("claimType", claimType);
        
        return searchClaims(searchCriteria);
    }
    
    /**
     * Get claims by policy ID
     */
    public ApiResponse<List<ClaimApiModel>> getClaimsByPolicyId(String policyId) throws FrameworkException {
        LoggingUtil.logInfo("Fetching claims by policy ID: " + policyId);
        
        if (policyId == null || policyId.trim().isEmpty()) {
            throw new FrameworkException("Policy ID cannot be null or empty");
        }
        
        Map<String, String> searchCriteria = new HashMap<>();
        searchCriteria.put("policyId", policyId);
        
        return searchClaims(searchCriteria);
    }
    
    /**
     * Get claims by claimant
     */
    public ApiResponse<List<ClaimApiModel>> getClaimsByClaimant(String claimantId) throws FrameworkException {
        LoggingUtil.logInfo("Fetching claims by claimant: " + claimantId);
        
        if (claimantId == null || claimantId.trim().isEmpty()) {
            throw new FrameworkException("Claimant ID cannot be null or empty");
        }
        
        Map<String, String> searchCriteria = new HashMap<>();
        searchCriteria.put("claimantId", claimantId);
        
        return searchClaims(searchCriteria);
    }
    
    /**
     * Update claim status
     */
    public ApiResponse<ClaimApiModel> updateClaimStatus(String claimId, String newStatus, String reason) throws FrameworkException {
        LoggingUtil.logInfo("Updating claim status - ID: " + claimId + ", Status: " + newStatus);
        
        if (claimId == null || claimId.trim().isEmpty()) {
            throw new FrameworkException("Claim ID cannot be null or empty");
        }
        
        if (newStatus == null || newStatus.trim().isEmpty()) {
            throw new FrameworkException("Status cannot be null or empty");
        }
        
        Map<String, String> statusUpdate = new HashMap<>();
        statusUpdate.put("status", newStatus);
        if (reason != null && !reason.trim().isEmpty()) {
            statusUpdate.put("reason", reason);
        }
        
        String endpoint = CLAIMS_ENDPOINT + "/" + claimId + "/status";
        Response response = executePutRequest(endpoint, statusUpdate);
        validateResponse(response, HTTP_OK);
        
        return parseResponse(response, ClaimApiModel.class);
    }
    
    /**
     * Assign claim to adjuster
     */
    public ApiResponse<ClaimApiModel> assignClaimToAdjuster(String claimId, String adjusterId) throws FrameworkException {
        LoggingUtil.logInfo("Assigning claim to adjuster - Claim: " + claimId + ", Adjuster: " + adjusterId);
        
        if (claimId == null || claimId.trim().isEmpty()) {
            throw new FrameworkException("Claim ID cannot be null or empty");
        }
        
        if (adjusterId == null || adjusterId.trim().isEmpty()) {
            throw new FrameworkException("Adjuster ID cannot be null or empty");
        }
        
        Map<String, String> assignment = new HashMap<>();
        assignment.put("adjusterId", adjusterId);
        
        String endpoint = CLAIMS_ENDPOINT + "/" + claimId + "/assign";
        Response response = executePutRequest(endpoint, assignment);
        validateResponse(response, HTTP_OK);
        
        return parseResponse(response, ClaimApiModel.class);
    }
    
    /**
     * Approve claim
     */
    public ApiResponse<ClaimApiModel> approveClaim(String claimId, String approvedAmount, String approvalNotes) throws FrameworkException {
        LoggingUtil.logInfo("Approving claim: " + claimId + " for amount: " + approvedAmount);
        
        if (claimId == null || claimId.trim().isEmpty()) {
            throw new FrameworkException("Claim ID cannot be null or empty");
        }
        
        if (approvedAmount == null || approvedAmount.trim().isEmpty()) {
            throw new FrameworkException("Approved amount cannot be null or empty");
        }
        
        Map<String, String> approval = new HashMap<>();
        approval.put("approvedAmount", approvedAmount);
        if (approvalNotes != null && !approvalNotes.trim().isEmpty()) {
            approval.put("notes", approvalNotes);
        }
        
        String endpoint = CLAIMS_ENDPOINT + "/" + claimId + "/approve";
        Response response = executePostRequest(endpoint, approval);
        validateResponse(response, HTTP_OK);
        
        return parseResponse(response, ClaimApiModel.class);
    }
    
    /**
     * Deny claim
     */
    public ApiResponse<ClaimApiModel> denyClaim(String claimId, String denialReason) throws FrameworkException {
        LoggingUtil.logInfo("Denying claim: " + claimId + " with reason: " + denialReason);
        
        if (claimId == null || claimId.trim().isEmpty()) {
            throw new FrameworkException("Claim ID cannot be null or empty");
        }
        
        if (denialReason == null || denialReason.trim().isEmpty()) {
            throw new FrameworkException("Denial reason cannot be null or empty");
        }
        
        Map<String, String> denial = new HashMap<>();
        denial.put("reason", denialReason);
        
        String endpoint = CLAIMS_ENDPOINT + "/" + claimId + "/deny";
        Response response = executePostRequest(endpoint, denial);
        validateResponse(response, HTTP_OK);
        
        return parseResponse(response, ClaimApiModel.class);
    }
    
    /**
     * Settle claim
     */
    public ApiResponse<ClaimApiModel> settleClaim(String claimId, String settlementAmount, String settlementNotes) throws FrameworkException {
        LoggingUtil.logInfo("Settling claim: " + claimId + " for amount: " + settlementAmount);
        
        if (claimId == null || claimId.trim().isEmpty()) {
            throw new FrameworkException("Claim ID cannot be null or empty");
        }
        
        if (settlementAmount == null || settlementAmount.trim().isEmpty()) {
            throw new FrameworkException("Settlement amount cannot be null or empty");
        }
        
        Map<String, String> settlement = new HashMap<>();
        settlement.put("settlementAmount", settlementAmount);
        if (settlementNotes != null && !settlementNotes.trim().isEmpty()) {
            settlement.put("notes", settlementNotes);
        }
        
        String endpoint = CLAIMS_ENDPOINT + "/" + claimId + "/settle";
        Response response = executePostRequest(endpoint, settlement);
        validateResponse(response, HTTP_OK);
        
        return parseResponse(response, ClaimApiModel.class);
    }
    
    /**
     * Add note to claim
     */
    public ApiResponse<ClaimApiModel> addClaimNote(String claimId, String noteText) throws FrameworkException {
        LoggingUtil.logInfo("Adding note to claim: " + claimId);
        
        if (claimId == null || claimId.trim().isEmpty()) {
            throw new FrameworkException("Claim ID cannot be null or empty");
        }
        
        if (noteText == null || noteText.trim().isEmpty()) {
            throw new FrameworkException("Note text cannot be null or empty");
        }
        
        Map<String, String> note = new HashMap<>();
        note.put("text", noteText);
        
        String endpoint = CLAIMS_ENDPOINT + "/" + claimId + "/notes";
        Response response = executePostRequest(endpoint, note);
        validateResponse(response, HTTP_CREATED);
        
        return parseResponse(response, ClaimApiModel.class);
    }
    
    /**
     * Get claim notes
     */
    public ApiResponse<List<ClaimApiModel.ClaimNote>> getClaimNotes(String claimId) throws FrameworkException {
        LoggingUtil.logInfo("Fetching notes for claim: " + claimId);
        
        if (claimId == null || claimId.trim().isEmpty()) {
            throw new FrameworkException("Claim ID cannot be null or empty");
        }
        
        String endpoint = CLAIMS_ENDPOINT + "/" + claimId + "/notes";
        Response response = executeGetRequest(endpoint);
        validateResponse(response, HTTP_OK);
        
        return parseResponse(response, List.class);
    }
    
    /**
     * Upload claim document
     */
    public ApiResponse<ClaimApiModel> uploadClaimDocument(String claimId, String documentName, String documentType, String documentUrl) throws FrameworkException {
        LoggingUtil.logInfo("Uploading document to claim: " + claimId + ", Document: " + documentName);
        
        if (claimId == null || claimId.trim().isEmpty()) {
            throw new FrameworkException("Claim ID cannot be null or empty");
        }
        
        if (documentName == null || documentName.trim().isEmpty()) {
            throw new FrameworkException("Document name cannot be null or empty");
        }
        
        Map<String, String> document = new HashMap<>();
        document.put("name", documentName);
        if (documentType != null && !documentType.trim().isEmpty()) {
            document.put("type", documentType);
        }
        if (documentUrl != null && !documentUrl.trim().isEmpty()) {
            document.put("url", documentUrl);
        }
        
        String endpoint = CLAIMS_ENDPOINT + "/" + claimId + "/documents";
        Response response = executePostRequest(endpoint, document);
        validateResponse(response, HTTP_CREATED);
        
        return parseResponse(response, ClaimApiModel.class);
    }
    
    /**
     * Get claim documents
     */
    public ApiResponse<List<ClaimApiModel.ClaimDocument>> getClaimDocuments(String claimId) throws FrameworkException {
        LoggingUtil.logInfo("Fetching documents for claim: " + claimId);
        
        if (claimId == null || claimId.trim().isEmpty()) {
            throw new FrameworkException("Claim ID cannot be null or empty");
        }
        
        String endpoint = CLAIMS_ENDPOINT + "/" + claimId + "/documents";
        Response response = executeGetRequest(endpoint);
        validateResponse(response, HTTP_OK);
        
        return parseResponse(response, List.class);
    }
    
    /**
     * Get claim status history
     */
    public ApiResponse<List<ClaimApiModel.StatusChange>> getClaimStatusHistory(String claimId) throws FrameworkException {
        LoggingUtil.logInfo("Fetching status history for claim: " + claimId);
        
        if (claimId == null || claimId.trim().isEmpty()) {
            throw new FrameworkException("Claim ID cannot be null or empty");
        }
        
        String endpoint = CLAIMS_ENDPOINT + "/" + claimId + "/status/history";
        Response response = executeGetRequest(endpoint);
        validateResponse(response, HTTP_OK);
        
        return parseResponse(response, List.class);
    }
    
    /**
     * Get open claims count
     */
    public ApiResponse<Integer> getOpenClaimsCount() throws FrameworkException {
        LoggingUtil.logInfo("Fetching open claims count");
        
        Response response = executeGetRequest(CLAIMS_ENDPOINT + "/count/open");
        validateResponse(response, HTTP_OK);
        
        return parseResponse(response, Integer.class);
    }
    
    /**
     * Get claims statistics
     */
    public ApiResponse<Map<String, Object>> getClaimsStatistics() throws FrameworkException {
        LoggingUtil.logInfo("Fetching claims statistics");
        
        Response response = executeGetRequest(CLAIMS_ENDPOINT + "/statistics");
        validateResponse(response, HTTP_OK);
        
        return parseResponse(response, Map.class);
    }
    
    /**
     * Validate claim number availability
     */
    public ApiResponse<Boolean> isClaimNumberAvailable(String claimNumber) throws FrameworkException {
        LoggingUtil.logInfo("Checking claim number availability: " + claimNumber);
        
        if (claimNumber == null || claimNumber.trim().isEmpty()) {
            throw new FrameworkException("Claim number cannot be null or empty");
        }
        
        String endpoint = CLAIMS_ENDPOINT + "/validate/number?claimNumber=" + claimNumber;
        Response response = executeGetRequest(endpoint);
        validateResponse(response, HTTP_OK);
        
        return parseResponse(response, Boolean.class);
    }
    
    /**
     * Bulk create claims
     */
    public ApiResponse<List<ClaimApiModel>> createClaimsBulk(List<ClaimApiModel> claims) throws FrameworkException {
        LoggingUtil.logInfo("Creating claims in bulk - count: " + (claims != null ? claims.size() : 0));
        
        if (claims == null || claims.isEmpty()) {
            throw new FrameworkException("Claims list cannot be null or empty");
        }
        
        // Validate all claims before sending
        for (ClaimApiModel claim : claims) {
            claim.validate();
        }
        
        Response response = executePostRequest(CLAIMS_ENDPOINT + "/bulk", claims);
        validateResponse(response, HTTP_CREATED);
        
        return parseResponse(response, List.class);
    }
}
