package model.statement;


import exception.MyException;
import model.state.MyIDictionary;
import model.state.MyILockTable;
import model.state.ProgramState;
import model.type.IntType;
import model.type.Type;
import model.value.IntValue;

public record NewLockStmt(String var) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        MyILockTable lockTable = state.myILockTable();
        var symTable = state.symbolTable();

        int freeAddr = lockTable.getFreeValue();

        lockTable.put(freeAddr, -1); // -1 free

        if (!symTable.isDefined(var))
            symTable.declareVariable(new IntType(), var);
        symTable.setValue(var, new IntValue(freeAddr));
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
        return new NewLockStmt(var);
    }

    @Override
    public String toString() {
        return "newLock(" + var + ")";
    }
}