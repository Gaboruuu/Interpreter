package model.statement;

import exception.MyException;
import model.expression.Expression;
import model.state.MyIDictionary;
import model.state.ProgramState;
import model.type.RefType;
import model.type.Type;
import model.value.RefValue;
import model.value.Value;

public record NewStmt(String varName, Expression expression) implements Statement {
    @Override
    public String toString() {
        return "new(" + varName + ", " + expression + ")";
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        var symbolTable = state.symbolTable();
        var heapTable = state.heapTable();

        if (!symbolTable.isDefined(varName)) {
            throw new RuntimeException("Variable " + varName + " is not defined.");
        }

        Type varType;

        try {
            varType = symbolTable.getVariableType(varName);
        } catch (Exception e) {
            throw new RuntimeException("Error looking up variable " + varName + ": " + e.getMessage());
        }

        if (!(varType instanceof RefType refType)) {
            throw new MyException("ReadFile: Variable " + varName + " is not a reference.");
        }

        Value currValue = symbolTable.getVariableValue(varName);
        if (!(currValue instanceof RefValue refValue)) {
            throw new MyException("ReadFile: Variable " + varName + " is not a reference.");
        }

        Value evaluated;

        try {
            evaluated = expression.evaluate(symbolTable, heapTable);
        } catch (Exception e) {
            throw new MyException("Error evaluating expression: " + e.getMessage());
        }

        if (!evaluated.getType().equals(refValue.getLocationType())) {
            throw new MyException(
                    "New: Type mismatch. Variable " + varName +
                            " points to locations of type " + refValue.getLocationType() +
                            " but expression has type " + evaluated.getType() + "."
            );
        }

        int newAddress = heapTable.allocate(evaluated);
        symbolTable.setValue(varName, new RefValue(newAddress, refValue.getLocationType()));

        return state;
    }

    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typevar = typeEnv.lookup(varName);
        Type typexp = expression.typecheck(typeEnv);
        if (typevar.equals(new RefType(typexp)))
            return typeEnv;
        else
            throw new MyException("NEW stmt: partea dreapta si stanga au tipuri diferite");
    }

}
