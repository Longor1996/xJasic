package de.longor1996.jasic.statements;

import de.longor1996.jasic.ArrayObjectValue;
import de.longor1996.jasic.Expression;
import de.longor1996.jasic.Jasic;
import de.longor1996.jasic.Statement;
import de.longor1996.jasic.Value;

public class ArrayAssignStatement implements Statement {
    /**
	 * 
	 */
	private final Jasic jasic;
	public ArrayAssignStatement(Jasic jasic, String name, Expression index, Expression value) {
        this.jasic = jasic;
		this.name = name;
        this.index = index;
        this.value = value;
    }
    
    @Override
	public boolean execute() {
    	Value pre_v = this.jasic.variables.get(this.name);
    	
    	if(pre_v == null)
    	{
        	System.err.println("[ARRAY_VALUE_NOT_FOUND]");
            return false;
    	}
    	
    	if(pre_v instanceof ArrayObjectValue)
    	{
    		ArrayObjectValue v = (ArrayObjectValue) pre_v;
    		v.set((int)this.index.evaluate().toNumber(), this.value.evaluate());
    		return true;
    	}
    	else
    	{
    		System.err.println("[VALUE_IS_NOT_ARRAY]");
        	return false;
    	}
    	
    }

    private final String name;
    private final Expression index;
    private final Expression value;
}