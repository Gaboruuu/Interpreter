package model.expression;

import exception.DivisionByZeroException;
import exception.MyException;
import model.state.HeapTable;
import model.state.SymbolTable;
import model.value.IntValue;
import model.value.Value;

public record ArithmeticExp(Expression left, Expression right, char operator) implements Expression {
    @Override
    public String toString() {
        return "(" + left.toString() + " " + operator + " " + right.toString() + ")";
    }

    @Override
    public Value evaluate(SymbolTable symbolTable, HeapTable heapTable) throws MyException {
        Value leftValue = left.evaluate(symbolTable, heapTable);
        Value rightValue = right.evaluate(symbolTable, heapTable);
        if (!(leftValue instanceof IntValue(int leftInt) && rightValue instanceof IntValue(int rightInt))) {
            throw new MyException("ArithmeticExpression: both operands must be integers");
        }
        return switch (operator) {
            case '+' -> new IntValue(leftInt + rightInt);
            case '-' -> new IntValue(leftInt - rightInt);
            case '*' -> new IntValue(leftInt * rightInt);
            case '/' -> divide(leftInt, rightInt);
            default -> throw new MyException("ArithmeticExpression: invalid operator " + operator);
        };
    }

    private static IntValue divide(int leftInt, int rightInt) throws DivisionByZeroException {
        if (rightInt == 0) {
            throw new DivisionByZeroException("ArithmeticExpression: division by zero");
        }
        return new IntValue(leftInt / rightInt);
    }
}
