package data;

import model.expression.ArithmeticExp;
import model.expression.ValueExp;
import model.expression.VarExp;
import model.statement.*;
import model.type.Type;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.StringValue;

public class Examples {
    private Examples() {}

    public static Statement example1() {
        return new CompStmt(new VarDeclStmt("v" ,Type.INTEGER),
                new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(2))),
                        new PrintStmt(new VarExp("v"))));
    }


    public static Statement example2() {
        return new CompStmt(
                new VarDeclStmt("a", Type.INTEGER),
                new CompStmt(
                        new VarDeclStmt("b", Type.INTEGER),
                        new CompStmt(
                                new AssignStmt(
                                        "a",
                                        new ArithmeticExp(
                                                new ValueExp(new IntValue(2)),
                                                new ArithmeticExp(
                                                        new ValueExp(new IntValue(3)),
                                                        new ValueExp(new IntValue(5)),
                                                        '*'
                                                ),
                                                '+'
                                        )
                                ),
                                new CompStmt(
                                        new AssignStmt(
                                                "b",
                                                new ArithmeticExp(
                                                        new VarExp("a"),
                                                        new ValueExp(new IntValue(1)),
                                                        '+'
                                                )
                                        ),
                                        new PrintStmt(new VarExp("b"))
                                )
                        )
                )
        );
    }

    public static Statement example3() {
        return new CompStmt(
                new VarDeclStmt("a", Type.BOOLEAN),
                new CompStmt(
                        new VarDeclStmt("v", Type.INTEGER),
                        new CompStmt(
                                new AssignStmt("a", new ValueExp(new BoolValue(true))),
                                new CompStmt(
                                        new IfStmt(
                                                new VarExp("a"),
                                                new AssignStmt("v", new ValueExp(new IntValue(2))),
                                                new AssignStmt("v", new ValueExp(new IntValue(3)))
                                            ),
                                            new PrintStmt(new VarExp("v")
                                        )
                                )
                        )
                )
        );
    }

    public static Statement fileExample() {
        return new CompStmt(
                new VarDeclStmt("varf", Type.STRING),
                new CompStmt(
                        new AssignStmt("varf", new ValueExp(new StringValue("test.in"))),
                        new CompStmt(
                                new OpenRFile(new VarExp("varf")),
                                new CompStmt(
                                        new VarDeclStmt("varc", Type.INTEGER),
                                        new CompStmt(
                                                new ReadFile(new VarExp("varf"), "varc"),
                                                new CompStmt(
                                                        new PrintStmt(new VarExp("varc")),
                                                        new CompStmt(
                                                                new ReadFile(new VarExp("varf"), "varc"),
                                                                new CompStmt(
                                                                        new PrintStmt(new VarExp("varc")),
                                                                        new CloseRFile(new VarExp("varf"))
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
    }

}
