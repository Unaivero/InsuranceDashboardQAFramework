package testdata.providers;

import testdata.models.PolicyModel;
import utils.ErrorHandler;
import utils.LoggingUtil;
import org.slf4j.Logger;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Predicate;

/**
 * Policy-specific test data provider with convenience methods
 */
public class PolicyDataProvider {
    
    private static final Logger logger = LoggingUtil.getLogger(PolicyDataProvider.class);
    private static final String DATA_TYPE = "policies";
    private final TestDataManager dataManager;
    
    public PolicyDataProvider() {
        this.dataManager = TestDataManager.getInstance();
        initializeDefaultPolicies();
    }
    
    /**
     * Initialize default policy test data
     */
    private void initializeDefaultPolicies() {
        try {
            // Active Auto Policy
            PolicyModel autoPolicy = new PolicyModel("POL12345", "AUTO-2024-001", "ACTIVE", "Auto Insurance");
            autoPolicy.setCategory("Personal Vehicle");
            autoPolicy.setPremiumAmount(new BigDecimal("1200.00"));
            autoPolicy.setCoverageAmount(new BigDecimal("50000.00"));
            autoPolicy.setDeductible(new BigDecimal("500.00"));
            autoPolicy.setEffectiveDate("01/15/2024");
            autoPolicy.setExpirationDate("01/15/2025");
            autoPolicy.setInsuredName("John Doe");
            autoPolicy.setAgentId("AGT001");
            autoPolicy.setAutoRenewal(true);
            autoPolicy.setDescription("Active auto insurance policy for testing");
            autoPolicy.addClaimId("CLM001");
            autoPolicy.addClaimId("CLM002");
            autoPolicy.addDocumentId("DOC001");
            autoPolicy.addDocumentId("DOC002");
            dataManager.addTestData(DATA_TYPE, autoPolicy);
            
            // Expired Home Policy
            PolicyModel homePolicy = new PolicyModel("POL67890", "HOME-2023-045", "EXPIRED", "Home Insurance");
            homePolicy.setCategory("Homeowners");
            homePolicy.setPremiumAmount(new BigDecimal("800.00"));
            homePolicy.setCoverageAmount(new BigDecimal("250000.00"));
            homePolicy.setDeductible(new BigDecimal("1000.00"));
            homePolicy.setEffectiveDate("03/01/2023");
            homePolicy.setExpirationDate("03/01/2024");
            homePolicy.setInsuredName("Jane Smith");
            homePolicy.setAgentId("AGT002");
            homePolicy.setAutoRenewal(false);
            homePolicy.setDescription("Expired home insurance policy for testing");
            homePolicy.addBeneficiary("Robert Smith (Spouse)");
            homePolicy.addBeneficiary("Emily Smith (Daughter)");
            homePolicy.addDocumentId("DOC003");
            dataManager.addTestData(DATA_TYPE, homePolicy);
            
            // Pending Life Policy
            PolicyModel lifePolicy = new PolicyModel("POL11111", "LIFE-2024-012", "PENDING", "Life Insurance");
            lifePolicy.setCategory("Term Life");
            lifePolicy.setPremiumAmount(new BigDecimal("2400.00"));
            lifePolicy.setCoverageAmount(new BigDecimal("500000.00"));
            lifePolicy.setDeductible(new BigDecimal("0.00"));
            lifePolicy.setEffectiveDate("06/01/2024");
            lifePolicy.setExpirationDate("06/01/2025");
            lifePolicy.setInsuredName("Michael Johnson");
            lifePolicy.setAgentId("AGT003");
            lifePolicy.setAutoRenewal(true);
            lifePolicy.setDescription("Pending life insurance policy for testing");
            lifePolicy.addBeneficiary("Jennifer Johnson (Spouse) - 70%");
            lifePolicy.addBeneficiary("David Johnson (Son) - 15%");
            lifePolicy.addBeneficiary("Sarah Johnson (Daughter) - 15%");
            lifePolicy.addDocumentId("DOC004");
            lifePolicy.addDocumentId("DOC005");
            dataManager.addTestData(DATA_TYPE, lifePolicy);
            
            // Active Health Policy
            PolicyModel healthPolicy = new PolicyModel("POL99999", "HEALTH-2024-089", "ACTIVE", "Health Insurance");
            healthPolicy.setCategory("Family Plan");
            healthPolicy.setPremiumAmount(new BigDecimal("3600.00"));
            healthPolicy.setCoverageAmount(new BigDecimal("100000.00"));
            healthPolicy.setDeductible(new BigDecimal("2000.00"));
            healthPolicy.setEffectiveDate("02/15/2024");
            healthPolicy.setExpirationDate("02/15/2025");
            healthPolicy.setInsuredName("Sarah Wilson");
            healthPolicy.setAgentId("AGT004");
            healthPolicy.setAutoRenewal(true);
            healthPolicy.setDescription("Active health insurance policy for testing");
            healthPolicy.addClaimId("CLM003");
            healthPolicy.addClaimId("CLM004");
            healthPolicy.addClaimId("CLM005");
            healthPolicy.addDocumentId("DOC006");
            healthPolicy.addDocumentId("DOC007");
            dataManager.addTestData(DATA_TYPE, healthPolicy);
            
            // Cancelled Business Policy
            PolicyModel businessPolicy = new PolicyModel("POL55555", "BIZ-2024-001", "CANCELLED", "Business Insurance");
            businessPolicy.setCategory("General Liability");
            businessPolicy.setPremiumAmount(new BigDecimal("5000.00"));
            businessPolicy.setCoverageAmount(new BigDecimal("1000000.00"));
            businessPolicy.setDeductible(new BigDecimal("5000.00"));
            businessPolicy.setEffectiveDate("01/01/2024");
            businessPolicy.setExpirationDate("01/01/2025");
            businessPolicy.setInsuredName("ABC Company LLC");
            businessPolicy.setAgentId("AGT005");
            businessPolicy.setAutoRenewal(false);
            businessPolicy.setDescription("Cancelled business insurance policy for testing");
            businessPolicy.addDocumentId("DOC008");
            dataManager.addTestData(DATA_TYPE, businessPolicy);
            
            logger.info("Initialized default policy test data");
            
        } catch (Exception e) {
            logger.error("Failed to initialize default policy test data", e);
        }
    }
    
