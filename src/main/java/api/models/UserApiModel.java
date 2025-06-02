package api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import exceptions.TestDataException;
import utils.LoggingUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * API Model for User
 * 
 * Represents a user in the insurance system with all relevant fields
 * for API operations including authentication and authorization
 * 
 * @author Insurance Dashboard QA Framework
 * @version 1.0
 */
public class UserApiModel extends BaseApiModel {
    
    @JsonProperty("username")
    private String username;
    
    @JsonProperty("email")
    private String email;
    
    @JsonProperty("firstName")
    private String firstName;
    
    @JsonProperty("lastName")
    private String lastName;
    
    @JsonProperty("password")
    private String password;
    
    @JsonProperty("role")
    private String role;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("department")
    private String department;
    
    @JsonProperty("phoneNumber")
    private String phoneNumber;
    
    @JsonProperty("address")
    private Address address;
    
    @JsonProperty("permissions")
    private List<String> permissions;
    
    @JsonProperty("managedPolicies")
    private List<String> managedPolicyIds;
    
    @JsonProperty("lastLoginDate")
    private java.time.LocalDateTime lastLoginDate;
    
    @JsonProperty("failedLoginAttempts")
    private Integer failedLoginAttempts;
    
    @JsonProperty("isAccountLocked")
    private Boolean isAccountLocked;
    
    @JsonProperty("mustChangePassword")
    private Boolean mustChangePassword;
    
    @JsonProperty("employeeId")
    private String employeeId;
    
    @JsonProperty("managerId")
    private String managerId;
    
    // User statuses
    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_INACTIVE = "INACTIVE";
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_SUSPENDED = "SUSPENDED";
    public static final String STATUS_LOCKED = "LOCKED";
    
    // User roles
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_AGENT = "AGENT";
    public static final String ROLE_CUSTOMER = "CUSTOMER";
    public static final String ROLE_MANAGER = "MANAGER";
    public static final String ROLE_UNDERWRITER = "UNDERWRITER";
    public static final String ROLE_CLAIMS_ADJUSTER = "CLAIMS_ADJUSTER";
    
    // Departments
    public static final String DEPT_SALES = "SALES";
    public static final String DEPT_UNDERWRITING = "UNDERWRITING";
    public static final String DEPT_CLAIMS = "CLAIMS";
    public static final String DEPT_CUSTOMER_SERVICE = "CUSTOMER_SERVICE";
    public static final String DEPT_IT = "IT";
    public static final String DEPT_MANAGEMENT = "MANAGEMENT";
    
    // Permissions
    public static final String PERM_READ_POLICIES = "READ_POLICIES";
    public static final String PERM_WRITE_POLICIES = "WRITE_POLICIES";
    public static final String PERM_DELETE_POLICIES = "DELETE_POLICIES";
    public static final String PERM_READ_USERS = "READ_USERS";
    public static final String PERM_WRITE_USERS = "WRITE_USERS";
    public static final String PERM_DELETE_USERS = "DELETE_USERS";
    public static final String PERM_MANAGE_CLAIMS = "MANAGE_CLAIMS";
    public static final String PERM_VIEW_REPORTS = "VIEW_REPORTS";
    public static final String PERM_ADMIN_ACCESS = "ADMIN_ACCESS";
    
    /**
     * Default constructor
     */
    public UserApiModel() {
        super();
        this.permissions = new ArrayList<>();
        this.managedPolicyIds = new ArrayList<>();
        this.failedLoginAttempts = 0;
        this.isAccountLocked = false;
        this.mustChangePassword = false;
    }
    
    /**
     * Constructor with username
     */
    public UserApiModel(String username) {
        this();
        this.username = username;
    }
    
    /**
     * Constructor with essential fields
     */
    public UserApiModel(String username, String email, String firstName, String lastName, String role) {
        this(username);
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.status = STATUS_ACTIVE;
    }
    
