package model.statement;

import exception.MyException;
import model.expression.Expression;
import model.state.ProgramState;
import model.type.Type;
import model.value.StringValue;
import model.value.Value;

import java.io.BufferedReader;

public record ReadFile(Expression expression, String variableName) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        var symbolTable = state.symbolTable();

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

        if (varType != Type.INTEGER) {
            throw new MyException("ReadFile: Variable " + variableName + " is not of type integer.");
        }

        Value value = expression.evaluate(symbolTable);
        if (value.getType() != Type.STRING) {
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
}
