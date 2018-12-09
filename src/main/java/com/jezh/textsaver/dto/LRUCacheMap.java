package com.jezh.textsaver.dto;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCacheMap<K, V> extends LinkedHashMap<K, V> {

    private int capacity;

    public LRUCacheMap(int capacity) {
        super(capacity, 1.01f, true);
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return (size() > capacity);
    }
}