    /**
     * Get policy by ID
     */
    public PolicyModel getPolicy(String policyId) {
        return dataManager.getTestData(DATA_TYPE, policyId, PolicyModel.class);
    }
    
    /**
     * Get all policies
     */
    public List<PolicyModel> getAllPolicies() {
        return dataManager.getAllTestData(DATA_TYPE, PolicyModel.class);
    }
    
    /**
     * Get active policies only
     */
    public List<PolicyModel> getActivePolicies() {
        return dataManager.getTestDataWhere(DATA_TYPE, PolicyModel.class, PolicyModel::isActive);
    }
    
    /**
     * Get expired policies only
     */
    public List<PolicyModel> getExpiredPolicies() {
        return dataManager.getTestDataWhere(DATA_TYPE, PolicyModel.class, PolicyModel::isExpired);
    }
    
    /**
     * Get pending policies only
     */
    public List<PolicyModel> getPendingPolicies() {
        return dataManager.getTestDataWhere(DATA_TYPE, PolicyModel.class, PolicyModel::isPending);
    }
    
    /**
     * Get cancelled policies only
     */
    public List<PolicyModel> getCancelledPolicies() {
        return dataManager.getTestDataWhere(DATA_TYPE, PolicyModel.class, PolicyModel::isCancelled);
    }
    
    /**
     * Get policies by status
     */
    public List<PolicyModel> getPoliciesByStatus(String status) {
        ErrorHandler.validateNotEmpty(status, "status");
        return dataManager.getTestDataWhere(DATA_TYPE, PolicyModel.class, 
            policy -> status.equalsIgnoreCase(policy.getStatus()));
    }
    
