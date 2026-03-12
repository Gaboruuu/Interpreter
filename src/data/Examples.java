package data;

import model.expression.*;
import model.statement.*;
import model.type.IntType;
import model.type.RefType;
import model.value.BoolValue;
import model.value.IntValue;

public class Examples {
    private Examples() {
    }

//    public static Statement example1() {
//        return new CompStmt(new VarDeclStmt("v" , Type.INTEGER),
//                new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(2))),
//                        new PrintStmt(new VarExp("v"))));
//    }
//
//
//    public static Statement example2() {
//        return new CompStmt(
//                new VarDeclStmt("a", Type.INTEGER),
//                new CompStmt(
//                        new VarDeclStmt("b", Type.INTEGER),
//                        new CompStmt(
//                                new AssignStmt(
//                                        "a",
//                                        new ArithmeticExp(
//                                                new ValueExp(new IntValue(2)),
//                                                new ArithmeticExp(
//                                                        new ValueExp(new IntValue(3)),
//                                                        new ValueExp(new IntValue(5)),
//                                                        '*'
//                                                ),
//                                                '+'
//                                        )
//                                ),
//                                new CompStmt(
//                                        new AssignStmt(
//                                                "b",
//                                                new ArithmeticExp(
//                                                        new VarExp("a"),
//                                                        new ValueExp(new IntValue(1)),
//                                                        '+'
//                                                )
//                                        ),
//                                        new PrintStmt(new VarExp("b"))
//                                )
//                        )
//                )
//        );
//    }
//
//    public static Statement example3() {
//        return new CompStmt(
//                new VarDeclStmt("a", Type.BOOLEAN),
//                new CompStmt(
//                        new VarDeclStmt("v", Type.INTEGER),
//                        new CompStmt(
//                                new AssignStmt("a", new ValueExp(new BoolValue(true))),
//                                new CompStmt(
//                                        new IfStmt(
//                                                new VarExp("a"),
//                                                new AssignStmt("v", new ValueExp(new IntValue(2))),
//                                                new AssignStmt("v", new ValueExp(new IntValue(3)))
//                                            ),
//                                            new PrintStmt(new VarExp("v")
//                                        )
//                                )
//                        )
//                )
//        );
//    }
//
//    public static Statement fileExample() {
//        return new CompStmt(
//                new VarDeclStmt("varf", Type.STRING),
//                new CompStmt(
//                        new AssignStmt("varf", new ValueExp(new StringValue("test.in"))),
//                        new CompStmt(
//                                new OpenRFile(new VarExp("varf")),
//                                new CompStmt(
//                                        new VarDeclStmt("varc", Type.INTEGER),
//                                        new CompStmt(
//                                                new ReadFile(new VarExp("varf"), "varc"),
//                                                new CompStmt(
//                                                        new PrintStmt(new VarExp("varc")),
//                                                        new CompStmt(
//                                                                new ReadFile(new VarExp("varf"), "varc"),
//                                                                new CompStmt(
//                                                                        new PrintStmt(new VarExp("varc")),
//                                                                        new CloseRFile(new VarExp("varf"))
//                                                                )
//                                                        )
//                                                )
//                                        )
//                                )
//                        )
//                )
//        );
//    }

    // Heap Example 1:
// Ref int v; new(v,20); Ref Ref int a; new(a,v); print(v); print(a)
    public static Statement hexample1() {
        return new CompStmt(
                new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(
                        // was BoolValue(true) which violates Ref int
                        new NewStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt(
                                new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                new CompStmt(
                                        new NewStmt("a", new VarExp("v")),
                                        new CompStmt(
                                                new PrintStmt(new VarExp("v")),
                                                new PrintStmt(new VarExp("a"))
                                        )
                                )
                        )
                )
        );
    }

