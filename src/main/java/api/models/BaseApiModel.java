package api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import exceptions.TestDataException;
import utils.LoggingUtil;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Base API Model providing common functionality for all API entities
 * 
 * Features:
 * - JSON serialization/deserialization
 * - Common metadata fields
 * - Validation framework
 * - Audit trail support
 * - Error handling
 * 
 * @author Insurance Dashboard QA Framework
 * @version 1.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class BaseApiModel {
    
    protected String id;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;
    protected String createdBy;
    protected String updatedBy;
    protected Integer version;
    protected Map<String, Object> metadata;
    
    private static final ObjectMapper objectMapper = createObjectMapper();
    
    /**
     * Default constructor
     */
    public BaseApiModel() {
        this.createdAt = LocalDateTime.now();
        this.version = 1;
    }
    
    /**
     * Constructor with ID
     */
    public BaseApiModel(String id) {
        this();
        this.id = id;
    }
    
    /**
     * Create configured ObjectMapper for JSON operations
     */
    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return mapper;
    }
    
    /**
     * Convert object to JSON string
     */
    public String toJson() throws TestDataException {
        try {
            String json = objectMapper.writeValueAsString(this);
            LoggingUtil.logInfo("Serialized " + this.getClass().getSimpleName() + " to JSON");
            return json;
        } catch (JsonProcessingException e) {
            String errorMsg = "Failed to serialize " + this.getClass().getSimpleName() + " to JSON: " + e.getMessage();
            LoggingUtil.logError(errorMsg, e);
            throw new TestDataException(errorMsg, e);
        }
    }
    
    /**
     * Create object from JSON string
     */
    public static <T extends BaseApiModel> T fromJson(String json, Class<T> clazz) throws TestDataException {
        try {
            T object = objectMapper.readValue(json, clazz);
            LoggingUtil.logInfo("Deserialized JSON to " + clazz.getSimpleName());
            return object;
        } catch (JsonProcessingException e) {
            String errorMsg = "Failed to deserialize JSON to " + clazz.getSimpleName() + ": " + e.getMessage();
            LoggingUtil.logError(errorMsg, e);
            throw new TestDataException(errorMsg, e);
        }
    }
    
    /**
     * Convert object to pretty JSON string
     */
    public String toPrettyJson() throws TestDataException {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            String errorMsg = "Failed to serialize " + this.getClass().getSimpleName() + " to pretty JSON: " + e.getMessage();
            LoggingUtil.logError(errorMsg, e);
            throw new TestDataException(errorMsg, e);
        }
    }
    
    /**
     * Validate the model - to be implemented by subclasses
     */
    public abstract boolean validate() throws TestDataException;
    
    /**
     * Get validation errors - to be implemented by subclasses
     */
    public abstract java.util.List<String> getValidationErrors();
    
    /**
     * Check if model is valid
     */
    public boolean isValid() {
        try {
            return validate();
        } catch (TestDataException e) {
            LoggingUtil.logError("Validation failed for " + this.getClass().getSimpleName(), e);
            return false;
        }
    }
    
    /**
     * Update metadata
     */
    public void updateMetadata(String key, Object value) {
        if (this.metadata == null) {
            this.metadata = new java.util.HashMap<>();
        }
        this.metadata.put(key, value);
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Mark as updated
     */
    public void markAsUpdated(String updatedBy) {
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = updatedBy;
        if (this.version != null) {
            this.version++;
        }
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    
    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
    
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    
    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    
    @Override
    public String toString() {
        try {
            return toPrettyJson();
        } catch (TestDataException e) {
            return super.toString();
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BaseApiModel that = (BaseApiModel) obj;
        return java.util.Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return java.util.Objects.hash(id);
    }
}
