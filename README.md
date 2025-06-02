# 🛡️ Insurance Dashboard QA Automation Framework

A comprehensive, enterprise-grade test automation framework designed specifically for insurance dashboard applications. Built with Java, Selenium WebDriver, Cucumber BDD, and modern testing practices.

## 🚀 Features

### **Core Testing Capabilities**
- **Web UI Automation** - Selenium WebDriver 4.20+ with modern locator strategies
- **API Testing** - REST Assured integration for comprehensive API validation
- **BDD Testing** - Cucumber 7.15+ with Gherkin feature files
- **Cross-Browser Testing** - Chrome, Firefox, Edge, Safari support
- **Parallel Execution** - Multi-threaded test execution for faster feedback

### **Insurance Domain Specific**
- **Policy Management Testing** - Complete policy lifecycle validation
- **Claims Processing** - End-to-end claims workflow automation  
- **Authentication & Security** - Comprehensive security testing scenarios
- **Dashboard Navigation** - User interface and navigation validation
- **Data-Driven Testing** - JSON/CSV test data management

### **Enterprise-Grade Features**
- **Allure Reporting** - Rich HTML reports with screenshots and logs
- **Jenkins Integration** - CI/CD pipeline ready with declarative pipelines
- **Test Data Management** - Modular and maintainable test data structure
- **Error Handling** - Robust error recovery and logging mechanisms
- **Performance Monitoring** - Built-in performance and load time validation

## 📁 Project Structure

```
InsuranceDashboardQAFramework/
├── src/
│   ├── main/java/                     # Main application code
│   └── test/
│       ├── java/
│       │   ├── api/                   # API testing classes
│       │   ├── runners/               # Cucumber test runners
│       │   ├── stepdefinitions/       # BDD step definitions
│       │   └── testdata/              # Test data models
│       └── resources/
│           ├── features/              # Cucumber feature files
│           │   ├── authentication/    # Login & security tests
│           │   ├── policies/          # Policy management tests
│           │   └── common/            # Common navigation tests
│           ├── testdata/              # JSON/CSV test data
│           └── logback-test.xml       # Logging configuration
├── jenkins/                           # CI/CD pipeline configurations
├── temp_files/                        # Temporary test artifacts
├── pom.xml                           # Maven dependencies & configuration
└── README.md                         # This file
```

## 🛠️ Tech Stack

| Category | Technology | Version |
|----------|------------|---------|
| **Language** | Java | 11+ |
| **Build Tool** | Maven | 3.6+ |
| **UI Automation** | Selenium WebDriver | 4.20.0 |
| **BDD Framework** | Cucumber | 7.15.0 |
| **Test Framework** | JUnit 5 | 5.10.2 |
| **API Testing** | REST Assured | 5.4.0 |
| **Reporting** | Allure | 2.27.0 |
| **Logging** | Logback | 1.4.14 |
| **JSON Processing** | Jackson | 2.16.1 |
| **Driver Management** | WebDriverManager | 5.8.0 |

## 🚦 Quick Start

### **Prerequisites**
- Java 11 or higher
- Maven 3.6 or higher
- Chrome browser (latest version)
- Git

### **Installation**
```bash
# Clone the repository
git clone https://github.com/Unaivero/InsuranceDashboardQAFramework.git
cd InsuranceDashboardQAFramework

# Install dependencies
mvn clean compile

# Verify setup
mvn test-compile
```

### **Run Tests**
```bash
# Run all tests
mvn test

# Run smoke tests only
mvn test -Dtest=RunSmokeTests

# Run specific feature area
mvn test -Dtest=RunAuthenticationTests
mvn test -Dtest=RunPolicyTests

# Run with specific browser
mvn test -Dbrowser=chrome -Dheadless=false

# Run tests with tags
mvn test -Dcucumber.filter.tags="@smoke"
mvn test -Dcucumber.filter.tags="@regression"
```

## 🎯 Test Categories

### **Test Runners Available**
- **`RunCucumberTest`** - Complete test suite execution
- **`RunSmokeTests`** - Critical path validation (fastest)
- **`RunAuthenticationTests`** - Login and security focused
- **`RunPolicyTests`** - Policy management workflows

### **Tag-Based Execution**
```bash
# Test Types
@smoke          # Critical functionality tests
@regression     # Full regression suite
@security       # Security validation tests

# Feature Areas  
@authentication # Login and access tests
@policies       # Policy management tests
@claims         # Claims processing tests
@dashboard      # UI navigation tests

# Priority Levels
@high           # High priority tests
@medium         # Medium priority tests  
@low            # Low priority tests
```

## 📊 Test Coverage

### **Authentication & Security** (7 scenarios)
- User login/logout workflows
- Session timeout validation
- SQL injection prevention
- Account lockout mechanisms
- Multi-factor authentication
- Password security validation
- Remember me functionality

