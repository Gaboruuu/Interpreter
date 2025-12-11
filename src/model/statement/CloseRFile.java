package model.statement;

import exception.MyException;
import model.expression.Expression;
import model.state.MyIDictionary;
import model.state.ProgramState;
import model.type.StringType;
import model.type.Type;
import model.value.StringValue;
import model.value.Value;

import java.io.BufferedReader;
import java.io.IOException;

public record CloseRFile(Expression expression) implements Statement {

    @Override
    public String toString() {
        return "CloseRFile(" + expression + ")";
    }


    @Override
    public ProgramState execute(ProgramState programState) throws MyException {
        var symbolTable = programState.symbolTable();
        var heap = programState.heapTable();
        Value value = expression.evaluate(symbolTable, heap);

        if (!(value.getType() instanceof StringType)) {
            throw new MyException("closeRFile: expression is not of type string");
        }


        StringValue fileName = (StringValue) value;
        var fileTable = programState.fileTable();

        BufferedReader br = fileTable.get(fileName);
        if (br == null) {
            throw new MyException("closeRFile: file " + fileName + " is not open");
        }

        try {
            br.close();
        } catch (IOException e) {
            throw new MyException("closeRFile: IO error " + e.getMessage());
        }

        fileTable.remove(fileName);
        return programState;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typeExp = expression.typecheck(typeEnv);
        if (typeExp.equals(new StringType())) {
            return typeEnv;
        } else {
            throw new MyException("CloseRFile: Expresia trebuie sa fie StringType!");
        }
    }
}
