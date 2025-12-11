package model.statement;

import exception.MyException;
import model.expression.Expression;
import model.state.MyIDictionary;
import model.state.ProgramState;
import model.type.RefType;
import model.type.Type;
import model.value.RefValue;
import model.value.Value;

public record WriteHeapStmt(String varName, Expression expression) implements Statement {
    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        var symTable = state.symbolTable();
        var heap = state.heapTable();

        // 1. varName must be defined
        if (!symTable.isDefined(varName)) {
            throw new MyException("writeHeap: variable " + varName + " is not defined.");
        }

        Value varValue = symTable.getVariableValue(varName); // adapt name if different
        if (!(varValue instanceof RefValue refValue)) {
            throw new MyException("writeHeap: variable " + varName + " is not a RefValue.");
        }

        int address = refValue.getAddress();
        if (!heap.contains(address)) {
            throw new MyException("writeHeap: address " + address + " not found in heap.");
        }

        Value evaluated = expression.evaluate(symTable, heap);
        var locationType = refValue.getLocationType(); // or whatever you named it
        if (!evaluated.getType().equals(locationType)) {
            throw new MyException("writeHeap: type mismatch. Expected " +
                    locationType + " but got " + evaluated.getType());
        }

        heap.update(address, evaluated);

        return state;
    }

    @Override
    public String toString() {
        return "writeHeap(" + varName + ", " + expression + ")";
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typeVar = typeEnv.lookup(varName); // Sau 'id', depinde cum ai numit campul
        Type typeExp = expression.typecheck(typeEnv);

        if (typeVar.equals(new RefType(typeExp))) {
            return typeEnv;
        } else {
            throw new MyException("WriteHeap: Partea dreapta (" + typeExp + ") nu corespunde cu tipul referit de " + varName + " (" + typeVar + ")");
        }
    }
}
