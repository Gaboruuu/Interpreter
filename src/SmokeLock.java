import controller.Controller;
import data.Examples;
import repo.ArrayRepo;

public class SmokeLock {
    public static void main(String[] args) throws Exception {
        System.out.println("[SmokeLock] starting");

        var repo = new ArrayRepo("log_lock_smoke.txt");
        var ctrl = new Controller(repo);

        ctrl.addNewProgram(Examples.exampleLock());

        // capture shared Out from initial state (forks share this reference)
        var sharedOut = repo.getProgramStates().get(0).out();

        ctrl.allSteps();

        java.util.List<Object> out = new java.util.ArrayList<>();
        for (var v : sharedOut.values()) {
            out.add(v);
        }

        System.out.println("OUT = " + out);
        System.out.println("Expected: [190 or 199, 350 or 305] (order may vary depending on scheduling)." );
    }
}
