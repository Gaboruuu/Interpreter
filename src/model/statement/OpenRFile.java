package model.statement;

import exception.MyException;
import model.expression.Expression;
import model.state.FileTable;
import model.state.ProgramState;
import model.type.StringType;
import model.value.StringValue;
import model.value.Value;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public record OpenRFile(Expression expression) implements Statement{

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        var symbolTable = state.symbolTable();
        var heap = state.heapTable();
        Value value = expression.evaluate(symbolTable, heap);

        if (!(value.getType() instanceof StringType)) {
            throw new MyException("openRFile: expression is not of type string");
        }

        StringValue fileName = (StringValue) value;
        FileTable fileTable = state.fileTable();

        if (fileTable.isDefined(fileName)) {
            throw new MyException("openRFile: file already opened: " + fileName);
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName.value()));
            fileTable.put(fileName, br);
        } catch (IOException e) {
            throw new MyException("openRFile: cannot open file: " + e.getMessage());
        }

        return state;
    }

    @Override
    public String toString() {
        return "openRFile(" + expression + ")";
    }
}
