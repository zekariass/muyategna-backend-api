package com.muyategna.backend.system.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.muyategna.backend.common.Constants;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Configures Caffeine cache with a maximum size of 10,000 entries and an expiration time of 2 minutes.
     *
     * @return Caffeine cache configuration
     */
    @Bean
    public Caffeine<Object, Object> caffeineConfig() {
        return Caffeine.newBuilder()
                .maximumSize(10_000)
                .expireAfterWrite(Duration.ofMinutes(2));
    }

    /**
     * Configures the CacheManager to use Caffeine as the caching provider.
     *
     * @param caffeine Caffeine cache configuration
     * @return CacheManager instance configured with Caffeine
     */
    @Bean
    public CacheManager cacheManager(Caffeine<Object, Object> caffeine) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(Constants.ALL_CACHE_NAMES);
        cacheManager.setCaffeine(caffeine);
        return cacheManager;
    }
}
