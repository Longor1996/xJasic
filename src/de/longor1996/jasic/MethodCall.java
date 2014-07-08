package de.longor1996.jasic;

import java.util.ArrayList;

public class MethodCall implements Statement, Expression
{
	/**
	 * 
	 */
	private final Jasic jasic;
	final String functionName;
	final Expression[] functionArguments;
	Function function;
	
	public MethodCall(Jasic jasic, String name, ArrayList<Expression> parameters)
	{
		this.jasic = jasic;
		this.functionName = name;
		
		this.functionArguments = new Expression[parameters.size()];
		parameters.toArray(this.functionArguments);
	}
	
	private void resolveFunctionName()
	{
		Function func = this.jasic.functions.get(this.functionName);
		
		if(func == null)
		{
			return;
		}
		
		this.function = func;
	}
	
	@Override
	public Value evaluate()
	{
		if(this.function == null)
		{
			this.resolveFunctionName();
		}
		
		if(this.function == null)
		{
			// Return 'void'.
			return new StringValue("function not found: " + this.functionName);
		}
		
		return this.function.callFunction(this.functionArguments);
	}
	
	@Override
	public boolean execute()
	{
		this.evaluate();
		return true;
	}
	
}