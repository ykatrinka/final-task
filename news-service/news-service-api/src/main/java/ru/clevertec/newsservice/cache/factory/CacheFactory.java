package ru.clevertec.newsservice.cache.factory;


import ru.clevertec.newsservice.cache.CacheType;
import ru.clevertec.newsservice.cache.CustomCache;

public interface CacheFactory<K, V> {

    CustomCache<K, V> getInstance(CacheType type);
}
