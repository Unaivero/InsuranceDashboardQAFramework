package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import utils.LoggingUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * EditPolicyPage for handling policy editing functionality
 * This page would be navigated to when clicking "Edit Policy" from PolicyDetailsPage
 */
public class EditPolicyPage extends BasePage {

    // --- Form Elements ---
    @FindBy(xpath = "//h1[contains(text(), 'Edit Policy')] | //h1[contains(text(), 'Editar Póliza')]")
    private WebElement editPolicyHeader;

    @FindBy(id = "policyNumberEdit")
    private WebElement policyNumberField;

    @FindBy(id = "premiumAmountEdit")
    private WebElement premiumAmountField;

    @FindBy(id = "coverageAmountEdit")
    private WebElement coverageAmountField;

    @FindBy(id = "deductibleEdit")
    private WebElement deductibleField;

    @FindBy(id = "insuredNameEdit")
    private WebElement insuredNameField;

    @FindBy(id = "effectiveDateEdit")
    private WebElement effectiveDateField;

    @FindBy(id = "expirationDateEdit")
    private WebElement expirationDateField;

    @FindBy(id = "policyTypeSelect")
    private WebElement policyTypeSelect;

    // --- Action Buttons ---
    @FindBy(xpath = "//button[contains(text(), 'Save Changes')] | //button[contains(text(), 'Guardar Cambios')]")
    private WebElement saveChangesButton;

    @FindBy(xpath = "//button[contains(text(), 'Cancel')] | //button[contains(text(), 'Cancelar')]")
    private WebElement cancelButton;

    @FindBy(xpath = "//button[contains(text(), 'Reset Form')] | //button[contains(text(), 'Restablecer')]")
    private WebElement resetFormButton;

    // --- Validation Messages ---
    @FindBy(css = ".validation-error")
    private List<WebElement> validationErrors;

    @FindBy(css = ".success-message")
    private WebElement successMessage;

    // --- Constructor ---
    public EditPolicyPage(WebDriver driver) {
        super(driver);
        LoggingUtil.setPageContext("EditPolicyPage");
    }

    public EditPolicyPage() {
        super();
        LoggingUtil.setPageContext("EditPolicyPage");
    }

    // --- Page Methods ---
    
    @Step("Check if Edit Policy page is displayed")
    public boolean isEditPolicyPageDisplayed() {
        return isElementDisplayed(editPolicyHeader);
    }

    @Step("Update premium amount to: {newAmount}")
    public void updatePremiumAmount(String newAmount) {
        waitForElementToBeVisible(premiumAmountField);
        typeText(premiumAmountField, newAmount);
        LoggingUtil.logPageAction("EditPolicyPage", "update", "premiumAmount:" + newAmount);
    }

    @Step("Update coverage amount to: {newAmount}")
    public void updateCoverageAmount(String newAmount) {
        waitForElementToBeVisible(coverageAmountField);
        typeText(coverageAmountField, newAmount);
        LoggingUtil.logPageAction("EditPolicyPage", "update", "coverageAmount:" + newAmount);
    }

    @Step("Update deductible to: {newDeductible}")
    public void updateDeductible(String newDeductible) {
        waitForElementToBeVisible(deductibleField);
        typeText(deductibleField, newDeductible);
        LoggingUtil.logPageAction("EditPolicyPage", "update", "deductible:" + newDeductible);
    }

    @Step("Save policy changes")
    public PolicyDetailsPage saveChanges() {
        waitForElementToBeClickable(saveChangesButton);
        clickElement(saveChangesButton);
        LoggingUtil.logPageAction("EditPolicyPage", "click", "saveChanges");
        return new PolicyDetailsPage(driver);
    }

    @Step("Cancel policy editing")
    public PolicyDetailsPage cancel() {
        waitForElementToBeClickable(cancelButton);
        clickElement(cancelButton);
        LoggingUtil.logPageAction("EditPolicyPage", "click", "cancel");
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
}
