package model.statement;

import exception.MyException;
import model.state.MyIDictionary;
import model.state.ProgramState;
import model.type.Type;

public interface Statement {
    ProgramState execute(ProgramState state) throws MyException;
    MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException;
}
