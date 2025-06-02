package testdata.factories;

import testdata.models.UserModel;
import testdata.models.PolicyModel;
import utils.ErrorHandler;
import utils.LoggingUtil;
import org.slf4j.Logger;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;

/**
 * Factory for generating dynamic test data
 * Provides methods to create realistic test data with random variations
 */
public class TestDataFactory {
    
    private static final Logger logger = LoggingUtil.getLogger(TestDataFactory.class);
    private static final Random random = new Random();
    
    // Data arrays for generating realistic test data
    private static final String[] FIRST_NAMES = {
        "John", "Jane", "Michael", "Sarah", "David", "Emily", "Robert", "Lisa",
        "Christopher", "Jennifer", "Matthew", "Amanda", "Daniel", "Jessica", "James", "Ashley"
    };
    
    private static final String[] LAST_NAMES = {
        "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis",
        "Rodriguez", "Martinez", "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson", "Thomas"
    };
    
    private static final String[] EMAIL_DOMAINS = {
        "gmail.com", "yahoo.com", "hotmail.com", "outlook.com", "test.com", "example.com"
    };
    
    private static final String[] POLICY_TYPES = {
        "Auto Insurance", "Home Insurance", "Life Insurance", "Health Insurance",
        "Business Insurance", "Travel Insurance", "Pet Insurance", "Renters Insurance"
    };
    
    private static final String[] POLICY_STATUSES = {
        "ACTIVE", "EXPIRED", "PENDING", "CANCELLED"
    };
    
    private static final String[] AUTO_CATEGORIES = {
        "Personal Vehicle", "Commercial Vehicle", "Motorcycle", "RV", "Classic Car"
    };
    
    private static final String[] HOME_CATEGORIES = {
        "Homeowners", "Condo", "Renters", "Mobile Home", "Vacation Home"
    };
    
    private static final String[] LIFE_CATEGORIES = {
        "Term Life", "Whole Life", "Universal Life", "Variable Life"
    };
    
    private static final String[] HEALTH_CATEGORIES = {
        "Individual Plan", "Family Plan", "Group Plan", "Medicare Supplement"
    };
    
    /**
     * Generate a random user with realistic data
     */
    public static UserModel generateRandomUser() {
        return generateRandomUser(null);
    }
    
    /**
     * Generate a random user with specific role
     */
    public static UserModel generateRandomUser(String role) {
        String firstName = getRandomFirstName();
        String lastName = getRandomLastName();
        String username = generateUsername(firstName, lastName);
        String email = generateEmail(firstName, lastName);
        String password = generatePassword();
        String userId = "USER_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        UserModel user = new UserModel(userId, username, password, firstName, lastName, email);
        user.setRole(role != null ? role : getRandomRole());
        user.setValid(random.nextBoolean() || role != null); // Bias towards valid users when role is specified
        user.setLocked(random.nextDouble() < 0.1); // 10% chance of being locked
        user.setDescription("Randomly generated test user");
        
        logger.debug("Generated random user: {}", user.getSummary());
        return user;
    }
    
    /**
     * Generate a specific type of user
     */
    public static UserModel generateValidUser() {
        UserModel user = generateRandomUser("USER");
        user.setValid(true);
        user.setLocked(false);
        user.setDescription("Generated valid user for positive testing");
        return user;
    }
    
    public static UserModel generateInvalidUser() {
        UserModel user = generateRandomUser("USER");
        user.setValid(false);
        user.setDescription("Generated invalid user for negative testing");
        return user;
    }
    
    public static UserModel generateAdminUser() {
        UserModel user = generateRandomUser("ADMIN");
        user.setValid(true);
        user.setLocked(false);
        user.setDescription("Generated admin user for privileged testing");
        return user;
    }
    
    public static UserModel generateLockedUser() {
        UserModel user = generateRandomUser("USER");
        user.setValid(true);
        user.setLocked(true);
        user.setFailedLoginAttempts(3);
        user.setDescription("Generated locked user for account lockout testing");
        return user;
    }
    
    /**
     * Generate a random policy with realistic data
     */
    public static PolicyModel generateRandomPolicy() {
        return generateRandomPolicy(null);
    }
    
