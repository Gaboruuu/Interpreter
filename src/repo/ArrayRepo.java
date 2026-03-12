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
    private List<ProgramState> programStates = new ArrayList<>();
    private final String logFilePath;


    public ArrayRepo(String logFilePath) {
        this.logFilePath = logFilePath;
    }

    @Override
    public synchronized void addProgramState(ProgramState programState) {
        programStates.add(programState);
    }

    @Override
    public synchronized ProgramState getCurrentState() {
        if (programStates.isEmpty()) {
            throw new IllegalStateException("Repository contains no program states");
        }
        return programStates.get(programStates.size() - 1);
    }

    @Override
    public synchronized void logPrgStateExec(ProgramState state) throws MyException, IOException {
        try (PrintWriter log = new PrintWriter((new BufferedWriter(new FileWriter(logFilePath, true))))) {
            log.println(state.toLogString());
            log.println("--------------------------------------------------");
        } catch (IOException e) {
            throw new MyException("Error writing log file: " + e.getMessage());
        }
    }


    @Override
    public synchronized List<ProgramState> getProgramStates() {
        return this.programStates;
    }

    @Override
    public synchronized void setProgramStates(List<ProgramState> states) {
        this.programStates = states;
    }
}
