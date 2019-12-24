package net.mbl.demo;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.util.ArrayList;
import java.util.List;

/**
 * Bloom filter
 */
public class App {
    private static final int SIZE = 1_000_000;
    private static final int SIZE2 = 10_000;

    private static BloomFilter<Integer> bloomFilter = BloomFilter.create(Funnels.integerFunnel(), SIZE);

    public static void main(String[] args) {
        for (int i = 0; i < SIZE; i++) {
            bloomFilter.put(i);
        }

        for (int i = 0; i < SIZE; i++) {
            if (!bloomFilter.mightContain(i)) {
                System.out.println("有坏人逃脱了");
            }
        }

        List<Integer> list = new ArrayList<>(1000);
        for (int i = SIZE; i < SIZE + SIZE2; i++) {
            if (bloomFilter.mightContain(i)) {
                list.add(i);
            }
        }
        System.out.println("有误伤的数量：" + list.size());
    }
}