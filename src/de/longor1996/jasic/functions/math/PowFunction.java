package de.longor1996.jasic.functions.math;

import de.longor1996.jasic.NumberValue;
import de.longor1996.jasic.functions.AbstractMathFunction;

public class PowFunction extends AbstractMathFunction
{
	
	public PowFunction()
	{
		super("pow");
	}
	
	@Override
	public NumberValue doMath(double[] args) {
		return new NumberValue(Math.pow(args[0], args[1]));
	}
	
}
