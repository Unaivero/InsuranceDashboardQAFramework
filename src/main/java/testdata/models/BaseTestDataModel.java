package testdata.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import exceptions.TestDataException;
import utils.ErrorHandler;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Base model class for all test data entities
 * Provides common functionality and validation
 */
public abstract class BaseTestDataModel {
    
    @JsonProperty("id")
    protected String id;
    
    @JsonProperty("createdDate")
    protected String createdDate;
    
    @JsonProperty("modifiedDate")
    protected String modifiedDate;
    
    @JsonProperty("isActive")
    protected boolean isActive = true;
    
    @JsonProperty("description")
    protected String description;
    
    public BaseTestDataModel() {
        this.createdDate = getCurrentDate();
        this.modifiedDate = getCurrentDate();
    }
    
    public BaseTestDataModel(String id) {
        this();
        this.id = id;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        ErrorHandler.validateNotEmpty(id, "ID");
        this.id = id;
        this.modifiedDate = getCurrentDate();
    }
    
    public String getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
    
    public String getModifiedDate() {
        return modifiedDate;
    }
    
    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
        this.modifiedDate = getCurrentDate();
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
        this.modifiedDate = getCurrentDate();
    }
    
    // Utility methods
    protected String getCurrentDate() {
        return LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
    
    /**
     * Validate the model - to be implemented by subclasses
     */
    public abstract void validate() throws TestDataException;
    
    /**
     * Get a summary of the model for logging purposes
     */
    public abstract String getSummary();
    
    /**
     * Create a copy of the model
     */
    public abstract BaseTestDataModel copy();
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseTestDataModel that = (BaseTestDataModel) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return String.format("%s{id='%s', active=%s, created='%s'}", 
                           getClass().getSimpleName(), id, isActive, createdDate);
    }
}
