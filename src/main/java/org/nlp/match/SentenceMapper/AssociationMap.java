package org.nlp.match.SentenceMapper;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 *
 * @author Bene
 */
public class AssociationMap {

    private HashMap<Integer, HashSet<Integer>> keyMap;
    private HashMap<String, Allocation> allocationMap;

    public AssociationMap() {
        keyMap = new HashMap<>();
        allocationMap = new HashMap<>();
    }

    public Allocation get(int key1, int key2) {
        return allocationMap.get(key1 + "-" + key2);
    }

    public boolean contains(int key1, int key2) {
        return allocationMap.containsKey(key1 + "-" + key2);
    }

    public boolean put(int key1, int key2, Allocation allocation) {
        if (!contains(key1, key2)) {
            allocationMap.put(key1 + "-" + key2, allocation);
            if (keyMap.containsKey(key1)) {
                keyMap.get(key1).add(key2);
            } else {
                HashSet<Integer> set = new HashSet<>();
                set.add(key2);
                keyMap.put(key1, set);
            }
            return true;
        } else {
            return false;
        }
    }

    public HashMap<Integer, HashSet<Integer>> getKeyMap() {
        return sortMapByKey(keyMap);
    }

    private static <K extends Comparable<? super K>, V> HashMap<K, V> sortMapByKey(HashMap<K, V> map) {
        HashMap<K, V> result = new LinkedHashMap<>();
        Stream<Map.Entry<K, V>> st = map.entrySet().stream();

        st.sorted(Comparator.comparing(e -> e.getKey()))
                .forEach(e -> result.put(e.getKey(), e.getValue()));

        return result;
    }
}
