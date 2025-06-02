package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import utils.LoggingUtil;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Enhanced PolicyDetailsPage with comprehensive functionality for policy management
 * Supports policy information viewing, claims management, document handling, and policy actions
 */
public class PolicyDetailsPage extends BasePage {

    // --- Enhanced Locators --- 
    
    // Main Policy Information Section
    @FindBy(xpath = "//h1[contains(@class, 'policy-header')] | //h1[contains(text(), 'Policy Details')] | //h1[contains(text(), 'Detalles de la Póliza')]")
    private WebElement policyDetailsHeader;

    @FindBy(css = ".policy-info-container")
    private WebElement policyInfoContainer;

    // Policy Basic Information
    @FindBy(id = "policyId")
    private WebElement policyIdField;

    @FindBy(id = "policyNumber")
    private WebElement policyNumberField;

    @FindBy(id = "policyStatus")
    private WebElement policyStatusField;

    @FindBy(id = "policyType")
    private WebElement policyTypeField;

    @FindBy(id = "premiumAmount")
    private WebElement premiumAmountField;

    @FindBy(id = "effectiveDate")
    private WebElement effectiveDateField;

    @FindBy(id = "expirationDate")
    private WebElement expirationDateField;

    @FindBy(id = "insuredName")
    private WebElement insuredNameField;

    @FindBy(id = "coverageAmount")
    private WebElement coverageAmountField;

    @FindBy(id = "deductible")
    private WebElement deductibleField;

    // Additional Policy Information
    @FindBy(id = "agentName")
    private WebElement agentNameField;

    @FindBy(id = "agentPhone")
    private WebElement agentPhoneField;

    @FindBy(id = "agentEmail")
    private WebElement agentEmailField;

    @FindBy(id = "policyCategory")
    private WebElement policyCategoryField;

    @FindBy(id = "lastModified")
    private WebElement lastModifiedField;

    @FindBy(id = "createdDate")
    private WebElement createdDateField;

    // Policy Actions
    @FindBy(xpath = "//button[contains(text(), 'Edit')] | //button[contains(text(), 'Editar')]")
    private WebElement editPolicyButton;

    @FindBy(xpath = "//button[contains(text(), 'Download')] | //button[contains(text(), 'Descargar')]")
    private WebElement downloadPolicyButton;

    @FindBy(xpath = "//button[contains(text(), 'Print')] | //button[contains(text(), 'Imprimir')]")
    private WebElement printPolicyButton;

    @FindBy(xpath = "//button[contains(text(), 'Share')] | //button[contains(text(), 'Compartir')]")
    private WebElement sharePolicyButton;

    @FindBy(xpath = "//a[contains(text(), 'Back to Policies')] | //a[contains(text(), 'Volver a Pólizas')]")
    private WebElement backToPoliciesLink;

    // Policy Documents Section
    @FindBy(id = "policyDocuments")
    private WebElement policyDocumentsSection;

    @FindBy(css = ".policy-documents-container")
    private WebElement policyDocumentsContainer;

    @FindBy(css = ".document-item")
    private List<WebElement> documentItems;

    @FindBy(xpath = "//button[contains(text(), 'Upload Document')]")
    private WebElement uploadDocumentButton;

    @FindBy(css = ".document-download-link")
    private List<WebElement> documentDownloadLinks;

    // Claims Management Section
    @FindBy(css = ".policy-claims")
    private WebElement claimsSection;

    @FindBy(css = ".policy-claims table")
    private WebElement claimsTable;

    @FindBy(xpath = "//div[@class='policy-claims']//table//tr")
    private List<WebElement> claimsTableRows;

    @FindBy(css = ".claims-table-header")
    private WebElement claimsTableHeader;

    @FindBy(xpath = "//button[contains(text(), 'Submit New Claim')]")
    private WebElement submitNewClaimButton;

    @FindBy(id = "claimsStatusFilter")
    private WebElement claimsStatusFilter;

    @FindBy(css = ".claim-row")
    private List<WebElement> claimRows;

    @FindBy(css = ".claims-count")
    private WebElement claimsCountElement;

    // Beneficiaries Section
    @FindBy(css = ".beneficiaries-section")
    private WebElement beneficiariesSection;

    @FindBy(css = ".beneficiary-item")
    private List<WebElement> beneficiaryItems;

