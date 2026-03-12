package model.state;

import exception.MyException;
import model.value.Value;

import java.util.HashMap;
import java.util.Map;

public class HeapTable implements MyIHeap {
    private Map<Integer, Value> heap = new HashMap<>();
    private int nextFreeAddress = 1;

    private int getFreeLocation() {
        return nextFreeAddress++;
    }

    @Override
    public synchronized int allocate(Value value) {
        int address = getFreeLocation();
        heap.put(address, value);
        return address;
    }

    @Override
    public synchronized Value get(int address) throws MyException {
        if (!heap.containsKey(address))
            throw new MyException("Heap: address " + address + " not found!");
        return heap.get(address);
    }

    @Override
    public synchronized void put(int address, Value value) throws MyException {
        if (!heap.containsKey(address))
            throw new MyException("Heap: address " + address + " not found!");
        heap.put(address, value);
    }

    @Override
    public synchronized void update(int address, Value value) {
        // update does NOT throw error in many student implementations,
        // but we still ensure correctness:
        heap.put(address, value);
    }

    @Override
    public synchronized boolean isDefined(int address) {
        return heap.containsKey(address);
    }

    @Override
    public synchronized boolean contains(int address) {
        return heap.containsKey(address);
    }

    @Override
    public synchronized Map<Integer, Value> heapTable() {
        return heap;
    }

    @Override
    public synchronized void setContent(Map<Integer, Value> newContent) {
        // IMPORTANT: don't replace the map reference.
        // ProgramStates (and GUI) may share the same HeapTable instance; swapping the map
        // can lead to some parts reading from an old snapshot.
        heap.clear();
        heap.putAll(newContent);
    }

    @Override
    public String toString() {
        return heap.toString();
    }
}
