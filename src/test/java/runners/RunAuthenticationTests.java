package runners;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.FILTER_TAGS_PROPERTY_NAME;

/**
 * Cucumber Test Runner for Authentication Tests
 * Runs only authentication-related test scenarios
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features/authentication")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "stepdefinitions")
@ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value = "@authentication")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty, html:target/cucumber-reports/authentication-tests.html, json:target/cucumber-reports/AuthenticationTestReport.json, io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm")
public class RunAuthenticationTests {
    // Authentication test runner - focused on login/logout functionality
}
