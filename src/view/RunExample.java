// src/view/RunExample.java
package view;

import controller.Controller;
import exception.MyException;

public class RunExample extends Command {
    private final Controller controller;

    public RunExample(String key, String desc, Controller controller) {
        super(key, desc);
        this.controller = controller;
    }

    @Override
    public void execute() {
        try {
            controller.allSteps();
        } catch (MyException e) {
            System.out.println("Error during program execution: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Runtime error: " + e.getMessage());
        }
    }
}
