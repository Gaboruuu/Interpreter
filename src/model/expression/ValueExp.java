package model.expression;

import exception.MyException;
import model.state.SymbolTable;
import model.value.Value;

public record ValueExp(Value value) implements Expression {
    @Override
    public Value evaluate(SymbolTable symbolTable) throws MyException {
        return value;
    }
}
