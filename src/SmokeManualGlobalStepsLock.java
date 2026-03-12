import controller.Controller;
import data.Examples;
import repo.ArrayRepo;

/**
 * Simulates pressing the GUI "Run One Step" button repeatedly (global step: one step per active thread).
 * Prints final OUT and HEAP so we can verify the lock example yields {190 or 199, 350 or 305}.
 */
public class SmokeManualGlobalStepsLock {
    public static void main(String[] args) throws Exception {
        var repo = new ArrayRepo("log_manual_steps_lock.txt");
        var ctrl = new Controller(repo);
        ctrl.addNewProgram(Examples.exampleLock());

        // capture shared references from the initial state
        var initial = repo.getProgramStates().get(0);
        var out = initial.out();
        var heap = initial.heapTable();

        int steps = 0;
        while (!ctrl.removeCompletedPrg(repo.getProgramStates()).isEmpty()) {
            steps++;
            ctrl.runGarbageCollector(repo.getProgramStates());
            ctrl.oneStepForAllPrg(repo.getProgramStates());
            repo.setProgramStates(ctrl.removeCompletedPrg(repo.getProgramStates()));
            if (steps > 50_000) {
                throw new RuntimeException("Too many steps; likely stuck in a loop.");
            }
        }

        System.out.println("Steps = " + steps);
        System.out.println("OUT  = " + out.getAll());
        System.out.println("HEAP = " + heap.heapTable());
    }
}
