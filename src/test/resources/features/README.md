# Feature Files Organization

This document describes the organization and structure of feature files in the InsuranceDashboardQAFramework.

## 📁 **Directory Structure**

```
src/test/resources/features/
├── authentication/
│   ├── login.feature                    # Basic login functionality
│   └── authentication_security.feature # Security-focused auth tests
├── policies/
│   ├── filter_policies.feature         # Policy filtering functionality
│   ├── view_policy_details.feature     # Policy details viewing
│   ├── policy_management.feature       # General policy management
│   └── policy_claims.feature          # Claims management
└── common/
    └── dashboard_navigation.feature    # Dashboard navigation tests
```

## 🏷️ **Tagging Strategy**

### **Test Type Tags**
- `@smoke` - Critical path tests for quick validation
- `@regression` - Full regression test suite
- `@security` - Security-focused test scenarios

### **Feature Tags**  
- `@authentication` - Authentication and login tests
- `@policies` - Policy management functionality
- `@claims` - Claims management functionality
- `@dashboard` - Dashboard and navigation tests

### **Priority Tags**
- `@high` - High priority test scenarios
- `@medium` - Medium priority test scenarios  
- `@low` - Low priority test scenarios

### **Environment Tags**
- `@dev` - Development environment only
- `@staging` - Staging environment tests
- `@prod` - Production environment tests

## 🎯 **Test Execution Strategies**

### **Quick Smoke Test**
```bash
mvn test -Dtest=RunSmokeTests
```
Runs only scenarios tagged with `@smoke`

### **Authentication Tests Only**
```bash
mvn test -Dtest=RunAuthenticationTests
```
Runs only authentication-related scenarios

### **Policy Tests Only**
```bash
mvn test -Dtest=RunPolicyTests
```
Runs only policy management scenarios

### **Full Test Suite**
```bash
mvn test -Dtest=RunCucumberTest
```
Runs all feature files in all directories

## 📋 **Feature File Standards**

### **Naming Convention**
- Use snake_case for feature file names
- Use descriptive names that clearly indicate functionality
- Group related features in appropriate subdirectories

### **Feature Structure**
```gherkin
Feature: Clear, concise feature description
  As a [user type]
  I want [functionality]
  So that [business value]

  Background:
    # Common preconditions for all scenarios

  @tag1 @tag2
  Scenario: Descriptive scenario name
    Given [precondition]
    When [action]
    Then [expected result]
```

### **Tagging Guidelines**
- Every scenario should have at least one functional tag
- Critical scenarios must have `@smoke` tag
- Security tests must have `@security` tag
- Use multiple tags for better test organization

## 🔄 **Adding New Features**

1. **Determine the appropriate directory** based on functionality
2. **Create the feature file** following naming conventions
3. **Add appropriate tags** for test organization
4. **Update this documentation** if new directories are added
5. **Consider creating specialized runners** for new feature areas

## 📊 **Test Coverage Areas**

### **Authentication** (`/authentication/`)
- ✅ Basic login/logout
- ✅ Security validations
- ⏳ Password reset (future)
- ⏳ Multi-factor authentication (future)

### **Policies** (`/policies/`)
- ✅ Policy listing and filtering
- ✅ Policy details viewing
- ✅ Policy management operations
- ✅ Claims management
- ⏳ Policy creation (future)
- ⏳ Policy updates (future)

### **Common** (`/common/`)
- ✅ Dashboard navigation
- ⏳ User profile management (future)
- ⏳ Settings management (future)
- ⏳ Help and documentation (future)

---

*This organization structure supports maintainable, scalable test automation with clear separation of concerns and flexible execution strategies.*
