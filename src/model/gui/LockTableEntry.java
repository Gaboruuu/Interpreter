package model.gui;

import javafx.beans.property.SimpleObjectProperty;

/**
 * GUI model for one row in the Lock Table.
 * Key = lock address, Value = owner thread id (-1 means FREE).
 */
public class LockTableEntry {
    private final SimpleObjectProperty<Integer> address;
    private final SimpleObjectProperty<Integer> owner;

    public LockTableEntry(Integer address, Integer owner) {
        this.address = new SimpleObjectProperty<>(address);
        this.owner = new SimpleObjectProperty<>(owner);
    }

    public Integer getAddress() {
        return address.get();
    }

    public SimpleObjectProperty<Integer> addressProperty() {
        return address;
    }

    public Integer getOwner() {
        return owner.get();
    }

    public SimpleObjectProperty<Integer> ownerProperty() {
        return owner;
    }
}