    /**
     * Get policies by type
     */
    public List<PolicyModel> getPoliciesByType(String type) {
        ErrorHandler.validateNotEmpty(type, "type");
        return dataManager.getTestDataWhere(DATA_TYPE, PolicyModel.class, 
            policy -> type.equalsIgnoreCase(policy.getType()));
    }
    
    /**
     * Get policies by category
     */
    public List<PolicyModel> getPoliciesByCategory(String category) {
        ErrorHandler.validateNotEmpty(category, "category");
        return dataManager.getTestDataWhere(DATA_TYPE, PolicyModel.class, 
            policy -> category.equalsIgnoreCase(policy.getCategory()));
    }
    
    /**
     * Get policies by insured name
     */
    public List<PolicyModel> getPoliciesByInsuredName(String insuredName) {
        ErrorHandler.validateNotEmpty(insuredName, "insured name");
        return dataManager.getTestDataWhere(DATA_TYPE, PolicyModel.class, 
            policy -> insuredName.equalsIgnoreCase(policy.getInsuredName()));
    }
    
    /**
     * Get policies with claims history
     */
    public List<PolicyModel> getPoliciesWithClaims() {
        return dataManager.getTestDataWhere(DATA_TYPE, PolicyModel.class, PolicyModel::hasClaimsHistory);
    }
    
    /**
     * Get policies with beneficiaries
     */
    public List<PolicyModel> getPoliciesWithBeneficiaries() {
        return dataManager.getTestDataWhere(DATA_TYPE, PolicyModel.class, PolicyModel::hasBeneficiaries);
    }
    
    /**
     * Get policies with expiry warning
     */
    public List<PolicyModel> getPoliciesWithExpiryWarning() {
        return dataManager.getTestDataWhere(DATA_TYPE, PolicyModel.class, PolicyModel::hasExpiryWarning);
    }
    
    /**
     * Get policies with auto renewal
     */
    public List<PolicyModel> getAutoRenewalPolicies() {
        return dataManager.getTestDataWhere(DATA_TYPE, PolicyModel.class, PolicyModel::isAutoRenewal);
    }
    
    /**
     * Get policies by premium range
     */
    public List<PolicyModel> getPoliciesByPremiumRange(BigDecimal minPremium, BigDecimal maxPremium) {
        ErrorHandler.validateNotNull(minPremium, "minimum premium");
        ErrorHandler.validateNotNull(maxPremium, "maximum premium");
        
        return dataManager.getTestDataWhere(DATA_TYPE, PolicyModel.class, policy -> {
            BigDecimal premium = policy.getPremiumAmount();
            return premium != null && 
                   premium.compareTo(minPremium) >= 0 && 
                   premium.compareTo(maxPremium) <= 0;
        });
    }
    
    /**
     * Get a random active policy
     */
    public PolicyModel getRandomActivePolicy() {
        List<PolicyModel> activePolicies = getActivePolicies();
        if (activePolicies.isEmpty()) {
            logger.warn("No active policies available for random selection");
            return null;
        }
        return activePolicies.get((int) (Math.random() * activePolicies.size()));
    }
    
    /**
     * Get a random policy by type
     */
    public PolicyModel getRandomPolicyByType(String type) {
        List<PolicyModel> typePolicies = getPoliciesByType(type);
        if (typePolicies.isEmpty()) {
            logger.warn("No policies of type '{}' available for random selection", type);
            return null;
        }
        return typePolicies.get((int) (Math.random() * typePolicies.size()));
    }
    
    /**
     * Create a new policy for testing
     */
    public PolicyModel createPolicy(String id, String policyNumber, String status, String type, String insuredName) {
        PolicyModel policy = new PolicyModel(id, policyNumber, status, type);
        policy.setInsuredName(insuredName);
        policy.setDescription("Dynamically created test policy");
        
        dataManager.addTestData(DATA_TYPE, policy);
        logger.debug("Created new test policy: {}", policy.getSummary());
        return policy;
    }
    
