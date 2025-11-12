package model.type;

import model.value.BoolValue;
import model.value.IntValue;
import model.value.Value;

public enum Type {
    INTEGER,
    BOOLEAN,
    ;

    @Override
    public String toString() {
        return switch (this) {
            case INTEGER -> "int";
            case BOOLEAN -> "bool";
        };
    }

    public Value getDefaultValue() {
        return switch (this) {
            case INTEGER -> new IntValue(0);
            case BOOLEAN -> new BoolValue(false);
        };

    }
}
