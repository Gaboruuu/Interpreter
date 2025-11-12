package model.state;

import model.value.Value;

public record ProgramState(ExecutionStack executionStack, SymbolTable symbolTable, Out out, FileTable fileTable) {
    public String toLogString() {
        StringBuilder sb = new StringBuilder();

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
}
