package com.anderson.filebrowserbackend.utils.classes.interfaces;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface HashTable<K, V> {
    int size();
    boolean isEmpty();
    boolean containsKey(K key);
    boolean containsValue(V value);
    V get(K key);
    V put(K key, V value);
    V remove(K key);
    boolean remove(K key, V value);
    void putAll(Map<? extends K, ? extends V> map);
    void clear();
    boolean equals(Object o);
    int hashCode();
    Set<Map.Entry<K, V>> entrySet();
    Set<K> keySet();
    Collection<V> values();
    V getOrDefault(K key, V defaultValue);
}
