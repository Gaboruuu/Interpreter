package model.state;

public record ProgramState(ExecutionStack executionStack, SymbolTable symbolTable, Out out) {
}
