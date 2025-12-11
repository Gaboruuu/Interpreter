package model.state;

import exception.MyException;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface MyIDictionary<K, V> {
    boolean isDefined(K key) throws MyException;
    void put(K key, V value) throws MyException;
    V lookup(K key) throws MyException;
    void update(K key, V value) throws MyException;
    Set<K> keySet() throws MyException;
    Collection<V> values() throws MyException;
    MyIDictionary<K, V> deepCopy() throws MyException;
    Map<K, V> getContent();
}
