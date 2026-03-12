package view;

import controller.Controller;
import data.Examples;
import exception.MyException;
import model.statement.Statement;

import java.util.Scanner;

public record TextUI (Controller controller) implements View {
    private static final Scanner sc = new Scanner(System.in);

    private void inputProgramMenu() {
        System.out.println("Choose a program to load:");
        System.out.println("1 - Example 1: int v; v=2; Print(v)");
        System.out.println("2 - Example 2: int a; int b; a=2+3*5; b=a+1; Print(b)");
        System.out.println("3 - Example 3: bool a; int v; a=true; If a Then v=2 Else v=3; Print(v)");
        System.out.print("Selection: ");
        String line = sc.nextLine().trim();
        Statement st;
        switch (line) {
            case "1" -> st = Examples.hexample1();
            case "2" -> st = Examples.hexample2();
            case "3" -> st = Examples.hexample3();
            case "4" -> st = Examples.hexample4();
            case "0" -> {
                System.out.println("Returning to main menu.");
                return;
            }
            default -> {
                System.out.println("Invalid choice.");
                return;
            }
        }
        controller.addNewProgram(st);
        System.out.println("Program loaded successfully.");
    }

    @Override
    public void start() {
        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1 - Input a program");
            System.out.println("2 - Execute current program (complete)");
            System.out.println("0 - Exit");
            System.out.print("Option: ");
            String line = sc.nextLine().trim();
            switch (line) {
                case "1" -> inputProgramMenu();
                case "2" -> executeProgram();
                case "0" -> {
                    System.out.println("Exiting.");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }

    }

    private void executeProgram() {
        try {
            // We no longer manually check the state here because
            // the controller handles the list of all active threads.

            System.out.println("Starting concurrent execution...");

            // This method now contains the while loop, the executor,
            // and the oneStepForAllPrg logic.
            controller.allSteps();

            System.out.println("Program finished execution.");

            // To see the final output, you can print the 'Out' list from
            // any of the states or the log file.
        } catch (MyException e) {
            System.out.println("Execution error: " + e.getMessage());
            // Clean up thread pool if an error occurs
            Thread.currentThread().interrupt();
        }
    }

}
