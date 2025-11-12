package model.statement;

import exception.MyException;
import model.state.ProgramState;

public interface Statement {
    ProgramState execute(ProgramState state) throws MyException;
}
