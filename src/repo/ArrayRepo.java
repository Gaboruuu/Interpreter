package repo;

import exception.MyException;
import model.state.ProgramState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ArrayRepo implements Repository {
    private final List<ProgramState> programStates = new ArrayList<>();
    private final String logFilePath;

    public ArrayRepo(String logFilePath) {
        this.logFilePath = logFilePath;
    }

    @Override
    public void addProgramState(ProgramState programState) {
        programStates.add(programState);
    }

    @Override
    public ProgramState getCurrentState() {
        if (programStates.isEmpty()) {
            throw new IllegalStateException("Repository contains no program states");
        }
        return programStates.get(programStates.size() - 1);
    }

    @Override
    public void logPrgStateExec() throws MyException {
        var state = getCurrentState();
        try (PrintWriter log = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)))) {
            log.println(state.toLogString());
        } catch (IOException e) {
            throw new MyException("Error writing log file: " + e.getMessage());
        }
    }

}
