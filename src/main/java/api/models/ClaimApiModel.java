package api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import exceptions.TestDataException;
import utils.LoggingUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * API Model for Insurance Claim
 * 
 * Represents an insurance claim with all relevant fields
 * for API operations including workflow and status management
 * 
 * @author Insurance Dashboard QA Framework
 * @version 1.0
 */
public class ClaimApiModel extends BaseApiModel {
    
    @JsonProperty("claimNumber")
    private String claimNumber;
    
    @JsonProperty("policyId")
    private String policyId;
    
    @JsonProperty("policyNumber")
    private String policyNumber;
    
    @JsonProperty("claimantName")
    private String claimantName;
    
    @JsonProperty("claimantId")
    private String claimantId;
    
    @JsonProperty("incidentDate")
    private LocalDate incidentDate;
    
    @JsonProperty("reportedDate")
    private LocalDateTime reportedDate;
    
    @JsonProperty("claimType")
    private String claimType;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("priority")
    private String priority;
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("incidentLocation")
    private String incidentLocation;
    
    @JsonProperty("claimedAmount")
    private BigDecimal claimedAmount;
    
    @JsonProperty("approvedAmount")
    private BigDecimal approvedAmount;
    
    @JsonProperty("deductibleAmount")
    private BigDecimal deductibleAmount;
    
    @JsonProperty("adjusterId")
    private String adjusterId;
    
    @JsonProperty("adjusterName")
    private String adjusterName;
    
    @JsonProperty("estimatedSettlementDate")
    private LocalDate estimatedSettlementDate;
    
    @JsonProperty("actualSettlementDate")
    private LocalDate actualSettlementDate;
    
    @JsonProperty("documents")
    private List<ClaimDocument> documents;
    
    @JsonProperty("statusHistory")
    private List<StatusChange> statusHistory;
    
    @JsonProperty("notes")
    private List<ClaimNote> notes;
    
    // Claim statuses
    public static final String STATUS_SUBMITTED = "SUBMITTED";
    public static final String STATUS_UNDER_REVIEW = "UNDER_REVIEW";
    public static final String STATUS_INVESTIGATING = "INVESTIGATING";
    public static final String STATUS_PENDING_DOCUMENTS = "PENDING_DOCUMENTS";
    public static final String STATUS_APPROVED = "APPROVED";
    public static final String STATUS_DENIED = "DENIED";
    public static final String STATUS_SETTLED = "SETTLED";
    public static final String STATUS_CLOSED = "CLOSED";
    public static final String STATUS_CANCELLED = "CANCELLED";
    
    // Claim types
    public static final String TYPE_AUTO_COLLISION = "AUTO_COLLISION";
    public static final String TYPE_AUTO_COMPREHENSIVE = "AUTO_COMPREHENSIVE";
    public static final String TYPE_AUTO_LIABILITY = "AUTO_LIABILITY";
    public static final String TYPE_HOME_FIRE = "HOME_FIRE";
    public static final String TYPE_HOME_THEFT = "HOME_THEFT";
    public static final String TYPE_HOME_WATER = "HOME_WATER";
    public static final String TYPE_MEDICAL = "MEDICAL";
    public static final String TYPE_DISABILITY = "DISABILITY";
    public static final String TYPE_LIFE = "LIFE";
    
    // Priority levels
    public static final String PRIORITY_LOW = "LOW";
    public static final String PRIORITY_MEDIUM = "MEDIUM";
    public static final String PRIORITY_HIGH = "HIGH";
    public static final String PRIORITY_URGENT = "URGENT";
    
    /**
     * Default constructor
     */
    public ClaimApiModel() {
        super();
        this.documents = new ArrayList<>();
        this.statusHistory = new ArrayList<>();
        this.notes = new ArrayList<>();
        this.reportedDate = LocalDateTime.now();
        this.status = STATUS_SUBMITTED;
        this.priority = PRIORITY_MEDIUM;
    }
    
    /**
     * Constructor with claim number
     */
    public ClaimApiModel(String claimNumber) {
        this();
        this.claimNumber = claimNumber;
    }
    
    /**
     * Constructor with essential fields
     */
    public ClaimApiModel(String claimNumber, String policyId, String claimantName, 
                        String claimType, BigDecimal claimedAmount) {
        this(claimNumber);
        this.policyId = policyId;
        this.claimantName = claimantName;
        this.claimType = claimType;
        this.claimedAmount = claimedAmount;
    }
    
