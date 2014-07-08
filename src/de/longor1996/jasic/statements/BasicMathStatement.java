package de.longor1996.jasic.statements;

import de.longor1996.jasic.Expression;
import de.longor1996.jasic.Jasic;
import de.longor1996.jasic.Statement;
import de.longor1996.jasic.Value;

public abstract class BasicMathStatement implements Statement
{
	private final Jasic jasic;
	private final String var;
	private final Expression exp;
	
	public BasicMathStatement(Jasic jasic, String output, Expression input)
	{
		this.jasic = jasic;
		this.var = output;
		this.exp = input;
		
	}
	
	@Override public boolean execute() {
		this.jasic.setVariable(this.var,this.calc(this.exp.evaluate()));
		return true;
	}
	
	public abstract Value calc(Value evaluationResult);
}