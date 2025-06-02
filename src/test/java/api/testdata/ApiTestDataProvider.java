package api.testdata;

import api.models.PolicyApiModel;
import api.models.UserApiModel;
import api.models.ClaimApiModel;
import testdata.TestDataUtil;
import utils.LoggingUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * API Test Data Provider
 * 
 * Provides specialized test data for API testing scenarios
 * Includes realistic and edge case data for comprehensive API testing
 * 
 * @author Insurance Dashboard QA Framework
 * @version 1.0
 */
public class ApiTestDataProvider {
    
    private static ApiTestDataProvider instance;
    private final Random random;
    private final TestDataUtil testDataUtil;
    
    /**
     * Private constructor for singleton pattern
     */
    private ApiTestDataProvider() {
        this.random = new Random();
        this.testDataUtil = TestDataUtil.getInstance();
    }
    
    /**
     * Get singleton instance
     */
    public static synchronized ApiTestDataProvider getInstance() {
        if (instance == null) {
            instance = new ApiTestDataProvider();
        }
        return instance;
    }
    
    /**
     * Generate sample API policy for testing
     */
    public PolicyApiModel generateSampleApiPolicy() {
        LoggingUtil.logInfo("Generating sample API policy for testing");
        
        PolicyApiModel policy = new PolicyApiModel();
        
        // Generate unique policy number
        String policyNumber = "API-TEST-" + System.currentTimeMillis() + "-" + random.nextInt(1000);
        policy.setPolicyNumber(policyNumber);
        
        // Set basic policy details
        policy.setPolicyType(getRandomPolicyType());
        policy.setStatus(PolicyApiModel.STATUS_ACTIVE);
        policy.setCustomerName(generateRandomCustomerName());
        policy.setPremiumAmount(generateRandomPremiumAmount());
        policy.setDeductibleAmount(generateRandomDeductibleAmount());
        policy.setCoverageAmount(generateRandomCoverageAmount());
        policy.setStartDate(LocalDate.now());
        policy.setEndDate(LocalDate.now().plusYears(1));
        policy.setRenewalDate(LocalDate.now().plusYears(1).minusDays(30));
        policy.setDescription("API test policy - " + policy.getPolicyType());
        policy.setRiskLevel(getRandomRiskLevel());
        policy.setPaymentFrequency(getRandomPaymentFrequency());
        
        LoggingUtil.logInfo("Generated sample API policy: " + policyNumber);
        return policy;
    }
    
    /**
     * Generate sample API user for testing
     */
    public UserApiModel generateSampleApiUser() {
        LoggingUtil.logInfo("Generating sample API user for testing");
        
        UserApiModel user = new UserApiModel();
        
        // Generate unique username
        String username = "apiuser" + System.currentTimeMillis() + random.nextInt(1000);
        user.setUsername(username);
        
        // Set basic user details
        user.setEmail(username + "@apitest.com");
        user.setFirstName(generateRandomFirstName());
        user.setLastName(generateRandomLastName());
        user.setPassword("ApiTest123!");
        user.setRole(getRandomUserRole());
        user.setStatus(UserApiModel.STATUS_ACTIVE);
        user.setDepartment(getRandomDepartment());
        user.setPhoneNumber(generateRandomPhoneNumber());
        
        // Add some permissions
        user.addPermission(UserApiModel.PERM_READ_POLICIES);
        user.addPermission(UserApiModel.PERM_VIEW_REPORTS);
        
        LoggingUtil.logInfo("Generated sample API user: " + username);
        return user;
    }
    
