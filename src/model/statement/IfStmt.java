package model.statement;

import exception.MyException;
import model.expression.Expression;
import model.state.MyIDictionary;
import model.state.ProgramState;
import model.type.BoolType;
import model.type.Type;
import model.value.BoolValue;
import model.value.Value;

public record IfStmt(Expression condition, Statement thenStatement, Statement elseStatement) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        var heap = state.heapTable();
        Value value = condition.evaluate(state.symbolTable(), heap);
        if (!(value.getType() instanceof BoolType)) {
            throw new MyException("Condition expression is not of boolean type.");
        }

        BoolValue boolValue = (BoolValue) value;
        Statement chosenStatement = boolValue.value() ? thenStatement : elseStatement;
        state.executionStack().push(chosenStatement);
        return state;
    }

    @Override
    public String toString() {
        return "if(" + condition.toString() + ") then {" + thenStatement.toString() + "} else {" + elseStatement.toString() + "}";
    }

    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typexp = condition.typecheck(typeEnv);
        if (typexp.equals(new BoolType())) {
            // Trebuie să clonăm mediul pentru ramuri, ca modificările din ele să nu afecteze mediul extern
            // Asigură-te că MyIDictionary are o metodă de 'clone' sau 'copy'
            thenStatement.typecheck(typeEnv.deepCopy());
            elseStatement.typecheck(typeEnv.deepCopy());
            return typeEnv;
        } else {
            throw new MyException("Conditia din IF nu este de tip bool");
        }
    }
}
