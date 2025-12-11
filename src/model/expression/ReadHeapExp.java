package model.expression;

import exception.MyException;
import model.state.HeapTable;
import model.state.MyIDictionary;
import model.state.SymbolTable;
import model.type.RefType;
import model.type.Type;
import model.value.RefValue;
import model.value.Value;

public record ReadHeapExp(Expression expression) implements Expression {

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

    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typ = expression.typecheck(typeEnv);
        if (typ instanceof RefType reft) {
            return reft.getInner();
        } else {
            throw new MyException("Argumentul RH nu este de tip RefType");
        }
    }
}
