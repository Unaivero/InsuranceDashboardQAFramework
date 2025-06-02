package testdata.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import exceptions.TestDataException;
import utils.ErrorHandler;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Policy test data model for insurance policy testing
 */
public class PolicyModel extends BaseTestDataModel {
    
    @JsonProperty("policyNumber")
    private String policyNumber;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("type")
    private String type;
    
    @JsonProperty("category")
    private String category;
    
    @JsonProperty("premiumAmount")
    private BigDecimal premiumAmount;
    
    @JsonProperty("coverageAmount")
    private BigDecimal coverageAmount;
    
    @JsonProperty("deductible")
    private BigDecimal deductible;
    
    @JsonProperty("effectiveDate")
    private String effectiveDate;
    
    @JsonProperty("expirationDate")
    private String expirationDate;
    
    @JsonProperty("insuredName")
    private String insuredName;
    
    @JsonProperty("agentId")
    private String agentId;
    
    @JsonProperty("claims")
    private List<String> claimIds;
    
    @JsonProperty("documents")
    private List<String> documentIds;
    
    @JsonProperty("beneficiaries")
    private List<String> beneficiaries;
    
    @JsonProperty("hasExpiryWarning")
    private boolean hasExpiryWarning;
    
    @JsonProperty("autoRenewal")
    private boolean autoRenewal;
    
    // Constructors
    public PolicyModel() {
        super();
        this.claimIds = new ArrayList<>();
        this.documentIds = new ArrayList<>();
        this.beneficiaries = new ArrayList<>();
        this.status = "PENDING";
        this.autoRenewal = false;
    }
    
    public PolicyModel(String id, String policyNumber, String status, String type) {
        super(id);
        this.policyNumber = policyNumber;
        this.status = status;
        this.type = type;
        this.claimIds = new ArrayList<>();
        this.documentIds = new ArrayList<>();
        this.beneficiaries = new ArrayList<>();
        this.autoRenewal = false;
    }
    
    // Getters and Setters
    public String getPolicyNumber() {
        return policyNumber;
    }
    
    public void setPolicyNumber(String policyNumber) {
        ErrorHandler.validateNotEmpty(policyNumber, "policy number");
        this.policyNumber = policyNumber;
        setModifiedDate(getCurrentDate());
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        ErrorHandler.validateNotEmpty(status, "status");
        this.status = status.toUpperCase();
        setModifiedDate(getCurrentDate());
        updateExpiryWarning();
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        ErrorHandler.validateNotEmpty(type, "type");
        this.type = type;
        setModifiedDate(getCurrentDate());
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
        setModifiedDate(getCurrentDate());
    }
    
    public BigDecimal getPremiumAmount() {
        return premiumAmount;
    }
    
    public void setPremiumAmount(BigDecimal premiumAmount) {
        if (premiumAmount != null && premiumAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new TestDataException("Policy", "validation", "Premium amount cannot be negative");
        }
        this.premiumAmount = premiumAmount;
        setModifiedDate(getCurrentDate());
    }
    
    public BigDecimal getCoverageAmount() {
        return coverageAmount;
    }
    
    public void setCoverageAmount(BigDecimal coverageAmount) {
        if (coverageAmount != null && coverageAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new TestDataException("Policy", "validation", "Coverage amount cannot be negative");
        }
        this.coverageAmount = coverageAmount;
        setModifiedDate(getCurrentDate());
    }
    
    public BigDecimal getDeductible() {
        return deductible;
    }
    
    public void setDeductible(BigDecimal deductible) {
        if (deductible != null && deductible.compareTo(BigDecimal.ZERO) < 0) {
            throw new TestDataException("Policy", "validation", "Deductible cannot be negative");
        }
        this.deductible = deductible;
        setModifiedDate(getCurrentDate());
    }
    
