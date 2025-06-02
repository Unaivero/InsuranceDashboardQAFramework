package testdata;

/**
 * Test data class for User-related test scenarios
 * Provides predefined test data for various user authentication testing scenarios
 */
public class UserTestData {
    
    // Valid user credentials
    public static final String VALID_USERNAME = "user@test.com";
    public static final String VALID_PASSWORD = "Pass123!";
    
    // Invalid user credentials
    public static final String INVALID_USERNAME = "invalid@test.com";
    public static final String INVALID_PASSWORD = "InvalidPass";
    public static final String WRONG_PASSWORD = "wrongPass";
    
    // Edge cases
    public static final String EMPTY_USERNAME = "";
    public static final String EMPTY_PASSWORD = "";
    public static final String NULL_USERNAME = null;
    public static final String NULL_PASSWORD = null;
    
    // Test User Data
    public static class TestUser {
        public String username;
        public String password;
        public String firstName;
        public String lastName;
        public String email;
        public boolean isValid;
        
        public TestUser(String username, String password, String firstName, String lastName, String email, boolean isValid) {
            this.username = username;
            this.password = password;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.isValid = isValid;
        }
        
        public String getFullName() {
            return firstName + " " + lastName;
        }
    }
    
    // Sample test users
    public static final TestUser VALID_USER = new TestUser(
        VALID_USERNAME,
        VALID_PASSWORD,
        "John",
        "Doe",
        "user@test.com",
        true
    );
    
    public static final TestUser INVALID_USER = new TestUser(
        INVALID_USERNAME,
        INVALID_PASSWORD,
        "Invalid",
        "User",
        "invalid@test.com",
        false
    );
    
    public static final TestUser ADMIN_USER = new TestUser(
        "admin@test.com",
        "AdminPass123!",
        "Admin",
        "User",
        "admin@test.com",
        true
    );
    
    /**
     * Get valid user credentials
     * @return TestUser object with valid credentials
     */
    public static TestUser getValidUser() {
        return VALID_USER;
    }
    
    /**
     * Get invalid user credentials
     * @return TestUser object with invalid credentials
     */
    public static TestUser getInvalidUser() {
        return INVALID_USER;
    }
    
    /**
     * Get admin user credentials
     * @return TestUser object with admin credentials
     */
    public static TestUser getAdminUser() {
        return ADMIN_USER;
    }
    
    /**
     * Create a test user with custom credentials
     * @param username The username
     * @param password The password
     * @param isValid Whether the credentials are valid
     * @return TestUser object with specified credentials
     */
    public static TestUser createTestUser(String username, String password, boolean isValid) {
        return new TestUser(username, password, "Test", "User", username, isValid);
    }
}
