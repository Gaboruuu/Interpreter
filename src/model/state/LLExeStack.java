package model.state;

import exception.EmptyExecutionStackException;
import model.statement.Statement;

import java.util.LinkedList;

public class LLExeStack implements ExecutionStack {
    private final LinkedList<Statement> statements = new LinkedList<>();

    @Override
    public synchronized void push(Statement statement) {
        statements.addFirst(statement);
    }

    @Override
    public synchronized Statement pop() throws EmptyExecutionStackException {
        if (statements.isEmpty()) {
            throw new EmptyExecutionStackException("Execution stack is empty");
        }
        return statements.removeFirst();
    }

    @Override
    public synchronized boolean isEmpty() {
        return statements.isEmpty();
    }

    @Override
    public synchronized Iterable<Statement> topToBottom() {
        // return a snapshot to avoid concurrent modification while iterating
        return new LinkedList<>(statements);
    }

    @Override
    public synchronized String toString() {
        return "Execution Stack: " + statements;
    }
}
