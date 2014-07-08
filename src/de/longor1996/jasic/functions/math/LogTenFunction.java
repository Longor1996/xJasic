package de.longor1996.jasic.functions.math;

import de.longor1996.jasic.NumberValue;
import de.longor1996.jasic.functions.AbstractMathFunction;

public class LogTenFunction extends AbstractMathFunction
{
	
	public LogTenFunction()
	{
		super("log10");
	}
	
	@Override
	public NumberValue doMath(double[] args) {
		return new NumberValue(Math.log10(args[0]));
	}
	
}
