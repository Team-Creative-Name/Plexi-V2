package com.github.tcn.plexi.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

//This class is from Almighty-Alpaca/JDA-Butler
public class FixedSizeCache<K, V> {
    private final Map<K, V> map = new HashMap<>();
    private final K[] keys;
    private int currIndex = 0;

    @SuppressWarnings("unchecked")
    public FixedSizeCache(int size) {
        if(size < 1)
            throw new IllegalArgumentException("Cache size must be at least 1!");
        this.keys = (K[]) new Object[size];
    }

    public V find(Predicate<K> test) {
        return map.entrySet()
                .stream()
                .filter(it -> test.test(it.getKey()))
                .map(Map.Entry::getValue)
                .findFirst().orElse(null);
    }

    //This method has been modified to allow overwriting a key for multilayered menus
    public void add(K key, V value) {
        if(keys[currIndex] != null) {
            map.remove(keys[currIndex]);
        }
        map.put(key, value);
        keys[currIndex] = key;
        currIndex = (currIndex + 1) % keys.length;
    }

    //submenus will require us to remove the reference to the old buttons in order to handle new ones
    public void remove(K key){
        if(!contains(key)){
            return;
        }
    }

    public boolean contains(K key) {
        return map.containsKey(key);
    }

    public V get(K key) {
        return map.get(key);
    }
}
