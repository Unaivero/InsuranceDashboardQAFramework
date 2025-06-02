    @FindBy(xpath = "//button[contains(text(), 'Cancel')] | //button[contains(text(), 'Cancelar')]")
    private WebElement cancelButton;

    // --- Validation and Messages ---
    @FindBy(css = ".validation-error")
    private List<WebElement> validationErrors;

    @FindBy(css = ".success-message")
    private WebElement successMessage;

    @FindBy(css = ".info-message")
    private WebElement infoMessage;

    // --- Constructor ---
    public ClaimSubmissionPage(WebDriver driver) {
        super(driver);
        LoggingUtil.setPageContext("ClaimSubmissionPage");
    }

    public ClaimSubmissionPage() {
        super();
        LoggingUtil.setPageContext("ClaimSubmissionPage");
    }

    // --- Page Methods ---
    
    @Step("Check if Claim Submission page is displayed")
    public boolean isClaimSubmissionPageDisplayed() {
        return isElementDisplayed(claimSubmissionHeader);
    }

    @Step("Verify form is pre-populated with policy information")
    public boolean isFormPrePopulated() {
        try {
            String policyId = getElementText(policyIdField);
            String policyNumber = getElementText(policyNumberField);
            String insuredName = getElementText(insuredNameField);
            
            return !policyId.isEmpty() && !policyNumber.isEmpty() && !insuredName.isEmpty();
        } catch (Exception e) {
            logger.debug("Form pre-population check failed: {}", e.getMessage());
            return false;
        }
    }

    @Step("Select claim type: {claimType}")
    public void selectClaimType(String claimType) {
        waitForElementToBeClickable(claimTypeSelect);
        Select select = new Select(claimTypeSelect);
        select.selectByVisibleText(claimType);
        LoggingUtil.logPageAction("ClaimSubmissionPage", "select", "claimType:" + claimType);
    }

    @Step("Enter claim description: {description}")
    public void enterClaimDescription(String description) {
        waitForElementToBeVisible(claimDescriptionField);
        typeText(claimDescriptionField, description);
        LoggingUtil.logPageAction("ClaimSubmissionPage", "enter", "claimDescription");
    }

    @Step("Enter incident date: {date}")
    public void enterIncidentDate(String date) {
        waitForElementToBeVisible(incidentDateField);
        typeText(incidentDateField, date);
        LoggingUtil.logPageAction("ClaimSubmissionPage", "enter", "incidentDate:" + date);
    }

    @Step("Enter claim amount: {amount}")
    public void enterClaimAmount(String amount) {
        waitForElementToBeVisible(claimAmountField);
        typeText(claimAmountField, amount);
        LoggingUtil.logPageAction("ClaimSubmissionPage", "enter", "claimAmount:" + amount);
    }

    @Step("Enter incident location: {location}")
    public void enterIncidentLocation(String location) {
        waitForElementToBeVisible(incidentLocationField);
        typeText(incidentLocationField, location);
        LoggingUtil.logPageAction("ClaimSubmissionPage", "enter", "incidentLocation:" + location);
    }

    @Step("Enter contact phone: {phone}")
    public void enterContactPhone(String phone) {
        waitForElementToBeVisible(contactPhoneField);
        typeText(contactPhoneField, phone);
        LoggingUtil.logPageAction("ClaimSubmissionPage", "enter", "contactPhone:" + phone);
    }

    @Step("Enter contact email: {email}")
    public void enterContactEmail(String email) {
        waitForElementToBeVisible(contactEmailField);
        typeText(contactEmailField, email);
        LoggingUtil.logPageAction("ClaimSubmissionPage", "enter", "contactEmail:" + email);
    }

    @Step("Upload supporting document: {filePath}")
    public void uploadSupportingDocument(String filePath) {
        waitForElementToBeVisible(documentUploadField);
        documentUploadField.sendKeys(filePath);
        LoggingUtil.logPageAction("ClaimSubmissionPage", "upload", "document:" + filePath);
    }

    @Step("Get number of uploaded documents")
    public int getUploadedDocumentsCount() {
        return uploadedDocuments.size();
    }

    @Step("Submit claim")
    public void submitClaim() {
        waitForElementToBeClickable(submitClaimButton);
        clickElement(submitClaimButton);
        LoggingUtil.logPageAction("ClaimSubmissionPage", "click", "submitClaim");
    }

    @Step("Save claim as draft")
    public void saveAsDraft() {
        waitForElementToBeClickable(saveAsDraftButton);
        clickElement(saveAsDraftButton);
        LoggingUtil.logPageAction("ClaimSubmissionPage", "click", "saveAsDraft");
    }

    @Step("Cancel claim submission")
    public PolicyDetailsPage cancel() {
        waitForElementToBeClickable(cancelButton);
        clickElement(cancelButton);
        LoggingUtil.logPageAction("ClaimSubmissionPage", "click", "cancel");
        return new PolicyDetailsPage(driver);
    }

    @Step("Check for validation errors")
    public boolean hasValidationErrors() {
        return !validationErrors.isEmpty();
    }

    @Step("Get validation error messages")
    public List<String> getValidationErrors() {
        return validationErrors.stream()
                .map(this::getElementText)
                .collect(Collectors.toList());
    }

    @Step("Check if success message is displayed")
    public boolean isSuccessMessageDisplayed() {
        return isElementDisplayed(successMessage);
    }

    @Step("Get success message text")
    public String getSuccessMessage() {
        if (isSuccessMessageDisplayed()) {
            return getElementText(successMessage);
        }
        return "";
    }

    @Step("Get pre-populated policy ID")
    public String getPrePopulatedPolicyId() {
        return getElementText(policyIdField);
    }

    @Step("Get pre-populated policy number")
    public String getPrePopulatedPolicyNumber() {
        return getElementText(policyNumberField);
    }

    @Step("Get pre-populated insured name")
    public String getPrePopulatedInsuredName() {
        return getElementText(insuredNameField);
    }
}
