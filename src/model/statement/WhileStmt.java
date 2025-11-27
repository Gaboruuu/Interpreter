package model.statement;

import exception.MyException;
import model.expression.Expression;
import model.state.ProgramState;
import model.value.BoolValue;
import model.value.Value;

public record WhileStmt(Expression condition, Statement body) implements Statement {
    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        var stk = state.executionStack();
        var symTable = state.symbolTable();
        var heap = state.heapTable();

        Value value = condition.evaluate(symTable, heap);

        if (!(value instanceof BoolValue(boolean value1))) {
            throw new MyException("While: condition is not a boolean. Got: " + value);
        }

        if (value1) {
            stk.push(this);
            stk.push(body);
        }

        return state;
    }

    @Override
    public String toString() {
        return "while(" + condition + ") " + body;
    }
}
