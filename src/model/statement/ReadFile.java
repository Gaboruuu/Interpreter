package model.statement;

import exception.MyException;
import model.expression.Expression;
import model.state.MyIDictionary;
import model.state.ProgramState;
import model.type.IntType;
import model.type.StringType;
import model.type.Type;
import model.value.StringValue;
import model.value.Value;

import java.io.BufferedReader;

public record ReadFile(Expression expression, String variableName) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        var symbolTable = state.symbolTable();
        var heap = state.heapTable();

        if (!symbolTable.isDefined(variableName))
        {
            throw new MyException("ReadFile: Variable " + variableName + " is not defined.");
        }

        Type varType;
        try {
            varType = symbolTable.getVariableType(variableName);
        } catch (MyException e) {
            throw new MyException("ReadFile: " + e.getMessage());
        }

        if (!(varType instanceof  IntType)) {
            throw new MyException("ReadFile: Variable " + variableName + " is not of type integer.");
        }

        Value value = expression.evaluate(symbolTable, heap);
        if (!(value.getType() instanceof StringType)) {
            throw new MyException("ReadFile: Expression does not evaluate to a string.");
        }

        StringValue stringValue = (StringValue) value;
        var fileTable = state.fileTable();

        BufferedReader br = fileTable.get(stringValue);
        if (br == null) {
            throw new MyException("ReadFile: File " + stringValue.value() + " is not opened.");
        }

        try {
            String line = br.readLine();
            int intValue;
            if (line == null) {
                intValue = 0;
            } else {
                intValue = Integer.parseInt(line);
            }
            symbolTable.setValue(variableName, new model.value.IntValue(intValue));
        } catch (Exception e) {
            throw new MyException("ReadFile: Error reading from file " + stringValue.value() + ": " + e.getMessage());
        }

        return state;
    }

    @Override
    public String toString() {
        return "ReadFile(" + expression.toString() + ", " + variableName + ")";
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typeExp = expression.typecheck(typeEnv);
        Type typeVar = typeEnv.lookup(variableName);

        if (!typeExp.equals(new StringType())) {
            throw new MyException("ReadFile: Numele fisierului trebuie sa fie StringType!");
        }

        if (!typeVar.equals(new IntType())) {
            throw new MyException("ReadFile: Variabila in care citim trebuie sa fie IntType!");
        }

        return typeEnv;
    }
}
