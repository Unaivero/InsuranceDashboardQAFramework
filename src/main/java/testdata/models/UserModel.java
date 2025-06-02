package testdata.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import exceptions.TestDataException;
import utils.ErrorHandler;

/**
 * User test data model for authentication and user management testing
 */
public class UserModel extends BaseTestDataModel {
    
    @JsonProperty("username")
    private String username;
    
    @JsonProperty("password")
    private String password;
    
    @JsonProperty("firstName")
    private String firstName;
    
    @JsonProperty("lastName")
    private String lastName;
    
    @JsonProperty("email")
    private String email;
    
    @JsonProperty("role")
    private String role;
    
    @JsonProperty("isValid")
    private boolean isValid = true;
    
    @JsonProperty("isLocked")
    private boolean isLocked = false;
    
    @JsonProperty("lastLoginDate")
    private String lastLoginDate;
    
    @JsonProperty("failedLoginAttempts")
    private int failedLoginAttempts = 0;
    
    // Constructors
    public UserModel() {
        super();
        this.role = "USER";
    }
    
    public UserModel(String id, String username, String password, String firstName, String lastName, String email) {
        super(id);
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = "USER";
    }
    
    // Getters and Setters
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        ErrorHandler.validateNotEmpty(username, "username");
        this.username = username;
        setModifiedDate(getCurrentDate());
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        ErrorHandler.validateNotEmpty(password, "password");
        this.password = password;
        setModifiedDate(getCurrentDate());
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
        setModifiedDate(getCurrentDate());
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
        setModifiedDate(getCurrentDate());
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
        setModifiedDate(getCurrentDate());
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        ErrorHandler.validateNotEmpty(role, "role");
        this.role = role;
        setModifiedDate(getCurrentDate());
    }
    
    public boolean isValid() {
        return isValid;
    }
    
    public void setValid(boolean valid) {
        isValid = valid;
        setModifiedDate(getCurrentDate());
    }
    
    public boolean isLocked() {
        return isLocked;
    }
    
    public void setLocked(boolean locked) {
        isLocked = locked;
        setModifiedDate(getCurrentDate());
    }
    
    public String getLastLoginDate() {
        return lastLoginDate;
    }
    
    public void setLastLoginDate(String lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
        setModifiedDate(getCurrentDate());
    }
    
    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }
    
    public void setFailedLoginAttempts(int failedLoginAttempts) {
        this.failedLoginAttempts = Math.max(0, failedLoginAttempts);
        setModifiedDate(getCurrentDate());
    }
    
    // Utility methods
    public String getFullName() {
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        } else if (firstName != null) {
            return firstName;
        } else if (lastName != null) {
            return lastName;
        }
        return username;
    }
    
    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(role);
    }
    
    public boolean canLogin() {
        return isValid && !isLocked && isActive();
    }
    
    public void incrementFailedLoginAttempts() {
        this.failedLoginAttempts++;
        if (this.failedLoginAttempts >= 3) {
            this.isLocked = true;
        }
        setModifiedDate(getCurrentDate());
    }
    
    public void resetFailedLoginAttempts() {
        this.failedLoginAttempts = 0;
        this.isLocked = false;
        setModifiedDate(getCurrentDate());
    }
    
    @Override
    public void validate() throws TestDataException {
        if (username == null || username.trim().isEmpty()) {
            throw new TestDataException("User", "validation", "Username is required");
        }
        
        if (password == null || password.trim().isEmpty()) {
            throw new TestDataException("User", "validation", "Password is required");
        }
        
        if (email != null && !email.trim().isEmpty() && !isValidEmail(email)) {
            throw new TestDataException("User", "validation", "Invalid email format: " + email);
        }
        
        if (role == null || role.trim().isEmpty()) {
            throw new TestDataException("User", "validation", "Role is required");
        }
    }
    
    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }
    
    @Override
    public String getSummary() {
        return String.format("User[id=%s, username=%s, email=%s, role=%s, valid=%s, locked=%s]", 
                           getId(), username, email, role, isValid, isLocked);
    }
    
    @Override
    public BaseTestDataModel copy() {
        UserModel copy = new UserModel();
        copy.setId(this.getId());
        copy.setUsername(this.username);
        copy.setPassword(this.password);
        copy.setFirstName(this.firstName);
        copy.setLastName(this.lastName);
        copy.setEmail(this.email);
        copy.setRole(this.role);
        copy.setValid(this.isValid);
        copy.setLocked(this.isLocked);
        copy.setLastLoginDate(this.lastLoginDate);
        copy.setFailedLoginAttempts(this.failedLoginAttempts);
        copy.setDescription(this.getDescription());
        copy.setActive(this.isActive());
        return copy;
    }
}
