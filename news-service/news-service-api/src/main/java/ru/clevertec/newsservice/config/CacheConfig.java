package ru.clevertec.newsservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.clevertec.newsservice.adapter.output.persistence.jpa.entity.NewsEntity;
import ru.clevertec.newsservice.cache.CacheType;
import ru.clevertec.newsservice.cache.CustomCache;
import ru.clevertec.newsservice.cache.factory.impl.CacheFactoryImpl;

import java.util.UUID;


@Profile("dev")
@Configuration
public class CacheConfig {

    @Value("${local-cache.type}")
    private CacheType cacheType;

    @Bean
    public CustomCache<UUID, NewsEntity> cacheBean() {
        return new CacheFactoryImpl<UUID, NewsEntity>().getInstance(cacheType);
    }
}
