package de.longor1996.jasic.functions;

import de.longor1996.jasic.Expression;
import de.longor1996.jasic.Function;
import de.longor1996.jasic.NumberValue;
import de.longor1996.jasic.Value;

public class TimeFunction implements Function
{
	
	@Override
	public String getFunctionName()
	{
		return "time";
	}
	
	@Override
	public Value callFunction(Expression[] arguments)
	{
		return new NumberValue(System.currentTimeMillis());
	}
	
}
