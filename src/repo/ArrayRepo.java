package repo;

import model.state.ProgramState;

import java.util.ArrayList;
import java.util.List;

public class ArrayRepo implements Repository {
    private final List<ProgramState> programStates = new ArrayList<>();

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

}
