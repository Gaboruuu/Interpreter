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
            throw new MyException("new: variable " + varName + " is not defined.");
        }

        Type varType = symbolTable.getVariableType(varName);
        if (!(varType instanceof RefType refType)) {
            throw new MyException("new: variable " + varName + " is not of RefType.");
        }

        Value evaluated = expression.evaluate(symbolTable, heapTable);
        if (!evaluated.getType().equals(refType.getInner())) {
            throw new MyException(
                    "new: type mismatch. Variable " + varName + " has type " + varType +
                            " but expression has type " + evaluated.getType()
            );
        }

        int newAddress = heapTable.allocate(evaluated);
        symbolTable.setValue(varName, new RefValue(newAddress, refType.getInner()));

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
