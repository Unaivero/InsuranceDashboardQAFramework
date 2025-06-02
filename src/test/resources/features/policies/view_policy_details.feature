Feature: View Policy Details
  As a user
  I want to view the details of a specific policy
  So that I can get comprehensive information about it

  Background: User is logged in and on policy listing page
    Given I am logged in as "user@test.com" with password "Pass123!"
    And I am on the policy listing page

  Scenario: View Details of an Existing Policy
    When I click on policy with ID "POL12345"
    Then I should be on the policy detail page for "POL12345"
    And I should see the policy information

  Scenario: View Complete Policy Information
    When I click on policy with ID "POL12345"
    Then I should be on the policy detail page for "POL12345"
    And I should see policy details including status, type, and coverage information

  Scenario: View Policy Documents Section
    When I click on policy with ID "POL12345"
    Then I should be on the policy detail page for "POL12345"
    And I should see policy documents section

  Scenario: View Claims History
    When I click on policy with ID "POL12345"
    Then I should be on the policy detail page for "POL12345"
    And I should see claims history table

  Scenario: Navigate Back to Policy List
    When I click on policy with ID "POL12345"
    And I should be on the policy detail page for "POL12345"
    When I navigate back to the policy list
    Then I should be on the policy listing page

  Scenario: Edit Policy from Details Page
    When I click on policy with ID "POL12345"
    And I should be on the policy detail page for "POL12345"
    When I click the edit policy button
    # Then I should be redirected to edit policy page (would need EditPolicyPage implementation)

  Scenario: Download Policy Document
    When I click on policy with ID "POL12345"
    And I should be on the policy detail page for "POL12345"
    When I click the download policy button
    # Then the policy document should be downloaded (would need file download verification)
