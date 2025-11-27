package model.value;

import model.type.StringType;
import model.type.Type;

public record StringValue(String value) implements Value {

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public Type getType() {
        return new StringType();
    }
}
