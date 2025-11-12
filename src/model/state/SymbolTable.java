package model.state;

import model.type.Type;
import model.value.Value;
import exception.UndefinedVariableException;

public interface SymbolTable {
    void declareVariable(Type type, String variableName);
    Value getVariableValue(String variableName) throws UndefinedVariableException;
    Type getVariableType(String variableName) throws UndefinedVariableException;
    void setValue(String variableName, Value value) throws UndefinedVariableException;
    boolean isDefined(String variableName);
}
