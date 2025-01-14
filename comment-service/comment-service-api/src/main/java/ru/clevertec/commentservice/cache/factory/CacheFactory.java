package ru.clevertec.commentservice.cache.factory;


import ru.clevertec.commentservice.cache.CacheType;
import ru.clevertec.commentservice.cache.CustomCache;

public interface CacheFactory<K, V> {

    CustomCache<K, V> getInstance(CacheType type);
}