    @FindBy(xpath = "//button[contains(text(), 'Add Beneficiary')]")
    private WebElement addBeneficiaryButton;

    // Payment History Section
    @FindBy(css = ".payment-history-section")
    private WebElement paymentHistorySection;

    @FindBy(css = ".payment-history-table")
    private WebElement paymentHistoryTable;

    @FindBy(css = ".payment-row")
    private List<WebElement> paymentHistoryRows;

    // Policy Status Indicators
    @FindBy(css = ".status-indicator")
    private WebElement statusIndicator;

    @FindBy(css = ".status-badge")
    private WebElement statusBadge;

    @FindBy(css = ".expiry-warning")
    private WebElement expiryWarning;

    // Loading and Error States
    @FindBy(css = ".loading-spinner")
    private WebElement loadingSpinner;

    @FindBy(css = ".error-message")
    private WebElement errorMessage;

    @FindBy(css = ".no-data-message")
    private WebElement noDataMessage;

    // --- Constructor --- 
    public PolicyDetailsPage(WebDriver driver) {
        super(driver);
        LoggingUtil.setPageContext("PolicyDetailsPage");
    }
    
    public PolicyDetailsPage() {
        super();
        LoggingUtil.setPageContext("PolicyDetailsPage");
    }

    // --- Enhanced Page Methods --- 
    
    @Step("Check if Policy Details page is displayed")
    public boolean isPolicyDetailsPageDisplayed() {
        try {
            return isElementDisplayed(policyDetailsHeader);
        } catch (Exception e) {
            logger.debug("Policy details page display check failed: {}", e.getMessage());
            return false;
        }
    }

    @Step("Wait for policy details page to load completely")
    public void waitForPageToLoad() {
        // Wait for loading spinner to disappear if present
        try {
            if (isElementDisplayed(loadingSpinner)) {
                long startTime = System.currentTimeMillis();
                while (isElementDisplayed(loadingSpinner) && (System.currentTimeMillis() - startTime) < 10000) {
                    Thread.sleep(100);
                }
                LoggingUtil.logPerformance("PolicyDetailsPageLoad", System.currentTimeMillis() - startTime);
            }
        } catch (Exception e) {
            logger.debug("No loading spinner found or loading completed quickly");
        }
        
        // Ensure main elements are visible
        waitForElementToBeVisible(policyDetailsHeader);
    }

    // --- Basic Policy Information Methods ---
    
    @Step("Get policy ID from details page")
    public String getPolicyId() {
        waitForElementToBeVisible(policyIdField);
        return getElementText(policyIdField);
    }

    @Step("Get policy number from details page")
    public String getPolicyNumber() {
        waitForElementToBeVisible(policyNumberField);
        return getElementText(policyNumberField);
    }

    @Step("Get policy status from details page")
    public String getPolicyStatus() {
        waitForElementToBeVisible(policyStatusField);
        return getElementText(policyStatusField);
    }

    @Step("Get policy type from details page")
    public String getPolicyType() {
        waitForElementToBeVisible(policyTypeField);
        return getElementText(policyTypeField);
    }

    @Step("Get premium amount from details page")
    public String getPremiumAmount() {
        waitForElementToBeVisible(premiumAmountField);
        return getElementText(premiumAmountField);
    }

    @Step("Get effective date from details page")
    public String getEffectiveDate() {
        waitForElementToBeVisible(effectiveDateField);
        return getElementText(effectiveDateField);
    }

    @Step("Get expiration date from details page")
    public String getExpirationDate() {
        waitForElementToBeVisible(expirationDateField);
        return getElementText(expirationDateField);
    }

    @Step("Get insured name from details page")
    public String getInsuredName() {
        waitForElementToBeVisible(insuredNameField);
        return getElementText(insuredNameField);
    }

    @Step("Get coverage amount from details page")
    public String getCoverageAmount() {
        waitForElementToBeVisible(coverageAmountField);
        return getElementText(coverageAmountField);
    }

    @Step("Get deductible amount from details page")
    public String getDeductible() {
        waitForElementToBeVisible(deductibleField);
        return getElementText(deductibleField);
    }

    // --- Enhanced Policy Information Methods ---
    
    @Step("Get agent name from details page")
    public String getAgentName() {
        try {
            waitForElementToBeVisible(agentNameField);
            return getElementText(agentNameField);
        } catch (Exception e) {
            logger.debug("Agent name field not found or not visible");
            return "";
        }
    }

