package ru.clevertec.commentservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.clevertec.commentservice.adapter.output.persistence.jpa.entity.CommentEntity;
import ru.clevertec.commentservice.cache.CustomCache;
import ru.clevertec.commentservice.cache.CacheType;
import ru.clevertec.commentservice.cache.factory.impl.CacheFactoryImpl;

import java.util.UUID;


@Profile("dev")
@Configuration
public class CacheConfig {

    @Value("${local-cache.type}")
    private CacheType cacheType;

    @Bean
    public CustomCache<UUID, CommentEntity> cacheBean() {
        return new CacheFactoryImpl<UUID, CommentEntity>().getInstance(cacheType);
    }
}
