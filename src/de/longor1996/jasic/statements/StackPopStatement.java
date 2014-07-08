package de.longor1996.jasic.statements;

import de.longor1996.jasic.Jasic;
import de.longor1996.jasic.Statement;
import de.longor1996.jasic.Value;

/**
 * ???
 */
public class StackPopStatement implements Statement {
    /**
	 * 
	 */
	private final Jasic jasic;

	public StackPopStatement(Jasic jasic, String variableName) {
        this.jasic = jasic;
		this.variableName = variableName;
    }
    
    @Override
	public boolean execute() {
    	Value value = this.jasic.interpreterStack.pop();
    	this.jasic.variables.put(this.variableName, value);
    	return true;
    }
    
    private final String variableName;
}