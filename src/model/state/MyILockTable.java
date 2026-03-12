package model.state;
import java.util.HashMap;

public interface MyILockTable {
    int getFreeValue();
    void put(int key, int value);
    void update(int key, int value);
    boolean containsKey(int key);
    int get(int key);
    HashMap<Integer, Integer> getContent();
    void setContent(HashMap<Integer, Integer> content);
}