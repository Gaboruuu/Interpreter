package model.expression;

import exception.MyException;
import model.state.HeapTable;
import model.state.SymbolTable;
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
}
