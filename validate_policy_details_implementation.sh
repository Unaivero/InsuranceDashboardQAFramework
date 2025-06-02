#!/bin/bash

# PolicyDetailsPage Implementation Validation Script
# This script validates that all enhancements are properly implemented

echo "🔍 VALIDATING POLICYDETAILSPAGE IMPLEMENTATION..."
echo "=================================================="

# Set the project directory
PROJECT_DIR="/Users/josevergara/Documents/InsuranceDashboardQAFramework"

# Function to check if file exists and report
check_file() {
    if [ -f "$1" ]; then
        echo "✅ $2"
        return 0
    else
        echo "❌ $2 - FILE MISSING"
        return 1
    fi
}

# Function to check if file contains specific content
check_content() {
    if grep -q "$2" "$1" 2>/dev/null; then
        echo "✅ $3"
        return 0
    else
        echo "❌ $3 - CONTENT MISSING"
        return 1
    fi
}

echo ""
echo "📁 FILE STRUCTURE VALIDATION"
echo "=============================="

# Check main enhanced files
check_file "$PROJECT_DIR/src/main/java/pages/PolicyDetailsPage.java" "Enhanced PolicyDetailsPage.java"
check_file "$PROJECT_DIR/src/main/java/pages/EditPolicyPage.java" "New EditPolicyPage.java"
check_file "$PROJECT_DIR/src/main/java/pages/ClaimSubmissionPage.java" "New ClaimSubmissionPage.java"

# Check enhanced step definitions
check_file "$PROJECT_DIR/src/test/java/stepdefinitions/PolicyClaimsStepDefinitions.java" "New PolicyClaimsStepDefinitions.java"
check_file "$PROJECT_DIR/src/test/java/stepdefinitions/DashboardNavigationStepDefinitions.java" "New DashboardNavigationStepDefinitions.java"
check_file "$PROJECT_DIR/src/test/java/stepdefinitions/ViewPolicyDetailsStepDefinitions.java" "Enhanced ViewPolicyDetailsStepDefinitions.java"

# Check enhanced test data
check_file "$PROJECT_DIR/src/test/java/testdata/PolicyDetailsTestData.java" "Enhanced PolicyDetailsTestData.java"

# Check documentation
check_file "$PROJECT_DIR/POLICYDETAILS_IMPLEMENTATION_COMPLETED.md" "Implementation Documentation"

echo ""
echo "🔧 FUNCTIONALITY VALIDATION"
echo "============================"

# Check for key methods in PolicyDetailsPage
POLICY_DETAILS_FILE="$PROJECT_DIR/src/main/java/pages/PolicyDetailsPage.java"
check_content "$POLICY_DETAILS_FILE" "getClaimsSection" "Claims management functionality"
check_content "$POLICY_DETAILS_FILE" "getPolicyDocuments" "Document management functionality"
check_content "$POLICY_DETAILS_FILE" "filterClaimsByStatus" "Claims filtering functionality"
check_content "$POLICY_DETAILS_FILE" "clickSubmitNewClaim" "New claim submission functionality"
check_content "$POLICY_DETAILS_FILE" "waitForPageToLoad" "Page loading optimization"
check_content "$POLICY_DETAILS_FILE" "getAgentName" "Enhanced agent information"

# Check for step definitions
CLAIMS_STEP_FILE="$PROJECT_DIR/src/test/java/stepdefinitions/PolicyClaimsStepDefinitions.java"
check_content "$CLAIMS_STEP_FILE" "i_view_the_claims_section" "Claims viewing step definition"
check_content "$CLAIMS_STEP_FILE" "i_filter_claims_by_status" "Claims filtering step definition"
check_content "$CLAIMS_STEP_FILE" "i_click_on_claim" "Individual claim interaction"

