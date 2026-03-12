import controller.Controller;
import data.Examples;
import model.state.ProgramState;
import repo.ArrayRepo;

public class SmokeForkFor {
    public static void main(String[] args) throws Exception {
        System.out.println("[SmokeForkFor] starting");
        var repo = new ArrayRepo("log_fork_smoke.txt");
        var ctrl = new Controller(repo);

        ctrl.addNewProgram(Examples.exampleFor());

        // Capture the shared Out instance from the initial state (forks share this reference).
        var initial = repo.getProgramStates().get(0);
        var sharedOut = initial.out();

        try {
            ctrl.allSteps();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        java.util.List<Object> out = new java.util.ArrayList<>();
        for (var v : sharedOut.values()) {
            out.add(v);
        }
        System.out.println("OUT = " + out);
    }
}
