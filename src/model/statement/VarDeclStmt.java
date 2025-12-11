package model.statement;

import exception.MyException;
import model.state.MyIDictionary;
import model.state.ProgramState;
import model.type.Type;

public record VarDeclStmt(String variableName, Type type) implements Statement {
    @Override
    public ProgramState execute(ProgramState state) {
        var symbolTable = state.symbolTable();
        symbolTable.declareVariable(type, variableName);
        return state;
    }

    @Override
    public String toString() {
        return type.toString() + " " + variableName;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        typeEnv.put(variableName, type);
        return typeEnv;
    }
}
