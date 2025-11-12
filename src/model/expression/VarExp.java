package model.expression;

import exception.MyException;
import model.state.SymbolTable;
import model.value.Value;

public record VarExp(String varName) implements Expression {
    @Override
    public Value evaluate(SymbolTable symbolTable) throws MyException {
        // Delegate the existence check and error reporting to the symbol table implementation
        return symbolTable.getVariableValue(varName);
    }
}
