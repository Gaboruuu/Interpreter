// src/Interpreter.java  (note: default package)
import controller.Controller;
import data.Examples;
import model.statement.Statement;
import repo.ArrayRepo;
import repo.Repository;
import view.ExitCommand;
import view.RunExample;
import view.TextMenu;

public class Interpreter {
    public static void main(String[] args) {
        Statement ex1 = Examples.example1();
        Statement ex2 = Examples.example2();
        Statement ex3 = Examples.example3();
        Statement ex4 = Examples.fileExample();

        Repository repo1 = new ArrayRepo("log1.txt");
        Controller ctr1 = new Controller(repo1);
        ctr1.addNewProgram(ex1);

        Repository repo2 = new ArrayRepo("log2.txt");
        Controller ctr2 = new Controller(repo2);
        ctr2.addNewProgram(ex2);

        Repository repo3 = new ArrayRepo("log3.txt");
        Controller ctr3 = new Controller(repo3);
        ctr3.addNewProgram(ex3);

        Repository repo4 = new ArrayRepo("log4.txt");
        Controller ctr4 = new Controller(repo4);
        ctr4.addNewProgram(ex4);

        TextMenu menu = new TextMenu();
        menu.addCommand(new ExitCommand("0", "exit"));
        menu.addCommand(new RunExample("1", ex1.toString(), ctr1));
        menu.addCommand(new RunExample("2", ex2.toString(), ctr2));
        menu.addCommand(new RunExample("3", ex3.toString(), ctr3));
        menu.addCommand(new RunExample("4", ex4.toString(), ctr4));

        menu.show();
    }
}
