package model.value;

import model.type.RefType;
import model.type.Type;

public record RefValue(int address, Type location) implements  Value {

    public int getAddress() {
        return address;
    }

    public Type getLocationType() {
        return location;
    }

    @Override
    public Type getType() {
        return new RefType(location);
    }

    @Override
    public boolean equals(Object another) {
        if (another instanceof RefValue otherRef)
            return address == otherRef.address &&
                    location.equals(otherRef.location);
        return false;
    }

    @Override
    public String toString() {
        return "(" + address + ", " + location.toString() + ")";
    }
}