    @Override
    public boolean validate() throws TestDataException {
        List<String> errors = getValidationErrors();
        if (!errors.isEmpty()) {
            String errorMsg = "Claim validation failed: " + String.join(", ", errors);
            LoggingUtil.logError(errorMsg);
            throw new TestDataException(errorMsg);
        }
        return true;
    }
    
    @Override
    public List<String> getValidationErrors() {
        List<String> errors = new ArrayList<>();
        
        // Required field validations
        if (claimNumber == null || claimNumber.trim().isEmpty()) {
            errors.add("Claim number is required");
        }
        
        if (policyId == null || policyId.trim().isEmpty()) {
            errors.add("Policy ID is required");
        }
        
        if (claimantName == null || claimantName.trim().isEmpty()) {
            errors.add("Claimant name is required");
        }
        
        if (claimType == null || claimType.trim().isEmpty()) {
            errors.add("Claim type is required");
        }
        
        if (claimedAmount == null || claimedAmount.compareTo(BigDecimal.ZERO) <= 0) {
            errors.add("Claimed amount must be greater than zero");
        }
        
        if (incidentDate == null) {
            errors.add("Incident date is required");
        }
        
        // Business rule validations
        if (status != null && !isValidStatus(status)) {
            errors.add("Invalid claim status: " + status);
        }
        
        if (claimType != null && !isValidClaimType(claimType)) {
            errors.add("Invalid claim type: " + claimType);
        }
        
        if (priority != null && !isValidPriority(priority)) {
            errors.add("Invalid priority: " + priority);
        }
        
        // Date validations
        if (incidentDate != null && incidentDate.isAfter(LocalDate.now())) {
            errors.add("Incident date cannot be in the future");
        }
        
        if (reportedDate != null && incidentDate != null && 
            reportedDate.toLocalDate().isBefore(incidentDate)) {
            errors.add("Reported date cannot be before incident date");
        }
        
        // Amount validations
        if (approvedAmount != null && claimedAmount != null && 
            approvedAmount.compareTo(claimedAmount) > 0) {
            errors.add("Approved amount cannot exceed claimed amount");
        }
        
        if (deductibleAmount != null && deductibleAmount.compareTo(BigDecimal.ZERO) < 0) {
            errors.add("Deductible amount cannot be negative");
        }
        
        return errors;
    }
    
    // Validation helper methods
    private boolean isValidStatus(String status) {
        return STATUS_SUBMITTED.equals(status) || STATUS_UNDER_REVIEW.equals(status) ||
               STATUS_INVESTIGATING.equals(status) || STATUS_PENDING_DOCUMENTS.equals(status) ||
               STATUS_APPROVED.equals(status) || STATUS_DENIED.equals(status) ||
               STATUS_SETTLED.equals(status) || STATUS_CLOSED.equals(status) ||
               STATUS_CANCELLED.equals(status);
    }
    
    private boolean isValidClaimType(String type) {
        return TYPE_AUTO_COLLISION.equals(type) || TYPE_AUTO_COMPREHENSIVE.equals(type) ||
               TYPE_AUTO_LIABILITY.equals(type) || TYPE_HOME_FIRE.equals(type) ||
               TYPE_HOME_THEFT.equals(type) || TYPE_HOME_WATER.equals(type) ||
               TYPE_MEDICAL.equals(type) || TYPE_DISABILITY.equals(type) ||
               TYPE_LIFE.equals(type);
    }
    
    private boolean isValidPriority(String priority) {
        return PRIORITY_LOW.equals(priority) || PRIORITY_MEDIUM.equals(priority) ||
               PRIORITY_HIGH.equals(priority) || PRIORITY_URGENT.equals(priority);
    }
    
    // Business logic methods
    public boolean isOpen() {
        return STATUS_SUBMITTED.equals(status) || STATUS_UNDER_REVIEW.equals(status) ||
               STATUS_INVESTIGATING.equals(status) || STATUS_PENDING_DOCUMENTS.equals(status);
    }
    