    /**
     * Generate sample API claim for testing
     */
    public ClaimApiModel generateSampleApiClaim() {
        LoggingUtil.logInfo("Generating sample API claim for testing");
        
        ClaimApiModel claim = new ClaimApiModel();
        
        // Generate unique claim number
        String claimNumber = "CLAIM-API-" + System.currentTimeMillis() + "-" + random.nextInt(1000);
        claim.setClaimNumber(claimNumber);
        
        // Set basic claim details
        claim.setPolicyId("POLICY-" + random.nextInt(1000));
        claim.setClaimantName(generateRandomCustomerName());
        claim.setClaimType(getRandomClaimType());
        claim.setClaimedAmount(generateRandomClaimAmount());
        claim.setIncidentDate(LocalDate.now().minusDays(random.nextInt(30)));
        claim.setDescription("API test claim - " + claim.getClaimType());
        claim.setIncidentLocation(generateRandomLocation());
        claim.setPriority(getRandomPriority());
        
        LoggingUtil.logInfo("Generated sample API claim: " + claimNumber);
        return claim;
    }
    
    /**
     * Generate multiple sample policies for bulk testing
     */
    public List<PolicyApiModel> generateMultipleSamplePolicies(int count) {
        LoggingUtil.logInfo("Generating " + count + " sample policies for bulk testing");
        
        List<PolicyApiModel> policies = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            policies.add(generateSampleApiPolicy());
        }
        
