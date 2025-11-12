package model.state;

import model.type.Type;
import model.value.Value;
import exception.UndefinedVariableException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MapSymTable implements SymbolTable {
    private final Map<String, Value> symbolTable = new HashMap<>();

    @Override
    public void declareVariable(Type type, String varName) {
        symbolTable.put(varName, type.getDefaultValue());
    }

    @Override
    public Value getVariableValue(String variableName) throws UndefinedVariableException {
        if (!symbolTable.containsKey(variableName)) {
            throw new UndefinedVariableException("Variable " + variableName + " not defined.");
        }
        return symbolTable.get(variableName);
    }

    @Override
    public Type getVariableType(String variableName) throws UndefinedVariableException {
        if (!symbolTable.containsKey(variableName)) {
            throw new UndefinedVariableException("Variable " + variableName + " not defined.");
        }
        return symbolTable.get(variableName).getType();
    }

    @Override
    public void setValue(String variableName, Value value) throws UndefinedVariableException {
        if (!symbolTable.containsKey(variableName)) {
            throw new UndefinedVariableException("Variable " + variableName + " not defined.");
        }
        symbolTable.put(variableName, value);
    }

    @Override
    public boolean isDefined(String variableName) {
        return symbolTable.containsKey(variableName);
    }

    @Override
    public Set<Map.Entry<String, Value>> entries() {
        return symbolTable.entrySet();
    }

    @Override
    public String toString() {
        return "SymbolTable" + symbolTable;
    }

}
