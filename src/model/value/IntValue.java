package model.value;

import model.type.IntType;
import model.type.Type;

public record IntValue(int value) implements Value {

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public Type getType() {
        return new IntType();
    }
}
