package model.expression;

import exception.MyException;
import model.state.HeapTable;
import model.state.MyIDictionary;
import model.state.SymbolTable;
import model.type.Type;
import model.value.Value;

public record ValueExp(Value value) implements Expression {
    @Override
    public Value evaluate(SymbolTable symbolTable, HeapTable heapTable) throws MyException {
        return value;
    }
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        return value.getType();
    }
}
