package api.clients;

import api.models.ApiResponse;
import api.models.UserApiModel;
import exceptions.FrameworkException;
import io.restassured.response.Response;
import utils.LoggingUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User API Client
 * 
 * Handles all User-related API operations including:
 * - User management (CRUD)
 * - Authentication operations
 * - Role and permission management
 * - User search and filtering
 * 
 * @author Insurance Dashboard QA Framework
 * @version 1.0
 */
public class UserApiClient extends BaseApiClient {
    
    private static UserApiClient instance;
    
    /**
     * Private constructor for singleton pattern
     */
    private UserApiClient() {
        super();
        initializeClient();
    }
    
    /**
     * Get singleton instance
     */
    public static synchronized UserApiClient getInstance() {
        if (instance == null) {
            instance = new UserApiClient();
        }
        return instance;
    }
    
    @Override
    protected void initializeClient() {
        LoggingUtil.logInfo("Initializing User API Client");
        addHeader("X-API-Client", "UserApiClient");
    }
    
    /**
     * Get all users
     */
    public ApiResponse<List<UserApiModel>> getAllUsers() throws FrameworkException {
        LoggingUtil.logInfo("Fetching all users");
        
        Response response = executeGetRequest(USERS_ENDPOINT);
        validateResponse(response, HTTP_OK);
        
        return parseResponse(response, List.class);
    }
    
    /**
     * Get users with pagination
     */
    public ApiResponse<List<UserApiModel>> getAllUsers(int page, int size) throws FrameworkException {
        LoggingUtil.logInfo("Fetching users with pagination - page: " + page + ", size: " + size);
        
        String endpoint = USERS_ENDPOINT + "?page=" + page + "&size=" + size;
        Response response = executeGetRequest(endpoint);
        validateResponse(response, HTTP_OK);
        
        return parseResponse(response, List.class);
    }
    
    /**
     * Get user by ID
     */
    public ApiResponse<UserApiModel> getUserById(String userId) throws FrameworkException {
        LoggingUtil.logInfo("Fetching user by ID: " + userId);
        
        if (userId == null || userId.trim().isEmpty()) {
            throw new FrameworkException("User ID cannot be null or empty");
        }
        
        String endpoint = USERS_ENDPOINT + "/" + userId;
        Response response = executeGetRequest(endpoint);
        validateResponse(response, HTTP_OK);
        
        return parseResponse(response, UserApiModel.class);
    }
    
    /**
     * Get user by username
     */
    public ApiResponse<UserApiModel> getUserByUsername(String username) throws FrameworkException {
        LoggingUtil.logInfo("Fetching user by username: " + username);
        
        if (username == null || username.trim().isEmpty()) {
            throw new FrameworkException("Username cannot be null or empty");
        }
        
        String endpoint = USERS_ENDPOINT + "/search?username=" + username;
        Response response = executeGetRequest(endpoint);
        validateResponse(response, HTTP_OK);
        
        return parseResponse(response, UserApiModel.class);
    }
    
    /**
     * Get user by email
     */
    public ApiResponse<UserApiModel> getUserByEmail(String email) throws FrameworkException {
        LoggingUtil.logInfo("Fetching user by email: " + email);
        
        if (email == null || email.trim().isEmpty()) {
            throw new FrameworkException("Email cannot be null or empty");
        }
        
        String endpoint = USERS_ENDPOINT + "/search?email=" + email;
        Response response = executeGetRequest(endpoint);
        validateResponse(response, HTTP_OK);
        
        return parseResponse(response, UserApiModel.class);
    }
    
    /**
     * Create new user
     */
    public ApiResponse<UserApiModel> createUser(UserApiModel user) throws FrameworkException {
        LoggingUtil.logInfo("Creating new user: " + user.getUsername());
        
        if (user == null) {
            throw new FrameworkException("User object cannot be null");
        }
        
        // Validate user before sending
        user.validate();
        
        Response response = executePostRequest(USERS_ENDPOINT, user);
        validateResponse(response, HTTP_CREATED);
        
        return parseResponse(response, UserApiModel.class);
    }
    
