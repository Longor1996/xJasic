package de.longor1996.jasic.statements;

import java.io.IOException;

import de.longor1996.jasic.Jasic;
import de.longor1996.jasic.NumberValue;
import de.longor1996.jasic.Statement;
import de.longor1996.jasic.StringValue;

/**
 * An "input" statement reads input from the user and stores it in a
 * variable.
 */
public class InputStatement implements Statement {
    /**
	 * 
	 */
	private final Jasic jasic;

	public InputStatement(Jasic jasic, String name) {
        this.jasic = jasic;
		this.name = name;
    }
    
    @Override
	public boolean execute() {
        try {
            String input = this.jasic.lineIn.readLine();
            
            // Store it as a number if possible, otherwise use a string.
            try {
                double value = Double.parseDouble(input);
                this.jasic.variables.put(this.name, new NumberValue(value));
            } catch (NumberFormatException e) {
                this.jasic.variables.put(this.name, new StringValue(input));
            }
        } catch (IOException e1) {
            // HACK: Just ignore the problem.
        }
        
        return true;
    }

    private final String name;
}