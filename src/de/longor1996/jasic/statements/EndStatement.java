package de.longor1996.jasic.statements;

import de.longor1996.jasic.Statement;

/**
 * ???
 */
public class EndStatement implements Statement {
    public EndStatement() {
    	// Nothing to do here!
    }
    
    @Override
	public boolean execute() {
    	return false;
    }
}