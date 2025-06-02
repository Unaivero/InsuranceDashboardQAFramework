Feature: Authentication Security
  As a system
  I want to ensure secure authentication
  So that unauthorized users cannot access the system

  Background:
    Given I am on the login page

  @security @authentication
  Scenario: Session Timeout After Inactivity
    Given I am logged in as "user@test.com" with password "Pass123!"
    When I remain inactive for more than session timeout period
    Then I should be automatically logged out
    And I should be redirected to the login page

  @security @authentication
  Scenario: Prevent SQL Injection in Login
    When I enter "admin'; DROP TABLE users; --" as username and "password" as password
    And I click the login button
    Then I should see an error message "login.error.invalidCredentials"
    And the system should remain secure

  @security @authentication
  Scenario: Account Lockout After Multiple Failed Attempts
    When I attempt login with wrong password 5 times
    Then the account should be temporarily locked
    And I should see an error message about account lockout

  @security @authentication
  Scenario: Case Sensitive Password Validation
    When I enter "user@test.com" as username and "pass123!" as password
    And I click the login button
    Then I should see an error message "login.error.invalidCredentials"

  @authentication
  Scenario: Remember Me Functionality
    When I enter "user@test.com" as username and "Pass123!" as password
    And I check the "Remember Me" checkbox
    And I click the login button
    Then I should be redirected to the dashboard page
    When I close and reopen the browser
    Then I should still be logged in
