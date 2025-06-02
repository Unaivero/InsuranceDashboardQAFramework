dition", duration);
            return result;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            LoggingUtil.logWaitOperation("anyCondition", "Multiple conditions", timeoutInSeconds, false);
            LoggingUtil.logPerformance("waitForAnyCondition_Failed", duration);
            logger.warn("Any condition wait failed after {}ms", duration);
            return false;
        }
    }

    /**
     * Sleep for specified milliseconds (use sparingly, prefer explicit waits)
     */
    public static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
            logger.debug("Explicit sleep for {}ms", milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Sleep interrupted");
        }
    }

    /**
     * Smart wait that combines multiple strategies for robust waiting
     */
    public static boolean smartWait(By locator) {
        return smartWait(locator, DEFAULT_TIMEOUT_SECONDS);
    }

    /**
     * Smart wait that combines multiple strategies for robust waiting with custom timeout
     */
    public static boolean smartWait(By locator, long timeoutInSeconds) {
        long startTime = System.currentTimeMillis();
        
        try {
            // Strategy 1: Wait for element to be present
            waitForElementPresent(locator, timeoutInSeconds / 3);
            
            // Strategy 2: Wait for element to be visible
            waitForElementVisible(locator, timeoutInSeconds / 3);
            
            // Strategy 3: Wait for element to be clickable
            waitForElementClickable(locator, timeoutInSeconds / 3);
            
            // Strategy 4: Wait for any animations to complete
            WebElement element = driver.findElement(locator);
            waitForElementToStopMoving(element, ANIMATION_TIMEOUT);
            
            long duration = System.currentTimeMillis() - startTime;
            LoggingUtil.logWaitOperation("smartWait", locator.toString(), timeoutInSeconds, true);
            LoggingUtil.logPerformance("smartWait", duration);
            return true;
            
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            LoggingUtil.logWaitOperation("smartWait", locator.toString(), timeoutInSeconds, false);
            LoggingUtil.logPerformance("smartWait_Failed", duration);
            logger.warn("Smart wait failed for: {} after {}ms", locator, duration);
            return false;
        }
    }

    /**
     * Wait for element list to have expected size
     */
    public static boolean waitForElementListSize(By locator, int expectedSize) {
        return waitForElementListSize(locator, expectedSize, DEFAULT_TIMEOUT_SECONDS);
    }

    /**
     * Wait for element list to have expected size with custom timeout
     */
    public static boolean waitForElementListSize(By locator, int expectedSize, long timeoutInSeconds) {
        long startTime = System.currentTimeMillis();
        try {
            ExpectedCondition<Boolean> listSizeCondition = driver -> {
                try {
                    List<WebElement> elements = driver.findElements(locator);
                    return elements.size() == expectedSize;
                } catch (Exception e) {
                    return false;
                }
            };
            
            boolean result = getWait(timeoutInSeconds).until(listSizeCondition);
            long duration = System.currentTimeMillis() - startTime;
            LoggingUtil.logWaitOperation("listSize", locator.toString() + " size=" + expectedSize, timeoutInSeconds, result);
            LoggingUtil.logPerformance("waitForElementListSize", duration);
            return result;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            LoggingUtil.logWaitOperation("listSize", locator.toString() + " size=" + expectedSize, timeoutInSeconds, false);
            LoggingUtil.logPerformance("waitForElementListSize_Failed", duration);
            logger.warn("Element list size wait failed for: {} expected size: {} after {}ms", locator, expectedSize, duration);
            return false;
        }
    }

    /**
     * Wait for element list to have minimum size
     */
    public static boolean waitForElementListMinSize(By locator, int minSize) {
        return waitForElementListMinSize(locator, minSize, DEFAULT_TIMEOUT_SECONDS);
    }

    /**
     * Wait for element list to have minimum size with custom timeout
     */
    public static boolean waitForElementListMinSize(By locator, int minSize, long timeoutInSeconds) {
        long startTime = System.currentTimeMillis();
        try {
            ExpectedCondition<Boolean> listMinSizeCondition = driver -> {
                try {
                    List<WebElement> elements = driver.findElements(locator);
                    return elements.size() >= minSize;
                } catch (Exception e) {
                    return false;
                }
            };
            
            boolean result = getWait(timeoutInSeconds).until(listMinSizeCondition);
            long duration = System.currentTimeMillis() - startTime;
            LoggingUtil.logWaitOperation("listMinSize", locator.toString() + " minSize=" + minSize, timeoutInSeconds, result);
            LoggingUtil.logPerformance("waitForElementListMinSize", duration);
            return result;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            LoggingUtil.logWaitOperation("listMinSize", locator.toString() + " minSize=" + minSize, timeoutInSeconds, false);
            LoggingUtil.logPerformance("waitForElementListMinSize_Failed", duration);
            logger.warn("Element list min size wait failed for: {} min size: {} after {}ms", locator, minSize, duration);
            return false;
        }
    }

    // ==================== CONFIGURATION METHODS ====================

    /**
     * Get current timeout configurations
     */
    public static String getTimeoutConfiguration() {
        return String.format(
            "Wait Timeouts Configuration:\n" +
            "- Default Timeout: %d seconds\n" +
            "- Short Timeout: %d seconds\n" +
            "- Long Timeout: %d seconds\n" +
            "- Polling Interval: %d milliseconds\n" +
            "- Element Timeout: %d seconds\n" +
            "- Page Load Timeout: %d seconds\n" +
            "- Ajax Timeout: %d seconds\n" +
            "- Animation Timeout: %d seconds\n" +
            "- Download Timeout: %d seconds",
            DEFAULT_TIMEOUT_SECONDS, SHORT_TIMEOUT_SECONDS, LONG_TIMEOUT_SECONDS,
            POLLING_INTERVAL_MILLIS, ELEMENT_TIMEOUT, PAGE_LOAD_TIMEOUT,
            AJAX_TIMEOUT, ANIMATION_TIMEOUT, DOWNLOAD_TIMEOUT
        );
    }

    /**
     * Log current wait configuration
     */
    public static void logWaitConfiguration() {
        logger.info("WaitUtil initialized with configuration:\n{}", getTimeoutConfiguration());
    }

    // Static initializer to log configuration when class is loaded
    static {
        logWaitConfiguration();
    }
}
