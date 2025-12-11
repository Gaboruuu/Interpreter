package model.expression;

import exception.MyException;
import model.state.HeapTable;
import model.state.MyIDictionary;
import model.state.MyIHeap;
import model.state.SymbolTable;
import model.type.Type;
import model.value.Value;

public interface Expression {
    Value evaluate(SymbolTable symbolTable, HeapTable heapTable) throws MyException;
    Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException;
}
