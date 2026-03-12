import controller.Controller;
import model.expression.ReadHeapExp;
import model.expression.ValueExp;
import model.expression.VarExp;
import model.statement.*;
import model.type.IntType;
import model.type.RefType;
import model.value.IntValue;
import repo.ArrayRepo;

public class SmokeForkHeapWrite {
    public static void main(String[] args) throws Exception {
        System.out.println("[SmokeForkHeapWrite] starting");

        // Ref int v; new(v,20); fork(wH(v,99)); print(rH(v));
        Statement prg = new CompStmt(
                new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(
                        new NewStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt(
                                new ForkStmt(new WriteHeapStmt("v", new ValueExp(new IntValue(99)))),
                                new PrintStmt(new ReadHeapExp(new VarExp("v")))
                        )
                )
        );

        var repo = new ArrayRepo("log_fork_heapwrite_smoke.txt");
        var ctrl = new Controller(repo);
        ctrl.addNewProgram(prg);

        var sharedOut = repo.getProgramStates().get(0).out();
        ctrl.allSteps();

        System.out.println("OUT = " + sharedOut.values());
        System.out.println("(Depending on scheduling, this may be [20] or [99], but it should NOT always be [20].)");
    }
}
