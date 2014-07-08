package de.longor1996.jasic.functions.math;

import de.longor1996.jasic.NumberValue;
import de.longor1996.jasic.functions.AbstractMathFunction;

public class LogFunction extends AbstractMathFunction
{
	
	public LogFunction()
	{
		super("log");
	}
	
	@Override
	public NumberValue doMath(double[] args) {
		return new NumberValue(Math.log(args[0]));
	}
	
}
