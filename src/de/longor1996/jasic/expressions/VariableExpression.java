package de.longor1996.jasic.expressions;

import de.longor1996.jasic.Expression;
import de.longor1996.jasic.Jasic;
import de.longor1996.jasic.NumberValue;
import de.longor1996.jasic.Value;

/**
 * A variable expression evaluates to the current value stored in that
 * variable.
 */
public class VariableExpression implements Expression {
    /**
	 * 
	 */
	private final Jasic jasic;

	public VariableExpression(Jasic jasic, String name) {
        this.jasic = jasic;
		this.name = name;
    }
    
    @Override
	public Value evaluate() {
        if (this.jasic.variables.containsKey(this.name)) {
			return this.jasic.variables.get(this.name);
		}
        return new NumberValue(0);
    }
    
    private final String name;
}