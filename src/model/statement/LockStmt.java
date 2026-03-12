package model.statement;

import exception.MyException;
import model.state.MyIDictionary;
import model.state.MyILockTable;
import model.state.ProgramState;
import model.type.IntType;
import model.type.Type;
import model.value.IntValue;

public record LockStmt(String var) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        MyILockTable lockTable = state.myILockTable();
        var symTable = state.symbolTable();

        if (!symTable.isDefined(var))
            throw new MyException("Variable not defined!");

        // 1. Get the Lock Address from SymTable
        int foundIndex = ((IntValue) symTable.getVariableValue(var)).value();

        if (!lockTable.containsKey(foundIndex))
            throw new MyException("Lock Table index not found!");


        if (lockTable.get(foundIndex) == -1) {
            lockTable.update(foundIndex, state.id()); // take it if free
        } else {
            state.executionStack().push(this); // push back, try later
        }

        return state;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        if (typeEnv.lookup(var).equals(new IntType()))
            return typeEnv;
        else
            throw new MyException("Variable " + var + " must be of type int!");
    }


    public Statement deepCopy() {
        return new LockStmt(var);
    }

    @Override
    public String toString() {
        return "lock(" + var + ")";
    }
}