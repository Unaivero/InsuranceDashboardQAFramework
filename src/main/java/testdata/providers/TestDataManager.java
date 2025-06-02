package testdata.providers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import exceptions.TestDataException;
import org.slf4j.Logger;
import testdata.models.BaseTestDataModel;
import utils.ErrorHandler;
import utils.LoggingUtil;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Centralized test data manager for loading, caching, and providing test data
 * Supports multiple data sources: JSON files, CSV files, hardcoded data
 */
public class TestDataManager {
    
    private static final Logger logger = LoggingUtil.getLogger(TestDataManager.class);
    private static TestDataManager instance;
    private final Map<String, Map<String, BaseTestDataModel>> dataCache;
    private final ObjectMapper objectMapper;
    private final Set<String> loadedSources;
    
    private TestDataManager() {
        this.dataCache = new ConcurrentHashMap<>();
        this.objectMapper = new ObjectMapper();
        this.loadedSources = new HashSet<>();
        logger.info("TestDataManager initialized");
    }
    
    /**
     * Get singleton instance of TestDataManager
     */
    public static synchronized TestDataManager getInstance() {
        if (instance == null) {
            instance = new TestDataManager();
        }
        return instance;
    }
    
    /**
     * Load test data from JSON file
     */
    public <T extends BaseTestDataModel> void loadFromJson(String dataType, String filePath, Class<T> modelClass) {
        try {
            ErrorHandler.validateNotEmpty(dataType, "data type");
            ErrorHandler.validateNotEmpty(filePath, "file path");
            ErrorHandler.validateNotNull(modelClass, "model class");
            
            logger.info("Loading test data from JSON - Type: {}, File: {}", dataType, filePath);
            
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
            if (inputStream == null) {
                throw new TestDataException("TestDataManager", "loadFromJson", 
                    "JSON file not found: " + filePath);
            }
            
            String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            
            // Try to parse as array first, then as single object
            List<T> dataList;
            try {
                CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, modelClass);
                dataList = objectMapper.readValue(content, listType);
            } catch (Exception e) {
                // If array parsing fails, try single object
                T singleObject = objectMapper.readValue(content, modelClass);
                dataList = Collections.singletonList(singleObject);
            }
            
            // Store in cache
            Map<String, BaseTestDataModel> typeCache = dataCache.computeIfAbsent(dataType, k -> new ConcurrentHashMap<>());
            
            for (T item : dataList) {
                item.validate(); // Validate before storing
                typeCache.put(item.getId(), item);
                logger.debug("Loaded test data item: {}", item.getSummary());
            }
            
            loadedSources.add(filePath);
            logger.info("Successfully loaded {} test data items of type '{}' from JSON file: {}", 
                       dataList.size(), dataType, filePath);
            
        } catch (Exception e) {
            ErrorHandler.handleTestDataError(dataType, "loadFromJson", 
                String.format("Failed to load from file: %s", filePath));
        }
    }
    
    /**
     * Add test data programmatically
     */
    public <T extends BaseTestDataModel> void addTestData(String dataType, T data) {
        try {
            ErrorHandler.validateNotEmpty(dataType, "data type");
            ErrorHandler.validateNotNull(data, "test data");
            
            data.validate(); // Validate before storing
            
            Map<String, BaseTestDataModel> typeCache = dataCache.computeIfAbsent(dataType, k -> new ConcurrentHashMap<>());
            typeCache.put(data.getId(), data);
            
            logger.debug("Added test data item: {}", data.getSummary());
            
        } catch (Exception e) {
            ErrorHandler.handleTestDataError(dataType, "addTestData", 
                String.format("Failed to add data with ID: %s", data != null ? data.getId() : "null"));
        }
    }
    
    /**
     * Get test data by ID and type
     */
    @SuppressWarnings("unchecked")
    public <T extends BaseTestDataModel> T getTestData(String dataType, String id, Class<T> expectedClass) {
        try {
            ErrorHandler.validateNotEmpty(dataType, "data type");
            ErrorHandler.validateNotEmpty(id, "ID");
            ErrorHandler.validateNotNull(expectedClass, "expected class");
            
            Map<String, BaseTestDataModel> typeCache = dataCache.get(dataType);
            if (typeCache == null) {
                logger.warn("No test data loaded for type: {}", dataType);
                return null;
            }
            
            BaseTestDataModel data = typeCache.get(id);
            if (data == null) {
                logger.warn("Test data not found - Type: {}, ID: {}", dataType, id);
                return null;
            }
            
            if (!expectedClass.isInstance(data)) {
                throw new TestDataException("TestDataManager", "getTestData", 
                    String.format("Data type mismatch - Expected: %s, Actual: %s", 
                                expectedClass.getSimpleName(), data.getClass().getSimpleName()));
            }
            
            logger.debug("Retrieved test data: {}", data.getSummary());
            return (T) data;
            
        } catch (Exception e) {
            ErrorHandler.handleTestDataError(dataType, "getTestData", 
                String.format("Failed to retrieve data with ID: %s", id));
            return null;
        }
    }
    
    /**
     * Get all test data of a specific type
     */
    @SuppressWarnings("unchecked")
    public <T extends BaseTestDataModel> List<T> getAllTestData(String dataType, Class<T> expectedClass) {
        try {
            ErrorHandler.validateNotEmpty(dataType, "data type");
            ErrorHandler.validateNotNull(expectedClass, "expected class");
            
            Map<String, BaseTestDataModel> typeCache = dataCache.get(dataType);
            if (typeCache == null) {
                logger.warn("No test data loaded for type: {}", dataType);
                return new ArrayList<>();
            }
            
            List<T> result = new ArrayList<>();
            for (BaseTestDataModel data : typeCache.values()) {
                if (expectedClass.isInstance(data)) {
                    result.add((T) data);
                }
            }
            
            logger.debug("Retrieved {} test data items of type: {}", result.size(), dataType);
            return result;
            
        } catch (Exception e) {
            ErrorHandler.handleTestDataError(dataType, "getAllTestData", 
                "Failed to retrieve all data");
            return new ArrayList<>();
        }
    }
    
    /**
     * Get test data with custom filter
     */
    @SuppressWarnings("unchecked")
    public <T extends BaseTestDataModel> List<T> getTestDataWhere(String dataType, Class<T> expectedClass, Predicate<T> filter) {
        try {
            ErrorHandler.validateNotEmpty(dataType, "data type");
            ErrorHandler.validateNotNull(expectedClass, "expected class");
            ErrorHandler.validateNotNull(filter, "filter");
            
            List<T> allData = getAllTestData(dataType, expectedClass);
            List<T> filtered = allData.stream()
                .filter(filter)
                .collect(Collectors.toList());
            
            logger.debug("Filtered {} items from {} total items of type: {}", 
                        filtered.size(), allData.size(), dataType);
            return filtered;
            
        } catch (Exception e) {
            ErrorHandler.handleTestDataError(dataType, "getTestDataWhere", 
                "Failed to filter data");
            return new ArrayList<>();
        }
    }
    
    /**
     * Get random test data item of a specific type
     */
    public <T extends BaseTestDataModel> T getRandomTestData(String dataType, Class<T> expectedClass) {
        try {
            List<T> allData = getAllTestData(dataType, expectedClass);
            if (allData.isEmpty()) {
                logger.warn("No test data available for random selection - Type: {}", dataType);
                return null;
            }
            
            Random random = new Random();
            T randomData = allData.get(random.nextInt(allData.size()));
            logger.debug("Selected random test data: {}", randomData.getSummary());
            return randomData;
            
        } catch (Exception e) {
            ErrorHandler.handleTestDataError(dataType, "getRandomTestData", 
                "Failed to get random data");
            return null;
        }
    }
    
    /**
     * Update existing test data
     */
    public <T extends BaseTestDataModel> void updateTestData(String dataType, T data) {
        try {
            ErrorHandler.validateNotEmpty(dataType, "data type");
            ErrorHandler.validateNotNull(data, "test data");
            
            data.validate(); // Validate before updating
            
            Map<String, BaseTestDataModel> typeCache = dataCache.get(dataType);
            if (typeCache == null) {
                throw new TestDataException("TestDataManager", "updateTestData", 
                    "No data cache exists for type: " + dataType);
            }
            
            if (!typeCache.containsKey(data.getId())) {
                throw new TestDataException("TestDataManager", "updateTestData", 
                    String.format("Test data not found for update - Type: %s, ID: %s", dataType, data.getId()));
            }
            
            typeCache.put(data.getId(), data);
            logger.debug("Updated test data: {}", data.getSummary());
            
        } catch (Exception e) {
            ErrorHandler.handleTestDataError(dataType, "updateTestData", 
                String.format("Failed to update data with ID: %s", data != null ? data.getId() : "null"));
        }
    }
    
    /**
     * Remove test data by ID
     */
    public void removeTestData(String dataType, String id) {
        try {
            ErrorHandler.validateNotEmpty(dataType, "data type");
            ErrorHandler.validateNotEmpty(id, "ID");
            
            Map<String, BaseTestDataModel> typeCache = dataCache.get(dataType);
            if (typeCache == null) {
                logger.warn("No data cache exists for type: {}", dataType);
                return;
            }
            
            BaseTestDataModel removed = typeCache.remove(id);
            if (removed != null) {
                logger.debug("Removed test data: {}", removed.getSummary());
            } else {
                logger.warn("Test data not found for removal - Type: {}, ID: {}", dataType, id);
            }
            
        } catch (Exception e) {
            ErrorHandler.handleTestDataError(dataType, "removeTestData", 
                String.format("Failed to remove data with ID: %s", id));
        }
    }
    
    /**
     * Clear all test data of a specific type
     */
    public void clearTestData(String dataType) {
        try {
            ErrorHandler.validateNotEmpty(dataType, "data type");
            
            Map<String, BaseTestDataModel> typeCache = dataCache.get(dataType);
            if (typeCache != null) {
                int count = typeCache.size();
                typeCache.clear();
                logger.info("Cleared {} test data items of type: {}", count, dataType);
            } else {
                logger.warn("No data cache exists for type: {}", dataType);
            }
            
        } catch (Exception e) {
            ErrorHandler.handleTestDataError(dataType, "clearTestData", 
                "Failed to clear data");
        }
    }
    
    /**
     * Clear all test data
     */
    public void clearAllTestData() {
        try {
            int totalCount = dataCache.values().stream()
                .mapToInt(Map::size)
                .sum();
            
            dataCache.clear();
            loadedSources.clear();
            
            logger.info("Cleared all test data - {} items across {} types", 
                       totalCount, dataCache.size());
            
        } catch (Exception e) {
            ErrorHandler.handleTestDataError("ALL", "clearAllTestData", 
                "Failed to clear all data");
        }
    }
    
    /**
     * Get data cache statistics
     */
    public Map<String, Integer> getDataStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        
        for (Map.Entry<String, Map<String, BaseTestDataModel>> entry : dataCache.entrySet()) {
            stats.put(entry.getKey(), entry.getValue().size());
        }
        
        return stats;
    }
    
    /**
     * Check if data type is loaded
     */
    public boolean isDataTypeLoaded(String dataType) {
        return dataCache.containsKey(dataType) && !dataCache.get(dataType).isEmpty();
    }
    
    /**
     * Get loaded sources
     */
    public Set<String> getLoadedSources() {
        return new HashSet<>(loadedSources);
    }
    
    /**
     * Export test data to JSON string
     */
    public String exportToJson(String dataType) {
        try {
            ErrorHandler.validateNotEmpty(dataType, "data type");
            
            List<BaseTestDataModel> data = getAllTestData(dataType, BaseTestDataModel.class);
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
            
        } catch (Exception e) {
            ErrorHandler.handleTestDataError(dataType, "exportToJson", 
                "Failed to export data to JSON");
            return "{}";
        }
    }
    
    /**
     * Create a copy of test data (useful for test isolation)
     */
    @SuppressWarnings("unchecked")
    public <T extends BaseTestDataModel> T copyTestData(String dataType, String id, Class<T> expectedClass) {
        T original = getTestData(dataType, id, expectedClass);
        if (original == null) {
            return null;
        }
        
        return (T) original.copy();
    }
    
    /**
     * Validate all test data of a specific type
     */
    public List<String> validateTestData(String dataType) {
        List<String> validationErrors = new ArrayList<>();
        
        try {
            Map<String, BaseTestDataModel> typeCache = dataCache.get(dataType);
            if (typeCache == null) {
                validationErrors.add("No data cache exists for type: " + dataType);
                return validationErrors;
            }
            
            for (BaseTestDataModel data : typeCache.values()) {
                try {
                    data.validate();
                } catch (TestDataException e) {
                    validationErrors.add(String.format("Validation failed for %s: %s", 
                                                     data.getSummary(), e.getMessage()));
                }
            }
            
            logger.info("Validated {} test data items of type '{}' - {} errors found", 
                       typeCache.size(), dataType, validationErrors.size());
            
        } catch (Exception e) {
            validationErrors.add("Validation process failed: " + e.getMessage());
        }
        
        return validationErrors;
    }
}
