package com.example.zoo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
@EnableCaching
@EnableScheduling
public class CachingConfig {

    private static final long CACHE_TIMEOUT = 100;

    public static final String COUNTRIES = "countries";
    public static final String COUNTRIES_ELASTIC = "countriesElastic";

    public static final String ANIMALS = "animals";
    public static final String ANIMALS_ELASTIC = "animalsElastic";

    public static final String ZOO = "zoo";
    public static final String ZOO_ELASTIC = "zooElastic";

    @Scheduled(fixedRate = CACHE_TIMEOUT, timeUnit = TimeUnit.MINUTES)
    @CacheEvict(value = {
            COUNTRIES,
            COUNTRIES_ELASTIC,
            ANIMALS,
            ANIMALS_ELASTIC,
            ZOO,
            ZOO_ELASTIC
    }, allEntries = true)
    public void evictCache() {
        log.info("Evicting all entries from cache, happens once in a " + CACHE_TIMEOUT + " minutes.");
    }

    @Bean
    @Primary
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(COUNTRIES, COUNTRIES_ELASTIC, ANIMALS, ANIMALS_ELASTIC, ZOO, ZOO_ELASTIC);
    }
}
