// model/state/FileTable.java
package model.state;

import model.value.StringValue;

import java.io.BufferedReader;
import java.util.Map;
import java.util.Set;

public interface FileTable {
    boolean isDefined(StringValue key);
    void put(StringValue key, BufferedReader reader);
    BufferedReader get(StringValue key);
    void remove(StringValue key);

    // for logging
    Set<Map.Entry<StringValue, BufferedReader>> entries();
}
