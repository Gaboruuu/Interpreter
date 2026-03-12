// src/model/expression/RelationalExp.java
package model.expression;

import exception.MyException;
import model.state.HeapTable;
import model.state.MyIDictionary;
import model.state.SymbolTable;
import model.type.BoolType;
import model.type.IntType;
import model.type.Type;
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

        if (!(leftValue instanceof IntValue) || !(rightValue instanceof IntValue)) {
            throw new MyException("RelationalExpression: both operands must be integers.");
        }

        int value = ((IntValue) leftValue).value();
        int value1 = ((IntValue) rightValue).value();

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

    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typ1, typ2;
        typ1 = left.typecheck(typeEnv);
        typ2 = right.typecheck(typeEnv);

        if (typ1.equals(new IntType())) {
            if (typ2.equals(new IntType())) {
                return new BoolType(); // Relaționalele returnează Bool
            } else {
                throw new MyException("Operandul 2 nu este intreg");
            }
        } else {
            throw new MyException("Operandul 1 nu este intreg");
        }
    }
}