    /**
     * Update existing user
     */
    public ApiResponse<UserApiModel> updateUser(String userId, UserApiModel user) throws FrameworkException {
        LoggingUtil.logInfo("Updating user: " + userId);
        
        if (userId == null || userId.trim().isEmpty()) {
            throw new FrameworkException("User ID cannot be null or empty");
        }
        
        if (user == null) {
            throw new FrameworkException("User object cannot be null");
        }
        
        // Validate user before sending
        user.validate();
        
        String endpoint = USERS_ENDPOINT + "/" + userId;
        Response response = executePutRequest(endpoint, user);
        validateResponse(response, HTTP_OK);
        
        return parseResponse(response, UserApiModel.class);
    }
    
    /**
     * Delete user
     */
    public ApiResponse<Void> deleteUser(String userId) throws FrameworkException {
        LoggingUtil.logInfo("Deleting user: " + userId);
        
        if (userId == null || userId.trim().isEmpty()) {
            throw new FrameworkException("User ID cannot be null or empty");
        }
        
        String endpoint = USERS_ENDPOINT + "/" + userId;
        Response response = executeDeleteRequest(endpoint);
        validateResponse(response, HTTP_NO_CONTENT);
        
        return parseResponse(response, Void.class);
    }
    
    /**
     * Search users by criteria
     */
    public ApiResponse<List<UserApiModel>> searchUsers(Map<String, String> searchCriteria) throws FrameworkException {
        LoggingUtil.logInfo("Searching users with criteria: " + searchCriteria);
        
        StringBuilder queryString = new StringBuilder();
        if (searchCriteria != null && !searchCriteria.isEmpty()) {
            queryString.append("?");
            searchCriteria.forEach((key, value) -> 
                queryString.append(key).append("=").append(value).append("&"));
            // Remove last &
            queryString.setLength(queryString.length() - 1);
        }
        
        String endpoint = USERS_ENDPOINT + "/search" + queryString.toString();
        Response response = executeGetRequest(endpoint);
        validateResponse(response, HTTP_OK);
        
        return parseResponse(response, List.class);
    }
    
    /**
     * Get users by role
     */
    public ApiResponse<List<UserApiModel>> getUsersByRole(String role) throws FrameworkException {
        LoggingUtil.logInfo("Fetching users by role: " + role);
        
        if (role == null || role.trim().isEmpty()) {
            throw new FrameworkException("Role cannot be null or empty");
        }
        
        Map<String, String> searchCriteria = new HashMap<>();
        searchCriteria.put("role", role);
        
        return searchUsers(searchCriteria);
    }
    
    /**
     * Get users by status
     */
    public ApiResponse<List<UserApiModel>> getUsersByStatus(String status) throws FrameworkException {
        LoggingUtil.logInfo("Fetching users by status: " + status);
        
        if (status == null || status.trim().isEmpty()) {
            throw new FrameworkException("Status cannot be null or empty");
        }
        
        Map<String, String> searchCriteria = new HashMap<>();
        searchCriteria.put("status", status);
        
        return searchUsers(searchCriteria);
    }
    
    /**
     * Get users by department
     */
    public ApiResponse<List<UserApiModel>> getUsersByDepartment(String department) throws FrameworkException {
        LoggingUtil.logInfo("Fetching users by department: " + department);
        
        if (department == null || department.trim().isEmpty()) {
            throw new FrameworkException("Department cannot be null or empty");
        }
        
        Map<String, String> searchCriteria = new HashMap<>();
        searchCriteria.put("department", department);
        
        return searchUsers(searchCriteria);
    }
    
    /**
     * Update user status
     */
    public ApiResponse<UserApiModel> updateUserStatus(String userId, String newStatus) throws FrameworkException {
        LoggingUtil.logInfo("Updating user status - ID: " + userId + ", Status: " + newStatus);
        
        if (userId == null || userId.trim().isEmpty()) {
            throw new FrameworkException("User ID cannot be null or empty");
        }
        
        if (newStatus == null || newStatus.trim().isEmpty()) {
            throw new FrameworkException("Status cannot be null or empty");
        }
        
        Map<String, String> statusUpdate = new HashMap<>();
        statusUpdate.put("status", newStatus);
        
        String endpoint = USERS_ENDPOINT + "/" + userId + "/status";
        Response response = executePutRequest(endpoint, statusUpdate);
        validateResponse(response, HTTP_OK);
        
        return parseResponse(response, UserApiModel.class);
    }
    
