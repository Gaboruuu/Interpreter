package model.statement;

import exception.MyException;
import model.expression.Expression;
import model.state.ProgramState;

public record PrintStmt(Expression expression) implements Statement {
    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        state.out().add(expression.evaluate(state.symbolTable()));
        return state;
    }

    @Override
    public String toString() {
        return "print(" + expression.toString() + ")";
    }
}
