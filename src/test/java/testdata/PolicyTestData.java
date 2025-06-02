package testdata;

/**
 * Test data class for Policy-related test scenarios
 * Provides predefined test data for various policy testing scenarios
 */
public class PolicyTestData {
    
    // Test Policy IDs
    public static final String VALID_POLICY_ID = "POL12345";
    public static final String ACTIVE_POLICY_ID = "POL12345";
    public static final String EXPIRED_POLICY_ID = "POL67890";
    public static final String PENDING_POLICY_ID = "POL11111";
    public static final String CANCELLED_POLICY_ID = "POL99999";
    
    // Test Policy Data
    public static class TestPolicy {
        public String id;
        public String number;
        public String status;
        public String type;
        public String premiumAmount;
        public String coverageAmount;
        public String deductible;
        public String effectiveDate;
        public String expirationDate;
        public String insuredName;
        
        public TestPolicy(String id, String number, String status, String type, 
                         String premiumAmount, String coverageAmount, String deductible,
                         String effectiveDate, String expirationDate, String insuredName) {
            this.id = id;
            this.number = number;
            this.status = status;
            this.type = type;
            this.premiumAmount = premiumAmount;
            this.coverageAmount = coverageAmount;
            this.deductible = deductible;
            this.effectiveDate = effectiveDate;
            this.expirationDate = expirationDate;
            this.insuredName = insuredName;
        }
    }
    
    // Sample test policies
    public static final TestPolicy ACTIVE_AUTO_POLICY = new TestPolicy(
        "POL12345",
        "AUTO-2024-001",
        "Active",
        "Auto Insurance",
        "$1,200.00",
        "$50,000.00",
        "$500.00",
        "01/15/2024",
        "01/15/2025",
        "John Doe"
    );
    
    public static final TestPolicy EXPIRED_HOME_POLICY = new TestPolicy(
        "POL67890",
        "HOME-2023-045",
        "Expired",
        "Home Insurance",
        "$800.00",
        "$250,000.00",
        "$1,000.00",
        "03/01/2023",
        "03/01/2024",
        "Jane Smith"
    );
    
    public static final TestPolicy PENDING_LIFE_POLICY = new TestPolicy(
        "POL11111",
        "LIFE-2024-012",
        "Pending",
        "Life Insurance",
        "$2,400.00",
        "$500,000.00",
        "$0.00",
        "06/01/2024",
        "06/01/2025",
        "Michael Johnson"
    );
    
    public static final TestPolicy CANCELLED_HEALTH_POLICY = new TestPolicy(
        "POL99999",
        "HEALTH-2024-089",
        "Cancelled",
        "Health Insurance",
        "$3,600.00",
        "$100,000.00",
        "$2,000.00",
        "02/15/2024",
        "02/15/2025",
        "Sarah Wilson"
    );
    
    /**
     * Get test policy by ID
     * @param policyId The policy ID to search for
     * @return TestPolicy object or null if not found
     */
    public static TestPolicy getPolicyById(String policyId) {
        switch (policyId) {
            case "POL12345":
                return ACTIVE_AUTO_POLICY;
            case "POL67890":
                return EXPIRED_HOME_POLICY;
            case "POL11111":
                return PENDING_LIFE_POLICY;
            case "POL99999":
                return CANCELLED_HEALTH_POLICY;
            default:
                return null;
        }
    }
    
    /**
     * Get all available test policy IDs
     * @return Array of test policy IDs
     */
    public static String[] getAllPolicyIds() {
        return new String[]{
            VALID_POLICY_ID,
            EXPIRED_POLICY_ID,
            PENDING_POLICY_ID,
            CANCELLED_POLICY_ID
        };
    }
}