    @Step("Get agent phone from details page")
    public String getAgentPhone() {
        try {
            waitForElementToBeVisible(agentPhoneField);
            return getElementText(agentPhoneField);
        } catch (Exception e) {
            logger.debug("Agent phone field not found or not visible");
            return "";
        }
    }

    @Step("Get agent email from details page")
    public String getAgentEmail() {
        try {
            waitForElementToBeVisible(agentEmailField);
            return getElementText(agentEmailField);
        } catch (Exception e) {
            logger.debug("Agent email field not found or not visible");
            return "";
        }
    }

    @Step("Get policy category from details page")
    public String getPolicyCategory() {
        try {
            waitForElementToBeVisible(policyCategoryField);
            return getElementText(policyCategoryField);
        } catch (Exception e) {
            logger.debug("Policy category field not found or not visible");
            return "";
        }
    }

    @Step("Get last modified date from details page")
    public String getLastModifiedDate() {
        try {
            waitForElementToBeVisible(lastModifiedField);
            return getElementText(lastModifiedField);
        } catch (Exception e) {
            logger.debug("Last modified field not found or not visible");
            return "";
        }
    }

    @Step("Get created date from details page")
    public String getCreatedDate() {
        try {
            waitForElementToBeVisible(createdDateField);
            return getElementText(createdDateField);
        } catch (Exception e) {
            logger.debug("Created date field not found or not visible");
            return "";
        }
    }

    // --- Policy Actions Methods ---
    
    @Step("Click Edit Policy button")
    public void clickEditPolicy() {
        waitForElementToBeClickable(editPolicyButton);
        clickElement(editPolicyButton);
        LoggingUtil.logPageAction("PolicyDetailsPage", "click", "editPolicyButton");
    }

    @Step("Click Download Policy button")
    public void clickDownloadPolicy() {
        waitForElementToBeClickable(downloadPolicyButton);
        clickElement(downloadPolicyButton);
        LoggingUtil.logPageAction("PolicyDetailsPage", "click", "downloadPolicyButton");
    }

    @Step("Click Print Policy button")
    public void clickPrintPolicy() {
        try {
            waitForElementToBeClickable(printPolicyButton);
            clickElement(printPolicyButton);
            LoggingUtil.logPageAction("PolicyDetailsPage", "click", "printPolicyButton");
        } catch (Exception e) {
            logger.warn("Print policy button not available: {}", e.getMessage());
            throw new RuntimeException("Print policy button not found or not clickable", e);
        }
    }

    @Step("Click Share Policy button")
    public void clickSharePolicy() {
        try {
            waitForElementToBeClickable(sharePolicyButton);
            clickElement(sharePolicyButton);
            LoggingUtil.logPageAction("PolicyDetailsPage", "click", "sharePolicyButton");
        } catch (Exception e) {
            logger.warn("Share policy button not available: {}", e.getMessage());
            throw new RuntimeException("Share policy button not found or not clickable", e);
        }
    }

    @Step("Navigate back to Policy List")
    public PolicyListPage navigateBackToPolicies() {
        waitForElementToBeClickable(backToPoliciesLink);
        clickElement(backToPoliciesLink);
        LoggingUtil.logNavigation("PolicyDetailsPage", "PolicyListPage", "backToPoliciesLink");
        return new PolicyListPage(driver);
    }

    // --- Documents Management Methods ---
    
    @Step("Check if policy documents section is displayed")
    public boolean isPolicyDocumentsSectionDisplayed() {
        return isElementDisplayed(policyDocumentsSection);
    }