        LoggingUtil.logInfo("Generated " + count + " sample policies");
        return policies;
    }
    
    /**
     * Generate multiple sample users for bulk testing
     */
    public List<UserApiModel> generateMultipleSampleUsers(int count) {
        LoggingUtil.logInfo("Generating " + count + " sample users for bulk testing");
        
        List<UserApiModel> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            users.add(generateSampleApiUser());
        }
        
        LoggingUtil.logInfo("Generated " + count + " sample users");
        return users;
    }
    
    /**
     * Generate multiple sample claims for bulk testing
     */
    public List<ClaimApiModel> generateMultipleSampleClaims(int count) {
        LoggingUtil.logInfo("Generating " + count + " sample claims for bulk testing");
        
        List<ClaimApiModel> claims = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            claims.add(generateSampleApiClaim());
        }
        
        LoggingUtil.logInfo("Generated " + count + " sample claims");
        return claims;
    }
    
    /**
     * Generate invalid policy for negative testing
     */
    public PolicyApiModel generateInvalidPolicy() {
        LoggingUtil.logInfo("Generating invalid policy for negative testing");
        
        PolicyApiModel policy = new PolicyApiModel();
        
        // Set invalid data intentionally
        policy.setPolicyNumber(""); // Empty policy number
        policy.setPolicyType("INVALID_TYPE");
        policy.setStatus("INVALID_STATUS");
        policy.setCustomerName(""); // Empty customer name
        policy.setPremiumAmount(new BigDecimal("-100")); // Negative premium
        
        LoggingUtil.logInfo("Generated invalid policy for negative testing");
        return policy;
    }
    
    /**
     * Generate invalid user for negative testing
     */
    public UserApiModel generateInvalidUser() {
        LoggingUtil.logInfo("Generating invalid user for negative testing");
        
        UserApiModel user = new UserApiModel();
        
        // Set invalid data intentionally
        user.setUsername(""); // Empty username
        user.setEmail("invalid-email"); // Invalid email format
        user.setFirstName(""); // Empty first name
        user.setLastName(""); // Empty last name
        user.setPassword("123"); // Weak password
        user.setRole("INVALID_ROLE");
        user.setStatus("INVALID_STATUS");
        
        LoggingUtil.logInfo("Generated invalid user for negative testing");
        return user;
    }
    
    // Helper methods for generating random data
    private String getRandomPolicyType() {
        String[] types = {
            PolicyApiModel.TYPE_AUTO,
            PolicyApiModel.TYPE_HOME,
            PolicyApiModel.TYPE_LIFE,
            PolicyApiModel.TYPE_HEALTH,
            PolicyApiModel.TYPE_BUSINESS
        };
        return types[random.nextInt(types.length)];
    }
    
    private String getRandomUserRole() {
        String[] roles = {
            UserApiModel.ROLE_AGENT,
            UserApiModel.ROLE_MANAGER,
            UserApiModel.ROLE_UNDERWRITER,
            UserApiModel.ROLE_CLAIMS_ADJUSTER
        };
        return roles[random.nextInt(roles.length)];
    }
    
    private String getRandomDepartment() {
        String[] departments = {
            UserApiModel.DEPT_SALES,
            UserApiModel.DEPT_UNDERWRITING,
            UserApiModel.DEPT_CLAIMS,
            UserApiModel.DEPT_CUSTOMER_SERVICE
        };
        return departments[random.nextInt(departments.length)];
    }
    
    private String getRandomClaimType() {
        String[] types = {
            ClaimApiModel.TYPE_AUTO_COLLISION,
            ClaimApiModel.TYPE_AUTO_COMPREHENSIVE,
            ClaimApiModel.TYPE_HOME_FIRE,
            ClaimApiModel.TYPE_HOME_THEFT,
            ClaimApiModel.TYPE_MEDICAL
        };
        return types[random.nextInt(types.length)];
    }
    
    private String getRandomRiskLevel() {
        String[] levels = {
            PolicyApiModel.RISK_LOW,
            PolicyApiModel.RISK_MEDIUM,
            PolicyApiModel.RISK_HIGH
        };
        return levels[random.nextInt(levels.length)];
    }
    
    private String getRandomPaymentFrequency() {
        String[] frequencies = {
            PolicyApiModel.FREQUENCY_MONTHLY,
            PolicyApiModel.FREQUENCY_QUARTERLY,
            PolicyApiModel.FREQUENCY_ANNUAL
        };
        return frequencies[random.nextInt(frequencies.length)];
    }
    
    private String getRandomPriority() {
        String[] priorities = {
            ClaimApiModel.PRIORITY_LOW,
            ClaimApiModel.PRIORITY_MEDIUM,
            ClaimApiModel.PRIORITY_HIGH
        };
        return priorities[random.nextInt(priorities.length)];
    }
    
    private String generateRandomCustomerName() {
        String[] firstNames = {"John", "Jane", "Mike", "Sarah", "David", "Lisa", "Bob", "Alice", "Tom", "Emma"};
        String[] lastNames = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez"};
        
        return firstNames[random.nextInt(firstNames.length)] + " " + 
               lastNames[random.nextInt(lastNames.length)];
    }
    
    private String generateRandomFirstName() {
        String[] names = {"John", "Jane", "Mike", "Sarah", "David", "Lisa", "Bob", "Alice", "Tom", "Emma"};
        return names[random.nextInt(names.length)];
    }
    
    private String generateRandomLastName() {
        String[] names = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez"};
        return names[random.nextInt(names.length)];
    }
    
    private String generateRandomPhoneNumber() {
        return String.format("+1%03d%03d%04d", 
                           random.nextInt(800) + 200,
                           random.nextInt(800) + 200,
                           random.nextInt(9000) + 1000);
    }
    
    private String generateRandomLocation() {
        String[] cities = {"New York", "Los Angeles", "Chicago", "Houston", "Phoenix", "Philadelphia", "San Antonio", "San Diego", "Dallas", "San Jose"};
        String[] states = {"NY", "CA", "IL", "TX", "AZ", "PA", "TX", "CA", "TX", "CA"};
        
        int index = random.nextInt(cities.length);
        return cities[index] + ", " + states[index];
    }
    
    private BigDecimal generateRandomPremiumAmount() {
        // Generate premium between $500 and $5000
        double amount = 500 + (random.nextDouble() * 4500);
        return new BigDecimal(String.format("%.2f", amount));
    }
    
    private BigDecimal generateRandomDeductibleAmount() {
        // Generate deductible between $250 and $2000
        double amount = 250 + (random.nextDouble() * 1750);
        return new BigDecimal(String.format("%.2f", amount));
    }
    
    private BigDecimal generateRandomCoverageAmount() {
        // Generate coverage between $10,000 and $100,000
        double amount = 10000 + (random.nextDouble() * 90000);
        return new BigDecimal(String.format("%.2f", amount));
    }
    
    private BigDecimal generateRandomClaimAmount() {
        // Generate claim amount between $100 and $50,000
        double amount = 100 + (random.nextDouble() * 49900);
        return new BigDecimal(String.format("%.2f", amount));
    }
}