    /**
     * Update user role
     */
    public ApiResponse<UserApiModel> updateUserRole(String userId, String newRole) throws FrameworkException {
        LoggingUtil.logInfo("Updating user role - ID: " + userId + ", Role: " + newRole);
        
        if (userId == null || userId.trim().isEmpty()) {
            throw new FrameworkException("User ID cannot be null or empty");
        }
        
        if (newRole == null || newRole.trim().isEmpty()) {
            throw new FrameworkException("Role cannot be null or empty");
        }
        
        Map<String, String> roleUpdate = new HashMap<>();
        roleUpdate.put("role", newRole);
        
        String endpoint = USERS_ENDPOINT + "/" + userId + "/role";
        Response response = executePutRequest(endpoint, roleUpdate);
        validateResponse(response, HTTP_OK);
        
        return parseResponse(response, UserApiModel.class);
    }
    
    /**
     * Lock user account
     */
    public ApiResponse<UserApiModel> lockUserAccount(String userId) throws FrameworkException {
        LoggingUtil.logInfo("Locking user account: " + userId);
        
        if (userId == null || userId.trim().isEmpty()) {
            throw new FrameworkException("User ID cannot be null or empty");
        }
        
        String endpoint = USERS_ENDPOINT + "/" + userId + "/lock";
        Response response = executePostRequest(endpoint, null);
        validateResponse(response, HTTP_OK);
        
        return parseResponse(response, UserApiModel.class);
    }
    
    /**
     * Unlock user account
     */
    public ApiResponse<UserApiModel> unlockUserAccount(String userId) throws FrameworkException {
        LoggingUtil.logInfo("Unlocking user account: " + userId);
        
        if (userId == null || userId.trim().isEmpty()) {
            throw new FrameworkException("User ID cannot be null or empty");
        }
        
        String endpoint = USERS_ENDPOINT + "/" + userId + "/unlock";
        Response response = executePostRequest(endpoint, null);
        validateResponse(response, HTTP_OK);
        
        return parseResponse(response, UserApiModel.class);
    }
    
    /**
     * Change user password
     */
    public ApiResponse<Void> changeUserPassword(String userId, String currentPassword, String newPassword) throws FrameworkException {
        LoggingUtil.logInfo("Changing password for user: " + userId);
        
        if (userId == null || userId.trim().isEmpty()) {
            throw new FrameworkException("User ID cannot be null or empty");
        }
        
        if (currentPassword == null || currentPassword.trim().isEmpty()) {
            throw new FrameworkException("Current password cannot be null or empty");
        }
        
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new FrameworkException("New password cannot be null or empty");
        }
        
        Map<String, String> passwordChange = new HashMap<>();
        passwordChange.put("currentPassword", currentPassword);
        passwordChange.put("newPassword", newPassword);
        
        String endpoint = USERS_ENDPOINT + "/" + userId + "/password";
        Response response = executePutRequest(endpoint, passwordChange);
        validateResponse(response, HTTP_NO_CONTENT);
        
