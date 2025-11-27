package controller;

import exception.EmptyExecutionStackException;
import exception.MyException;
import model.state.*;
import model.statement.Statement;
import model.value.RefValue;
import model.value.Value;
import repo.Repository;

import java.util.*;
import java.util.stream.Collectors;

public record Controller(Repository repo) {
    public void addNewProgram(Statement program) {
        var executionStack = new LLExeStack();
        executionStack.push(program);

        repo.addProgramState(new model.state.ProgramState(
                executionStack,
                new MapSymTable(),
                new ArrayListOut(),
                new MapFileTable(),
                new HeapTable()
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

            var symTableValues = state.symbolTable().entries().stream()
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());

            var heap = state.heapTable();        // HeapTable implements MyIHeap
            var newHeapContent = garbageCollector(symTableValues, heap.heapTable());
            heap.setContent(newHeapContent);
        }

    }

    private Set<Integer> getReachableAddresses(Collection<Value> symTableValues,
                                               Map<Integer, Value> heapContent) {

        Set<Integer> reachable = new HashSet<>();
        Deque<Integer> worklist = new ArrayDeque<>();

        // 1. Direct addresses from SymTable (roots)
        for (Value v : symTableValues) {
            if (v instanceof RefValue refValue) {
                int addr = refValue.getAddress();   // adapt if your RefValue uses getAddr()
                if (addr != 0 && reachable.add(addr)) {
                    worklist.add(addr);
                }
            }
        }

        // 2. Follow references inside the heap (transitive closure)
        while (!worklist.isEmpty()) {
            int addr = worklist.removeFirst();
            Value heapVal = heapContent.get(addr);
            if (heapVal instanceof RefValue refValue) {
                int innerAddr = refValue.getAddress();
                if (innerAddr != 0 && reachable.add(innerAddr)) {
                    worklist.add(innerAddr);
                }
            }
        }

        return reachable;
    }

    private Map<Integer, Value> garbageCollector(Collection<Value> symTableValues,
                                                 Map<Integer, Value> heapContent) {

        Set<Integer> reachable = getReachableAddresses(symTableValues, heapContent);

        return heapContent.entrySet().stream()
                .filter(e -> reachable.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void displayCurrentState() {
        IO.println(repo.getCurrentState());
    }
}
