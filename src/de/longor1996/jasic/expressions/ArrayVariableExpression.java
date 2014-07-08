package de.longor1996.jasic.expressions;

import de.longor1996.jasic.ArrayObjectValue;
import de.longor1996.jasic.Expression;
import de.longor1996.jasic.Jasic;
import de.longor1996.jasic.StringValue;
import de.longor1996.jasic.Value;

/**
 * A variable expression evaluates to the current value stored in that
 * variable.
 */
public class ArrayVariableExpression implements Expression {

	/**
	 * 
	 */
	private final Jasic jasic;
	public ArrayVariableExpression(Jasic jasic, String name, Expression index) {
        this.jasic = jasic;
		this.name = name;
        this.index = index;
    }
    
    @Override
	public Value evaluate() {
    	Value pre_v = this.jasic.variables.get(this.name);
    	
    	if(pre_v instanceof ArrayObjectValue) {
			return ((ArrayObjectValue) pre_v).content[(int) this.index.evaluate().toNumber()];
		}
    	
    	if(pre_v == null) {
			return new StringValue("[VALUE_NOT_FOUND]");
		}
    	
        return new StringValue("[NOT_AN_ARRAY]");
    }
    
    private final String name;
    private final Expression index;
}