package testdata;

import testdata.providers.TestDataManager;
import testdata.providers.UserDataProvider;
import testdata.providers.PolicyDataProvider;
import testdata.factories.TestDataFactory;
import testdata.models.UserModel;
import testdata.models.PolicyModel;
import utils.ErrorHandler;
import utils.LoggingUtil;
import org.slf4j.Logger;

/**
 * Centralized test data utility providing easy access to all test data functionality
 * This is the main entry point for test data operations in the framework
 */
public class TestDataUtil {
    
    private static final Logger logger = LoggingUtil.getLogger(TestDataUtil.class);
    private static TestDataUtil instance;
    
    private final TestDataManager dataManager;
    private final UserDataProvider userProvider;
    private final PolicyDataProvider policyProvider;
    private boolean isInitialized = false;
    
    private TestDataUtil() {
        this.dataManager = TestDataManager.getInstance();
        this.userProvider = new UserDataProvider();
        this.policyProvider = new PolicyDataProvider();
    }
    
    /**
     * Get singleton instance of TestDataUtil
     */
    public static synchronized TestDataUtil getInstance() {
        if (instance == null) {
            instance = new TestDataUtil();
        }
        return instance;
    }
    
    /**
     * Initialize test data from all sources
     */
    public void initializeTestData() {
        if (isInitialized) {
            logger.debug("Test data already initialized");
            return;
        }
        
        try {
            logger.info("Initializing test data from all sources...");
            
            // Load from JSON files if they exist
            loadTestDataFromFiles();
            
            // Initialize default data (already done in providers constructors)
            logger.info("Default test data initialized by providers");
            
            // Log statistics
            logTestDataStatistics();
            
            isInitialized = true;
            logger.info("Test data initialization completed successfully");
            
        } catch (Exception e) {
            logger.error("Failed to initialize test data", e);
            ErrorHandler.handleTestDataError("ALL", "initialization", "Test data initialization failed");
        }
    }
    
    /**
     * Load test data from JSON files
     */
    private void loadTestDataFromFiles() {
        try {
            // Load users from JSON
            userProvider.loadFromJson("testdata/users.json");
            logger.debug("Loaded users from JSON file");
        } catch (Exception e) {
            logger.warn("Could not load users from JSON file: {}", e.getMessage());
        }
        
        try {
            // Load policies from JSON
            policyProvider.loadFromJson("testdata/policies.json");
            logger.debug("Loaded policies from JSON file");
        } catch (Exception e) {
            logger.warn("Could not load policies from JSON file: {}", e.getMessage());
        }
    }
    
    /**
     * Log test data statistics
     */
    private void logTestDataStatistics() {
        try {
            UserDataProvider.UserStatistics userStats = userProvider.getStatistics();
            PolicyDataProvider.PolicyStatistics policyStats = policyProvider.getStatistics();
            
            logger.info("Test Data Statistics:");
            logger.info("  Users: {}", userStats.toString());
            logger.info("  Policies: {}", policyStats.toString());
            
        } catch (Exception e) {
            logger.warn("Failed to log test data statistics: {}", e.getMessage());
        }
    }
    
    // ==================== USER DATA METHODS ====================
    
    /**
     * Get user data provider
     */
    public UserDataProvider users() {
        return userProvider;
    }
    
    /**
     * Quick access methods for common user operations
     */
    public UserModel getValidUser() {
        UserModel user = userProvider.getRandomValidUser();
        if (user == null) {
            logger.warn("No valid users available, generating one");
            user = TestDataFactory.generateValidUser();
            userProvider.updateUser(user);
        }
        return user;
    }
    
    public UserModel getInvalidUser() {
        UserModel user = userProvider.getRandomInvalidUser();
        if (user == null) {
            logger.warn("No invalid users available, generating one");
            user = TestDataFactory.generateInvalidUser();
            userProvider.updateUser(user);
        }
        return user;
    }
    
    public UserModel getAdminUser() {
        return userProvider.getUsersByRole("ADMIN").stream()
                .filter(UserModel::canLogin)
                .findFirst()
                .orElseGet(() -> {
                    logger.warn("No admin users available, generating one");
                    UserModel admin = TestDataFactory.generateAdminUser();
                    userProvider.updateUser(admin);
                    return admin;
                });
    }
    
    public UserModel getLockedUser() {
        return userProvider.getLockedUsers().stream()
                .findFirst()
                .orElseGet(() -> {
                    logger.warn("No locked users available, generating one");
                    UserModel locked = TestDataFactory.generateLockedUser();
                    userProvider.updateUser(locked);
                    return locked;
                });
    }
    
    public UserModel getUserById(String userId) {
        return userProvider.getUser(userId);
    }
    
    public UserModel createTestUser(String role, boolean isValid) {
        UserModel user = TestDataFactory.generateUserWithConstraints(isValid, "ADMIN".equals(role), false);
        userProvider.updateUser(user);
        return user;
    }
    
    // ==================== POLICY DATA METHODS ====================
    
