package model.statement;
// ... imports ...

import exception.MyException;
import model.state.MyIDictionary;
import model.state.MyILockTable;
import model.state.ProgramState;
import model.type.IntType;
import model.type.Type;
import model.value.IntValue;
import model.value.Value;

public class UnlockStmt implements Statement {
    private final String var;

    public UnlockStmt(String var) {
        this.var = var;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        MyILockTable lockTable = state.myILockTable();
        var symTable = state.symbolTable();

        if (!symTable.isDefined(var))
            throw new MyException("Variable not defined!");

        int foundIndex = ((IntValue)symTable.getVariableValue(var)).value();

        if (!lockTable.containsKey(foundIndex))
            throw new MyException("Lock Table index not found!");

        if (lockTable.get(foundIndex) == state.id()) {
            lockTable.update(foundIndex, -1); // Set to FREE if owned
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


    public Statement deepCopy() { return new UnlockStmt(var); }
    @Override
    public String toString() { return "unlock(" + var + ")"; }
}