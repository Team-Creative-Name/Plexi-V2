/*
 *  Copyright (C) 2021 Team Creative Name, https://github.com/Team-Creative-Name
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published
 *  by the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
