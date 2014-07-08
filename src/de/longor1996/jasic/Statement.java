package de.longor1996.jasic;

/**
 * Base interface for a Jasic statement. The different supported statement
 * types like "print" and "goto" implement this.
 */
public interface Statement {
    /**
     * Statements implement this to actually perform whatever behavior the
     * statement causes. "print" statements will display text here, "goto"
     * statements will change the current statement, etc.
     * @return
     */
    boolean execute();
    
    public class EmptyStatement implements Statement
    {
		@Override public boolean execute() {
			return true;
		}
	}
}