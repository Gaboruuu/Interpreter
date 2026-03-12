// src/Interpreter.java  (note: default package)
import model.gui.InterpreterGUI;

public class Interpreter {
    public static void main(String[] args) {
        // Launch the JavaFX GUI
        InterpreterGUI.main(args);

        // Previous text-based menu (commented out - uncomment to use text UI instead)
//        TextMenu textMenu = new TextMenu();
//        textMenu.addCommand(new ExitCommand("0", "exit"));
//
//        Statement example = Examples.examplethred();
//        try {
//            MyIDictionary<String, Type> typeEnv = new MyDictionary<>();
//            example.typecheck(typeEnv);
//            Repository repo = new ArrayRepo("log_thread.txt");
//            Controller controller = new Controller(repo);
//            controller.addNewProgram(example);
//            textMenu.addCommand(new RunExample("1", example.toString(), controller));
//        } catch (MyException e) {
//            System.out.println("The example did not pass type checking: " + e.getMessage());
//        }
//        textMenu.show();
    }
}
