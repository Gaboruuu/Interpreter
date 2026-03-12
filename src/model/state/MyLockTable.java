package model.state;
import java.util.HashMap;

public class MyLockTable implements MyILockTable {
    private HashMap<Integer, Integer> lockTable;
    private int freeLocation = 0;

    public MyLockTable() {
        this.lockTable = new HashMap<>();
    }

    @Override
    public synchronized int getFreeValue() {
        freeLocation++;
        return freeLocation;
    }

    @Override
    public synchronized void put(int key, int value) {
        lockTable.put(key, value);
    }

    @Override
    public synchronized void update(int key, int value) {
        lockTable.put(key, value);
    }

    @Override
    public synchronized boolean containsKey(int key) {
        return lockTable.containsKey(key);
    }

    @Override
    public synchronized int get(int key) {
        return lockTable.get(key);
    }

    @Override
    public synchronized HashMap<Integer, Integer> getContent() {
        return lockTable;
    }

    @Override
    public synchronized void setContent(HashMap<Integer, Integer> content) {
        this.lockTable = content;
    }

    @Override
    public String toString() {
        return lockTable.toString();
    }
}