    /**
     * Get policy data provider
     */
    public PolicyDataProvider policies() {
        return policyProvider;
    }
    
    /**
     * Quick access methods for common policy operations
     */
    public PolicyModel getActivePolicy() {
        PolicyModel policy = policyProvider.getRandomActivePolicy();
        if (policy == null) {
            logger.warn("No active policies available, generating one");
            policy = TestDataFactory.generateActivePolicy();
            policyProvider.updatePolicy(policy);
        }
        return policy;
    }
    
    public PolicyModel getExpiredPolicy() {
        return policyProvider.getExpiredPolicies().stream()
                .findFirst()
                .orElseGet(() -> {
                    logger.warn("No expired policies available, generating one");
                    PolicyModel expired = TestDataFactory.generateExpiredPolicy();
                    policyProvider.updatePolicy(expired);
                    return expired;
                });
    }
    
    public PolicyModel getPendingPolicy() {
        return policyProvider.getPendingPolicies().stream()
                .findFirst()
                .orElseGet(() -> {
                    logger.warn("No pending policies available, generating one");
                    PolicyModel pending = TestDataFactory.generatePendingPolicy();
                    policyProvider.updatePolicy(pending);
                    return pending;
                });
    }
    
    public PolicyModel getPolicyById(String policyId) {
        return policyProvider.getPolicy(policyId);
    }
    
    public PolicyModel getPolicyByType(String type) {
        PolicyModel policy = policyProvider.getRandomPolicyByType(type);
        if (policy == null) {
            logger.warn("No {} policies available, generating one", type);
            policy = TestDataFactory.generateRandomPolicy(type);
            policyProvider.updatePolicy(policy);
        }
        return policy;
    }
    
    public PolicyModel getAutoPolicy() {
        return getPolicyByType("Auto Insurance");
    }
    
    public PolicyModel getHomePolicy() {
        return getPolicyByType("Home Insurance");
    }
    
    public PolicyModel getLifePolicy() {
        return getPolicyByType("Life Insurance");
    }
    
    public PolicyModel getHealthPolicy() {
        return getPolicyByType("Health Insurance");
    }
    
    public PolicyModel getPolicyWithClaims() {
        return policyProvider.getPoliciesWithClaims().stream()
                .findFirst()
                .orElseGet(() -> {
                    logger.warn("No policies with claims available, creating one");
                    PolicyModel policy = TestDataFactory.generateActivePolicy();
                    policy.addClaimId("CLM_TEST_001");
                    policy.addClaimId("CLM_TEST_002");
                    policyProvider.updatePolicy(policy);
                    return policy;
                });
    }
    
    public PolicyModel createTestPolicy(String type, String status) {
        PolicyModel policy = TestDataFactory.generatePolicyWithConstraints(type, status, null, null);
        policyProvider.updatePolicy(policy);
        return policy;
    }
    
    // ==================== BULK DATA OPERATIONS ====================
    
    /**
     * Generate bulk test data for load testing
     */
    public void generateBulkTestData(int userCount, int policyCount) {
        try {
            logger.info("Generating bulk test data - Users: {}, Policies: {}", userCount, policyCount);
            
            // Generate users
            UserModel[] users = TestDataFactory.generateMultipleUsers(userCount);
            for (UserModel user : users) {
                userProvider.updateUser(user);
            }
            
            // Generate policies
            PolicyModel[] policies = TestDataFactory.generateMultiplePolicies(policyCount);
            for (PolicyModel policy : policies) {
                policyProvider.updatePolicy(policy);
            }
            
            logger.info("Bulk test data generation completed");
            logTestDataStatistics();
            
        } catch (Exception e) {
            logger.error("Failed to generate bulk test data", e);
            ErrorHandler.handleTestDataError("BULK", "generation", 
                String.format("Failed to generate %d users and %d policies", userCount, policyCount));
        }
    }
    
    /**
     * Generate test data for specific scenario
     */
    public void generateScenarioData(String scenarioName) {
        try {
            logger.info("Generating test data for scenario: {}", scenarioName);
            
            switch (scenarioName.toLowerCase()) {
                case "login_testing":
                    generateLoginTestingData();
                    break;
                case "policy_management":
                    generatePolicyManagementData();
                    break;
                case "user_roles":
                    generateUserRoleTestingData();
                    break;
                case "policy_lifecycle":
                    generatePolicyLifecycleData();
                    break;
                default:
                    logger.warn("Unknown scenario: {}. Generating default test data", scenarioName);
                    generateDefaultScenarioData();
                    break;
            }
            
            logger.info("Scenario test data generation completed for: {}", scenarioName);
            
        } catch (Exception e) {
            logger.error("Failed to generate scenario data for: {}", scenarioName, e);
            ErrorHandler.handleTestDataError("SCENARIO", "generation", 
                "Failed to generate data for scenario: " + scenarioName);
        }
    }
    
