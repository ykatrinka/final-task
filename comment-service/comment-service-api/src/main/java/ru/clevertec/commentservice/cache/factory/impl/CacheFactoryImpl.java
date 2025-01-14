package ru.clevertec.commentservice.cache.factory.impl;


import ru.clevertec.commentservice.cache.CustomCache;
import ru.clevertec.commentservice.cache.factory.CacheFactory;
import ru.clevertec.commentservice.cache.CacheType;
import ru.clevertec.commentservice.cache.impl.LFUCache;
import ru.clevertec.commentservice.cache.impl.LRUCache;

public class CacheFactoryImpl<K, V> implements CacheFactory<K, V> {
    @Override
    public CustomCache<K, V> getInstance(CacheType type) {
        return switch (type) {
            case LFU -> new LFUCache<>();
            case LRU -> new LRUCache<>();
        };
    }
}