    /**
     * Generate a random policy with specific type
     */
    public static PolicyModel generateRandomPolicy(String type) {
        String policyType = type != null ? type : getRandomPolicyType();
        String policyId = "POL_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String policyNumber = generatePolicyNumber(policyType);
        String status = getRandomPolicyStatus();
        
        PolicyModel policy = new PolicyModel(policyId, policyNumber, status, policyType);
        
        // Set category based on type
        policy.setCategory(getCategoryForType(policyType));
        
        // Generate financial amounts based on policy type
        PolicyAmounts amounts = generateAmountsForType(policyType);
        policy.setPremiumAmount(amounts.premium);
        policy.setCoverageAmount(amounts.coverage);
        policy.setDeductible(amounts.deductible);
        
        // Generate dates
        LocalDate effectiveDate = generateEffectiveDate();
        LocalDate expirationDate = effectiveDate.plusYears(1);
        policy.setEffectiveDate(effectiveDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        policy.setExpirationDate(expirationDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        
        // Generate insured name
        policy.setInsuredName(getRandomFirstName() + " " + getRandomLastName());
        
        // Set agent ID
        policy.setAgentId("AGT" + String.format("%03d", random.nextInt(999) + 1));
        
        // Set auto renewal (80% chance)
        policy.setAutoRenewal(random.nextDouble() < 0.8);
        
        policy.setDescription("Randomly generated test policy");
        
        // Add some claims and documents for variety
        if (random.nextDouble() < 0.3) { // 30% chance of having claims
            int numClaims = random.nextInt(3) + 1;
            for (int i = 0; i < numClaims; i++) {
                policy.addClaimId("CLM_" + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
            }
        }
        
        // Always add at least one document
        int numDocs = random.nextInt(3) + 1;
        for (int i = 0; i < numDocs; i++) {
            policy.addDocumentId("DOC_" + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        }
        
        // Add beneficiaries for life insurance
        if ("Life Insurance".equals(policyType)) {
            policy.addBeneficiary(getRandomFirstName() + " " + getRandomLastName() + " (Spouse) - 60%");
            policy.addBeneficiary(getRandomFirstName() + " " + getRandomLastName() + " (Child) - 40%");
        }
        
        logger.debug("Generated random policy: {}", policy.getSummary());
        return policy;
    }
    
    /**
     * Generate specific types of policies
     */
    public static PolicyModel generateActivePolicy() {
        PolicyModel policy = generateRandomPolicy();
        policy.setStatus("ACTIVE");
        policy.setDescription("Generated active policy for testing");
        return policy;
    }
    
    public static PolicyModel generateExpiredPolicy() {
        PolicyModel policy = generateRandomPolicy();
        policy.setStatus("EXPIRED");
        
        // Set dates in the past
        LocalDate effectiveDate = LocalDate.now().minusYears(2);
        LocalDate expirationDate = LocalDate.now().minusYears(1);
        policy.setEffectiveDate(effectiveDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        policy.setExpirationDate(expirationDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        
        policy.setDescription("Generated expired policy for testing");
        return policy;
    }
    
    public static PolicyModel generatePendingPolicy() {
        PolicyModel policy = generateRandomPolicy();
        policy.setStatus("PENDING");
        
        // Set future effective date
        LocalDate effectiveDate = LocalDate.now().plusDays(random.nextInt(30) + 1);
        LocalDate expirationDate = effectiveDate.plusYears(1);
        policy.setEffectiveDate(effectiveDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        policy.setExpirationDate(expirationDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        
        policy.setDescription("Generated pending policy for testing");
        return policy;
    }
    
    public static PolicyModel generateAutoPolicy() {
        PolicyModel policy = generateRandomPolicy("Auto Insurance");
        policy.setDescription("Generated auto insurance policy for testing");
        return policy;
    }
    
    public static PolicyModel generateHomePolicy() {
        PolicyModel policy = generateRandomPolicy("Home Insurance");
        policy.setDescription("Generated home insurance policy for testing");
        return policy;
    }
    
    public static PolicyModel generateLifePolicy() {
        PolicyModel policy = generateRandomPolicy("Life Insurance");
        policy.setDescription("Generated life insurance policy for testing");
        return policy;
    }
    
    public static PolicyModel generateHealthPolicy() {
        PolicyModel policy = generateRandomPolicy("Health Insurance");
        policy.setDescription("Generated health insurance policy for testing");
        return policy;
    }
    
    // Utility methods for data generation
    private static String getRandomFirstName() {
        return FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
    }
    
    private static String getRandomLastName() {
        return LAST_NAMES[random.nextInt(LAST_NAMES.length)];
    }
    
    private static String generateUsername(String firstName, String lastName) {
        return (firstName.toLowerCase() + "." + lastName.toLowerCase() + random.nextInt(100))
            .replaceAll("[^a-zA-Z0-9.]", "");
    }
    
    private static String generateEmail(String firstName, String lastName) {
        String domain = EMAIL_DOMAINS[random.nextInt(EMAIL_DOMAINS.length)];
        return generateUsername(firstName, lastName) + "@" + domain;
    }
    
    private static String generatePassword() {
        String[] passwords = {
            "Password123!", "SecurePass1!", "TestPass99!", "MyPass456!",
            "StrongPwd1!", "SafeCode123!", "TestUser1!", "ValidPass9!"
        };
        return passwords[random.nextInt(passwords.length)];
    }
    
    private static String getRandomRole() {
        return random.nextDouble() < 0.1 ? "ADMIN" : "USER"; // 10% chance of admin
    }
    
    private static String getRandomPolicyType() {
        return POLICY_TYPES[random.nextInt(POLICY_TYPES.length)];
    }
    
    private static String getRandomPolicyStatus() {
        // Bias towards active policies
        double rand = random.nextDouble();
        if (rand < 0.6) return "ACTIVE";
        if (rand < 0.8) return "EXPIRED";
        if (rand < 0.95) return "PENDING";
        return "CANCELLED";
    }
    
    private static String generatePolicyNumber(String policyType) {
        String prefix = policyType.substring(0, Math.min(4, policyType.length())).toUpperCase().replaceAll("[^A-Z]", "");
        int year = LocalDate.now().getYear();
        int number = random.nextInt(999) + 1;
        return String.format("%s-%d-%03d", prefix, year, number);
    }
    
    private static String getCategoryForType(String policyType) {
        switch (policyType) {
            case "Auto Insurance":
                return AUTO_CATEGORIES[random.nextInt(AUTO_CATEGORIES.length)];
            case "Home Insurance":
                return HOME_CATEGORIES[random.nextInt(HOME_CATEGORIES.length)];
            case "Life Insurance":
                return LIFE_CATEGORIES[random.nextInt(LIFE_CATEGORIES.length)];
            case "Health Insurance":
                return HEALTH_CATEGORIES[random.nextInt(HEALTH_CATEGORIES.length)];
            default:
                return "Standard";
        }
    }
    
    private static PolicyAmounts generateAmountsForType(String policyType) {
        switch (policyType) {
            case "Auto Insurance":
                return new PolicyAmounts(
                    new BigDecimal(800 + random.nextInt(1200)), // Premium: $800-2000
                    new BigDecimal(25000 + random.nextInt(75000)), // Coverage: $25k-100k
                    new BigDecimal(250 + random.nextInt(1750)) // Deductible: $250-2000
                );
            case "Home Insurance":
                return new PolicyAmounts(
                    new BigDecimal(600 + random.nextInt(1400)), // Premium: $600-2000
                    new BigDecimal(100000 + random.nextInt(400000)), // Coverage: $100k-500k
                    new BigDecimal(500 + random.nextInt(2500)) // Deductible: $500-3000
                );
            case "Life Insurance":
                return new PolicyAmounts(
                    new BigDecimal(1200 + random.nextInt(3800)), // Premium: $1200-5000
                    new BigDecimal(100000 + random.nextInt(900000)), // Coverage: $100k-1M
                    BigDecimal.ZERO // No deductible for life insurance
                );
            case "Health Insurance":
                return new PolicyAmounts(
                    new BigDecimal(2400 + random.nextInt(4800)), // Premium: $2400-7200
                    new BigDecimal(50000 + random.nextInt(150000)), // Coverage: $50k-200k
                    new BigDecimal(1000 + random.nextInt(4000)) // Deductible: $1000-5000
                );
            default:
                return new PolicyAmounts(
                    new BigDecimal(500 + random.nextInt(2000)), // Premium: $500-2500
                    new BigDecimal(10000 + random.nextInt(90000)), // Coverage: $10k-100k
                    new BigDecimal(250 + random.nextInt(1250)) // Deductible: $250-1500
                );
        }
    }
    
    private static LocalDate generateEffectiveDate() {
        // Generate date within last 2 years to current date + 6 months
        int daysRange = 365 * 2 + 180; // 2.5 years range
        int daysFromNow = random.nextInt(daysRange) - (365 * 2); // Can be negative (past) or positive (future)
        return LocalDate.now().plusDays(daysFromNow);
    }
    
    /**
     * Generate bulk test data
     */
    public static UserModel[] generateMultipleUsers(int count) {
        ErrorHandler.validateNotNull(count, "count");
        if (count <= 0) {
            throw new IllegalArgumentException("Count must be positive");
        }
        
        UserModel[] users = new UserModel[count];
        for (int i = 0; i < count; i++) {
            users[i] = generateRandomUser();
        }
        
        logger.info("Generated {} random users", count);
        return users;
    }
    
    public static PolicyModel[] generateMultiplePolicies(int count) {
        ErrorHandler.validateNotNull(count, "count");
        if (count <= 0) {
            throw new IllegalArgumentException("Count must be positive");
        }
        
        PolicyModel[] policies = new PolicyModel[count];
        for (int i = 0; i < count; i++) {
            policies[i] = generateRandomPolicy();
        }
        
        logger.info("Generated {} random policies", count);
        return policies;
    }
    
    public static UserModel[] generateUsersWithRoles(int userCount, int adminCount) {
        ErrorHandler.validateNotNull(userCount, "user count");
        ErrorHandler.validateNotNull(adminCount, "admin count");
        
        if (userCount < 0 || adminCount < 0) {
            throw new IllegalArgumentException("Counts must be non-negative");
        }
        
        UserModel[] users = new UserModel[userCount + adminCount];
        int index = 0;
        
        // Generate regular users
        for (int i = 0; i < userCount; i++) {
            users[index++] = generateValidUser();
        }
        
        // Generate admin users
        for (int i = 0; i < adminCount; i++) {
            users[index++] = generateAdminUser();
        }
        
        logger.info("Generated {} users ({} regular, {} admin)", userCount + adminCount, userCount, adminCount);
        return users;
    }
    
    public static PolicyModel[] generatePoliciesByType(String policyType, int count) {
        ErrorHandler.validateNotEmpty(policyType, "policy type");
        ErrorHandler.validateNotNull(count, "count");
        
        if (count <= 0) {
            throw new IllegalArgumentException("Count must be positive");
        }
        
        PolicyModel[] policies = new PolicyModel[count];
        for (int i = 0; i < count; i++) {
            policies[i] = generateRandomPolicy(policyType);
        }
        
        logger.info("Generated {} {} policies", count, policyType);
        return policies;
    }
    
    /**
     * Helper class for policy amounts
     */
    private static class PolicyAmounts {
        final BigDecimal premium;
        final BigDecimal coverage;
        final BigDecimal deductible;
        
        PolicyAmounts(BigDecimal premium, BigDecimal coverage, BigDecimal deductible) {
            this.premium = premium;
            this.coverage = coverage;
            this.deductible = deductible;
        }
    }
    
    /**
     * Generate test data with specific constraints
     */
    public static UserModel generateUserWithConstraints(boolean mustBeValid, boolean mustBeAdmin, boolean mustBeLocked) {
        UserModel user = generateRandomUser(mustBeAdmin ? "ADMIN" : "USER");
        user.setValid(mustBeValid);
        user.setLocked(mustBeLocked);
        
        if (mustBeLocked) {
            user.setFailedLoginAttempts(3);
        }
        
        user.setDescription("Generated user with specific constraints");
        return user;
    }
    
    public static PolicyModel generatePolicyWithConstraints(String requiredType, String requiredStatus, 
                                                          BigDecimal minPremium, BigDecimal maxPremium) {
        PolicyModel policy = generateRandomPolicy(requiredType);
        policy.setStatus(requiredStatus);
        
        if (minPremium != null && maxPremium != null) {
            BigDecimal range = maxPremium.subtract(minPremium);
            BigDecimal randomAmount = minPremium.add(range.multiply(new BigDecimal(random.nextDouble())));
            policy.setPremiumAmount(randomAmount);
        }
        
        policy.setDescription("Generated policy with specific constraints");
        return policy;
    }
}
