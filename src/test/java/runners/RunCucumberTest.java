package runners;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

/**
 * Main Cucumber Test Runner for Insurance Dashboard QA Framework
 * 
 * This runner executes ALL feature files in the organized directory structure:
 * - features/authentication/ - Login and security tests
 * - features/policies/ - Policy management tests  
 * - features/common/ - Dashboard and navigation tests
 * 
 * For focused test execution, use specialized runners:
 * - RunSmokeTests.java - Critical path tests only
 * - RunAuthenticationTests.java - Authentication tests only
 * - RunPolicyTests.java - Policy management tests only
 * 
 * Usage:
 * mvn test -Dtest=RunCucumberTest
 * 
 * With system properties:
 * mvn test -Dtest=RunCucumberTest -Dbrowser=chrome -Dlanguage=en -Dheadless=false
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features") // Recursively finds all .feature files in features/ and subdirectories
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "stepdefinitions") // Step definitions package
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty, html:target/cucumber-reports/cucumber-pretty.html, json:target/cucumber-reports/CucumberTestReport.json, io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm")
// Optional tag filtering - uncomment and modify as needed:
// @ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value = "@smoke")
// @ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value = "@regression")
// @ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value = "not @ignore")
public class RunCucumberTest {
    // This class serves as a trigger for JUnit Platform to run Cucumber tests.
    // No code implementation needed - annotations handle the configuration.
}
