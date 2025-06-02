package testdata.providers;

import testdata.models.UserModel;
import utils.ErrorHandler;
import utils.LoggingUtil;
import org.slf4j.Logger;

import java.util.List;
import java.util.function.Predicate;

/**
 * User-specific test data provider with convenience methods
 */
public class UserDataProvider {
    
    private static final Logger logger = LoggingUtil.getLogger(UserDataProvider.class);
    private static final String DATA_TYPE = "users";
    private final TestDataManager dataManager;
    
    public UserDataProvider() {
        this.dataManager = TestDataManager.getInstance();
        initializeDefaultUsers();
    }
    
    /**
     * Initialize default user test data
     */
    private void initializeDefaultUsers() {
        try {
            // Valid user
            UserModel validUser = new UserModel("user001", "user@test.com", "Pass123!", 
                                               "John", "Doe", "user@test.com");
            validUser.setRole("USER");
            validUser.setValid(true);
            validUser.setDescription("Standard valid user for positive testing");
            dataManager.addTestData(DATA_TYPE, validUser);
            
            // Admin user
            UserModel adminUser = new UserModel("admin001", "admin@test.com", "AdminPass123!", 
                                               "Admin", "User", "admin@test.com");
            adminUser.setRole("ADMIN");
            adminUser.setValid(true);
            adminUser.setDescription("Administrator user for privileged operations testing");
            dataManager.addTestData(DATA_TYPE, adminUser);
            
            // Invalid user
            UserModel invalidUser = new UserModel("invalid001", "invalid@test.com", "InvalidPass", 
                                                 "Invalid", "User", "invalid@test.com");
            invalidUser.setRole("USER");
            invalidUser.setValid(false);
            invalidUser.setDescription("Invalid user for negative testing");
            dataManager.addTestData(DATA_TYPE, invalidUser);
            
            // Locked user
            UserModel lockedUser = new UserModel("locked001", "locked@test.com", "LockedPass123!", 
                                                "Locked", "User", "locked@test.com");
            lockedUser.setRole("USER");
            lockedUser.setValid(true);
            lockedUser.setLocked(true);
            lockedUser.setFailedLoginAttempts(3);
            lockedUser.setDescription("Locked user for account lockout testing");
            dataManager.addTestData(DATA_TYPE, lockedUser);
            
            // User with special characters
            UserModel specialUser = new UserModel("special001", "test+user@test.com", "Special@Pass123!", 
                                                 "Test", "User", "test+user@test.com");
            specialUser.setRole("USER");
            specialUser.setValid(true);
            specialUser.setDescription("User with special characters for edge case testing");
            dataManager.addTestData(DATA_TYPE, specialUser);
            
            logger.info("Initialized default user test data");
            
        } catch (Exception e) {
            logger.error("Failed to initialize default user test data", e);
        }
    }
    
    /**
     * Get user by ID
     */
    public UserModel getUser(String userId) {
        return dataManager.getTestData(DATA_TYPE, userId, UserModel.class);
    }
    
    /**
     * Get all users
     */
    public List<UserModel> getAllUsers() {
        return dataManager.getAllTestData(DATA_TYPE, UserModel.class);
    }
    
    /**
     * Get valid users only
     */
    public List<UserModel> getValidUsers() {
        return dataManager.getTestDataWhere(DATA_TYPE, UserModel.class, UserModel::canLogin);
    }
    
    /**
     * Get invalid users only
     */
    public List<UserModel> getInvalidUsers() {
        return dataManager.getTestDataWhere(DATA_TYPE, UserModel.class, user -> !user.canLogin());
    }
    
    /**
     * Get users by role
     */
    public List<UserModel> getUsersByRole(String role) {
        ErrorHandler.validateNotEmpty(role, "role");
        return dataManager.getTestDataWhere(DATA_TYPE, UserModel.class, 
            user -> role.equalsIgnoreCase(user.getRole()));
    }
    
    /**
     * Get admin users
     */
    public List<UserModel> getAdminUsers() {
        return dataManager.getTestDataWhere(DATA_TYPE, UserModel.class, UserModel::isAdmin);
    }
    
