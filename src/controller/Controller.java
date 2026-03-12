package controller;

import exception.MyException;
import model.state.*;
import model.statement.Statement;
import model.value.RefValue;
import model.value.Value;
import repo.Repository;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Controller {
    private final Repository repo;
    private ExecutorService executor;

    public Controller(Repository repo) {
        this.repo = repo;
        // Initialize executor for GUI usage (with fixed thread pool)
        this.executor = Executors.newFixedThreadPool(2);
    }

    // 1. Updated oneStepForAllPrg (Task 15 in Lab8)
    public void oneStepForAllPrg(List<ProgramState> ignored) {
        // Take a snapshot of active states at the start of this "global step".
        // Any newly forked states will be appended, but will run starting with the next global step.
        List<ProgramState> current = repo.getProgramStates();
        List<ProgramState> activeSnapshot = current.stream()
                .filter(ProgramState::isNotCompleted)
                .collect(Collectors.toList());

        // Log before
        activeSnapshot.forEach(prg -> {
            try {
                repo.logPrgStateExec(prg);
            } catch (MyException e) {
                System.err.println(e.getMessage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        List<ProgramState> newStates = new ArrayList<>();
        for (ProgramState p : activeSnapshot) {
            try {
                ProgramState maybeChild = p.oneStep();
                if (maybeChild != null) {
                    newStates.add(maybeChild);
                }
            } catch (MyException e) {
                if (e.getMessage() != null && e.getMessage().contains("Execution stack is empty")) {
                    continue;
                }
                throw new RuntimeException("Execution failed", e);
            }
        }

        // Log after
        activeSnapshot.forEach(prg -> {
            try {
                repo.logPrgStateExec(prg);
            } catch (MyException | IOException e) {
                System.err.println(e.getMessage());
            }
        });
        newStates.forEach(prg -> {
            try {
                repo.logPrgStateExec(prg);
            } catch (MyException | IOException e) {
                System.err.println(e.getMessage());
            }
        });

        // Persist: keep whatever is currently in the repo (including completed ones), plus new children.
        // Completed states are removed by allSteps()/GUI after each step.
        List<ProgramState> merged = new ArrayList<>(current.size() + newStates.size());
        merged.addAll(current);
        merged.addAll(newStates);
        repo.setProgramStates(merged);
    }

    // 2. Updated allSteps (Task 16 in Lab8)
    public void allSteps() throws MyException {
        while (true) {
            List<ProgramState> prgList = removeCompletedPrg(repo.getProgramStates());
            if (prgList.isEmpty()) {
                break;
            }

            // Run conservative GC on the shared heap of the first active state
            prgList.get(0).heapTable().setContent(
                    garbageCollector(
                            getAllSymTableValues(prgList),
                            prgList.get(0).heapTable().heapTable()
                    )
            );

            oneStepForAllPrg(prgList);
        }
        // no executor to shutdown in sequential mode
    }

    // Helper: Collect all values from all symbol tables of all active threads
    private List<Value> getAllSymTableValues(List<ProgramState> prgList) {
        List<Value> allValues = new ArrayList<>();
        prgList.forEach(prg -> allValues.addAll(prg.symbolTable().entries().stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toList())));
        return allValues;
    }

    public List<ProgramState> removeCompletedPrg(List<ProgramState> inPrgList) {
        return inPrgList.stream()
                .filter(ProgramState::isNotCompleted)
                .collect(Collectors.toList());
    }

    // Public method for GUI to call garbage collector
    public void runGarbageCollector(List<ProgramState> prgList) {
        if (!prgList.isEmpty()) {
            prgList.get(0).heapTable().setContent(
                    garbageCollector(
                            getAllSymTableValues(prgList),
                            prgList.get(0).heapTable().heapTable()
                    )
            );
        }
    }

    // --- Garbage Collector remains similar but uses the collected values ---

    private Map<Integer, Value> garbageCollector(Collection<Value> symTableValues, Map<Integer, Value> heapContent) {
        Set<Integer> reachable = getReachableAddresses(symTableValues, heapContent);
        return heapContent.entrySet().stream()
                .filter(e -> reachable.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Set<Integer> getReachableAddresses(Collection<Value> symTableValues, Map<Integer, Value> heapContent) {
        Set<Integer> reachable = new HashSet<>();
        Deque<Integer> worklist = new ArrayDeque<>();

        for (Value v : symTableValues) {
            if (v instanceof RefValue refValue) {
                int addr = refValue.getAddress();
                if (addr != 0 && reachable.add(addr)) {
                    worklist.add(addr);
                }
            }
        }

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

    public void addNewProgram(Statement program) {
        // 1. Every new program starts with a fresh Execution Stack
        var executionStack = new LLExeStack();
        executionStack.push(program);

        // 2. Create the initial ProgramState (the main thread)
        // It gets a new SymTable, Out, FileTable, and HeapTable
        ProgramState initialState = new ProgramState(
                ProgramState.getNextId(),
                executionStack,
                new MapSymTable(),
                new ArrayListOut(),
                new MapFileTable(),
                new HeapTable(),
                new MyLockTable()
        );

        // 3. Add this first thread to the repository list
        repo.addProgramState(initialState);
    }
}
