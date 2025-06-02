package api.validators;

import api.models.ApiResponse;
import exceptions.FrameworkException;
import io.restassured.response.Response;
import utils.LoggingUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * API Response Validator
 * 
 * Provides comprehensive validation for API responses including:
 * - Status code validation
 * - Content type validation
 * - Response time validation
 * - Schema validation
 * - Business rule validation
 * 
 * @author Insurance Dashboard QA Framework
 * @version 1.0
 */
public class ApiResponseValidator {
    
    /**
     * Validate response status code
     */
    public void validateStatusCode(Response response, int expectedStatusCode) throws FrameworkException {
        int actualStatusCode = response.getStatusCode();
        
        if (actualStatusCode != expectedStatusCode) {
            String errorMsg = String.format("Expected status code %d but got %d. Response body: %s", 
                                           expectedStatusCode, actualStatusCode, response.getBody().asString());
            LoggingUtil.logError(errorMsg);
            throw new FrameworkException(errorMsg);
        }
        
        LoggingUtil.logInfo("Status code validation passed: " + actualStatusCode);
    }
    
    /**
     * Validate response status code is in range
     */
    public void validateStatusCodeInRange(Response response, int minStatusCode, int maxStatusCode) throws FrameworkException {
        int actualStatusCode = response.getStatusCode();
        
        if (actualStatusCode < minStatusCode || actualStatusCode > maxStatusCode) {
            String errorMsg = String.format("Expected status code between %d and %d but got %d. Response body: %s", 
                                           minStatusCode, maxStatusCode, actualStatusCode, response.getBody().asString());
            LoggingUtil.logError(errorMsg);
            throw new FrameworkException(errorMsg);
        }
        
        LoggingUtil.logInfo("Status code range validation passed: " + actualStatusCode);
    }
    
    /**
     * Validate response is successful (2xx)
     */
    public void validateSuccessResponse(Response response) throws FrameworkException {
        validateStatusCodeInRange(response, 200, 299);
    }
    
    /**
     * Validate response content type
     */
    public void validateContentType(Response response, String expectedContentType) throws FrameworkException {
        String actualContentType = response.getContentType();
        
        if (actualContentType == null) {
            String errorMsg = "Response content type is null, expected: " + expectedContentType;
            LoggingUtil.logError(errorMsg);
            throw new FrameworkException(errorMsg);
        }
        
        // Check if the actual content type contains the expected type (handles charset and other parameters)
        if (!actualContentType.toLowerCase().contains(expectedContentType.toLowerCase())) {
            String errorMsg = String.format("Expected content type to contain '%s' but got '%s'", 
                                           expectedContentType, actualContentType);
            LoggingUtil.logError(errorMsg);
            throw new FrameworkException(errorMsg);
        }
        
        LoggingUtil.logInfo("Content type validation passed: " + actualContentType);
    }
    
    /**
     * Validate response time
     */
    public void validateResponseTime(Response response, long maxResponseTimeMs) throws FrameworkException {
        long actualResponseTime = response.getTime();
        
        if (actualResponseTime > maxResponseTimeMs) {
            String errorMsg = String.format("Response time %dms exceeded maximum allowed time %dms", 
                                           actualResponseTime, maxResponseTimeMs);
            LoggingUtil.logError(errorMsg);
            throw new FrameworkException(errorMsg);
        }
        
        LoggingUtil.logInfo("Response time validation passed: " + actualResponseTime + "ms");
    }
    
    /**
     * Validate response body is not empty
     */
    public void validateResponseBodyNotEmpty(Response response) throws FrameworkException {
        String responseBody = response.getBody().asString();
        
        if (responseBody == null || responseBody.trim().isEmpty()) {
            String errorMsg = "Response body is empty";
            LoggingUtil.logError(errorMsg);
            throw new FrameworkException(errorMsg);
        }
        
        LoggingUtil.logInfo("Response body validation passed - body is not empty");
    }
    
    /**
     * Validate response body contains specific text
     */
    public void validateResponseBodyContains(Response response, String expectedText) throws FrameworkException {
        String responseBody = response.getBody().asString();
        
        if (responseBody == null || !responseBody.contains(expectedText)) {
            String errorMsg = String.format("Response body does not contain expected text '%s'. Actual body: %s", 
                                           expectedText, responseBody);
            LoggingUtil.logError(errorMsg);
            throw new FrameworkException(errorMsg);
        }
        
        LoggingUtil.logInfo("Response body contains validation passed for text: " + expectedText);
    }
    
