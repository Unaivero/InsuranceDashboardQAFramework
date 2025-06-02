package runners;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.FILTER_TAGS_PROPERTY_NAME;

/**
 * Cucumber Test Runner for Policy Management Tests
 * Runs only policy-related test scenarios
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features/policies")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "stepdefinitions")
@ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value = "@policies")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty, html:target/cucumber-reports/policy-tests.html, json:target/cucumber-reports/PolicyTestReport.json, io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm")
public class RunPolicyTests {
    // Policy test runner - focused on policy management functionality
}