    private void generateLoginTestingData() {
        // Generate users for login testing
        userProvider.updateUser(TestDataFactory.generateValidUser());
        userProvider.updateUser(TestDataFactory.generateInvalidUser());
        userProvider.updateUser(TestDataFactory.generateLockedUser());
        userProvider.updateUser(TestDataFactory.generateAdminUser());
        
        logger.debug("Generated login testing data");
    }
    
    private void generatePolicyManagementData() {
        // Generate policies for management testing
        policyProvider.updatePolicy(TestDataFactory.generateActivePolicy());
        policyProvider.updatePolicy(TestDataFactory.generateExpiredPolicy());
        policyProvider.updatePolicy(TestDataFactory.generatePendingPolicy());
        policyProvider.updatePolicy(TestDataFactory.generateAutoPolicy());
        policyProvider.updatePolicy(TestDataFactory.generateHomePolicy());
        
        logger.debug("Generated policy management data");
    }
    
    private void generateUserRoleTestingData() {
        // Generate users with different roles
        UserModel[] users = TestDataFactory.generateUsersWithRoles(5, 2);
        for (UserModel user : users) {
            userProvider.updateUser(user);
        }
        
        logger.debug("Generated user role testing data");
    }
    
    private void generatePolicyLifecycleData() {
        // Generate policies in different states
        policyProvider.updatePolicy(TestDataFactory.generatePendingPolicy());
        policyProvider.updatePolicy(TestDataFactory.generateActivePolicy());
        policyProvider.updatePolicy(TestDataFactory.generateExpiredPolicy());
        
        PolicyModel cancelled = TestDataFactory.generateRandomPolicy();
        cancelled.setStatus("CANCELLED");
        policyProvider.updatePolicy(cancelled);
        
        logger.debug("Generated policy lifecycle data");
    }
    
    private void generateDefaultScenarioData() {
        generateLoginTestingData();
        generatePolicyManagementData();
    }
    
    // ==================== DATA MANAGEMENT METHODS ====================
    
    /**
     * Clear all test data
     */
    public void clearAllTestData() {
        try {
            logger.info("Clearing all test data...");
            dataManager.clearAllTestData();
            isInitialized = false;
            logger.info("All test data cleared");
        } catch (Exception e) {
            logger.error("Failed to clear test data", e);
            ErrorHandler.handleTestDataError("ALL", "clear", "Failed to clear all test data");
        }
    }
    
    /**
     * Reset test data to initial state
     */
    public void resetTestData() {
        try {
            logger.info("Resetting test data to initial state...");
            clearAllTestData();
            initializeTestData();
            logger.info("Test data reset completed");
        } catch (Exception e) {
            logger.error("Failed to reset test data", e);
            ErrorHandler.handleTestDataError("ALL", "reset", "Failed to reset test data");
        }
    }
    
    /**
     * Validate all test data
     */
    public boolean validateAllTestData() {
        try {
            logger.info("Validating all test data...");
            
            java.util.List<String> userErrors = userProvider.validateAllUsers();
            java.util.List<String> policyErrors = policyProvider.validateAllPolicies();
            
            boolean isValid = userErrors.isEmpty() && policyErrors.isEmpty();
            
            if (isValid) {
                logger.info("All test data validation passed");
            } else {
                logger.warn("Test data validation failed - User errors: {}, Policy errors: {}", 
                           userErrors.size(), policyErrors.size());
                
                userErrors.forEach(error -> logger.warn("User validation error: {}", error));
                policyErrors.forEach(error -> logger.warn("Policy validation error: {}", error));
            }
            
            return isValid;
            
        } catch (Exception e) {
            logger.error("Failed to validate test data", e);
            return false;
        }
    }
    
    /**
     * Export all test data to JSON
     */
    public String exportAllTestData() {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("{\n");
            sb.append("  \"users\": ").append(userProvider.exportToJson()).append(",\n");
            sb.append("  \"policies\": ").append(policyProvider.exportToJson()).append("\n");
            sb.append("}");
            
            logger.info("Exported all test data to JSON");
            return sb.toString();
            
        } catch (Exception e) {
            logger.error("Failed to export test data", e);
            ErrorHandler.handleTestDataError("ALL", "export", "Failed to export test data to JSON");
            return "{}";
        }
    }
    
    /**
     * Get test data statistics
     */
    public String getTestDataStatistics() {
        try {
            UserDataProvider.UserStatistics userStats = userProvider.getStatistics();
            PolicyDataProvider.PolicyStatistics policyStats = policyProvider.getStatistics();
            
            return String.format("Test Data Statistics:\n%s\n%s", 
                                userStats.toString(), policyStats.toString());
                                
        } catch (Exception e) {
            logger.error("Failed to get test data statistics", e);
            return "Statistics unavailable";
        }
    }
    
    /**
     * Check if test data is initialized
     */
    public boolean isInitialized() {
        return isInitialized;
    }
    
    /**
     * Get the underlying test data manager (for advanced operations)
     */
    public TestDataManager getDataManager() {
        return dataManager;
    }
}
