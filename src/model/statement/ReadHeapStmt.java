package model.statement;

import exception.MyException;
import model.expression.Expression;
import model.state.HeapTable;
import model.state.SymbolTable;
import model.value.RefValue;
import model.value.Value;

public record ReadHeapStmt(Expression expression) implements Expression {

    @Override
    public Value evaluate(SymbolTable symbolTable, HeapTable heapTable) throws MyException {
        Value value = expression.evaluate(symbolTable, heapTable);

        if (!(value instanceof RefValue refValue)) {
            throw new MyException("readHeap: expression does not evaluate to a RefValue. Got: " + value);
        }

        int address = refValue.getAddress();
        if (!heapTable.contains(address)) {
            throw new MyException("readHeap: address " + address + " not found in heap.");
        }
        return heapTable.get(address);
    }

    @Override
    public String toString() {
        return "readHeap(" + expression + ")";
    }
}
