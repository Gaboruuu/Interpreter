package model.statement;

import exception.MyException;
import model.expression.Expression;
import model.state.ProgramState;

public record AssignStmt(String variableName, Expression expression) implements Statement {
    @Override
    public String toString() {
        return variableName + " = " + expression.toString();
    }

    @Override
    public ProgramState execute(ProgramState state) throws  MyException {
        var value = expression.evaluate(state.symbolTable());
        var expressionType = value.getType();
        var variableType = state.symbolTable().getVariableType(variableName);
        if (expressionType != variableType) {
            throw new MyException("Type mismatch: cannot assign " + expressionType + " to variable of type " + variableType);
        }
        state.symbolTable().setValue(variableName, value);
        return state;
    }
}
