package runners;

import io.cucumber.junit.platform.engine.Constants;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

/**
 * API Test Runner
 * 
 * Cucumber test runner specifically for API tests
 * Executes all API-related feature files and scenarios
 * 
 * @author Insurance Dashboard QA Framework
 * @version 1.0
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features/api")
@ConfigurationParameter(key = Constants.PLUGIN_PROPERTY_NAME, 
                       value = "pretty," +
                               "html:target/cucumber-reports/api," +
                               "json:target/cucumber-reports/api/Cucumber.json," +
                               "junit:target/cucumber-reports/api/Cucumber.xml," +
                               "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm")
@ConfigurationParameter(key = Constants.GLUE_PROPERTY_NAME, 
                       value = "api.stepdefinitions,stepdefinitions")
@ConfigurationParameter(key = Constants.FEATURES_PROPERTY_NAME, 
                       value = "src/test/resources/features/api")
@ConfigurationParameter(key = Constants.FILTER_TAGS_PROPERTY_NAME, 
                       value = "@api")
@ConfigurationParameter(key = Constants.EXECUTION_DRY_RUN_PROPERTY_NAME, 
                       value = "false")
@ConfigurationParameter(key = Constants.PLUGIN_PUBLISH_ENABLED_PROPERTY_NAME, 
                       value = "false")
public class RunApiTests {
    
    // This class serves as the entry point for running API tests
    // All configuration is handled through annotations
    
    /**
     * To run specific API test tags, use:
     * 
     * @api - All API tests
     * @api and @smoke - API smoke tests
     * @api and @policies - Policy API tests
     * @api and @negative - API negative tests
     * @api and @performance - API performance tests
     * @api and @integration - API integration tests
     * 
     * Example: mvn test -Dtest=RunApiTests -Dcucumber.filter.tags="@api and @smoke"
     */
}
