package model.state;

import model.value.Value;

import java.util.ArrayList;
import java.util.List;

public class ArrayListOut implements Out {
    private final List<Value> values = new ArrayList<Value>();

    @Override
    public void add(Value value) {
        values.add(value);
    }

    @Override
    public Iterable<Value> values() {
        return values;
    }

    @Override
    public String toString() {
        return values.toString();
    }
}