    /**
     * Get locked users
     */
    public List<UserModel> getLockedUsers() {
        return dataManager.getTestDataWhere(DATA_TYPE, UserModel.class, UserModel::isLocked);
    }
    
    /**
     * Get a random valid user
     */
    public UserModel getRandomValidUser() {
        List<UserModel> validUsers = getValidUsers();
        if (validUsers.isEmpty()) {
            logger.warn("No valid users available for random selection");
            return null;
        }
        return validUsers.get((int) (Math.random() * validUsers.size()));
    }
    
    /**
     * Get a random invalid user
     */
    public UserModel getRandomInvalidUser() {
        List<UserModel> invalidUsers = getInvalidUsers();
        if (invalidUsers.isEmpty()) {
            logger.warn("No invalid users available for random selection");
            return null;
        }
        return invalidUsers.get((int) (Math.random() * invalidUsers.size()));
    }
    
    /**
     * Create a new user for testing
     */
    public UserModel createUser(String id, String username, String password, String role, boolean isValid) {
        UserModel user = new UserModel(id, username, password, "Test", "User", username);
        user.setRole(role);
        user.setValid(isValid);
        user.setDescription("Dynamically created test user");
        
        dataManager.addTestData(DATA_TYPE, user);
        logger.debug("Created new test user: {}", user.getSummary());
        return user;
    }
    
    /**
     * Create a copy of existing user for test isolation
     */
    public UserModel copyUser(String userId) {
        return dataManager.copyTestData(DATA_TYPE, userId, UserModel.class);
    }
    
    /**
     * Update user data
     */
    public void updateUser(UserModel user) {
        dataManager.updateTestData(DATA_TYPE, user);
    }
    
    /**
     * Remove user
     */
    public void removeUser(String userId) {
        dataManager.removeTestData(DATA_TYPE, userId);
    }
    
    /**
     * Get users with custom filter
     */
    public List<UserModel> getUsersWhere(Predicate<UserModel> filter) {
        return dataManager.getTestDataWhere(DATA_TYPE, UserModel.class, filter);
    }
    
    /**
     * Load users from JSON file
     */
    public void loadFromJson(String filePath) {
        dataManager.loadFromJson(DATA_TYPE, filePath, UserModel.class);
    }
    
    /**
     * Export users to JSON
     */
    public String exportToJson() {
        return dataManager.exportToJson(DATA_TYPE);
    }
    
    /**
     * Validate all user data
     */
    public List<String> validateAllUsers() {
        return dataManager.validateTestData(DATA_TYPE);
    }
    
    /**
     * Clear all user data
     */
    public void clearAllUsers() {
        dataManager.clearTestData(DATA_TYPE);
    }
    
    /**
     * Get user statistics
     */
    public UserStatistics getStatistics() {
        List<UserModel> allUsers = getAllUsers();
        
        long validCount = allUsers.stream().filter(UserModel::canLogin).count();
        long invalidCount = allUsers.stream().filter(user -> !user.canLogin()).count();
        long adminCount = allUsers.stream().filter(UserModel::isAdmin).count();
        long lockedCount = allUsers.stream().filter(UserModel::isLocked).count();
        
        return new UserStatistics(allUsers.size(), validCount, invalidCount, adminCount, lockedCount);
    }
    
    /**
     * User statistics class
     */
    public static class UserStatistics {
        public final int totalUsers;
        public final long validUsers;
        public final long invalidUsers;
        public final long adminUsers;
        public final long lockedUsers;
        
        public UserStatistics(int totalUsers, long validUsers, long invalidUsers, long adminUsers, long lockedUsers) {
            this.totalUsers = totalUsers;
            this.validUsers = validUsers;
            this.invalidUsers = invalidUsers;
            this.adminUsers = adminUsers;
            this.lockedUsers = lockedUsers;
        }
        
        @Override
        public String toString() {
            return String.format("UserStatistics{total=%d, valid=%d, invalid=%d, admin=%d, locked=%d}", 
                               totalUsers, validUsers, invalidUsers, adminUsers, lockedUsers);
        }
    }
}
