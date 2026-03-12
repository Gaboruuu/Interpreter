package model.gui;

import data.Examples;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.statement.Statement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProgramSelectorController {
    @FXML
    private ListView<String> programListView;

    @FXML
    private Button selectButton;

    private List<Statement> programs;
    private ObservableList<String> programDescriptions;

    @FXML
    public void initialize() {
        // Initialize the list of available programs
        programs = new ArrayList<>();
        programDescriptions = FXCollections.observableArrayList();

        // Add all example programs
        Statement ex1 = Examples.hexample1();
        Statement ex2 = Examples.hexample2();
        Statement ex3 = Examples.hexample3();
        Statement ex4 = Examples.hexample4();
        Statement exThread = Examples.examplethred();
        Statement exFor = Examples.exampleFor();
        Statement exLock = Examples.exampleLock();


        programs.add(ex1);
        programs.add(ex2);
        programs.add(ex3);
        programs.add(ex4);
        programs.add(exThread);
        programs.add(exFor);
        programs.add(exLock);


        // Add descriptions`
        programDescriptions.add("Example 1 (Heap): " + ex1.toString());
        programDescriptions.add("Example 2 (ReadHeap): " + ex2.toString());
        programDescriptions.add("Example 3 (WriteHeap): " + ex3.toString());
        programDescriptions.add("Example 4 (GC): " + ex4.toString());
        programDescriptions.add("Example Thread (Fork): " + exThread.toString());
        programDescriptions.add("Example For Loop: " + exFor.toString());
        programDescriptions.add("Example Lock (Concurrency): " + exLock.toString());

        programListView.setItems(programDescriptions);
    }

    @FXML
    private void handleSelectButton() {
        int selectedIndex = programListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex < 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Program Selected");
            alert.setContentText("Please select a program to execute.");
            alert.showAndWait();
            return;
        }

        Statement selectedProgram = programs.get(selectedIndex);

        try {
            // Load the main window
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
            Parent root = loader.load();

            // Get the controller and initialize with the selected program
            MainWindowController controller = loader.getController();
            controller.setProgram(selectedProgram);

            // Create and show the main window
            Stage stage = new Stage();
            stage.setTitle("Interpreter - " + programDescriptions.get(selectedIndex));
            stage.setScene(new Scene(root, 1200, 800));
            stage.show();

            // Close the selector window
            Stage currentStage = (Stage) selectButton.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load main window");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}

