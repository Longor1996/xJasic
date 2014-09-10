package de.longor1996.jasic.statements;

import de.longor1996.jasic.Expression;
import de.longor1996.jasic.Statement;

/**
 * A "print" statement evaluates an expression, converts the result to a
 * string, and displays it to the user.
 */
public class PrintStatement implements Statement {
    public PrintStatement(Expression expression) {
        this.expression = expression;
    }
    
    @Override
	public boolean execute() {
        System.out.println(this.expression.evaluate().toString());
        return true;
    }
    
    private final Expression expression;
}