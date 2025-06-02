package api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import exceptions.TestDataException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Generic API Response wrapper
 * 
 * Standardizes API response format across all endpoints
 * Provides consistent error handling and metadata
 * 
 * @author Insurance Dashboard QA Framework
 * @version 1.0
 */
public class ApiResponse<T> extends BaseApiModel {
    
    @JsonProperty("success")
    private Boolean success;
    
    @JsonProperty("message")
    private String message;
    
    @JsonProperty("data")
    private T data;
    
    @JsonProperty("errors")
    private List<String> errors;
    
    @JsonProperty("warnings")
    private List<String> warnings;
    
    @JsonProperty("timestamp")
    private LocalDateTime timestamp;
    
    @JsonProperty("requestId")
    private String requestId;
    
    @JsonProperty("statusCode")
    private Integer statusCode;
    
    @JsonProperty("pagination")
    private PaginationInfo pagination;
    
    @JsonProperty("links")
    private Map<String, String> links;
    
    /**
     * Default constructor
     */
    public ApiResponse() {
        super();
        this.timestamp = LocalDateTime.now();
        this.success = true;
    }
    
    /**
     * Success response constructor
     */
    public ApiResponse(T data) {
        this();
        this.data = data;
        this.success = true;
        this.statusCode = 200;
    }
    
    /**
     * Success response with message
     */
    public ApiResponse(T data, String message) {
        this(data);
        this.message = message;
    }
    
    /**
     * Error response constructor
     */
    public ApiResponse(List<String> errors, Integer statusCode) {
        this();
        this.errors = errors;
        this.success = false;
        this.statusCode = statusCode;
    }
    
    /**
     * Single error response constructor
     */
    public ApiResponse(String error, Integer statusCode) {
        this();
        this.errors = List.of(error);
        this.success = false;
        this.statusCode = statusCode;
    }
    
    @Override
    public boolean validate() throws TestDataException {
        return getValidationErrors().isEmpty();
    }
    
    @Override
    public List<String> getValidationErrors() {
        List<String> validationErrors = new java.util.ArrayList<>();
        
        if (success == null) {
            validationErrors.add("Success flag is required");
        }
        
        if (timestamp == null) {
            validationErrors.add("Timestamp is required");
        }
        
        if (statusCode != null && (statusCode < 100 || statusCode > 599)) {
            validationErrors.add("Invalid HTTP status code: " + statusCode);
        }
        
        return validationErrors;
    }
    
    // Helper methods for common response types
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data);
    }
    
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(data, message);
    }
    
    public static <T> ApiResponse<T> error(String error, Integer statusCode) {
        return new ApiResponse<>(error, statusCode);
    }
    
    public static <T> ApiResponse<T> error(List<String> errors, Integer statusCode) {
        return new ApiResponse<>(errors, statusCode);
    }
    
    public static <T> ApiResponse<T> badRequest(String error) {
        return new ApiResponse<>(error, 400);
    }
    
    public static <T> ApiResponse<T> unauthorized(String error) {
        return new ApiResponse<>(error, 401);
    }
    
    public static <T> ApiResponse<T> forbidden(String error) {
        return new ApiResponse<>(error, 403);
    }
    
    public static <T> ApiResponse<T> notFound(String error) {
        return new ApiResponse<>(error, 404);
    }
    
    public static <T> ApiResponse<T> internalServerError(String error) {
        return new ApiResponse<>(error, 500);
    }
    
    public void addWarning(String warning) {
        if (this.warnings == null) {
            this.warnings = new java.util.ArrayList<>();
        }
        this.warnings.add(warning);
    }
    
    public void addError(String error) {
        if (this.errors == null) {
            this.errors = new java.util.ArrayList<>();
        }
        this.errors.add(error);
        this.success = false;
    }
    
    public void addLink(String rel, String href) {
        if (this.links == null) {
            this.links = new java.util.HashMap<>();
        }
        this.links.put(rel, href);
    }
    
    public boolean hasErrors() {
        return errors != null && !errors.isEmpty();
    }
    
    public boolean hasWarnings() {
        return warnings != null && !warnings.isEmpty();
    }
    
    public boolean isSuccess() {
        return Boolean.TRUE.equals(success) && !hasErrors();
    }
    
    // Getters and Setters
    public Boolean getSuccess() { return success; }
    public void setSuccess(Boolean success) { this.success = success; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
    
    public List<String> getErrors() { return errors; }
    public void setErrors(List<String> errors) { this.errors = errors; }
    
    public List<String> getWarnings() { return warnings; }
    public void setWarnings(List<String> warnings) { this.warnings = warnings; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }
    
    public Integer getStatusCode() { return statusCode; }
    public void setStatusCode(Integer statusCode) { this.statusCode = statusCode; }
    
    public PaginationInfo getPagination() { return pagination; }
    public void setPagination(PaginationInfo pagination) { this.pagination = pagination; }
    
    public Map<String, String> getLinks() { return links; }
    public void setLinks(Map<String, String> links) { this.links = links; }
    
    /**
     * Inner class for pagination information
     */
    public static class PaginationInfo {
        @JsonProperty("page")
        private Integer page;
        
        @JsonProperty("size")
        private Integer size;
        
        @JsonProperty("totalElements")
        private Long totalElements;
        
        @JsonProperty("totalPages")
        private Integer totalPages;
        
        @JsonProperty("hasNext")
        private Boolean hasNext;
        
        @JsonProperty("hasPrevious")
        private Boolean hasPrevious;
        
        public PaginationInfo() {}
        
        public PaginationInfo(Integer page, Integer size, Long totalElements) {
            this.page = page;
            this.size = size;
            this.totalElements = totalElements;
            this.totalPages = (int) Math.ceil((double) totalElements / size);
            this.hasNext = page < totalPages - 1;
            this.hasPrevious = page > 0;
        }
        
        // Getters and Setters
        public Integer getPage() { return page; }
        public void setPage(Integer page) { this.page = page; }
        
        public Integer getSize() { return size; }
        public void setSize(Integer size) { this.size = size; }
        
        public Long getTotalElements() { return totalElements; }
        public void setTotalElements(Long totalElements) { this.totalElements = totalElements; }
        
        public Integer getTotalPages() { return totalPages; }
        public void setTotalPages(Integer totalPages) { this.totalPages = totalPages; }
        
        public Boolean getHasNext() { return hasNext; }
        public void setHasNext(Boolean hasNext) { this.hasNext = hasNext; }
        
        public Boolean getHasPrevious() { return hasPrevious; }
        public void setHasPrevious(Boolean hasPrevious) { this.hasPrevious = hasPrevious; }
    }
}
