Feature: Policy Claims Management
  As a user
  I want to view and manage claims for my policies
  So that I can track the status of my insurance claims

  Background:
    Given I am logged in as "user@test.com" with password "Pass123!"
    And I am on the policy detail page for "POL12345"

  @claims
  Scenario: View Claims History for a Policy
    When I view the claims section
    Then I should see a list of all claims for this policy
    And each claim should show claim number, date, status, and amount

  @claims
  Scenario: Filter Claims by Status
    When I filter claims by status "Open"
    Then I should see only open claims
    And the claims count should be updated

  @claims
  Scenario: View Claim Details
    When I click on claim "CLM001"
    Then I should see detailed information about the claim
    And I should see claim documents if available

  @claims
  Scenario: Submit New Claim
    When I click "Submit New Claim" button
    Then I should be redirected to the claim submission form
    And the form should be pre-populated with policy information

  @claims @smoke
  Scenario: Claims Section Loads Correctly
    Then the claims history table should be displayed
    And the table should have proper headers
    And claims should be sorted by date in descending order
