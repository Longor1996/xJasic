package de.longor1996.jasic.statements;

import de.longor1996.jasic.Expression;
import de.longor1996.jasic.Jasic;
import de.longor1996.jasic.Statement;

/**
 * An assignment statement evaluates an expression and stores the result in
 * a variable.
 */
public class AssignStatement implements Statement {
    /**
	 * 
	 */
	private final Jasic jasic;
	public AssignStatement(Jasic jasic, String name, Expression value) {
        this.jasic = jasic;
		this.name = name;
        this.value = value;
    }
    
    @Override
	public boolean execute() {
        this.jasic.variables.put(this.name, this.value.evaluate());
        return true;
    }

    private final String name;
    private final Expression value;
}