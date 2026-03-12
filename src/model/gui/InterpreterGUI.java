package model.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class InterpreterGUI extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the program selector window
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ProgramSelector.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("Interpreter - Program Selector");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