        return parseResponse(response, Void.class);
    }
    
    /**
     * Reset user password
     */
    public ApiResponse<String> resetUserPassword(String userId) throws FrameworkException {
        LoggingUtil.logInfo("Resetting password for user: " + userId);
        
        if (userId == null || userId.trim().isEmpty()) {
            throw new FrameworkException("User ID cannot be null or empty");
        }
        
        String endpoint = USERS_ENDPOINT + "/" + userId + "/password/reset";
        Response response = executePostRequest(endpoint, null);
        validateResponse(response, HTTP_OK);
        
        return parseResponse(response, String.class);
    }
    
    /**
     * Get user permissions
     */
    public ApiResponse<List<String>> getUserPermissions(String userId) throws FrameworkException {
        LoggingUtil.logInfo("Fetching permissions for user: " + userId);
        
        if (userId == null || userId.trim().isEmpty()) {
            throw new FrameworkException("User ID cannot be null or empty");
        }
        
        String endpoint = USERS_ENDPOINT + "/" + userId + "/permissions";
        Response response = executeGetRequest(endpoint);
        validateResponse(response, HTTP_OK);
        
        return parseResponse(response, List.class);
    }
    
    /**
     * Add user permission
     */
    public ApiResponse<UserApiModel> addUserPermission(String userId, String permission) throws FrameworkException {
        LoggingUtil.logInfo("Adding permission to user - ID: " + userId + ", Permission: " + permission);
        
        if (userId == null || userId.trim().isEmpty()) {
            throw new FrameworkException("User ID cannot be null or empty");
        }
        
        if (permission == null || permission.trim().isEmpty()) {
            throw new FrameworkException("Permission cannot be null or empty");
        }
        
        Map<String, String> permissionData = new HashMap<>();
        permissionData.put("permission", permission);
        
        String endpoint = USERS_ENDPOINT + "/" + userId + "/permissions";
        Response response = executePostRequest(endpoint, permissionData);
        validateResponse(response, HTTP_OK);
        
        return parseResponse(response, UserApiModel.class);
    }
    
    /**
     * Remove user permission
     */
    public ApiResponse<UserApiModel> removeUserPermission(String userId, String permission) throws FrameworkException {
        LoggingUtil.logInfo("Removing permission from user - ID: " + userId + ", Permission: " + permission);
        
        if (userId == null || userId.trim().isEmpty()) {
            throw new FrameworkException("User ID cannot be null or empty");
        }
        
        if (permission == null || permission.trim().isEmpty()) {
            throw new FrameworkException("Permission cannot be null or empty");
        }
        
        String endpoint = USERS_ENDPOINT + "/" + userId + "/permissions/" + permission;
        Response response = executeDeleteRequest(endpoint);
        validateResponse(response, HTTP_OK);
        
        return parseResponse(response, UserApiModel.class);
    }
    
    /**
     * Get active users count
     */
    public ApiResponse<Integer> getActiveUsersCount() throws FrameworkException {
        LoggingUtil.logInfo("Fetching active users count");
        
        Response response = executeGetRequest(USERS_ENDPOINT + "/count/active");
        validateResponse(response, HTTP_OK);
        
        return parseResponse(response, Integer.class);
    }
    
    /**
     * Get users statistics
     */
    public ApiResponse<Map<String, Object>> getUsersStatistics() throws FrameworkException {
        LoggingUtil.logInfo("Fetching users statistics");
        
        Response response = executeGetRequest(USERS_ENDPOINT + "/statistics");
        validateResponse(response, HTTP_OK);
        
        return parseResponse(response, Map.class);
    }
    
    /**
     * Validate username availability
     */
    public ApiResponse<Boolean> isUsernameAvailable(String username) throws FrameworkException {
        LoggingUtil.logInfo("Checking username availability: " + username);
        
        if (username == null || username.trim().isEmpty()) {
            throw new FrameworkException("Username cannot be null or empty");
        }
        
        String endpoint = USERS_ENDPOINT + "/validate/username?username=" + username;
        Response response = executeGetRequest(endpoint);
        validateResponse(response, HTTP_OK);
        
        return parseResponse(response, Boolean.class);
    }
    
    /**
     * Validate email availability
     */
    public ApiResponse<Boolean> isEmailAvailable(String email) throws FrameworkException {
        LoggingUtil.logInfo("Checking email availability: " + email);
        
        if (email == null || email.trim().isEmpty()) {
            throw new FrameworkException("Email cannot be null or empty");
        }
        
        String endpoint = USERS_ENDPOINT + "/validate/email?email=" + email;
        Response response = executeGetRequest(endpoint);
        validateResponse(response, HTTP_OK);
        
        return parseResponse(response, Boolean.class);
    }
    
    /**
     * Bulk create users
     */
    public ApiResponse<List<UserApiModel>> createUsersBulk(List<UserApiModel> users) throws FrameworkException {
        LoggingUtil.logInfo("Creating users in bulk - count: " + (users != null ? users.size() : 0));
        
        if (users == null || users.isEmpty()) {
            throw new FrameworkException("Users list cannot be null or empty");
        }
        
        // Validate all users before sending
        for (UserApiModel user : users) {
            user.validate();
        }
        
        Response response = executePostRequest(USERS_ENDPOINT + "/bulk", users);
        validateResponse(response, HTTP_CREATED);
        
        return parseResponse(response, List.class);
    }
}
