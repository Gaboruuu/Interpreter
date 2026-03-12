package model.state;

import model.value.Value;

public interface Out {
    void add(Value value);
    Iterable<Value> values();

    default java.util.List<Value> getAll() {
        java.util.ArrayList<Value> out = new java.util.ArrayList<>();
        for (Value v : values()) {
            out.add(v);
        }
        return out;
    }
}
