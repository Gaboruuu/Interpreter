package model.expression;

import exception.MyException;
import model.state.HeapTable;
import model.state.MyIDictionary;
import model.state.SymbolTable;
import model.type.Type;
import model.value.Value;

public record VarExp(String varName) implements Expression {
    @Override
    public Value evaluate(SymbolTable symbolTable, HeapTable heapTable) throws MyException {
        // Delegate the existence check and error reporting to the symbol table implementation
        return symbolTable.getVariableValue(varName);
    }

    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        return typeEnv.lookup(varName);
    }
}
