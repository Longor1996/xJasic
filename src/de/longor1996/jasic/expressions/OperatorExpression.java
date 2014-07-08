package de.longor1996.jasic.expressions;

import de.longor1996.jasic.Expression;
import de.longor1996.jasic.NumberValue;
import de.longor1996.jasic.StringValue;
import de.longor1996.jasic.Value;

/**
 * An operator expression evaluates two expressions and then performs some
 * arithmetic operation on the results.
 */
public class OperatorExpression implements Expression {
    public OperatorExpression(Expression left, char operator,
                              Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }
    
    @Override
	public Value evaluate() {
        Value leftVal = this.left.evaluate();
        Value rightVal = this.right.evaluate();
        
        switch (this.operator) {
        case '=':
            // Coerce to the left argument's type, then compare.
            if (leftVal instanceof NumberValue) {
				return new NumberValue((leftVal.toNumber() ==
                                        rightVal.toNumber()) ? 1 : 0);
			} else {
				return new NumberValue(leftVal.toString().equals(
                                       rightVal.toString()) ? 1 : 0);
			}
        case '+':
            // Addition if the left argument is a number, otherwise do
            // string concatenation.
            if (leftVal instanceof NumberValue) {
				return new NumberValue(leftVal.toNumber() + rightVal.toNumber());
			} else {
				return new StringValue(leftVal.toString() + rightVal.toString());
			}
        case '-':
            return new NumberValue(leftVal.toNumber() - rightVal.toNumber());
        case '*':
            return new NumberValue(leftVal.toNumber() * rightVal.toNumber());
        case '/':
            return new NumberValue(leftVal.toNumber() / rightVal.toNumber());
        case 'P':
            return new NumberValue(Math.pow(leftVal.toNumber(),rightVal.toNumber()));
        case '<':
            // Coerce to the left argument's type, then compare.
            if (leftVal instanceof NumberValue) {
				return new NumberValue((leftVal.toNumber() < rightVal.toNumber()) ? 1 : 0);
			} else {
				return new NumberValue((leftVal.toString().compareTo(rightVal.toString()) < 0) ? 1 : 0);
			}
        case '>':
            // Coerce to the left argument's type, then compare.
            if (leftVal instanceof NumberValue) {
				return new NumberValue((leftVal.toNumber() > rightVal.toNumber()) ? 1 : 0);
			} else {
				return new NumberValue((leftVal.toString().compareTo(rightVal.toString()) > 0) ? 1 : 0);
			}
        case '&':
        	return new NumberValue((int)leftVal.toNumber() & (int)rightVal.toNumber(), true);
        case '|':
        	return new NumberValue((int)leftVal.toNumber() | (int)rightVal.toNumber(), true);
        case '^':
        	return new NumberValue((int)leftVal.toNumber() ^ (int)rightVal.toNumber(), true);
        }
        throw new Error("Unknown operator.");
    }
    
    private final Expression left;
    private final char operator;
    private final Expression right;
}