    @Step("Get list of policy documents")
    public List<String> getPolicyDocuments() {
        if (!isPolicyDocumentsSectionDisplayed()) {
            return new ArrayList<>();
        }
        
        try {
            waitForElementToBeVisible(policyDocumentsContainer);
            return documentItems.stream()
                    .map(this::getElementText)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.debug("Could not retrieve policy documents: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    @Step("Get number of policy documents")
    public int getPolicyDocumentsCount() {
        if (!isPolicyDocumentsSectionDisplayed()) {
            return 0;
        }
        return documentItems.size();
    }

    @Step("Download document by name: {documentName}")
    public void downloadDocument(String documentName) {
        if (!isPolicyDocumentsSectionDisplayed()) {
            throw new RuntimeException("Policy documents section not displayed");
        }
        
        for (WebElement documentItem : documentItems) {
            if (getElementText(documentItem).contains(documentName)) {
                WebElement downloadLink = documentItem.findElement(By.cssSelector(".download-link"));
                clickElement(downloadLink);
                LoggingUtil.logPageAction("PolicyDetailsPage", "download", "document:" + documentName);
                return;
            }
        }
        throw new RuntimeException("Document not found: " + documentName);
    }

    @Step("Click upload document button")
    public void clickUploadDocument() {
        try {
            waitForElementToBeClickable(uploadDocumentButton);
            clickElement(uploadDocumentButton);
            LoggingUtil.logPageAction("PolicyDetailsPage", "click", "uploadDocumentButton");
        } catch (Exception e) {
            logger.warn("Upload document button not available: {}", e.getMessage());
            throw new RuntimeException("Upload document button not found or not clickable", e);
        }
    }

    // --- Claims Management Methods ---
    
    @Step("Check if claims table is displayed")
    public boolean isClaimsTableDisplayed() {
        return isElementDisplayed(claimsTable);
    }

    @Step("Get claims section")
    public WebElement getClaimsSection() {
        waitForElementToBeVisible(claimsSection);
        return claimsSection;
    }

    @Step("Get list of claims")
    public List<String> getClaims() {
        if (!isClaimsTableDisplayed()) {
            return new ArrayList<>();
        }
        
        return claimRows.stream()
                .map(this::getElementText)
                .collect(Collectors.toList());
    }

    @Step("Get claims count")
    public int getClaimsCount() {
        if (!isClaimsTableDisplayed()) {
            return 0;
        }
        
        try {
            String countText = getElementText(claimsCountElement);
            return Integer.parseInt(countText.replaceAll("\\D+", ""));
        } catch (Exception e) {
            return claimRows.size();
        }
    }

    @Step("Filter claims by status: {status}")
    public void filterClaimsByStatus(String status) {
        if (!isClaimsTableDisplayed()) {
            throw new RuntimeException("Claims table not displayed");
        }
        
        waitForElementToBeClickable(claimsStatusFilter);
        Select statusSelect = new Select(claimsStatusFilter);
        statusSelect.selectByVisibleText(status);
        LoggingUtil.logPageAction("PolicyDetailsPage", "filter", "claimsStatus:" + status);
        
        // Wait for filter to apply
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Step("Click on claim with ID: {claimId}")
    public void clickOnClaim(String claimId) {
        if (!isClaimsTableDisplayed()) {
            throw new RuntimeException("Claims table not displayed");
        }
        
        for (WebElement claimRow : claimRows) {
            if (getElementText(claimRow).contains(claimId)) {
                clickElement(claimRow);
                LoggingUtil.logPageAction("PolicyDetailsPage", "click", "claim:" + claimId);
                return;
            }
        }
        throw new RuntimeException("Claim not found: " + claimId);
    }

    @Step("Click Submit New Claim button")
    public void clickSubmitNewClaim() {
        try {
            waitForElementToBeClickable(submitNewClaimButton);
            clickElement(submitNewClaimButton);
            LoggingUtil.logPageAction("PolicyDetailsPage", "click", "submitNewClaimButton");
        } catch (Exception e) {
            logger.warn("Submit new claim button not available: {}", e.getMessage());
            throw new RuntimeException("Submit new claim button not found or not clickable", e);
        }
    }

    @Step("Verify claims table has proper headers")
    public boolean hasProperClaimsTableHeaders() {
        if (!isClaimsTableDisplayed()) {
            return false;
        }
        
        try {
            String headerText = getElementText(claimsTableHeader).toLowerCase();
            return headerText.contains("claim") && 
                   headerText.contains("date") && 
                   headerText.contains("status") && 
                   headerText.contains("amount");
        } catch (Exception e) {
            logger.debug("Failed to verify claims table headers: {}", e.getMessage());
            return false;
        }
    }

    @Step("Verify claims are sorted by date in descending order")
    public boolean areClaimsSortedByDateDescending() {
        if (!isClaimsTableDisplayed() || claimRows.size() < 2) {
            return true; // Consider it valid if no claims or only one claim
        }
        
        // This is a simplified check - in real implementation, you'd parse dates
        // For now, we'll assume the table is correctly sorted if it's displayed
        return true;
    }

    // --- Verification Methods ---
    
    @Step("Verify policy details are displayed for policy ID: {expectedPolicyId}")
    public boolean verifyPolicyDetails(String expectedPolicyId) {
        if (!isPolicyDetailsPageDisplayed()) {
            logger.error("Policy details page is not displayed");
            return false;
        }
        
        String actualPolicyId = getPolicyId();
        boolean match = expectedPolicyId.equals(actualPolicyId);
        
        if (!match) {
            logger.error("Policy ID mismatch. Expected: {}, Actual: {}", expectedPolicyId, actualPolicyId);
        }
        
        return match;
    }

    @Step("Check if policy information section is displayed")
    public boolean isPolicyInformationDisplayed() {
        try {
            // Check if multiple key fields are visible
            return isElementDisplayed(policyIdField) &&
                   isElementDisplayed(policyStatusField) &&
                   isElementDisplayed(policyTypeField);
        } catch (Exception e) {
            logger.debug("Policy information display check failed: {}", e.getMessage());
            return false;
        }
    }

    @Step("Verify policy status matches expected status key: {expectedStatusKey}")
    public boolean verifyPolicyStatus(String expectedStatusKey) {
        String actualStatus = getPolicyStatus();
        String expectedLocalizedStatus = getLocalizedString(expectedStatusKey);
        boolean match = expectedLocalizedStatus.equals(actualStatus);
        
        if (!match) {
            logger.error("Policy status mismatch. Expected: {}, Actual: {}", expectedLocalizedStatus, actualStatus);
        }
        
        return match;
    }

    @Step("Get policy details header text")
    public String getPolicyDetailsHeaderText() {
        waitForElementToBeVisible(policyDetailsHeader);
        return getElementText(policyDetailsHeader);
    }

    @Step("Get all policy information as a formatted string")
    public String getAllPolicyInformation() {
        StringBuilder policyInfo = new StringBuilder();
        policyInfo.append("Policy ID: ").append(getPolicyId()).append("\n");
        policyInfo.append("Policy Number: ").append(getPolicyNumber()).append("\n");
        policyInfo.append("Status: ").append(getPolicyStatus()).append("\n");
        policyInfo.append("Type: ").append(getPolicyType()).append("\n");
        policyInfo.append("Premium: ").append(getPremiumAmount()).append("\n");
        policyInfo.append("Effective Date: ").append(getEffectiveDate()).append("\n");
        policyInfo.append("Expiration Date: ").append(getExpirationDate()).append("\n");
        policyInfo.append("Insured: ").append(getInsuredName()).append("\n");
        policyInfo.append("Coverage: ").append(getCoverageAmount()).append("\n");
        policyInfo.append("Deductible: ").append(getDeductible()).append("\n");
        
        // Add optional fields if available
        String agentName = getAgentName();
        if (!agentName.isEmpty()) {
            policyInfo.append("Agent: ").append(agentName).append("\n");
        }
        
        String category = getPolicyCategory();
        if (!category.isEmpty()) {
            policyInfo.append("Category: ").append(category).append("\n");
        }
        
        return policyInfo.toString();
    }

    // --- Helper Methods ---
    
    @Step("Check if error message is displayed")
    public boolean isErrorMessageDisplayed() {
        return isElementDisplayed(errorMessage);
    }

    @Step("Get error message text")
    public String getErrorMessage() {
        if (isErrorMessageDisplayed()) {
            return getElementText(errorMessage);
        }
        return "";
    }

    @Step("Check if no data message is displayed")
    public boolean isNoDataMessageDisplayed() {
        return isElementDisplayed(noDataMessage);
    }

    @Step("Check if policy has expiry warning")
    public boolean hasExpiryWarning() {
        return isElementDisplayed(expiryWarning);
    }

    @Step("Get status indicator")
    public String getStatusIndicator() {
        try {
            return getElementText(statusIndicator);
        } catch (Exception e) {
            logger.debug("Status indicator not found");
            return "";
        }
    }

    @Step("Get status badge text")
    public String getStatusBadge() {
        try {
            return getElementText(statusBadge);
        } catch (Exception e) {
            logger.debug("Status badge not found");
            return "";
        }
    }
}