    public String getEffectiveDate() {
        return effectiveDate;
    }
    
    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
        setModifiedDate(getCurrentDate());
        updateExpiryWarning();
    }
    
    public String getExpirationDate() {
        return expirationDate;
    }
    
    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
        setModifiedDate(getCurrentDate());
        updateExpiryWarning();
    }
    
    public String getInsuredName() {
        return insuredName;
    }
    
    public void setInsuredName(String insuredName) {
        ErrorHandler.validateNotEmpty(insuredName, "insured name");
        this.insuredName = insuredName;
        setModifiedDate(getCurrentDate());
    }
    
    public String getAgentId() {
        return agentId;
    }
    
    public void setAgentId(String agentId) {
        this.agentId = agentId;
        setModifiedDate(getCurrentDate());
    }
    
    public List<String> getClaimIds() {
        return new ArrayList<>(claimIds);
    }
    
    public void addClaimId(String claimId) {
        ErrorHandler.validateNotEmpty(claimId, "claim ID");
        if (!this.claimIds.contains(claimId)) {
            this.claimIds.add(claimId);
            setModifiedDate(getCurrentDate());
        }
    }
    
    public void removeClaimId(String claimId) {
        if (this.claimIds.remove(claimId)) {
            setModifiedDate(getCurrentDate());
        }
    }
    
    public List<String> getDocumentIds() {
        return new ArrayList<>(documentIds);
    }
    
    public void addDocumentId(String documentId) {
        ErrorHandler.validateNotEmpty(documentId, "document ID");
        if (!this.documentIds.contains(documentId)) {
            this.documentIds.add(documentId);
            setModifiedDate(getCurrentDate());
        }
    }
    
    public void removeDocumentId(String documentId) {
        if (this.documentIds.remove(documentId)) {
            setModifiedDate(getCurrentDate());
        }
    }
    
    public List<String> getBeneficiaries() {
        return new ArrayList<>(beneficiaries);
    }
    
    public void addBeneficiary(String beneficiary) {
        ErrorHandler.validateNotEmpty(beneficiary, "beneficiary");
        if (!this.beneficiaries.contains(beneficiary)) {
            this.beneficiaries.add(beneficiary);
            setModifiedDate(getCurrentDate());
        }
    }
    
    public void removeBeneficiary(String beneficiary) {
        if (this.beneficiaries.remove(beneficiary)) {
            setModifiedDate(getCurrentDate());
        }
    }
    
    public boolean hasExpiryWarning() {
        return hasExpiryWarning;
    }
    
    public void setHasExpiryWarning(boolean hasExpiryWarning) {
        this.hasExpiryWarning = hasExpiryWarning;
        setModifiedDate(getCurrentDate());
    }
    
    public boolean isAutoRenewal() {
        return autoRenewal;
    }
    
    public void setAutoRenewal(boolean autoRenewal) {
        this.autoRenewal = autoRenewal;
        setModifiedDate(getCurrentDate());
    }
    
    // Utility methods
    public boolean isActive() {
        return "ACTIVE".equalsIgnoreCase(status);
    }
    
    public boolean isExpired() {
        return "EXPIRED".equalsIgnoreCase(status);
    }
    
    public boolean isPending() {
        return "PENDING".equalsIgnoreCase(status);
    }
    
    public boolean isCancelled() {
        return "CANCELLED".equalsIgnoreCase(status);
    }
    
    public boolean hasClaimsHistory() {
        return !claimIds.isEmpty();
    }
    
    public boolean hasDocuments() {
        return !documentIds.isEmpty();
    }
    
    public boolean hasBeneficiaries() {
        return !beneficiaries.isEmpty();
    }
    
    public String getFormattedPremiumAmount() {
        return premiumAmount != null ? String.format("$%,.2f", premiumAmount) : "$0.00";
    }
    
    public String getFormattedCoverageAmount() {
        return coverageAmount != null ? String.format("$%,.2f", coverageAmount) : "$0.00";
    }
    
    public String getFormattedDeductible() {
        return deductible != null ? String.format("$%,.2f", deductible) : "$0.00";
    }
    
    private void updateExpiryWarning() {
        if (expirationDate != null && !expirationDate.trim().isEmpty()) {
            try {
                LocalDate expiry = LocalDate.parse(expirationDate, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                LocalDate warningDate = LocalDate.now().plusDays(30);
                this.hasExpiryWarning = expiry.isBefore(warningDate);
            } catch (Exception e) {
                // If date parsing fails, assume no warning needed
                this.hasExpiryWarning = false;
            }
        }
    }
    
    @Override
    public void validate() throws TestDataException {
        if (policyNumber == null || policyNumber.trim().isEmpty()) {
            throw new TestDataException("Policy", "validation", "Policy number is required");
        }
        
        if (status == null || status.trim().isEmpty()) {
            throw new TestDataException("Policy", "validation", "Policy status is required");
        }
        
        if (type == null || type.trim().isEmpty()) {
            throw new TestDataException("Policy", "validation", "Policy type is required");
        }
        
        if (insuredName == null || insuredName.trim().isEmpty()) {
            throw new TestDataException("Policy", "validation", "Insured name is required");
        }
        
        if (premiumAmount != null && premiumAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new TestDataException("Policy", "validation", "Premium amount cannot be negative");
        }
        
        if (coverageAmount != null && coverageAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new TestDataException("Policy", "validation", "Coverage amount cannot be negative");
        }
        
        if (deductible != null && deductible.compareTo(BigDecimal.ZERO) < 0) {
            throw new TestDataException("Policy", "validation", "Deductible cannot be negative");
        }
    }
    
    @Override
    public String getSummary() {
        return String.format("Policy[id=%s, number=%s, type=%s, status=%s, insured=%s, premium=%s]", 
                           getId(), policyNumber, type, status, insuredName, getFormattedPremiumAmount());
    }
    
    @Override
    public BaseTestDataModel copy() {
        PolicyModel copy = new PolicyModel();
        copy.setId(this.getId());
        copy.setPolicyNumber(this.policyNumber);
        copy.setStatus(this.status);
        copy.setType(this.type);
        copy.setCategory(this.category);
        copy.setPremiumAmount(this.premiumAmount);
        copy.setCoverageAmount(this.coverageAmount);
        copy.setDeductible(this.deductible);
        copy.setEffectiveDate(this.effectiveDate);
        copy.setExpirationDate(this.expirationDate);
        copy.setInsuredName(this.insuredName);
        copy.setAgentId(this.agentId);
        copy.setHasExpiryWarning(this.hasExpiryWarning);
        copy.setAutoRenewal(this.autoRenewal);
        copy.setDescription(this.getDescription());
        copy.setActive(this.isActive());
        
        // Copy lists
        for (String claimId : this.claimIds) {
            copy.addClaimId(claimId);
        }
        for (String documentId : this.documentIds) {
            copy.addDocumentId(documentId);
        }
        for (String beneficiary : this.beneficiaries) {
            copy.addBeneficiary(beneficiary);
        }
        
        return copy;
    }
}
