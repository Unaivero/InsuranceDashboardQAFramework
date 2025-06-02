package api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import exceptions.TestDataException;
import utils.LoggingUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * API Model for Insurance Policy
 * 
 * Represents a complete insurance policy with all relevant fields
 * for API operations including validation and business logic
 * 
 * @author Insurance Dashboard QA Framework
 * @version 1.0
 */
public class PolicyApiModel extends BaseApiModel {
    
    @JsonProperty("policyNumber")
    private String policyNumber;
    
    @JsonProperty("policyType")
    private String policyType;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("customerName")
    private String customerName;
    
    @JsonProperty("customerId")
    private String customerId;
    
    @JsonProperty("premiumAmount")
    private BigDecimal premiumAmount;
    
    @JsonProperty("deductibleAmount")
    private BigDecimal deductibleAmount;
    
    @JsonProperty("coverageAmount")
    private BigDecimal coverageAmount;
    
    @JsonProperty("startDate")
    private LocalDate startDate;
    
    @JsonProperty("endDate")
    private LocalDate endDate;
    
    @JsonProperty("renewalDate")
    private LocalDate renewalDate;
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("agentId")
    private String agentId;
    
    @JsonProperty("agentName")
    private String agentName;
    
    @JsonProperty("riskLevel")
    private String riskLevel;
    
    @JsonProperty("paymentFrequency")
    private String paymentFrequency;
    
    @JsonProperty("coverageDetails")
    private List<CoverageDetail> coverageDetails;
    
    @JsonProperty("claims")
    private List<String> claimIds;
    
    // Policy statuses
    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_INACTIVE = "INACTIVE";
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_EXPIRED = "EXPIRED";
    public static final String STATUS_CANCELLED = "CANCELLED";
    public static final String STATUS_SUSPENDED = "SUSPENDED";
    
    // Policy types
    public static final String TYPE_AUTO = "Auto Insurance";
    public static final String TYPE_HOME = "Home Insurance";
    public static final String TYPE_LIFE = "Life Insurance";
    public static final String TYPE_HEALTH = "Health Insurance";
    public static final String TYPE_BUSINESS = "Business Insurance";
    
    // Risk levels
    public static final String RISK_LOW = "LOW";
    public static final String RISK_MEDIUM = "MEDIUM";
    public static final String RISK_HIGH = "HIGH";
    
    // Payment frequencies
    public static final String FREQUENCY_MONTHLY = "MONTHLY";
    public static final String FREQUENCY_QUARTERLY = "QUARTERLY";
    public static final String FREQUENCY_SEMI_ANNUAL = "SEMI_ANNUAL";
    public static final String FREQUENCY_ANNUAL = "ANNUAL";
    
    /**
     * Default constructor
     */
    public PolicyApiModel() {
        super();
        this.coverageDetails = new ArrayList<>();
        this.claimIds = new ArrayList<>();
    }
    
    /**
     * Constructor with policy number
     */
    public PolicyApiModel(String policyNumber) {
        this();
        this.policyNumber = policyNumber;
    }
    
    /**
     * Constructor with essential fields
     */
    public PolicyApiModel(String policyNumber, String policyType, String status, String customerName, BigDecimal premiumAmount) {
        this(policyNumber);
        this.policyType = policyType;
        this.status = status;
        this.customerName = customerName;
        this.premiumAmount = premiumAmount;
    }
    
    @Override
    public boolean validate() throws TestDataException {
        List<String> errors = getValidationErrors();
        if (!errors.isEmpty()) {
            String errorMsg = "Policy validation failed: " + String.join(", ", errors);
            LoggingUtil.logError(errorMsg);
            throw new TestDataException(errorMsg);
        }
        return true;
    }
    
    @Override
    public List<String> getValidationErrors() {
        List<String> errors = new ArrayList<>();
        
        // Required field validations
        if (policyNumber == null || policyNumber.trim().isEmpty()) {
            errors.add("Policy number is required");
        }
        
        if (policyType == null || policyType.trim().isEmpty()) {
            errors.add("Policy type is required");
        }
        
        if (status == null || status.trim().isEmpty()) {
            errors.add("Policy status is required");
        }
        
        if (customerName == null || customerName.trim().isEmpty()) {
            errors.add("Customer name is required");
        }
        
        if (premiumAmount == null || premiumAmount.compareTo(BigDecimal.ZERO) <= 0) {
            errors.add("Premium amount must be greater than zero");
        }
        
        // Business rule validations
        if (status != null && !isValidStatus(status)) {
            errors.add("Invalid policy status: " + status);
        }
        
        if (policyType != null && !isValidPolicyType(policyType)) {
            errors.add("Invalid policy type: " + policyType);
        }
        
        if (riskLevel != null && !isValidRiskLevel(riskLevel)) {
            errors.add("Invalid risk level: " + riskLevel);
        }
        
        if (paymentFrequency != null && !isValidPaymentFrequency(paymentFrequency)) {
            errors.add("Invalid payment frequency: " + paymentFrequency);
        }
        
        // Date validations
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            errors.add("Start date cannot be after end date");
        }
        
        if (premiumAmount != null && deductibleAmount != null && 
            deductibleAmount.compareTo(premiumAmount) > 0) {
            errors.add("Deductible amount cannot be greater than premium amount");
        }
        
