// src/model/expression/RelationalExp.java
package model.expression;

import exception.MyException;
import model.state.HeapTable;
import model.state.SymbolTable;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.Value;

public record RelationalExp(Expression left, Expression right, String operator)
        implements Expression {

    @Override
    public String toString() {
        return "(" + left + " " + operator + " " + right + ")";
    }

    @Override
    public Value evaluate(SymbolTable symbolTable, HeapTable heapTable) throws MyException {
        Value leftValue = left.evaluate(symbolTable, heapTable);
        Value rightValue = right.evaluate(symbolTable, heapTable);

        if (!(leftValue instanceof IntValue(int value) &&
                rightValue instanceof IntValue(int value1))) {
            throw new MyException("RelationalExpression: both operands must be integers.");
        }

        return switch (operator) {
            case "<"  -> new BoolValue(value < value1);
            case "<=" -> new BoolValue(value <= value1);
            case "==" -> new BoolValue(value == value1);
            case "!=" -> new BoolValue(value != value1);
            case ">"  -> new BoolValue(value > value1);
            case ">=" -> new BoolValue(value >= value1);
            default   -> throw new MyException("RelationalExpression: unknown operator " + operator);
        };
    }
}
