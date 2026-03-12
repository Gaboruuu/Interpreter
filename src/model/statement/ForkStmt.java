package model.statement;

import exception.MyException;
import model.state.LLExeStack;
import model.state.MyIDictionary;
import model.state.ProgramState;
import model.type.Type;

public record ForkStmt(Statement statement) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        // 1. Create a new stack for the new thread
        var newStack = new LLExeStack();
        // 2. Push the statement provided to the fork onto the new stack
        newStack.push(statement);

        // 3. Create a DEEP COPY of the current Symbol Table
        // Assuming your SymTable implementation has a clone/copy method
        var newSymTable = state.symbolTable().deepCopy();

        // 4. Create and return the new ProgramState
        // It shares the Heap, Out, and FileTable of the parent
        return new ProgramState(
                ProgramState.getNextId(),
                newStack,
                newSymTable,
                state.out(),
                state.fileTable(),
                state.heapTable(),
                state.myILockTable()
        );
    }


    @Override
    public String toString() {
        return "fork(" + statement.toString() + ")";
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        statement.typecheck(typeEnv.deepCopy());
        return typeEnv;
    }
}