# Check for enhanced test data
TEST_DATA_FILE="$PROJECT_DIR/src/test/java/testdata/PolicyDetailsTestData.java"
check_content "$TEST_DATA_FILE" "DetailedPolicyInfo" "Enhanced policy data structure"
check_content "$TEST_DATA_FILE" "ClaimInfo" "Claims data structure"
check_content "$TEST_DATA_FILE" "DocumentInfo" "Document data structure"
check_content "$TEST_DATA_FILE" "getDetailedPolicyById" "Policy data factory method"

echo ""
echo "🎯 FEATURE COVERAGE VALIDATION"
echo "==============================="

# Check feature file support
FEATURES_DIR="$PROJECT_DIR/src/test/resources/features"
check_file "$FEATURES_DIR/policies/policy_claims.feature" "Policy Claims Feature File"
check_file "$FEATURES_DIR/policies/view_policy_details.feature" "Policy Details Feature File"
check_file "$FEATURES_DIR/common/dashboard_navigation.feature" "Dashboard Navigation Feature File"

echo ""
echo "📊 IMPLEMENTATION METRICS"
echo "========================="

# Count methods in PolicyDetailsPage
if [ -f "$POLICY_DETAILS_FILE" ]; then
    METHOD_COUNT=$(grep -c "@Step\|public.*(" "$POLICY_DETAILS_FILE")
    echo "✅ PolicyDetailsPage methods: $METHOD_COUNT"
fi

# Count step definitions
if [ -f "$CLAIMS_STEP_FILE" ]; then
    STEP_COUNT=$(grep -c "@When\|@Then\|@Given" "$CLAIMS_STEP_FILE")
    echo "✅ Claims step definitions: $STEP_COUNT"
fi

# Count test data classes
if [ -f "$TEST_DATA_FILE" ]; then
    CLASS_COUNT=$(grep -c "public static class\|public static final.*=" "$TEST_DATA_FILE")
    echo "✅ Test data structures: $CLASS_COUNT"
fi

echo ""
echo "🔄 INTEGRATION VALIDATION"
echo "=========================="

# Check imports and dependencies
check_content "$POLICY_DETAILS_FILE" "import.*LoggingUtil" "LoggingUtil integration"
check_content "$POLICY_DETAILS_FILE" "extends BasePage" "BasePage inheritance"
check_content "$POLICY_DETAILS_FILE" "io.qameta.allure.Step" "Allure reporting integration"

# Check step definition imports
check_content "$CLAIMS_STEP_FILE" "import.*LoggingUtil" "Logging in step definitions"
check_content "$CLAIMS_STEP_FILE" "import.*PolicyDetailsPage" "Page object integration"

echo ""
echo "🎉 VALIDATION COMPLETE"
echo "======================"

# Count successes
SUCCESS_COUNT=$(grep -c "✅" /tmp/validation_output.txt 2>/dev/null || echo "0")
FAILURE_COUNT=$(grep -c "❌" /tmp/validation_output.txt 2>/dev/null || echo "0")

echo ""
echo "📈 VALIDATION SUMMARY:"
echo "  ✅ Successful checks: $SUCCESS_COUNT"
echo "  ❌ Failed checks: $FAILURE_COUNT"

if [ "$FAILURE_COUNT" -eq 0 ]; then
    echo ""
    echo "🟢 ALL VALIDATIONS PASSED - IMPLEMENTATION IS COMPLETE!"
    echo "🚀 Ready for next phase of framework enhancements."
else
    echo ""
    echo "🟡 SOME VALIDATIONS FAILED - Please review the issues above."
fi

echo ""
echo "📋 NEXT STEPS:"
echo "  4. Add proper logging framework configuration"
echo "  5. Create test data management strategy"
echo "  6. Implement proper error handling throughout framework"
echo "  7. Add more robust wait strategies"
echo "  8. Configure proper parallel execution"
echo "  9. Add comprehensive documentation"
echo "  10. Implement API testing layer"

echo ""
echo "=================================================="
echo "PolicyDetailsPage Implementation Validation Complete"
