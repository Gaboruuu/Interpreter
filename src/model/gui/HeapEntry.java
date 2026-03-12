package model.gui;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import model.value.Value;

public class HeapEntry {
    private final SimpleObjectProperty<Integer> address;
    private final SimpleStringProperty value;

    public HeapEntry(Integer address, Value value) {
        this.address = new SimpleObjectProperty<>(address);
        this.value = new SimpleStringProperty(value.toString());
    }

    public Integer getAddress() { return address.get(); }
    public String getValue() { return value.get(); }
}