    /**
     * Validate JSON response structure
     */
    public void validateJsonStructure(Response response, String... requiredFields) throws FrameworkException {
        try {
            List<String> missingFields = new ArrayList<>();
            
            for (String field : requiredFields) {
                try {
                    Object value = response.jsonPath().get(field);
                    if (value == null) {
                        missingFields.add(field);
                    }
                } catch (Exception e) {
                    missingFields.add(field);
                }
            }
            
            if (!missingFields.isEmpty()) {
                String errorMsg = "Missing required JSON fields: " + String.join(", ", missingFields) + 
                                ". Response body: " + response.getBody().asString();
                LoggingUtil.logError(errorMsg);
                throw new FrameworkException(errorMsg);
            }
            
            LoggingUtil.logInfo("JSON structure validation passed for fields: " + String.join(", ", requiredFields));
            
        } catch (Exception e) {
            String errorMsg = "Failed to validate JSON structure: " + e.getMessage() + 
                            ". Response body: " + response.getBody().asString();
            LoggingUtil.logError(errorMsg, e);
            throw new FrameworkException(errorMsg, e);
        }
    }
    
    /**
     * Validate ApiResponse wrapper structure
     */
    public void validateApiResponseStructure(Response response) throws FrameworkException {
        validateJsonStructure(response, "success", "timestamp");
        
        // Additional validation for ApiResponse specific fields
        try {
            Boolean success = response.jsonPath().get("success");
            if (success == null) {
                throw new FrameworkException("ApiResponse 'success' field is null");
            }
            
            // If success is false, there should be errors
            if (!success) {
                Object errors = response.jsonPath().get("errors");
                if (errors == null) {
                    LoggingUtil.logWarning("ApiResponse indicates failure but no errors field found");
                }
            }
            
            LoggingUtil.logInfo("ApiResponse structure validation passed");
            
        } catch (Exception e) {
            String errorMsg = "Failed to validate ApiResponse structure: " + e.getMessage();
            LoggingUtil.logError(errorMsg, e);
            throw new FrameworkException(errorMsg, e);
        }
    }
    
    /**
     * Validate API response indicates success
     */
    public void validateApiResponseSuccess(Response response) throws FrameworkException {
        try {
            Boolean success = response.jsonPath().get("success");
            
            if (success == null) {
                throw new FrameworkException("ApiResponse 'success' field is null");
            }
            
            if (!success) {
                Object errors = response.jsonPath().get("errors");
                String errorMsg = "ApiResponse indicates failure. Errors: " + errors;
                LoggingUtil.logError(errorMsg);
                throw new FrameworkException(errorMsg);
            }
            
            LoggingUtil.logInfo("ApiResponse success validation passed");
            
        } catch (Exception e) {
            String errorMsg = "Failed to validate ApiResponse success: " + e.getMessage();
            LoggingUtil.logError(errorMsg, e);
            throw new FrameworkException(errorMsg, e);
        }
    }
    
    /**
     * Validate pagination info if present
     */
    public void validatePaginationInfo(Response response) throws FrameworkException {
        try {
            Object pagination = response.jsonPath().get("pagination");
            
            if (pagination != null) {
                // Validate pagination structure
                validateJsonStructure(response, "pagination.page", "pagination.size", "pagination.totalElements");
                
                Integer page = response.jsonPath().get("pagination.page");
                Integer size = response.jsonPath().get("pagination.size");
                Long totalElements = response.jsonPath().get("pagination.totalElements");
                
                if (page == null || page < 0) {
                    throw new FrameworkException("Invalid pagination page: " + page);
                }
                
                if (size == null || size <= 0) {
                    throw new FrameworkException("Invalid pagination size: " + size);
                }
                
                if (totalElements == null || totalElements < 0) {
                    throw new FrameworkException("Invalid pagination totalElements: " + totalElements);
                }
                
                LoggingUtil.logInfo("Pagination validation passed - page: " + page + ", size: " + size + ", total: " + totalElements);
            }
            
        } catch (Exception e) {
            String errorMsg = "Failed to validate pagination info: " + e.getMessage();
            LoggingUtil.logError(errorMsg, e);
            throw new FrameworkException(errorMsg, e);
        }
    }
    
    /**
     * Validate response headers
     */
    public void validateResponseHeaders(Response response, String headerName, String expectedValue) throws FrameworkException {
        String actualValue = response.getHeader(headerName);
        
        if (actualValue == null) {
            String errorMsg = "Response header '" + headerName + "' is missing";
            LoggingUtil.logError(errorMsg);
            throw new FrameworkException(errorMsg);
        }
        
        if (!actualValue.equals(expectedValue)) {
            String errorMsg = String.format("Response header '%s' expected '%s' but got '%s'", 
                                           headerName, expectedValue, actualValue);
            LoggingUtil.logError(errorMsg);
            throw new FrameworkException(errorMsg);
        }
        
        LoggingUtil.logInfo("Response header validation passed: " + headerName + " = " + actualValue);
    }
    
