package repo;

import exception.MyException;
import model.state.ProgramState;

public interface Repository {
    void addProgramState(ProgramState programState);
    ProgramState getCurrentState();
    void logPrgStateExec() throws MyException;
}
