package model.state;

import model.type.Type;
import model.value.Value;
import exception.UndefinedVariableException;

import java.util.Map;
import java.util.Set;

public interface SymbolTable {
    void declareVariable(Type type, String variableName);
    Value getVariableValue(String variableName) throws UndefinedVariableException;
    Type getVariableType(String variableName) throws UndefinedVariableException;
    void setValue(String variableName, Value value) throws UndefinedVariableException;
    boolean isDefined(String variableName);
    Set<Map.Entry<String, Value>> entries();
}