    @Override
    public boolean validate() throws TestDataException {
        List<String> errors = getValidationErrors();
        if (!errors.isEmpty()) {
            String errorMsg = "User validation failed: " + String.join(", ", errors);
            LoggingUtil.logError(errorMsg);
            throw new TestDataException(errorMsg);
        }
        return true;
    }
    
    @Override
    public List<String> getValidationErrors() {
        List<String> errors = new ArrayList<>();
        
        // Required field validations
        if (username == null || username.trim().isEmpty()) {
            errors.add("Username is required");
        } else if (username.length() < 3) {
            errors.add("Username must be at least 3 characters long");
        }
        
        if (email == null || email.trim().isEmpty()) {
            errors.add("Email is required");
        } else if (!isValidEmail(email)) {
            errors.add("Invalid email format");
        }
        
        if (firstName == null || firstName.trim().isEmpty()) {
            errors.add("First name is required");
        }
        
        if (lastName == null || lastName.trim().isEmpty()) {
            errors.add("Last name is required");
        }
        
        if (role == null || role.trim().isEmpty()) {
            errors.add("Role is required");
        } else if (!isValidRole(role)) {
            errors.add("Invalid role: " + role);
        }
        
        if (status == null || status.trim().isEmpty()) {
            errors.add("Status is required");
        } else if (!isValidStatus(status)) {
            errors.add("Invalid status: " + status);
        }
        
        // Password validation (if provided)
        if (password != null && !password.isEmpty() && !isValidPassword(password)) {
            errors.add("Password must be at least 8 characters long and contain letters, numbers, and special characters");
        }
        
        // Phone number validation (if provided)
        if (phoneNumber != null && !phoneNumber.isEmpty() && !isValidPhoneNumber(phoneNumber)) {
            errors.add("Invalid phone number format");
        }
        
        // Department validation (if provided)
        if (department != null && !department.isEmpty() && !isValidDepartment(department)) {
            errors.add("Invalid department: " + department);
        }
        
        return errors;
    }
    
    // Validation helper methods
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
    
    private boolean isValidPassword(String password) {
        return password.length() >= 8 && 
               password.matches(".*[A-Za-z].*") && 
               password.matches(".*[0-9].*") && 
               password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
    }
    
    private boolean isValidPhoneNumber(String phone) {
        return phone.matches("^[+]?[1-9]?[0-9]{7,15}$");
    }
    
    private boolean isValidRole(String role) {
        return ROLE_ADMIN.equals(role) || ROLE_AGENT.equals(role) || 
               ROLE_CUSTOMER.equals(role) || ROLE_MANAGER.equals(role) ||
               ROLE_UNDERWRITER.equals(role) || ROLE_CLAIMS_ADJUSTER.equals(role);
    }
    
    private boolean isValidStatus(String status) {
        return STATUS_ACTIVE.equals(status) || STATUS_INACTIVE.equals(status) || 
               STATUS_PENDING.equals(status) || STATUS_SUSPENDED.equals(status) ||
               STATUS_LOCKED.equals(status);
    }
    
    private boolean isValidDepartment(String dept) {
        return DEPT_SALES.equals(dept) || DEPT_UNDERWRITING.equals(dept) || 
               DEPT_CLAIMS.equals(dept) || DEPT_CUSTOMER_SERVICE.equals(dept) ||
               DEPT_IT.equals(dept) || DEPT_MANAGEMENT.equals(dept);
    }
    
    // Business logic methods
    public boolean isActive() {
        return STATUS_ACTIVE.equals(this.status) && !Boolean.TRUE.equals(this.isAccountLocked);
    }
    
    public boolean canLogin() {
        return isActive() && !Boolean.TRUE.equals(this.mustChangePassword);
    }
    
    public boolean isAdmin() {
        return ROLE_ADMIN.equals(this.role);
    }
    
    public boolean isAgent() {
        return ROLE_AGENT.equals(this.role);
    }
    
    public boolean isManager() {
        return ROLE_MANAGER.equals(this.role);
    }
    
    public boolean hasPermission(String permission) {
        return this.permissions != null && this.permissions.contains(permission);
    }
    
