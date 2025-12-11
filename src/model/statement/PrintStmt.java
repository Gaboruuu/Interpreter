package model.statement;

import exception.MyException;
import model.expression.Expression;
import model.state.MyIDictionary;
import model.state.ProgramState;
import model.type.Type;

public record PrintStmt(Expression expression) implements Statement {
    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        var heap = state.heapTable();
        state.out().add(expression.evaluate(state.symbolTable(), heap));
        return state;
    }

    @Override
    public String toString() {
        return "print(" + expression.toString() + ")";
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        expression.typecheck(typeEnv);
        return typeEnv;
    }
}