    /**
     * Validate response header exists
     */
    public void validateResponseHeaderExists(Response response, String headerName) throws FrameworkException {
        String headerValue = response.getHeader(headerName);
        
        if (headerValue == null) {
            String errorMsg = "Response header '" + headerName + "' is missing";
            LoggingUtil.logError(errorMsg);
            throw new FrameworkException(errorMsg);
        }
        
        LoggingUtil.logInfo("Response header exists validation passed: " + headerName);
    }
    
    /**
     * Validate array response has expected size
     */
    public void validateArraySize(Response response, String jsonPath, int expectedSize) throws FrameworkException {
        try {
            List<?> array = response.jsonPath().getList(jsonPath);
            
            if (array == null) {
                throw new FrameworkException("Array at path '" + jsonPath + "' is null");
            }
            
            int actualSize = array.size();
            if (actualSize != expectedSize) {
                String errorMsg = String.format("Array at path '%s' expected size %d but got %d", 
                                               jsonPath, expectedSize, actualSize);
                LoggingUtil.logError(errorMsg);
                throw new FrameworkException(errorMsg);
            }
            
            LoggingUtil.logInfo("Array size validation passed: " + jsonPath + " has " + actualSize + " elements");
            
        } catch (Exception e) {
            String errorMsg = "Failed to validate array size at path '" + jsonPath + "': " + e.getMessage();
            LoggingUtil.logError(errorMsg, e);
            throw new FrameworkException(errorMsg, e);
        }
    }
    
    /**
     * Validate array response is not empty
     */
    public void validateArrayNotEmpty(Response response, String jsonPath) throws FrameworkException {
        try {
            List<?> array = response.jsonPath().getList(jsonPath);
            
            if (array == null || array.isEmpty()) {
                String errorMsg = "Array at path '" + jsonPath + "' is null or empty";
                LoggingUtil.logError(errorMsg);
                throw new FrameworkException(errorMsg);
            }
            
            LoggingUtil.logInfo("Array not empty validation passed: " + jsonPath + " has " + array.size() + " elements");
            
        } catch (Exception e) {
            String errorMsg = "Failed to validate array not empty at path '" + jsonPath + "': " + e.getMessage();
            LoggingUtil.logError(errorMsg, e);
            throw new FrameworkException(errorMsg, e);
        }
    }
    
    /**
     * Validate specific field value
     */
    public void validateFieldValue(Response response, String jsonPath, Object expectedValue) throws FrameworkException {
        try {
            Object actualValue = response.jsonPath().get(jsonPath);
            
            if (actualValue == null && expectedValue != null) {
                String errorMsg = String.format("Field '%s' is null but expected '%s'", jsonPath, expectedValue);
                LoggingUtil.logError(errorMsg);
                throw new FrameworkException(errorMsg);
            }
            
            if (actualValue != null && !actualValue.equals(expectedValue)) {
                String errorMsg = String.format("Field '%s' expected '%s' but got '%s'", 
                                               jsonPath, expectedValue, actualValue);
                LoggingUtil.logError(errorMsg);
                throw new FrameworkException(errorMsg);
            }
            
            LoggingUtil.logInfo("Field value validation passed: " + jsonPath + " = " + actualValue);
            
        } catch (Exception e) {
            String errorMsg = "Failed to validate field value at path '" + jsonPath + "': " + e.getMessage();
            LoggingUtil.logError(errorMsg, e);
            throw new FrameworkException(errorMsg, e);
        }
    }
    
    /**
     * Comprehensive response validation
     */
    public void validateResponse(Response response, int expectedStatusCode, String expectedContentType, 
                               long maxResponseTimeMs) throws FrameworkException {
        
        LoggingUtil.logInfo("Starting comprehensive response validation");
        
        // Validate status code
        validateStatusCode(response, expectedStatusCode);
        
        // Validate content type
        if (expectedContentType != null && !expectedContentType.isEmpty()) {
            validateContentType(response, expectedContentType);
        }
        
        // Validate response time
        validateResponseTime(response, maxResponseTimeMs);
        
        // Validate response body is not empty for success responses
        if (expectedStatusCode >= 200 && expectedStatusCode < 300) {
            validateResponseBodyNotEmpty(response);
        }
        
        LoggingUtil.logInfo("Comprehensive response validation completed successfully");
    }
}
