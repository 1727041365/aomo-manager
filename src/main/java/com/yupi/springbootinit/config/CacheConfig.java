package com.yupi.springbootinit.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@Slf4j
public class CacheConfig {

    private Cache<String, String> visitorCacheInstance;

    @Bean
    public Cache<String, String> visitorCache() {
        visitorCacheInstance = Caffeine.newBuilder()
                .maximumSize(2000)
                .build();
        return visitorCacheInstance;
    }

    /**
     * 获取缓存实例，供定时任务使用
     */
    public Cache<String, String> getVisitorCache() {
        return visitorCacheInstance;
    }

    /**
     * 每天凌晨清空访客缓存
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void clearVisitorCache() {
        if (visitorCacheInstance != null) {
            visitorCacheInstance.invalidateAll();
            log.info("访客缓存已清空");
        }
    }
}
