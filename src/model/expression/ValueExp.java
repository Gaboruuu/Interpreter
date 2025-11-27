package model.expression;

import exception.MyException;
import model.state.HeapTable;
import model.state.SymbolTable;
import model.value.Value;

public record ValueExp(Value value) implements Expression {
    @Override
    public Value evaluate(SymbolTable symbolTable, HeapTable heapTable) throws MyException {
        return value;
    }
}
