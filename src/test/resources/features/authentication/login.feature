Feature: Login Functionality
  As a user
  I want to log in to the insurance dashboard
  So that I can access my policy information

  Background:
    Given I am on the login page

  Scenario: Successful login with valid credentials
    When I enter "user@test.com" as username and "Pass123!" as password
    And I click the login button
    Then I should be redirected to the dashboard page

  Scenario: Failed login with invalid credentials
    When I enter "invalid@test.com" as username and "InvalidPass" as password
    And I click the login button
    Then I should see an error message "login.error.invalidCredentials"

  Scenario: Failed login with valid user but wrong password
    When I enter "user@test.com" as username and "wrongPass" as password
    And I click the login button
    Then I should see an error message "login.error.invalidCredentials"

  Scenario: Failed login with missing username
    When I enter "" as username and "Pass123!" as password
    And I click the login button
    Then I should see an error message "login.error.usernameRequired"

  Scenario: Failed login with missing password
    When I enter "user@test.com" as username and "" as password
    And I click the login button
    Then I should see an error message "login.error.passwordRequired"
