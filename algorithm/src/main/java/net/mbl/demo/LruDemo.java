package net.mbl.demo;

import org.apache.hadoop.yarn.util.LRUCacheHashMap;

import java.util.Collections;
import java.util.Map;

public class LruDemo {
    private static final int LINKED_HASH_MAP_INIT_CAPACITY = 10;
    private static final boolean LINKED_HASH_MAP_ACCESS_ORDERED = true;
    private static final boolean UNUSED_MAP_VALUE = true;

    /**
     * Access-ordered {@link java.util.LinkedHashMap} from blockId to {@link #UNUSED_MAP_VALUE}(just a
     * placeholder to occupy the value), acts as a LRU double linked list where most recently accessed
     * element is put at the tail while least recently accessed element is put at the head.
     */
    protected Map<Integer, Boolean> mLRUCache =
            Collections.synchronizedMap(new LRUCacheHashMap<Integer, Boolean>(LINKED_HASH_MAP_INIT_CAPACITY,
                    LINKED_HASH_MAP_ACCESS_ORDERED));
    public static void main(String[] args) throws Exception {
        LruDemo lruDemo = new LruDemo();
        for (int i = 0; i < 100; i++) {
            lruDemo.mLRUCache.put(i, true);
        }
        lruDemo.mLRUCache.get(93);
        System.out.println(lruDemo.mLRUCache);
        System.out.println(lruDemo.mLRUCache.size());
    }
}
