package model.statement;


import exception.MyException;
import model.expression.Expression;
import model.expression.RelationalExp;
import model.expression.VarExp;
import model.state.MyIDictionary;
import model.state.ProgramState;
import model.type.IntType;
import model.type.Type;

/**
 * @param exp1 Initialization (v = exp1)
 * @param exp2 Condition (v < exp2)
 * @param exp3 Increment (v = exp3)
 */
public record ForStmt(String var, Expression exp1, Expression exp2, Expression exp3,
                      Statement body) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) throws MyException {

        Statement converted = new CompStmt(
                new AssignStmt(var, exp1),
                new WhileStmt(
                        new RelationalExp(new VarExp(var), exp2, "<"),
                        new CompStmt(body, new AssignStmt(var, exp3))
                )
        );

        state.executionStack().push(converted);
        return state;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        // 1. exp1 must be int (assignment to v)
        Type type1 = exp1.typecheck(typeEnv);
        // 2. exp2 must be int (relational check < usually compares ints)
        Type type2 = exp2.typecheck(typeEnv);
        // 3. exp3 must be int (increment)
        Type type3 = exp3.typecheck(typeEnv);

        if (type1.equals(new IntType()) && type2.equals(new IntType()) && type3.equals(new IntType())) {

            if (typeEnv.isDefined(var) && typeEnv.lookup(var).equals(new IntType())) {

                body.typecheck(typeEnv.deepCopy());
                return typeEnv;
            } else {
                throw new MyException("The variable '" + var + "' in for-loop must be of type int!");
            }
        } else {
            throw new MyException("The expressions in a for-loop must be of type int!");
        }
    }


    @Override
    public String toString() {
        return "for(" + var + "=" + exp1 + "; " + var + "<" + exp2 + "; " + var + "=" + exp3 + ") {" + body + "}";
    }
}