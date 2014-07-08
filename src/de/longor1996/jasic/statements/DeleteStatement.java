package de.longor1996.jasic.statements;

import de.longor1996.jasic.Jasic;
import de.longor1996.jasic.Statement;

public class DeleteStatement implements Statement {
	/**
	 * 
	 */
	private final Jasic jasic;
	String varName;
	
    public DeleteStatement(Jasic jasic, String str) {
    	this.jasic = jasic;
		this.varName = str;
    }
	
    @Override
	public boolean execute()
    {
    	this.jasic.variables.remove(this.varName);
    	return true;
    }
    
}