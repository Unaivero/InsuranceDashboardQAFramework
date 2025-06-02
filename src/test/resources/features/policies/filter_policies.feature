Feature: Filter Policies
  As a user
  I want to filter policies
  So that I can view specific types of policies

  Background: User is logged in
    Given I am logged in as "user@test.com" with password "Pass123!"
    And I am on the policy listing page

  Scenario: Filter Active Policies
    When I filter policies by status "Active"
    Then I should see only "active" policies in the list

  Scenario: Filter Expired Policies
    When I filter policies by status "Expired"
    Then I should see only "expired" policies in the list
