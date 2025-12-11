package model.statement;

import exception.MyException;
import model.expression.Expression;
import model.state.MyIDictionary;
import model.state.ProgramState;
import model.type.BoolType;
import model.type.Type;
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

    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typexp = condition.typecheck(typeEnv);
        if (typexp.equals(new BoolType())) {
            body.typecheck(typeEnv.deepCopy());
            return typeEnv;
        } else {
            throw new MyException("Conditia din WHILE nu este de tip bool");
        }
    }
}