        return errors;
    }
    
    // Validation helper methods
    private boolean isValidStatus(String status) {
        return STATUS_ACTIVE.equals(status) || STATUS_INACTIVE.equals(status) || 
               STATUS_PENDING.equals(status) || STATUS_EXPIRED.equals(status) ||
               STATUS_CANCELLED.equals(status) || STATUS_SUSPENDED.equals(status);
    }
    
    private boolean isValidPolicyType(String type) {
        return TYPE_AUTO.equals(type) || TYPE_HOME.equals(type) || 
               TYPE_LIFE.equals(type) || TYPE_HEALTH.equals(type) || 
               TYPE_BUSINESS.equals(type);
    }
    
    private boolean isValidRiskLevel(String level) {
        return RISK_LOW.equals(level) || RISK_MEDIUM.equals(level) || RISK_HIGH.equals(level);
    }
    
    private boolean isValidPaymentFrequency(String frequency) {
        return FREQUENCY_MONTHLY.equals(frequency) || FREQUENCY_QUARTERLY.equals(frequency) ||
               FREQUENCY_SEMI_ANNUAL.equals(frequency) || FREQUENCY_ANNUAL.equals(frequency);
    }
    
    // Business logic methods
    public boolean isActive() {
        return STATUS_ACTIVE.equals(this.status);
    }
    
    public boolean isExpired() {
        return STATUS_EXPIRED.equals(this.status) || 
               (endDate != null && endDate.isBefore(LocalDate.now()));
    }
    
    public boolean isPending() {
        return STATUS_PENDING.equals(this.status);
    }
    
    public boolean isCancelled() {
        return STATUS_CANCELLED.equals(this.status);
    }
    
    public boolean isRenewable() {
        return isActive() || (STATUS_EXPIRED.equals(this.status) && 
               renewalDate != null && renewalDate.isAfter(LocalDate.now()));
    }
    
    public BigDecimal calculateAnnualPremium() {
        if (premiumAmount == null || paymentFrequency == null) {
            return BigDecimal.ZERO;
        }
        
        switch (paymentFrequency) {
            case FREQUENCY_MONTHLY:
                return premiumAmount.multiply(new BigDecimal("12"));
            case FREQUENCY_QUARTERLY:
                return premiumAmount.multiply(new BigDecimal("4"));
            case FREQUENCY_SEMI_ANNUAL:
                return premiumAmount.multiply(new BigDecimal("2"));
            case FREQUENCY_ANNUAL:
                return premiumAmount;
            default:
                return premiumAmount;
        }
    }
    
    public void addClaim(String claimId) {
        if (this.claimIds == null) {
            this.claimIds = new ArrayList<>();
        }
        if (!this.claimIds.contains(claimId)) {
            this.claimIds.add(claimId);
            markAsUpdated("system");
        }
    }
    
    public void addCoverageDetail(CoverageDetail detail) {
        if (this.coverageDetails == null) {
            this.coverageDetails = new ArrayList<>();
        }
        this.coverageDetails.add(detail);
        markAsUpdated("system");
    }
    
    // Getters and Setters
    public String getPolicyNumber() { return policyNumber; }
    public void setPolicyNumber(String policyNumber) { this.policyNumber = policyNumber; }
    
    public String getPolicyType() { return policyType; }
    public void setPolicyType(String policyType) { this.policyType = policyType; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    
    public BigDecimal getPremiumAmount() { return premiumAmount; }
    public void setPremiumAmount(BigDecimal premiumAmount) { this.premiumAmount = premiumAmount; }
    
    public BigDecimal getDeductibleAmount() { return deductibleAmount; }
    public void setDeductibleAmount(BigDecimal deductibleAmount) { this.deductibleAmount = deductibleAmount; }
    
    public BigDecimal getCoverageAmount() { return coverageAmount; }
    public void setCoverageAmount(BigDecimal coverageAmount) { this.coverageAmount = coverageAmount; }
    
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    
    public LocalDate getRenewalDate() { return renewalDate; }
    public void setRenewalDate(LocalDate renewalDate) { this.renewalDate = renewalDate; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getAgentId() { return agentId; }
    public void setAgentId(String agentId) { this.agentId = agentId; }
    
    public String getAgentName() { return agentName; }
    public void setAgentName(String agentName) { this.agentName = agentName; }
    
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    
    public String getPaymentFrequency() { return paymentFrequency; }
    public void setPaymentFrequency(String paymentFrequency) { this.paymentFrequency = paymentFrequency; }
    
    public List<CoverageDetail> getCoverageDetails() { return coverageDetails; }
    public void setCoverageDetails(List<CoverageDetail> coverageDetails) { this.coverageDetails = coverageDetails; }
    
    public List<String> getClaimIds() { return claimIds; }
    public void setClaimIds(List<String> claimIds) { this.claimIds = claimIds; }
    
    /**
     * Inner class for coverage details
     */
    public static class CoverageDetail {
        @JsonProperty("type")
        private String type;
        
        @JsonProperty("description")
        private String description;
        
        @JsonProperty("amount")
        private BigDecimal amount;
        
        @JsonProperty("deductible")
        private BigDecimal deductible;
        
        public CoverageDetail() {}
        
        public CoverageDetail(String type, String description, BigDecimal amount, BigDecimal deductible) {
            this.type = type;
            this.description = description;
            this.amount = amount;
            this.deductible = deductible;
        }
        
        // Getters and Setters
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
        
        public BigDecimal getDeductible() { return deductible; }
        public void setDeductible(BigDecimal deductible) { this.deductible = deductible; }
    }
}
