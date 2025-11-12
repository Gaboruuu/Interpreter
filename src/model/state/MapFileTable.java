package model.state;

import model.value.StringValue;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MapFileTable implements  FileTable {
    private final Map<StringValue,BufferedReader> map = new HashMap<>();

    @Override
    public boolean isDefined(StringValue key) {
        return map.containsKey(key);
    }

    @Override
    public void put(StringValue key, BufferedReader reader) {
        map.put(key, reader);
    }

    @Override
    public BufferedReader get(StringValue key) {
        return map.get(key);
    }

    @Override
    public void remove(StringValue key) {
        map.remove(key);
    }

    @Override
    public Set<Map.Entry<StringValue, BufferedReader>> entries() {
        return map.entrySet();
    }
}