    public void addPermission(String permission) {
        if (this.permissions == null) {
            this.permissions = new ArrayList<>();
        }
        if (!this.permissions.contains(permission)) {
            this.permissions.add(permission);
            markAsUpdated("system");
        }
    }
    
    public void removePermission(String permission) {
        if (this.permissions != null) {
            if (this.permissions.remove(permission)) {
                markAsUpdated("system");
            }
        }
    }
    
    public void lockAccount() {
        this.isAccountLocked = true;
        this.status = STATUS_LOCKED;
        markAsUpdated("system");
    }
    
    public void unlockAccount() {
        this.isAccountLocked = false;
        this.failedLoginAttempts = 0;
        if (STATUS_LOCKED.equals(this.status)) {
            this.status = STATUS_ACTIVE;
        }
        markAsUpdated("system");
    }
    
    public void incrementFailedLoginAttempts() {
        if (this.failedLoginAttempts == null) {
            this.failedLoginAttempts = 0;
        }
        this.failedLoginAttempts++;
        if (this.failedLoginAttempts >= 5) {
            lockAccount();
        }
        markAsUpdated("system");
    }
    
    public void recordSuccessfulLogin() {
        this.lastLoginDate = java.time.LocalDateTime.now();
        this.failedLoginAttempts = 0;
        markAsUpdated("system");
    }
    
    public String getFullName() {
        return (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
    }
    
    public void addManagedPolicy(String policyId) {
        if (this.managedPolicyIds == null) {
            this.managedPolicyIds = new ArrayList<>();
        }
        if (!this.managedPolicyIds.contains(policyId)) {
            this.managedPolicyIds.add(policyId);
            markAsUpdated("system");
        }
    }
    
    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }
    
    public List<String> getPermissions() { return permissions; }
    public void setPermissions(List<String> permissions) { this.permissions = permissions; }
    
    public List<String> getManagedPolicyIds() { return managedPolicyIds; }
    public void setManagedPolicyIds(List<String> managedPolicyIds) { this.managedPolicyIds = managedPolicyIds; }
    
    public java.time.LocalDateTime getLastLoginDate() { return lastLoginDate; }
    public void setLastLoginDate(java.time.LocalDateTime lastLoginDate) { this.lastLoginDate = lastLoginDate; }
    
    public Integer getFailedLoginAttempts() { return failedLoginAttempts; }
    public void setFailedLoginAttempts(Integer failedLoginAttempts) { this.failedLoginAttempts = failedLoginAttempts; }
    
    public Boolean getIsAccountLocked() { return isAccountLocked; }
    public void setIsAccountLocked(Boolean isAccountLocked) { this.isAccountLocked = isAccountLocked; }
    
    public Boolean getMustChangePassword() { return mustChangePassword; }
    public void setMustChangePassword(Boolean mustChangePassword) { this.mustChangePassword = mustChangePassword; }
    
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    
    public String getManagerId() { return managerId; }
    public void setManagerId(String managerId) { this.managerId = managerId; }
    
    /**
     * Inner class for Address
     */
    public static class Address {
        @JsonProperty("street")
        private String street;
        
        @JsonProperty("city")
        private String city;
        
        @JsonProperty("state")
        private String state;
        
        @JsonProperty("zipCode")
        private String zipCode;
        
        @JsonProperty("country")
        private String country;
        
        public Address() {}
        
        public Address(String street, String city, String state, String zipCode, String country) {
            this.street = street;
            this.city = city;
            this.state = state;
            this.zipCode = zipCode;
            this.country = country;
        }
        
        // Getters and Setters
        public String getStreet() { return street; }
        public void setStreet(String street) { this.street = street; }
        
        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }
        
        public String getState() { return state; }
        public void setState(String state) { this.state = state; }
        
        public String getZipCode() { return zipCode; }
        public void setZipCode(String zipCode) { this.zipCode = zipCode; }
        
        public String getCountry() { return country; }
        public void setCountry(String country) { this.country = country; }
        
        @Override
        public String toString() {
            return street + ", " + city + ", " + state + " " + zipCode + ", " + country;
        }
    }
}
