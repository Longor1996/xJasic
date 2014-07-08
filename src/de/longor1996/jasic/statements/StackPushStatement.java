package de.longor1996.jasic.statements;

import de.longor1996.jasic.Expression;
import de.longor1996.jasic.Jasic;
import de.longor1996.jasic.Statement;
import de.longor1996.jasic.Value;

/**
 * ???
 */
public class StackPushStatement implements Statement {
    /**
	 * 
	 */
	private final Jasic jasic;

	public StackPushStatement(Jasic jasic, Expression expression) {
        this.jasic = jasic;
		this.expression = expression;
    }
    
    @Override
	public boolean execute() {
    	Value value = this.expression.evaluate();
    	System.out.println("PUSH " + value);
        this.jasic.interpreterStack.add(value);
        return true;
    }
    
    private final Expression expression;
}