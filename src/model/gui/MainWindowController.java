package model.gui;

import controller.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.state.*;
import model.statement.Statement;
import model.value.Value;
import repo.ArrayRepo;
import repo.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainWindowController {
    @FXML
    private TextField nrProgramStatesTextField;

    @FXML
    private TableView<HeapEntry> heapTableView;

    @FXML
    private TableColumn<HeapEntry, Integer> heapAddressColumn;

    @FXML
    private TableColumn<HeapEntry, String> heapValueColumn;

    @FXML
    private ListView<String> outListView;

    @FXML
    private ListView<String> fileTableListView;

    @FXML
    private ListView<Integer> prgStateListView;

    @FXML
    private TableView<SymTableEntry> symTableView;

    @FXML
    private TableColumn<SymTableEntry, String> symTableVarNameColumn;

    @FXML
    private TableColumn<SymTableEntry, String> symTableValueColumn;

    @FXML
    private ListView<String> exeStackListView;

    @FXML
    private Button runOneStepButton;

    @FXML
    private Label lastExecutedLabel;

    @FXML
    private TableView<LockTableEntry> lockTableView;

    @FXML
    private TableColumn<LockTableEntry, Integer> lockAddressColumn;

    @FXML
    private TableColumn<LockTableEntry, Integer> lockOwnerColumn;

    private Controller controller;
    private Repository repository;
    private Statement initialProgram;

    private ObservableList<HeapEntry> heapEntries;
    private ObservableList<String> outList;
    private ObservableList<String> fileTableList;
    private ObservableList<Integer> prgStateIdList;
    private ObservableList<SymTableEntry> symTableEntries;
    private ObservableList<String> exeStackList;
    private ObservableList<LockTableEntry> lockTableEntries;

    // Keep a reference to the last known shared state (heap/out/fileTable)
    // so the GUI can still refresh final output even after all threads complete.
    private ProgramState lastSharedState;

    @FXML
    public void initialize() {
        // Initialize observable lists
        heapEntries = FXCollections.observableArrayList();
        outList = FXCollections.observableArrayList();
        fileTableList = FXCollections.observableArrayList();
        prgStateIdList = FXCollections.observableArrayList();
        symTableEntries = FXCollections.observableArrayList();
        exeStackList = FXCollections.observableArrayList();
        lockTableEntries = FXCollections.observableArrayList();

        // Setup table columns
        heapAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        heapValueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

        lockAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        lockOwnerColumn.setCellValueFactory(new PropertyValueFactory<>("owner"));

        symTableVarNameColumn.setCellValueFactory(new PropertyValueFactory<>("variableName"));
        symTableValueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

        // Bind observable lists to UI components
        heapTableView.setItems(heapEntries);
        lockTableView.setItems(lockTableEntries);
        outListView.setItems(outList);
        fileTableListView.setItems(fileTableList);
        prgStateListView.setItems(prgStateIdList);
        symTableView.setItems(symTableEntries);
        exeStackListView.setItems(exeStackList);

        // Add listener to program state selection
        prgStateListView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    updateSymTableAndExeStack(newValue);
                }
            }
        );
    }

    public void setProgram(Statement program) {
        // Create a unique log file for this execution
        String logFileName = "log_gui_" + System.currentTimeMillis() + ".txt";
        this.repository = new ArrayRepo(logFileName);
        this.controller = new Controller(repository);

        // Add the initial program
        controller.addNewProgram(program);

        // Update the display
        updateDisplay();
    }

    @FXML
    private void handleRunOneStep() {
        System.out.println("=== Run One Step Button Clicked ===");

        if (controller == null || repository == null) {
            System.err.println("ERROR: Controller or Repository is null!");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Initialization Error");
            alert.setContentText("Controller or Repository not initialized properly.");
            alert.showAndWait();
            return;
        }

        List<ProgramState> prgList = repository.getProgramStates();
        System.out.println("Number of program states: " + prgList.size());

        if (prgList.isEmpty()) {
            System.out.println("No program states - execution complete");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Execution Complete");
            alert.setHeaderText("Program Execution Finished");
            alert.setContentText("All program states have completed execution. Output is displayed above.");
            alert.showAndWait();
            runOneStepButton.setDisable(true);
            return;
        }

        try {
            System.out.println("Running garbage collector...");
            // Run garbage collector before execution
            controller.runGarbageCollector(prgList);

            System.out.println("Executing one step for all programs...");

            // Capture what's about to be executed for display
            StringBuilder executedInfo = new StringBuilder();
            int threadCount = 0;
            for (ProgramState ps : prgList) {
                if (!ps.executionStack().isEmpty()) {
                    Statement topStmt = null;
                    for (Statement s : ps.executionStack().topToBottom()) {
                        topStmt = s;
                        break; // Get first element (top of stack)
                    }
                    if (threadCount > 0) executedInfo.append(" | ");
                    executedInfo.append("Thread ").append(ps.id()).append(": ");

                    if (topStmt != null) {
                        String stmtClass = topStmt.getClass().getSimpleName();
                        executedInfo.append(stmtClass);

                        // Add more detail for certain statements
                        if (stmtClass.equals("CompStmt")) {
                            executedInfo.append(" (decomposing)");
                        }
                    }
                    threadCount++;
                }
            }

            // Show what's about to be executed
            System.out.println("\n--- BEFORE EXECUTION ---");
            for (ProgramState ps : prgList) {
                if (!ps.executionStack().isEmpty()) {
                    Statement topStmt = null;
                    for (Statement s : ps.executionStack().topToBottom()) {
                        topStmt = s;
                        break; // Get first element (top of stack)
                    }
                    System.out.println("Thread " + ps.id() + " will execute: " +
                        (topStmt != null ? topStmt.getClass().getSimpleName() : "null"));
                    System.out.println("  Full: " + (topStmt != null ? topStmt.toString() : "null"));
                }
            }

            // Run one step for all program states
            controller.oneStepForAllPrg(prgList);

            // Update the last executed label
            if (executedInfo.length() > 0) {
                lastExecutedLabel.setText(executedInfo.toString());
                lastExecutedLabel.setStyle("-fx-text-fill: #0066cc; -fx-font-weight: bold;");
            }

            System.out.println("\n--- AFTER EXECUTION ---");

            System.out.println("Updating display with current state (before removing completed programs)...");
            // Update display FIRST while we still have program states to read from
            updateDisplay();

            // Remove completed programs from the repository's *current* list (which may include new forks)
            List<ProgramState> currentStates = repository.getProgramStates();
            List<ProgramState> remaining = controller.removeCompletedPrg(currentStates);
            repository.setProgramStates(remaining);

            // Refresh again (preserves last Out/Heap even if the last thread just finished)
            updateDisplay();

            System.out.println("Program states remaining: " + remaining.size());

            if (remaining.isEmpty()) {
                System.out.println("All programs completed - final state is already visible");

                // Just update the count to 0 and clear the IDs/stacks
                nrProgramStatesTextField.setText("0");
                prgStateIdList.clear();
                exeStackList.clear();
                symTableEntries.clear();

                // Keep Out/Heap/FileTable visible (do NOT clear outList/heapEntries),
                // because output may be produced by the very last step.

                lastExecutedLabel.setText("EXECUTION COMPLETED - All output displayed above");
                lastExecutedLabel.setStyle("-fx-text-fill: #00aa00; -fx-font-weight: bold;");

                System.out.println("Final state preserved in display\n");

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Execution Complete");
                alert.setHeaderText("Program Execution Finished");
                alert.setContentText("All program states have completed execution.\n\nThe final output and heap state remain visible in the tables above.");
                alert.showAndWait();

                runOneStepButton.setDisable(true);
            }

        } catch (Exception e) {
            System.err.println("=== EXCEPTION CAUGHT ===");
            System.err.println("Exception type: " + e.getClass().getName());
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace();

            // Build detailed error message
            StringBuilder errorMsg = new StringBuilder();
            errorMsg.append("Error Type: ").append(e.getClass().getSimpleName()).append("\n\n");
            if (e.getMessage() != null) {
                errorMsg.append("Message: ").append(e.getMessage()).append("\n\n");
            }
            errorMsg.append("Check the console output for full stack trace.");

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Execution Error");
            alert.setHeaderText("An error occurred during execution");
            alert.setContentText(errorMsg.toString());
            alert.showAndWait();
        }

        System.out.println("=== Run One Step Completed ===\n");
    }

    private void updateDisplay() {
        List<ProgramState> prgStates = repository.getProgramStates();

        // Update number of program states
        nrProgramStatesTextField.setText(String.valueOf(prgStates.size()));

        if (prgStates.isEmpty()) {
            // No active states left, but we may still need to refresh Out/Heap from the last known state.
            if (lastSharedState != null) {
                updateHeapTable(lastSharedState.heapTable());
                updateLockTable(lastSharedState.myILockTable());
                updateOutList(lastSharedState.out());
                updateFileTableList(lastSharedState.fileTable());
            }
            return;
        }

        // Get the first program state (shared structures)
        ProgramState firstState = prgStates.get(0);
        lastSharedState = firstState;

        // Update heap table
        updateHeapTable(firstState.heapTable());

        // Update lock table
        updateLockTable(firstState.myILockTable());

        // Update out list
        updateOutList(firstState.out());

        // Update file table list
        updateFileTableList(firstState.fileTable());

        // Update program state IDs list
        updatePrgStateIdList(prgStates);

        // Update sym table and exe stack for the selected program state
        Integer selectedId = prgStateListView.getSelectionModel().getSelectedItem();
        if (selectedId != null) {
            updateSymTableAndExeStack(selectedId);
        } else if (!prgStates.isEmpty()) {
            // Select the first one by default
            prgStateListView.getSelectionModel().select(0);
        }
    }


    private void updateHeapTable(HeapTable heapTable) {
        heapEntries.clear();
        Map<Integer, Value> heap = heapTable.heapTable();
        for (Map.Entry<Integer, Value> entry : heap.entrySet()) {
            heapEntries.add(new HeapEntry(entry.getKey(), entry.getValue()));
        }
    }

    private void updateLockTable(MyILockTable lockTable) {
        lockTableEntries.clear();
        if (lockTable == null) {
            return;
        }
        for (Map.Entry<Integer, Integer> e : lockTable.getContent().entrySet()) {
            lockTableEntries.add(new LockTableEntry(e.getKey(), e.getValue()));
        }
    }

    private void updateOutList(Out out) {
        outList.clear();
        for (Value value : out.values()) {
            outList.add(value.toString());
        }
    }

    private void updateFileTableList(FileTable fileTable) {
        fileTableList.clear();
        for (Map.Entry<model.value.StringValue, java.io.BufferedReader> entry : fileTable.entries()) {
            fileTableList.add(entry.getKey().toString());
        }
    }

    private void updatePrgStateIdList(List<ProgramState> prgStates) {
        Integer currentlySelected = prgStateListView.getSelectionModel().getSelectedItem();
        prgStateIdList.clear();
        for (ProgramState state : prgStates) {
            prgStateIdList.add(state.id());
        }

        // Restore selection if possible
        if (currentlySelected != null && prgStateIdList.contains(currentlySelected)) {
            prgStateListView.getSelectionModel().select(currentlySelected);
        }
    }

    private void updateSymTableAndExeStack(Integer prgStateId) {
        List<ProgramState> prgStates = repository.getProgramStates();

        // Find the program state with the given ID
        ProgramState selectedState = null;
        for (ProgramState state : prgStates) {
            if (state.id() == prgStateId) {
                selectedState = state;
                break;
            }
        }

        if (selectedState == null) {
            return;
        }


        // Update symbol table
        symTableEntries.clear();
        for (Map.Entry<String, Value> entry : selectedState.symbolTable().entries()) {
            symTableEntries.add(new SymTableEntry(entry.getKey(), entry.getValue()));
        }

        // Update execution stack
        exeStackList.clear();
        List<String> stackStrings = new ArrayList<>();
        for (Statement stmt : selectedState.executionStack().topToBottom()) {
            stackStrings.add(stmt.toString());
        }
        exeStackList.addAll(stackStrings);
    }
}
