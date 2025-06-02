Feature: Dashboard Navigation
  As a user
  I want to navigate through the insurance dashboard
  So that I can access different sections of the application

  Background:
    Given I am logged in as "user@test.com" with password "Pass123!"
    And I should be redirected to the dashboard page

  Scenario: Navigate to Policy List from Dashboard
    When I navigate to Policy List from Dashboard
    Then I should be on the policy listing page

  Scenario: Verify Dashboard Welcome Message
    Then I should see the welcome message on dashboard
    And the welcome message should be localized correctly

  Scenario: Verify Dashboard Menu Options
    Then I should see all main navigation options
    And all menu items should be clickable

  @smoke
  Scenario: Dashboard Loads Successfully
    Then I should see the dashboard header
    And the dashboard should load within acceptable time
