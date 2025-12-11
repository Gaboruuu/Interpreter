package model.statement;

import exception.MyException;
import model.state.MyIDictionary;
import model.state.ProgramState;
import model.type.Type;

public record NoOpStmt() implements Statement {
    @Override
    public ProgramState execute(ProgramState state) {
        return state;
    }

    @Override
    public String toString() {
        return "no-op";
    }

    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        return typeEnv;
    }
}

