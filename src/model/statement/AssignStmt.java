package model.statement;

import exception.MyException;
import model.expression.Expression;
import model.state.MyIDictionary;
import model.state.ProgramState;
import model.type.Type;

public record AssignStmt(String variableName, Expression expression) implements Statement {
    @Override
    public String toString() {
        return variableName + " = " + expression.toString();
    }

    @Override
    public ProgramState execute(ProgramState state) throws  MyException {
        var heapTable = state.heapTable();
        var value = expression.evaluate(state.symbolTable(), heapTable);
        var expressionType = value.getType();
        var variableType = state.symbolTable().getVariableType(variableName);
        if (!expressionType.equals(variableType)) {
            throw new MyException("Type mismatch: cannot assign " + expressionType + " to variable of type " + variableType);
        }
        state.symbolTable().setValue(variableName, value);
        return state;
    }

    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typevar = typeEnv.lookup(variableName);
        Type typexp = expression.typecheck(typeEnv);

        if (typevar.equals(typexp))
            return typeEnv;
        else
            throw new MyException("Assignment: partea dreapta si partea stanga au tipuri diferite");
    }
}
