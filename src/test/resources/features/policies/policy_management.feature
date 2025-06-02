Feature: Policy Management
  As a user
  I want to manage my insurance policies
  So that I can keep my policy information up to date

  Background:
    Given I am logged in as "user@test.com" with password "Pass123!"
    And I am on the policy listing page

  @smoke @policies
  Scenario: View All Policies
    Then I should see a list of my policies
    And each policy should display basic information

  @policies
  Scenario: Search for Specific Policy
    When I search for policy "POL12345"
    Then I should see only policies matching "POL12345"

  @policies
  Scenario: Sort Policies by Status
    When I sort policies by "Status"
    Then policies should be arranged by status alphabetically

  @policies
  Scenario: Sort Policies by Premium Amount
    When I sort policies by "Premium"
    Then policies should be arranged by premium amount

  @policies
  Scenario: View Policy Summary Information
    Then each policy in the list should show:
      | Field          |
      | Policy Number  |
      | Status         |
      | Type           |
      | Premium Amount |
      | Expiration Date|

  @policies @pagination
  Scenario: Navigate Through Policy Pages
    Given I have more than 10 policies
    When I navigate to the next page
    Then I should see the next set of policies
    And pagination controls should be updated
