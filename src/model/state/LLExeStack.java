package model.state;

import exception.EmptyExecutionStackException;
import model.statement.Statement;

import java.util.LinkedList;
import java.util.List;

public class LLExeStack implements  ExecutionStack {
    private final List<Statement> statements = new LinkedList<>();

    @Override
    public void push(Statement statement) {
        statements.addFirst( statement);
    }

    @Override
    public Statement pop() throws EmptyExecutionStackException {
        if (statements.isEmpty()) {
            throw new EmptyExecutionStackException("Execution stack is empty");
        }
        return statements.removeFirst();
    }

    @Override
    public boolean isEmpty() {
        return statements.isEmpty();
    }

    @Override
    public Iterable<Statement> topToBottom() {
        return statements;
    }

    @Override
    public String toString() {
        return "Execution Stack: " + statements;
    }
}