    // Heap Example 2:
// Ref int v; new(v,20); Ref Ref int a; new(a,v);
// print(rH(v)); print(rH(rH(a))+5)
    public static Statement hexample2() {
        return new CompStmt(
                new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(
                        new NewStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt(
                                new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                new CompStmt(
                                        new NewStmt("a", new VarExp("v")),
                                        new CompStmt(
                                                new PrintStmt(new ReadHeapExp(new VarExp("v"))),
                                                new PrintStmt(
                                                        new ArithmeticExp(
                                                                new ReadHeapExp(
                                                                        new ReadHeapExp(new VarExp("a"))
                                                                ),
                                                                new ValueExp(new IntValue(5)),
                                                                '+'
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
    }

    // Heap Example 3:
// Ref int v; new(v,20); print(rH(v));
// wH(v,30); print(rH(v)+5);
    public static Statement hexample3() {
        return new CompStmt(
                new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(
                        new NewStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt(
                                new PrintStmt(new ReadHeapExp(new VarExp("v"))),
                                new CompStmt(
                                        new WriteHeapStmt("v", new ValueExp(new IntValue(30))),
                                        new PrintStmt(
                                                new ArithmeticExp(
                                                        new ReadHeapExp(new VarExp("v")),
                                                        new ValueExp(new IntValue(5)),
                                                        '+'
                                                )
                                        )
                                )
                        )
                )
        );
    }

    // Heap Example 4 (GC example):
// Ref int v; new(v,20); Ref Ref int a; new(a,v);
// new(v,30); print(rH(rH(a)))
    public static Statement hexample4() {
        return new CompStmt(
                new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(
                        new NewStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt(
                                new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                new CompStmt(
                                        new NewStmt("a", new VarExp("v")),
                                        new CompStmt(
                                                new NewStmt("v", new ValueExp(new IntValue(30))),
                                                new PrintStmt(
                                                        new ReadHeapExp(
                                                                new ReadHeapExp(new VarExp("a"))
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
    }

    public static Statement examplethred() {
        CompStmt second = new CompStmt(
                new PrintStmt(new VarExp("v")),
                new PrintStmt(new ReadHeapExp(new VarExp("a")))
        );
        return new CompStmt(
                new VarDeclStmt("v", new IntType()),
                new CompStmt(
                        new VarDeclStmt("a", new RefType(new IntType())),
                        new CompStmt(
                                new AssignStmt("v", new ValueExp(new IntValue(10))),
                                new CompStmt(
                                        new NewStmt("a", new ValueExp(new IntValue(22))),
                                        new CompStmt(
                                                new ForkStmt(
                                                        new CompStmt(
                                                                new WriteHeapStmt("a", new ValueExp(new IntValue(30))),
                                                                new CompStmt(
                                                                        new AssignStmt("v", new ValueExp(new IntValue(32))),
                                                                        second
                                                                )
                                                        )
                                                ),
                                                second
                                        )
                                )
                        )
                )
        );

    }

    public static Statement exampleFor() {
        return new CompStmt(
                new VarDeclStmt("a", new RefType(new IntType())),
                new CompStmt(
                        new VarDeclStmt("v", new IntType()),
                        new CompStmt(
                                new NewStmt("a", new ValueExp(new IntValue(20))),
                                new CompStmt(
                                        new ForStmt(
                                                "v",
                                                new ValueExp(new IntValue(0)),
                                                new ValueExp(new IntValue(3)),
                                                new ArithmeticExp(new VarExp("v"), new ValueExp(new IntValue(1)), '+'),
                                                new ForkStmt(
                                                        new CompStmt(
                                                                new PrintStmt(new VarExp("v")),
                                                                new AssignStmt(
                                                                        "v",
                                                                        new ArithmeticExp(
                                                                                new VarExp("v"),
                                                                                new ReadHeapExp(new VarExp("a")),
                                                                                '*'
                                                                        )
                                                                )
                                                        )
                                                )
                                        ),
                                        new PrintStmt(new ReadHeapExp(new VarExp("a")))
                                )
                        )
                )
        );
    }


    public static Statement exampleLock() {
        Statement decs = new CompStmt(
                new VarDeclStmt("v1", new RefType(new IntType())),
                new CompStmt(
                        new VarDeclStmt("v2", new RefType(new IntType())),
                        new CompStmt(
                                new VarDeclStmt("x", new IntType()),
                                new VarDeclStmt("q", new IntType())
                        )
                )
        );

        Statement init = new CompStmt(
                new NewStmt("v1", new ValueExp(new IntValue(20))),
                new CompStmt(
                        new NewStmt("v2", new ValueExp(new IntValue(30))),
                        new NewLockStmt("x")
                )
        );

        // fork( fork(lock(x); wH(v1, rH(v1)-1); unlock(x)); lock(x); wH(v1, rH(v1)*10); unlock(x) )
        // + print(rH(v1)) under lock at the end of the outer fork
        Statement xInnerFork = new ForkStmt(
                new CompStmt(
                        new LockStmt("x"),
                        new CompStmt(
                                new WriteHeapStmt(
                                        "v1",
                                        new ArithmeticExp(
                                                new ReadHeapExp(new VarExp("v1")),
                                                new ValueExp(new IntValue(1)),
                                                '-'
                                        )
                                ),
                                new UnlockStmt("x")
                        )
                )
        );

        Statement xOuterFork = new ForkStmt(
                new CompStmt(
                        xInnerFork,
                        new CompStmt(
                                new LockStmt("x"),
                                new CompStmt(
                                        new WriteHeapStmt(
                                                "v1",
                                                new ArithmeticExp(
                                                        new ReadHeapExp(new VarExp("v1")),
                                                        new ValueExp(new IntValue(10)),
                                                        '*'
                                                )
                                        ),
                                        new CompStmt(
                                                new UnlockStmt("x"),
                                                // print final v1 inside the fork
                                                new CompStmt(
                                                        new LockStmt("x"),
                                                        new CompStmt(
                                                                new PrintStmt(new ReadHeapExp(new VarExp("v1"))),
                                                                new UnlockStmt("x")
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );

        Statement newLockQ = new NewLockStmt("q");

        // fork( fork(lock(q); wH(v2, rH(v2)+5); unlock(q)); lock(q); wH(v2, rH(v2)*10); unlock(q) )
        // + print(rH(v2)) under lock at the end of the outer fork
        Statement qInnerFork = new ForkStmt(
                new CompStmt(
                        new LockStmt("q"),
                        new CompStmt(
                                new WriteHeapStmt(
                                        "v2",
                                        new ArithmeticExp(
                                                new ReadHeapExp(new VarExp("v2")),
                                                new ValueExp(new IntValue(5)),
                                                '+'
                                        )
                                ),
                                new UnlockStmt("q")
                        )
                )
        );

        Statement qOuterFork = new ForkStmt(
                new CompStmt(
                        qInnerFork,
                        new CompStmt(
                                new LockStmt("q"),
                                new CompStmt(
                                        new WriteHeapStmt(
                                                "v2",
                                                new ArithmeticExp(
                                                        new ReadHeapExp(new VarExp("v2")),
                                                        new ValueExp(new IntValue(10)),
                                                        '*'
                                                )
                                        ),
                                        new CompStmt(
                                                new UnlockStmt("q"),
                                                // print final v2 inside the fork
                                                new CompStmt(
                                                        new LockStmt("q"),
                                                        new CompStmt(
                                                                new PrintStmt(new ReadHeapExp(new VarExp("v2"))),
                                                                new UnlockStmt("q")
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );


        Statement nops = new CompStmt(new NoOpStmt(),
                new CompStmt(new NoOpStmt(),
                        new CompStmt(new NoOpStmt(), new NoOpStmt())));

        return new CompStmt(
                decs,
                new CompStmt(
                        init,
                        new CompStmt(
                                xOuterFork,
                                new CompStmt(
                                        newLockQ,
                                        new CompStmt(
                                                qOuterFork,
                                                // main thread only does nops now
                                                nops
                                        )
                                )
                        )
                )
        );
    }
}
