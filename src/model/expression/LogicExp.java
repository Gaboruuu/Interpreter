package model.expression;

import exception.MyException;
import model.state.HeapTable;
import model.state.MyIDictionary;
import model.state.SymbolTable;
import model.type.BoolType;
import model.type.Type;
import model.value.BoolValue;
import model.value.Value;

public record LogicExp(Expression left, Expression right, String operator) implements Expression {

    @Override
    public Value evaluate(SymbolTable symbolTable, HeapTable heapTable) throws MyException {
        Value leftValue = left.evaluate(symbolTable, heapTable);
        Value rightValue = right.evaluate(symbolTable, heapTable);

        if (!(leftValue instanceof BoolValue(boolean leftBool) && rightValue instanceof BoolValue(boolean rightBool))) {
            throw new ArithmeticException("LogicExpression: Both operands must be boolean values.");
        }
        return switch (operator) {
            case "&&" -> new BoolValue(leftBool && rightBool);
            case "||" -> new BoolValue(leftBool || rightBool);
            default -> throw new MyException("LogicExpression: Unknown operator " + operator);
        };
    }

    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typ1, typ2;
        typ1 = left.typecheck(typeEnv);
        typ2 = left.typecheck(typeEnv);

        if (typ1.equals(new BoolType())) {
            if (typ2.equals(new BoolType())) {
                return new BoolType();
            } else {
                throw new MyException("Operandul 2 nu este Bool");
            }
        } else {
            throw new MyException("Operandul 1 nu este Bool");
        }
    }
}
