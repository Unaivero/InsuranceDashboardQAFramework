Feature: Policy API Management
  As an insurance system user
  I want to manage policies through the API
  So that I can perform CRUD operations and maintain policy data

  Background:
    Given I am authenticated as a "admin" user for API access

  @api @policies @smoke
  Scenario: Create a new auto insurance policy
    When I create a new policy via API with the following details:
      | policyNumber   | AUTO-001-2025    |
      | policyType     | Auto Insurance   |
      | status         | ACTIVE           |
      | customerName   | John Smith       |
      | premiumAmount  | 1200.00          |
      | deductibleAmount| 500.00          |
      | coverageAmount | 25000.00         |
      | description    | Comprehensive auto coverage |
    Then the API response should be successful
    And the current policy should be set
    And the returned policy should have policy number "AUTO-001-2025"
    And the returned policy should have status "ACTIVE"
    And the returned policy should have customer name "John Smith"
    And the returned policy should have premium amount "1200.00"

  @api @policies @smoke
  Scenario: Retrieve all policies
    When I retrieve all policies via API
    Then the API response should be successful
    And the API response should contain at least 1 policies

  @api @policies
  Scenario: Retrieve policies with pagination
    When I retrieve policies with pagination page 0 and size 5
    Then the API response should be successful
    And the API response should have pagination information
    And the API response should contain at least 1 policies

  @api @policies
  Scenario: Search policies by status
    Given I create a new "Auto Insurance" policy for customer "Jane Doe" with premium "800.00"
    When I search for policies with status "ACTIVE"
    Then the API response should be successful
    And all returned policies should have status "ACTIVE"

  @api @policies
  Scenario: Search policies by type
    Given I create a new "Home Insurance" policy for customer "Bob Johnson" with premium "1500.00"
    When I search for policies with type "Home Insurance"
    Then the API response should be successful
    And all returned policies should have type "Home Insurance"

  @api @policies
  Scenario: Update policy status
    Given I create a new "Life Insurance" policy for customer "Alice Brown" with premium "2000.00"
    And the current policy should be set
    When I update the policy status to "SUSPENDED"
    Then the API response should be successful
    And the returned policy should have status "SUSPENDED"

  @api @policies
  Scenario: Update policy premium amount
    Given I create a new "Auto Insurance" policy for customer "Mike Wilson" with premium "1000.00"
    And the current policy should be set
    When I update the policy premium amount to "1100.00"
    Then the API response should be successful
    And the returned policy should have premium amount "1100.00"

  @api @policies
  Scenario: Retrieve policy by policy number
    Given I create a new policy via API with the following details:
      | policyNumber   | HOME-002-2025    |
      | policyType     | Home Insurance   |
      | status         | ACTIVE           |
      | customerName   | Sarah Davis      |
      | premiumAmount  | 1800.00          |
    When I retrieve policy by policy number "HOME-002-2025"
    Then the API response should be successful
    And the returned policy should have policy number "HOME-002-2025"
    And the returned policy should have customer name "Sarah Davis"

  @api @policies @negative
  Scenario: Create policy with invalid data should fail
    When I create a new policy via API with the following details:
      | policyNumber   |                  |
      | policyType     | Auto Insurance   |
      | status         | ACTIVE           |
      | customerName   | John Smith       |
      | premiumAmount  | -100.00          |
    Then the API response should indicate failure

  @api @policies @negative
  Scenario: Retrieve non-existent policy should fail
    When I retrieve policy by ID "non-existent-policy-id"
    Then the API response should indicate failure

  @api @policies @negative
  Scenario: Update non-existent policy should fail
    When I delete policy with ID "non-existent-policy-id"
    Then the API response should indicate failure

  @api @policies @cleanup
  Scenario: Delete policy
    Given I create a new "Auto Insurance" policy for customer "Test User" with premium "500.00"
    And the current policy should be set
    When I delete the current policy
    Then the API response should be successful

  @api @policies @integration
  Scenario Outline: Create policies of different types
    When I create a new "<policyType>" policy for customer "<customerName>" with premium "<premium>"
    Then the API response should be successful
    And the returned policy should have customer name "<customerName>"
    And the returned policy should have premium amount "<premium>"

    Examples:
      | policyType      | customerName    | premium  |
      | Auto Insurance  | Driver One      | 1200.00  |
      | Home Insurance  | Homeowner Two   | 1800.00  |
      | Life Insurance  | Policyholder Three | 2500.00 |
      | Health Insurance| Patient Four    | 3000.00  |

  @api @policies @performance
  Scenario: Performance test - Create multiple policies
    When I create a new "Auto Insurance" policy for customer "User 1" with premium "1000.00"
    And I create a new "Home Insurance" policy for customer "User 2" with premium "1500.00"
    And I create a new "Life Insurance" policy for customer "User 3" with premium "2000.00"
    And I create a new "Health Insurance" policy for customer "User 4" with premium "2500.00"
    And I create a new "Business Insurance" policy for customer "User 5" with premium "3000.00"
    Then the API response should be successful
    When I retrieve all policies via API
    Then the API response should be successful
    And the API response should contain at least 5 policies

  @api @policies @business-rules
  Scenario: Validate business rules for policy creation
    When I create a new policy via API with the following details:
      | policyNumber   | BIZ-001-2025     |
      | policyType     | Business Insurance |
      | status         | ACTIVE           |
      | customerName   | ABC Corporation  |
      | premiumAmount  | 5000.00          |
      | deductibleAmount| 1000.00         |
      | coverageAmount | 100000.00        |
      | description    | Commercial general liability |
    Then the API response should be successful
    And the returned policy should have policy number "BIZ-001-2025"
    And the returned policy should have status "ACTIVE"

  @api @policies @edge-cases
  Scenario: Handle edge cases in policy data
    When I create a new policy via API with the following details:
      | policyNumber   | EDGE-001-2025    |
      | policyType     | Auto Insurance   |
      | status         | PENDING          |
      | customerName   | O'Connor & Smith Inc. |
      | premiumAmount  | 0.01             |
      | deductibleAmount| 0.00            |
      | coverageAmount | 999999.99        |
      | description    | Edge case test with special characters & symbols |
    Then the API response should be successful
    And the returned policy should have customer name "O'Connor & Smith Inc."

  @api @policies @data-integrity
  Scenario: Verify data integrity after policy operations
    Given I create a new policy via API with the following details:
      | policyNumber   | DATA-001-2025    |
      | policyType     | Auto Insurance   |
      | status         | ACTIVE           |
      | customerName   | Data Test User   |
      | premiumAmount  | 1000.00          |
    And the current policy should be set
    When I retrieve policy by policy number "DATA-001-2025"
    Then the API response should be successful
    And the returned policy should have policy number "DATA-001-2025"
    And the returned policy should have customer name "Data Test User"
    And the returned policy should have premium amount "1000.00"
    When I update the policy premium amount to "1200.00"
    Then the API response should be successful
    When I retrieve policy by policy number "DATA-001-2025"
    Then the API response should be successful
    And the returned policy should have premium amount "1200.00"
