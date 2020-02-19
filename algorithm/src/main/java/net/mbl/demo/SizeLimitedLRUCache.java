package net.mbl.demo;

import java.util.LinkedHashMap;
import java.util.Map;

public class SizeLimitedLRUCache<K, V> extends LinkedHashMap<K, V> {
    private static final long serialVersionUID = 1L;
    private int maxSize;

    public SizeLimitedLRUCache(int maxSize, boolean accessOrder) {
        super(maxSize, 0.75F, accessOrder);
        this.maxSize = maxSize;
    }

    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return this.size() > this.maxSize;
    }
}
