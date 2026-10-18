package com.example.TechWeek25.service;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class CacheInspectionService {
    private final CacheManager cacheManager;

    // List of cache names used in your @Cacheable annotations
    private final List<String> appCaches = Arrays.asList(
            "generalCategories",
            "specificCategories",
            "femalePopulation",
            "malePopulation"
    );

    public CacheInspectionService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    /**
     * Prints the content for a specific dongCode across all relevant caches.
     */
    public void inspectAllCachesForDongCode(String dongCode) {
        System.out.println("======= Full Cache Inspection for DongCode: " + dongCode + " =======");
        for (String cacheName : appCaches) {
            printCacheValue(cacheName, dongCode);
        }
        System.out.println("================================================================");
    }

    /**
     * Inspects a specific key within a named cache.
     */
    public void printCacheValue(String cacheName, Object key) {
        Cache cache = cacheManager.getCache(cacheName);

        if (cache != null) {
            Cache.ValueWrapper wrapper = cache.get(key);

            System.out.println("[" + cacheName + "]");
            if (wrapper != null && wrapper.get() != null) {
                Object cachedValue = wrapper.get();

                if (cachedValue instanceof Map) {
                    Map<?, ?> map = (Map<?, ?>) cachedValue;
                    System.out.println("  Key: " + key);
                    System.out.println("  Entries found: " + map.size());
                    map.forEach((k, v) -> System.out.println("    - " + k + ": " + v));
                } else {
                    System.out.println("  Key: " + key + " | Value: " + cachedValue);
                }
            } else {
                System.out.println("  Key '" + key + "' not found.");
            }
        } else {
            System.out.println("  Cache '" + cacheName + "' does not exist.");
        }
        System.out.println();
    }
}