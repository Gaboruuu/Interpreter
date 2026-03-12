package model.gui;

import javafx.beans.property.SimpleStringProperty;
import model.value.Value;

public class SymTableEntry {
    private final SimpleStringProperty variableName;
    private final SimpleStringProperty value;

    public SymTableEntry(String variableName, Value value) {
        this.variableName = new SimpleStringProperty(variableName);
        this.value = new SimpleStringProperty(value.toString());
    }

    public String getVariableName() { return variableName.get(); }
    public String getValue() { return value.get(); }
}