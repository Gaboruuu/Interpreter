package model.state;

import exception.EmptyExecutionStackException;
import exception.MyException;
import model.statement.Statement;
import model.value.Value;

public record ProgramState(int id, ExecutionStack executionStack, SymbolTable symbolTable, Out out, FileTable fileTable, HeapTable heapTable, MyILockTable myILockTable) {

    private static int nextId = 0;

    public static synchronized int getNextId() {
        return nextId++;
    }

    public String toLogString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Thread ID: ").append(id).append("\n");

        sb.append("ExeStack:\n");
        for (var stmt : executionStack.topToBottom()) {
            sb.append(stmt).append("\n");
        }

        sb.append("SymTable:\n");
        for (var e : symbolTable.entries()) {
            sb.append(e.getKey()).append(" --> ").append(e.getValue()).append("\n");
        }

        sb.append("Out:\n");
        for (Value v : out.values()) {
            sb.append(v).append("\n");
        }

        sb.append("FileTable:\n");
        for (var e : fileTable.entries()) {
            sb.append(e.getKey()).append("\n"); // only the filename key per spec
        }

        return sb.toString();
    }

    public boolean isNotCompleted() {
        return !executionStack.isEmpty();
    }

    public ProgramState oneStep() throws MyException {
        try {
            Statement currentStatement = executionStack.pop();
            return currentStatement.execute(this);
        } catch (EmptyExecutionStackException e) {
            throw new MyException("Execution stack is empty");
        }
    }
}