### **Policy Management** (12 scenarios)
- Policy search and filtering
- Policy details viewing
- Policy creation workflows
- Policy modification tracking
- Policy document management
- Policy renewal processes
- Policy comparison features
- Pagination and sorting

### **Claims Processing** (8 scenarios)
- Claims submission workflows
- Claims status tracking
- Claims history viewing
- Claims documentation upload
- Claims approval processes
- Claims filtering and search
- Claims reporting features

### **Dashboard & Navigation** (10 scenarios)
- Dashboard loading validation
- Menu navigation testing
- Welcome message verification
- Multi-language support
- Responsive design validation
- Performance monitoring
- Accessibility compliance
- Cross-browser compatibility

## 📈 Reporting

### **Allure Reports**
```bash
# Generate Allure report
mvn allure:report

# Serve Allure report locally
mvn allure:serve

# View report
http://localhost:port/allure-results
```

### **Jenkins Integration**
The framework includes Jenkins pipeline configuration:
- Automated test execution on code commits
- Parallel test execution across multiple environments
- Allure report publishing
- Slack/email notifications on test failures
- Test trend analysis and reporting

## 🔧 Configuration

### **Browser Configuration**
```bash
# Chrome (default)
mvn test -Dbrowser=chrome

# Firefox
mvn test -Dbrowser=firefox

# Headless mode
mvn test -Dheadless=true

# Mobile testing
mvn test -Dbrowser=mobile
```

### **Environment Configuration**
```bash
# Test environments
mvn test -Denvironment=dev      # Development
mvn test -Denvironment=staging  # Staging  
mvn test -Denvironment=prod     # Production

# Language/Locale
mvn test -Dlanguage=en          # English
mvn test -Dlanguage=es          # Spanish
mvn test -Dlanguage=fr          # French
```

## 🏗️ Framework Architecture

### **Page Object Model**
- Modular page objects for maintainability
- WebDriverManager for automated driver management
- Explicit waits and robust element interaction
- Configurable timeouts and retry mechanisms

### **Data-Driven Testing**
- JSON-based test data management
- CSV support for large datasets
- Environment-specific test data
- Dynamic data generation capabilities

### **API Testing Layer**
- REST Assured integration
- JSON schema validation
- API response validation
- Request/response logging
- Authentication handling

## 🔍 Quality Assurance

### **Code Quality**
- Maven enforcer plugin for dependency management
- Consistent coding standards and conventions
- Comprehensive error handling and logging
- Modular and maintainable code structure

### **Test Quality**
- BDD scenarios with clear acceptance criteria
- Comprehensive test coverage across user journeys
- Cross-browser and cross-platform validation
- Performance and security testing integration

## 📋 Documentation Files

- **`API_TESTING_IMPLEMENTATION_COMPLETED.md`** - API testing setup and usage
- **`CRITICAL_BUGS_FIXED.md`** - Bug fixes and resolutions
- **`ERROR_HANDLING_IMPLEMENTATION.md`** - Error handling strategies
- **`FEATURE_ORGANIZATION_COMPLETED.md`** - Test organization guide
- **`LOGGING_IMPLEMENTATION.md`** - Logging configuration guide
- **`POLICYDETAILS_*.md`** - Policy testing implementation details
- **`TEST_DATA_MANAGEMENT_IMPLEMENTATION.md`** - Test data management
- **`TEST_EXECUTION_GUIDE.md`** - Detailed execution instructions

## 🤝 Contributing

1. **Fork the repository**
2. **Create a feature branch** (`git checkout -b feature/new-test-scenario`)
3. **Implement your changes** with proper test coverage
4. **Run the test suite** to ensure no regressions
5. **Commit your changes** (`git commit -m 'Add comprehensive claims testing'`)
6. **Push to the branch** (`git push origin feature/new-test-scenario`)
7. **Create a Pull Request** with detailed description

### **Contribution Guidelines**
- Follow existing code structure and naming conventions
- Add appropriate Cucumber scenarios for new features
- Include proper error handling and logging
- Update documentation for new capabilities
- Ensure cross-browser compatibility

## 📞 Support

For questions, issues, or contributions:

- **Create an Issue** - Use GitHub Issues for bug reports and feature requests
- **Documentation** - Check the comprehensive documentation files in the repository
- **Best Practices** - Review existing test scenarios for implementation patterns

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🏆 Achievements

- ✅ **37+ Comprehensive Test Scenarios** covering critical insurance workflows
- ✅ **Enterprise-Grade Architecture** with modular, maintainable code
- ✅ **CI/CD Integration** with Jenkins pipeline automation
- ✅ **Rich Reporting** with Allure test reports and analytics
- ✅ **Cross-Browser Testing** with automated browser management
- ✅ **BDD Implementation** with business-readable test scenarios
- ✅ **API Testing Integration** for complete validation coverage
- ✅ **Performance Monitoring** with built-in timing and metrics

---

⚡ **Ready for enterprise-scale insurance application testing with comprehensive automation coverage and robust reporting capabilities.**
