package model.state;

import model.state.MyIDictionary;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MyDictionary<K, V> implements MyIDictionary<K, V> {
    private HashMap<K, V> map;

    public MyDictionary() {
        this.map = new HashMap<>();
    }

    @Override
    public boolean isDefined(K key) {
        return map.containsKey(key);
    }

    @Override
    public void put(K key, V value) {
        map.put(key, value);
    }

    @Override
    public V lookup(K key) {
        return map.get(key);
    }

    @Override
    public void update(K key, V value) {
        map.put(key, value);
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        return map.values();
    }

    @Override
    public Map<K, V> getContent() {
        return map;
    }

    // Aceasta metoda este esentiala pentru TypeChecking la IfStmt si WhileStmt
    // pentru a crea un mediu nou, izolat de cel anterior
    @Override
    public MyIDictionary<K, V> deepCopy() {
        MyDictionary<K, V> newDict = new MyDictionary<>();
        for (K key : map.keySet()) {
            newDict.put(key, map.get(key));
        }
        return newDict;
    }

    @Override
    public String toString() {
        return map.toString();
    }
}