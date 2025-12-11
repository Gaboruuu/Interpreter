package model.statement;

import exception.MyException;
import model.state.ExecutionStack;
import model.state.MyIDictionary;
import model.state.ProgramState;
import model.type.Type;

public record CompStmt(Statement first, Statement second) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) {
        ExecutionStack executionStack = state.executionStack();
        executionStack.push(this.second);
        executionStack.push(this.first);
        return state;
    }

    @Override
    public String toString() {
        return "(" + first.toString() + "; " + second.toString() + ")";
    }

    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        return second.typecheck(first.typecheck(typeEnv));
    }
}
