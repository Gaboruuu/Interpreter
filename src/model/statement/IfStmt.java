package model.statement;

import exception.MyException;
import model.expression.Expression;
import model.state.ProgramState;
import model.type.Type;
import model.value.BoolValue;
import model.value.Value;

public record IfStmt(Expression condition, Statement thenStatement, Statement elseStatement) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        Value value = condition.evaluate(state.symbolTable());
        if (value.getType() != Type.BOOLEAN) {
            throw new MyException("Condition expression is not of boolean type.");
        }

        BoolValue boolValue = (BoolValue) value;
        Statement chosenStatement = boolValue.value() ? thenStatement : elseStatement;
        state.executionStack().push(chosenStatement);
        return state;
    }

    @Override
    public String toString() {
        return "if(" + condition.toString() + ") then {" + thenStatement.toString() + "} else {" + elseStatement.toString() + "}";
    }
}
