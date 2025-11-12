package model.expression;

import exception.MyException;
import model.state.SymbolTable;
import model.value.Value;

public interface Expression {
    Value evaluate(SymbolTable symbolTable) throws MyException;
}
