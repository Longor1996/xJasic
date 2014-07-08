package de.longor1996.jasic.functions;

import de.longor1996.jasic.Expression;
import de.longor1996.jasic.Function;
import de.longor1996.jasic.NumberValue;

public abstract class AbstractMathFunction implements Function
{
	final String functionName;
	
	public AbstractMathFunction(String functionName)
	{
		this.functionName = functionName;
	}
	
	@Override
	public String getFunctionName()
	{
		return this.functionName;
	}
	
	@Override
	public NumberValue callFunction(Expression[] arguments)
	{
		double[] args = new double[arguments.length];
		
		for(int i = 0; i < args.length; i++)
		{
			args[i] = arguments[i].evaluate().toNumber();
		}
		
		return this.doMath(args);
	}
	
	public abstract NumberValue doMath(double[] args);
	
	
	
	
}
