package api.clients;

import api.models.ApiResponse;
import api.utils.ApiConfigurationManager;
import api.validators.ApiResponseValidator;
import exceptions.FrameworkException;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.LoggingUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Base API Client
 * 
 * Provides common functionality for all API clients including:
 * - Configuration management
 * - Authentication handling
 * - Request/response logging
 * - Error handling
 * - Retry mechanisms
 * 
 * @author Insurance Dashboard QA Framework
 * @version 1.0
 */
public abstract class BaseApiClient {
    
    protected RequestSpecification requestSpec;
    protected ApiConfigurationManager configManager;
    protected ApiResponseValidator responseValidator;
    protected String baseUrl;
    protected String authToken;
    protected Map<String, String> defaultHeaders;
    
    // API endpoints
    protected static final String AUTH_ENDPOINT = "/auth/login";
    protected static final String USERS_ENDPOINT = "/users";
    protected static final String POLICIES_ENDPOINT = "/policies";
    protected static final String CLAIMS_ENDPOINT = "/claims";
    
    // HTTP status codes
    protected static final int HTTP_OK = 200;
    protected static final int HTTP_CREATED = 201;
    protected static final int HTTP_NO_CONTENT = 204;
    protected static final int HTTP_BAD_REQUEST = 400;
    protected static final int HTTP_UNAUTHORIZED = 401;
    protected static final int HTTP_FORBIDDEN = 403;
    protected static final int HTTP_NOT_FOUND = 404;
    protected static final int HTTP_INTERNAL_SERVER_ERROR = 500;
    
    /**
     * Constructor
     */
    public BaseApiClient() {
        this.configManager = ApiConfigurationManager.getInstance();
        this.responseValidator = new ApiResponseValidator();
        this.defaultHeaders = new HashMap<>();
        this.baseUrl = configManager.getBaseUrl();
        
        initializeRestAssured();
        setupDefaultHeaders();
    }
    
    /**
     * Initialize RestAssured configuration
     */
    private void initializeRestAssured() {
        try {
            // Set base configuration
            RestAssured.baseURI = baseUrl;
            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
            
            // Configure timeouts and connection settings
            RestAssuredConfig config = RestAssuredConfig.newConfig()
                .httpClient(HttpClientConfig.httpClientConfig()
                    .setParam("http.connection.timeout", configManager.getConnectionTimeout() * 1000)
                    .setParam("http.socket.timeout", configManager.getReadTimeout() * 1000));
            
            RestAssured.config = config;
            
            // Build request specification
            RequestSpecBuilder builder = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .log(LogDetail.ALL);
            
            this.requestSpec = builder.build();
            
            LoggingUtil.logInfo("RestAssured initialized with base URL: " + baseUrl);
            
        } catch (Exception e) {
            String errorMsg = "Failed to initialize RestAssured configuration: " + e.getMessage();
            LoggingUtil.logError(errorMsg, e);
            throw new FrameworkException(errorMsg, e);
        }
    }
    
    /**
     * Setup default headers
     */
    private void setupDefaultHeaders() {
        defaultHeaders.put("Content-Type", "application/json");
        defaultHeaders.put("Accept", "application/json");
        defaultHeaders.put("User-Agent", "Insurance-Dashboard-QA-Framework/1.0");
        
        // Add authentication header if token is available
        if (authToken != null && !authToken.isEmpty()) {
            defaultHeaders.put("Authorization", "Bearer " + authToken);
        }
    }
    
    /**
     * Authenticate and get access token
     */
    public void authenticate(String username, String password) throws FrameworkException {
        try {
            LoggingUtil.logInfo("Attempting authentication for user: " + username);
            
            Map<String, String> credentials = new HashMap<>();
            credentials.put("username", username);
            credentials.put("password", password);
            
            Response response = RestAssured
                .given(requestSpec)
                .body(credentials)
                .when()
                .post(AUTH_ENDPOINT)
                .then()
                .extract().response();
            
            if (response.getStatusCode() == HTTP_OK) {
                this.authToken = response.jsonPath().getString("token");
                if (this.authToken == null || this.authToken.isEmpty()) {
                    this.authToken = response.jsonPath().getString("data.token");
                }
                
                if (this.authToken != null) {
                    defaultHeaders.put("Authorization", "Bearer " + this.authToken);
                    LoggingUtil.logInfo("Authentication successful for user: " + username);
                } else {
                    throw new FrameworkException("Authentication response does not contain token");
                }
            } else {
                String errorMsg = "Authentication failed with status: " + response.getStatusCode() + 
                                ", body: " + response.getBody().asString();
                LoggingUtil.logError(errorMsg);
                throw new FrameworkException(errorMsg);
            }
            
        } catch (Exception e) {
            String errorMsg = "Authentication failed for user " + username + ": " + e.getMessage();
            LoggingUtil.logError(errorMsg, e);
            throw new FrameworkException(errorMsg, e);
        }
    }
    
    /**
     * Execute GET request with retry mechanism
     */
    protected Response executeGetRequest(String endpoint) throws FrameworkException {
        return executeRequest("GET", endpoint, null, null);
    }
    
    /**
     * Execute GET request with path parameters
     */
    protected Response executeGetRequest(String endpoint, Map<String, String> pathParams) throws FrameworkException {
        return executeRequest("GET", endpoint, null, pathParams);
    }
    
    /**
     * Execute POST request
     */
    protected Response executePostRequest(String endpoint, Object body) throws FrameworkException {
        return executeRequest("POST", endpoint, body, null);
    }
    
    /**
     * Execute PUT request
     */
    protected Response executePutRequest(String endpoint, Object body) throws FrameworkException {
        return executeRequest("PUT", endpoint, body, null);
    }
    
