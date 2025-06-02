# Test Execution Guide

This guide explains how to run tests with the improved feature file organization.

## 🚀 **Quick Start**

### **Run All Tests**
```bash
mvn test -Dtest=RunCucumberTest
```

### **Run Smoke Tests Only**
```bash
mvn test -Dtest=RunSmokeTests
```

### **Run Specific Test Suites**
```bash
# Authentication tests only
mvn test -Dtest=RunAuthenticationTests

# Policy management tests only  
mvn test -Dtest=RunPolicyTests
```

## ⚙️ **Configuration Options**

### **Browser Selection**
```bash
mvn test -Dtest=RunCucumberTest -Dbrowser=chrome
mvn test -Dtest=RunCucumberTest -Dbrowser=firefox
mvn test -Dtest=RunCucumberTest -Dbrowser=edge
```

### **Language Configuration**
```bash
mvn test -Dtest=RunCucumberTest -Dlanguage=en
mvn test -Dtest=RunCucumberTest -Dlanguage=es
```

### **Headless Execution**
```bash
mvn test -Dtest=RunCucumberTest -Dheadless=true
```

### **Combined Configuration**
```bash
mvn test -Dtest=RunSmokeTests -Dbrowser=chrome -Dlanguage=en -Dheadless=false
```

## 📁 **Test Organization**

### **Feature Files Structure**
```
features/
├── authentication/        # Login and security tests
│   ├── login.feature
│   └── authentication_security.feature
├── policies/             # Policy management tests
│   ├── filter_policies.feature
│   ├── view_policy_details.feature
│   ├── policy_management.feature
│   └── policy_claims.feature
└── common/               # Dashboard and navigation
    └── dashboard_navigation.feature
```

### **Test Runners**
```
runners/
├── RunCucumberTest.java        # All tests
├── RunSmokeTests.java          # Critical path only (@smoke)
├── RunAuthenticationTests.java # Authentication only
└── RunPolicyTests.java         # Policy management only
```

## 🏷️ **Tag-Based Execution**

You can also run tests by tags using Maven command line:

### **By Test Type**
```bash
# Run all smoke tests
mvn test -Dcucumber.filter.tags="@smoke"

# Run regression tests
mvn test -Dcucumber.filter.tags="@regression"

# Run security tests
mvn test -Dcucumber.filter.tags="@security"
```

### **By Feature Area**
```bash
# Run authentication tests
mvn test -Dcucumber.filter.tags="@authentication"

# Run policy tests
mvn test -Dcucumber.filter.tags="@policies"

# Run claims tests
mvn test -Dcucumber.filter.tags="@claims"
```

### **Complex Tag Expressions**
```bash
# Run smoke tests but exclude security tests
mvn test -Dcucumber.filter.tags="@smoke and not @security"

# Run authentication or policy tests
mvn test -Dcucumber.filter.tags="@authentication or @policies"

# Run high priority tests
mvn test -Dcucumber.filter.tags="@high"
```

## 📊 **Reports and Output**

### **HTML Reports**
After test execution, reports are generated in:
```
target/cucumber-reports/
├── cucumber-pretty.html      # Main test report
├── smoke-tests.html          # Smoke test report
├── authentication-tests.html # Authentication test report
└── policy-tests.html         # Policy test report
```

### **JSON Reports**
JSON reports for CI/CD integration:
```
target/cucumber-reports/
├── CucumberTestReport.json
├── SmokeTestReport.json
├── AuthenticationTestReport.json
└── PolicyTestReport.json
```

### **Allure Reports**
Generate Allure reports:
```bash
mvn allure:report
mvn allure:serve
```

## 🔧 **Parallel Execution**

The framework supports parallel execution (configured in pom.xml):
```bash
mvn test -Dtest=RunCucumberTest -DthreadCount=4
```

## 🐛 **Debugging**

### **Run Single Feature**
```bash
mvn test -Dtest=RunCucumberTest -Dcucumber.options="src/test/resources/features/authentication/login.feature"
```

### **Run Single Scenario**
```bash
mvn test -Dtest=RunCucumberTest -Dcucumber.options="src/test/resources/features/authentication/login.feature:10"
```

### **Verbose Output**
```bash
mvn test -Dtest=RunCucumberTest -Dcucumber.options="--plugin pretty --plugin html:target/cucumber-reports"
```

## 📋 **Best Practices**

1. **Use appropriate runners** for focused testing
2. **Tag scenarios properly** for better organization
3. **Run smoke tests** before full regression
4. **Use parallel execution** for faster feedback
5. **Check reports** for detailed test results
6. **Combine system properties** for environment-specific testing

---

*This organized structure supports efficient test execution with flexible options for different testing needs.*
