package de.longor1996.jasic.statements;

import de.longor1996.jasic.Expression;
import de.longor1996.jasic.Jasic;
import de.longor1996.jasic.Statement;

/**
 * An if then statement jumps execution to another place in the program, but
 * only if an expression evaluates to something other than 0.
 */
public class IfThenStatement implements Statement {
    /**
	 * 
	 */
	private final Jasic jasic;
	public IfThenStatement(Jasic jasic, Expression condition, String label) {
        this.jasic = jasic;
		this.condition = condition;
        this.label = label;
        this.statement = null;
    }
    
    public IfThenStatement(Jasic jasic, Expression condition, Statement statement) {
        this.jasic = jasic;
		this.condition = condition;
        this.label = null;
        this.statement = statement;
    }
    
    @Override
	public boolean execute() {
		double value = this.condition.evaluate().toNumber();
    	
		if (value != 0)
    	{
        	if(this.statement != null)
        	{
        		;return this.statement.execute();
        	}
        	else
        	{
        		if (this.jasic.labels.containsKey(this.label)) {
			    		this.jasic.currentStatement = this.jasic.labels.get(this.label).intValue();;
        		};
        	};
    	}
    	
        return true;
    }
    
    private final Expression condition;
    private final Statement statement;
    private final String label;
}