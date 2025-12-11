package data;

import model.expression.ArithmeticExp;
import model.expression.ReadHeapExp;
import model.expression.ValueExp;
import model.expression.VarExp;
import model.statement.*;
import model.type.IntType;
import model.type.RefType;
import model.value.IntValue;

public class Examples {
    private Examples() {}

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


}
