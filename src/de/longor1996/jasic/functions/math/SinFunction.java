package de.longor1996.jasic.functions.math;

import de.longor1996.jasic.NumberValue;
import de.longor1996.jasic.functions.AbstractMathFunction;

public class SinFunction extends AbstractMathFunction
{
	
	public SinFunction()
	{
		super("sin");
	}
	
	@Override
	public NumberValue doMath(double[] args) {
		return new NumberValue(Math.sin(args[0]));
	}
	
}