    /**
     * Create a copy of existing policy for test isolation
     */
    public PolicyModel copyPolicy(String policyId) {
        return dataManager.copyTestData(DATA_TYPE, policyId, PolicyModel.class);
    }
    
    /**
     * Update policy data
     */
    public void updatePolicy(PolicyModel policy) {
        dataManager.updateTestData(DATA_TYPE, policy);
    }
    
    /**
     * Remove policy
     */
    public void removePolicy(String policyId) {
        dataManager.removeTestData(DATA_TYPE, policyId);
    }
    
    /**
     * Get policies with custom filter
     */
    public List<PolicyModel> getPoliciesWhere(Predicate<PolicyModel> filter) {
        return dataManager.getTestDataWhere(DATA_TYPE, PolicyModel.class, filter);
    }
    
    /**
     * Load policies from JSON file
     */
    public void loadFromJson(String filePath) {
        dataManager.loadFromJson(DATA_TYPE, filePath, PolicyModel.class);
    }
    
    /**
     * Export policies to JSON
     */
    public String exportToJson() {
        return dataManager.exportToJson(DATA_TYPE);
    }
    
    /**
     * Validate all policy data
     */
    public List<String> validateAllPolicies() {
        return dataManager.validateTestData(DATA_TYPE);
    }
    
    /**
     * Clear all policy data
     */
    public void clearAllPolicies() {
        dataManager.clearTestData(DATA_TYPE);
    }
    
    /**
     * Get policy statistics
     */
    public PolicyStatistics getStatistics() {
        List<PolicyModel> allPolicies = getAllPolicies();
        
        long activeCount = allPolicies.stream().filter(PolicyModel::isActive).count();
        long expiredCount = allPolicies.stream().filter(PolicyModel::isExpired).count();
        long pendingCount = allPolicies.stream().filter(PolicyModel::isPending).count();
        long cancelledCount = allPolicies.stream().filter(PolicyModel::isCancelled).count();
        long withClaimsCount = allPolicies.stream().filter(PolicyModel::hasClaimsHistory).count();
        long autoRenewalCount = allPolicies.stream().filter(PolicyModel::isAutoRenewal).count();
        
        BigDecimal totalPremiums = allPolicies.stream()
            .filter(p -> p.getPremiumAmount() != null)
            .map(PolicyModel::getPremiumAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return new PolicyStatistics(allPolicies.size(), activeCount, expiredCount, pendingCount, 
                                   cancelledCount, withClaimsCount, autoRenewalCount, totalPremiums);
    }
    
    /**
     * Policy statistics class
     */
    public static class PolicyStatistics {
        public final int totalPolicies;
        public final long activePolicies;
        public final long expiredPolicies;
        public final long pendingPolicies;
        public final long cancelledPolicies;
        public final long policiesWithClaims;
        public final long autoRenewalPolicies;
        public final BigDecimal totalPremiums;
        
        public PolicyStatistics(int totalPolicies, long activePolicies, long expiredPolicies, 
                               long pendingPolicies, long cancelledPolicies, long policiesWithClaims, 
                               long autoRenewalPolicies, BigDecimal totalPremiums) {
            this.totalPolicies = totalPolicies;
            this.activePolicies = activePolicies;
            this.expiredPolicies = expiredPolicies;
            this.pendingPolicies = pendingPolicies;
            this.cancelledPolicies = cancelledPolicies;
            this.policiesWithClaims = policiesWithClaims;
            this.autoRenewalPolicies = autoRenewalPolicies;
            this.totalPremiums = totalPremiums;
        }
        
        @Override
        public String toString() {
            return String.format("PolicyStatistics{total=%d, active=%d, expired=%d, pending=%d, " +
                               "cancelled=%d, withClaims=%d, autoRenewal=%d, totalPremiums=$%.2f}", 
                               totalPolicies, activePolicies, expiredPolicies, pendingPolicies,
                               cancelledPolicies, policiesWithClaims, autoRenewalPolicies, totalPremiums);
        }
    }
}
