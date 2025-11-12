package model.state;

import exception.EmptyExecutionStackException;
import model.statement.Statement;

public interface ExecutionStack {
    void push(Statement statement);
    Statement pop() throws EmptyExecutionStackException;
    boolean isEmpty();
}