    /**
     * Execute DELETE request
     */
    protected Response executeDeleteRequest(String endpoint) throws FrameworkException {
        return executeRequest("DELETE", endpoint, null, null);
    }
    
    /**
     * Execute request with retry mechanism
     */
    private Response executeRequest(String method, String endpoint, Object body, Map<String, String> pathParams) 
            throws FrameworkException {
        
        int attempts = 0;
        int maxAttempts = configManager.getMaxRetryAttempts();
        Exception lastException = null;
        
        while (attempts < maxAttempts) {
            try {
                attempts++;
                LoggingUtil.logInfo("Executing " + method + " request to " + endpoint + " (attempt " + attempts + ")");
                
                RequestSpecification spec = RestAssured.given(requestSpec).headers(defaultHeaders);
                
                // Add path parameters if provided
                if (pathParams != null && !pathParams.isEmpty()) {
                    spec.pathParams(pathParams);
                }
                
                // Add request body if provided
                if (body != null) {
                    spec.body(body);
                }
                
                Response response;
                switch (method.toUpperCase()) {
                    case "GET":
                        response = spec.get(endpoint);
                        break;
                    case "POST":
                        response = spec.post(endpoint);
                        break;
                    case "PUT":
                        response = spec.put(endpoint);
                        break;
                    case "DELETE":
                        response = spec.delete(endpoint);
                        break;
                    default:
                        throw new FrameworkException("Unsupported HTTP method: " + method);
                }
                
                // Log response details
                LoggingUtil.logInfo("Response status: " + response.getStatusCode());
                LoggingUtil.logInfo("Response time: " + response.getTime() + "ms");
                
                // Validate response time
                if (response.getTime() > configManager.getMaxResponseTime()) {
                    LoggingUtil.logWarning("Response time (" + response.getTime() + "ms) exceeded threshold (" + 
                                         configManager.getMaxResponseTime() + "ms)");
                }
                
                return response;
                
            } catch (Exception e) {
                lastException = e;
                LoggingUtil.logError("Request attempt " + attempts + " failed: " + e.getMessage(), e);
                
                if (attempts < maxAttempts) {
                    int delay = configManager.getRetryDelay() * (int) Math.pow(2, attempts - 1);
                    LoggingUtil.logInfo("Retrying in " + delay + "ms...");
                    
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new FrameworkException("Request interrupted during retry delay", ie);
                    }
                }
            }
        }
        
        String errorMsg = "Request failed after " + maxAttempts + " attempts";
        LoggingUtil.logError(errorMsg, lastException);
        throw new FrameworkException(errorMsg, lastException);
    }
    
    /**
     * Validate API response
     */
    protected void validateResponse(Response response, int expectedStatusCode) throws FrameworkException {
        responseValidator.validateStatusCode(response, expectedStatusCode);
        responseValidator.validateContentType(response, "application/json");
        responseValidator.validateResponseTime(response, configManager.getMaxResponseTime());
    }
    
    /**
     * Parse response to ApiResponse object
     */
    protected <T> ApiResponse<T> parseResponse(Response response, Class<T> dataType) throws FrameworkException {
        try {
            if (response.getContentType().contains("application/json")) {
                String responseBody = response.getBody().asString();
                LoggingUtil.logInfo("Parsing response body to ApiResponse");
                
                // Try to parse as ApiResponse first
                try {
                    return response.as(ApiResponse.class);
                } catch (Exception e) {
                    // If that fails, create ApiResponse wrapper
                    ApiResponse<T> apiResponse = new ApiResponse<>();
                    apiResponse.setSuccess(response.getStatusCode() < 400);
                    apiResponse.setStatusCode(response.getStatusCode());
                    
                    if (response.getStatusCode() < 400) {
                        T data = response.as(dataType);
                        apiResponse.setData(data);
                    } else {
                        apiResponse.addError("Request failed with status: " + response.getStatusCode());
                    }
                    
                    return apiResponse;
                }
            } else {
                throw new FrameworkException("Unexpected response content type: " + response.getContentType());
            }
        } catch (Exception e) {
            String errorMsg = "Failed to parse API response: " + e.getMessage();
            LoggingUtil.logError(errorMsg, e);
            throw new FrameworkException(errorMsg, e);
        }
    }
    
    /**
     * Add custom header
     */
    public void addHeader(String name, String value) {
        defaultHeaders.put(name, value);
        LoggingUtil.logInfo("Added custom header: " + name + " = " + value);
    }
    
    /**
     * Remove header
     */
    public void removeHeader(String name) {
        defaultHeaders.remove(name);
        LoggingUtil.logInfo("Removed header: " + name);
    }
    
    /**
     * Clear authentication
     */
    public void clearAuthentication() {
        this.authToken = null;
        defaultHeaders.remove("Authorization");
        LoggingUtil.logInfo("Authentication cleared");
    }
    
    /**
     * Get current authentication token
     */
    public String getAuthToken() {
        return authToken;
    }
    
    /**
     * Check if client is authenticated
     */
    public boolean isAuthenticated() {
        return authToken != null && !authToken.isEmpty();
    }
    
    /**
     * Get base URL
     */
    public String getBaseUrl() {
        return baseUrl;
    }
    
    /**
     * Set custom base URL
     */
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        RestAssured.baseURI = baseUrl;
        LoggingUtil.logInfo("Base URL updated to: " + baseUrl);
    }
    
    /**
     * Get default headers
     */
    public Map<String, String> getDefaultHeaders() {
        return new HashMap<>(defaultHeaders);
    }
    
    /**
     * Abstract method for specific client initialization
     */
    protected abstract void initializeClient();
}
