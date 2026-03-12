import controller.Controller;
import model.expression.ReadHeapExp;
import model.expression.ValueExp;
import model.expression.VarExp;
import model.statement.*;
import model.type.IntType;
import model.type.RefType;
import model.value.IntValue;
import repo.ArrayRepo;

public class SmokeHeapWrite {
    public static void main(String[] args) throws Exception {
        System.out.println("[SmokeHeapWrite] starting");

        // Ref int v; new(v,20); wH(v,99); print(rH(v));
        Statement prg = new CompStmt(
                new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(
                        new NewStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt(
                                new WriteHeapStmt("v", new ValueExp(new IntValue(99))),
                                new PrintStmt(new ReadHeapExp(new VarExp("v")))
                        )
                )
        );

        var repo = new ArrayRepo("log_heapwrite_smoke.txt");
        var ctrl = new Controller(repo);
        ctrl.addNewProgram(prg);

        var sharedOut = repo.getProgramStates().get(0).out();
        ctrl.allSteps();

        System.out.println("OUT = " + sharedOut.values());
    }
}