    public boolean isClosed() {
        return STATUS_SETTLED.equals(status) || STATUS_CLOSED.equals(status) ||
               STATUS_DENIED.equals(status) || STATUS_CANCELLED.equals(status);
    }
    
    public boolean isApproved() {
        return STATUS_APPROVED.equals(status) || STATUS_SETTLED.equals(status);
    }
    
    public boolean isSettled() {
        return STATUS_SETTLED.equals(status);
    }
    
    public boolean isPendingDocuments() {
        return STATUS_PENDING_DOCUMENTS.equals(status);
    }
    
    public void updateStatus(String newStatus, String updatedBy, String reason) {
        if (isValidStatus(newStatus)) {
            StatusChange statusChange = new StatusChange(this.status, newStatus, updatedBy, reason);
            this.statusHistory.add(statusChange);
            this.status = newStatus;
            markAsUpdated(updatedBy);
            LoggingUtil.logInfo("Claim " + claimNumber + " status updated to " + newStatus);
        }
    }
    
    public void addDocument(ClaimDocument document) {
        if (this.documents == null) {
            this.documents = new ArrayList<>();
        }
        this.documents.add(document);
        markAsUpdated("system");
    }
    
    public void addNote(String noteText, String authorId, String authorName) {
        if (this.notes == null) {
            this.notes = new ArrayList<>();
        }
        ClaimNote note = new ClaimNote(noteText, authorId, authorName);
        this.notes.add(note);
        markAsUpdated(authorId);
    }
    
    public BigDecimal calculateNetPayment() {
        if (approvedAmount == null) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal deductible = deductibleAmount != null ? deductibleAmount : BigDecimal.ZERO;
        return approvedAmount.subtract(deductible).max(BigDecimal.ZERO);
    }
    
    public long getDaysInProcess() {
        if (reportedDate == null) {
            return 0;
        }
        
        LocalDateTime endDate = isSettled() && actualSettlementDate != null ? 
                                actualSettlementDate.atStartOfDay() : LocalDateTime.now();
        
        return java.time.Duration.between(reportedDate, endDate).toDays();
    }
    
    // Getters and Setters
    public String getClaimNumber() { return claimNumber; }
    public void setClaimNumber(String claimNumber) { this.claimNumber = claimNumber; }
    
    public String getPolicyId() { return policyId; }
    public void setPolicyId(String policyId) { this.policyId = policyId; }
    
    public String getPolicyNumber() { return policyNumber; }
    public void setPolicyNumber(String policyNumber) { this.policyNumber = policyNumber; }
    
    public String getClaimantName() { return claimantName; }
    public void setClaimantName(String claimantName) { this.claimantName = claimantName; }
    
    public String getClaimantId() { return claimantId; }
    public void setClaimantId(String claimantId) { this.claimantId = claimantId; }
    
    public LocalDate getIncidentDate() { return incidentDate; }
    public void setIncidentDate(LocalDate incidentDate) { this.incidentDate = incidentDate; }
    
    public LocalDateTime getReportedDate() { return reportedDate; }
    public void setReportedDate(LocalDateTime reportedDate) { this.reportedDate = reportedDate; }
    
