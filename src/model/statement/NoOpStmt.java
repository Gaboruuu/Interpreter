package model.statement;

import model.state.ProgramState;

public record NoOpStmt() implements Statement {
    @Override
    public ProgramState execute(ProgramState state) {
        return state;
    }

    @Override
    public String toString() {
        return "no-op";
    }
}
