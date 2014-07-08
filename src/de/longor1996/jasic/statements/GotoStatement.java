package de.longor1996.jasic.statements;

import de.longor1996.jasic.Jasic;
import de.longor1996.jasic.Statement;

/**
 * A "goto" statement jumps execution to another place in the program.
 */
public class GotoStatement implements Statement {
    /**
	 * 
	 */
	private final Jasic jasic;

	public GotoStatement(Jasic jasic, String label) {
        this.jasic = jasic;
		this.label = label;
    }
    
    @Override
	public boolean execute() {
        if (this.jasic.labels.containsKey(this.label)) {
			this.jasic.currentStatement = this.jasic.labels.get(this.label).intValue();
		}
        return true;
    }

    private final String label;
}