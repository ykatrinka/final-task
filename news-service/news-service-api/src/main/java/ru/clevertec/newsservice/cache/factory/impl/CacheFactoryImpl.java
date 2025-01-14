package ru.clevertec.newsservice.cache.factory.impl;


import ru.clevertec.newsservice.cache.CacheType;
import ru.clevertec.newsservice.cache.CustomCache;
import ru.clevertec.newsservice.cache.factory.CacheFactory;
import ru.clevertec.newsservice.cache.impl.LFUCache;
import ru.clevertec.newsservice.cache.impl.LRUCache;

public class CacheFactoryImpl<K, V> implements CacheFactory<K, V> {
    @Override
    public CustomCache<K, V> getInstance(CacheType type) {
        return switch (type) {
            case LFU -> new LFUCache<>();
            case LRU -> new LRUCache<>();
        };
    }
}
