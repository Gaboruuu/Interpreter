package model.state;

import exception.MyException;
import model.value.Value;

import java.util.Map;

public interface MyIHeap {
    int allocate(Value value);
    Value get(int address) throws MyException;
    void put(int address, Value value) throws MyException;
    void update(int address, Value value);
    boolean isDefined(int address);
    boolean contains(int address);
    Map<Integer, Value> heapTable();
    void setContent(Map<Integer, Value> newContent);
}
