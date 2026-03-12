package repo;

import exception.MyException;
import model.state.ProgramState;

import java.io.IOException;
import java.util.List;

public interface Repository {
    void addProgramState(ProgramState programState);
    ProgramState getCurrentState();
    public void logPrgStateExec(ProgramState state) throws MyException, IOException;
    public void setProgramStates(List<ProgramState> states);
    public List<ProgramState> getProgramStates();
}