    public String getClaimType() { return claimType; }
    public void setClaimType(String claimType) { this.claimType = claimType; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getIncidentLocation() { return incidentLocation; }
    public void setIncidentLocation(String incidentLocation) { this.incidentLocation = incidentLocation; }
    
    public BigDecimal getClaimedAmount() { return claimedAmount; }
    public void setClaimedAmount(BigDecimal claimedAmount) { this.claimedAmount = claimedAmount; }
    
    public BigDecimal getApprovedAmount() { return approvedAmount; }
    public void setApprovedAmount(BigDecimal approvedAmount) { this.approvedAmount = approvedAmount; }
    
    public BigDecimal getDeductibleAmount() { return deductibleAmount; }
    public void setDeductibleAmount(BigDecimal deductibleAmount) { this.deductibleAmount = deductibleAmount; }
    
    public String getAdjusterId() { return adjusterId; }
    public void setAdjusterId(String adjusterId) { this.adjusterId = adjusterId; }
    
    public String getAdjusterName() { return adjusterName; }
    public void setAdjusterName(String adjusterName) { this.adjusterName = adjusterName; }
    
    public LocalDate getEstimatedSettlementDate() { return estimatedSettlementDate; }
    public void setEstimatedSettlementDate(LocalDate estimatedSettlementDate) { this.estimatedSettlementDate = estimatedSettlementDate; }
    
    public LocalDate getActualSettlementDate() { return actualSettlementDate; }
    public void setActualSettlementDate(LocalDate actualSettlementDate) { this.actualSettlementDate = actualSettlementDate; }
    
    public List<ClaimDocument> getDocuments() { return documents; }
    public void setDocuments(List<ClaimDocument> documents) { this.documents = documents; }
    
    public List<StatusChange> getStatusHistory() { return statusHistory; }
    public void setStatusHistory(List<StatusChange> statusHistory) { this.statusHistory = statusHistory; }
    
    public List<ClaimNote> getNotes() { return notes; }
    public void setNotes(List<ClaimNote> notes) { this.notes = notes; }
    
    /**
     * Inner class for Claim Documents
     */
    public static class ClaimDocument {
        @JsonProperty("id")
        private String id;
        
        @JsonProperty("name")
        private String name;
        
        @JsonProperty("type")
        private String type;
        
        @JsonProperty("url")
        private String url;
        
        @JsonProperty("uploadedDate")
        private LocalDateTime uploadedDate;
        
        @JsonProperty("uploadedBy")
        private String uploadedBy;
        
        public ClaimDocument() {}
        
        public ClaimDocument(String name, String type, String url, String uploadedBy) {
            this.name = name;
            this.type = type;
            this.url = url;
            this.uploadedBy = uploadedBy;
            this.uploadedDate = LocalDateTime.now();
        }
        
        // Getters and Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
        
        public LocalDateTime getUploadedDate() { return uploadedDate; }
        public void setUploadedDate(LocalDateTime uploadedDate) { this.uploadedDate = uploadedDate; }
        
        public String getUploadedBy() { return uploadedBy; }
        public void setUploadedBy(String uploadedBy) { this.uploadedBy = uploadedBy; }
    }
    
    /**
     * Inner class for Status Changes
     */
    public static class StatusChange {
        @JsonProperty("fromStatus")
        private String fromStatus;
        
        @JsonProperty("toStatus")
        private String toStatus;
        
        @JsonProperty("changedDate")
        private LocalDateTime changedDate;
        
        @JsonProperty("changedBy")
        private String changedBy;
        
        @JsonProperty("reason")
        private String reason;
        
        public StatusChange() {}
        
        public StatusChange(String fromStatus, String toStatus, String changedBy, String reason) {
            this.fromStatus = fromStatus;
            this.toStatus = toStatus;
            this.changedBy = changedBy;
            this.reason = reason;
            this.changedDate = LocalDateTime.now();
        }
        
        // Getters and Setters
        public String getFromStatus() { return fromStatus; }
        public void setFromStatus(String fromStatus) { this.fromStatus = fromStatus; }
        
        public String getToStatus() { return toStatus; }
        public void setToStatus(String toStatus) { this.toStatus = toStatus; }
        
        public LocalDateTime getChangedDate() { return changedDate; }
        public void setChangedDate(LocalDateTime changedDate) { this.changedDate = changedDate; }
        
        public String getChangedBy() { return changedBy; }
        public void setChangedBy(String changedBy) { this.changedBy = changedBy; }
        
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
    }
    
    /**
     * Inner class for Claim Notes
     */
    public static class ClaimNote {
        @JsonProperty("id")
        private String id;
        
        @JsonProperty("text")
        private String text;
        
        @JsonProperty("createdDate")
        private LocalDateTime createdDate;
        
        @JsonProperty("authorId")
        private String authorId;
        
        @JsonProperty("authorName")
        private String authorName;
        
        public ClaimNote() {}
        
        public ClaimNote(String text, String authorId, String authorName) {
            this.text = text;
            this.authorId = authorId;
            this.authorName = authorName;
            this.createdDate = LocalDateTime.now();
        }
        
        // Getters and Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        
        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
        
        public LocalDateTime getCreatedDate() { return createdDate; }
        public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
        
        public String getAuthorId() { return authorId; }
        public void setAuthorId(String authorId) { this.authorId = authorId; }
        
        public String getAuthorName() { return authorName; }
        public void setAuthorName(String authorName) { this.authorName = authorName; }
    }
}
