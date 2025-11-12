package controller;

import exception.EmptyExecutionStackException;
import exception.MyException;
import model.state.ArrayListOut;
import model.state.LLExeStack;
import model.state.MapSymTable;
import model.state.ProgramState;
import model.statement.Statement;
import repo.Repository;

public record Controller(Repository repo) {
    public void addNewProgram(Statement program) {
        var executionStack = new LLExeStack();
        executionStack.push(program);

        repo.addProgramState(new model.state.ProgramState(
                executionStack,
                new MapSymTable(),
                new ArrayListOut(),
                new MapFileTable()
        ));
    }

    public ProgramState execOneStep() throws MyException {
        var state = repo.getCurrentState();
        if (state.executionStack().isEmpty()) {
            throw new EmptyExecutionStackException("Execution stack is empty");
        }

        Statement nextStatement = state.executionStack().pop();
        return nextStatement.execute(state);
    }

    public void allSteps() throws MyException {
        var state = repo.getCurrentState();

        repo.logPrgStateExec();

        while (!state.executionStack().isEmpty()) {
            state = execOneStep();
            repo.logPrgStateExec();
        }

    }

    public void displayCurrentState() {
        IO.println(repo.getCurrentState());
    }
}
