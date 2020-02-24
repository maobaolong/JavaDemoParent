package net.mbl.demo.common;

import java.util.LinkedHashMap;
import java.util.Map;

public class SizeLimitedLruCache<K, V> extends LinkedHashMap<K, V> {
    private static final long serialVersionUID = 1L;
    private int maxSize;

    public SizeLimitedLruCache(int maxSize, boolean accessOrder) {
        super(maxSize, 0.75F, accessOrder);
        this.maxSize = maxSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return this.size() > this.maxSize;
    }
}
