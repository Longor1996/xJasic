package de.longor1996.jasic.functions.math;

import de.longor1996.jasic.NumberValue;
import de.longor1996.jasic.functions.AbstractMathFunction;

public class SqrtFunction extends AbstractMathFunction
{
	
	public SqrtFunction()
	{
		super("sqrt");
	}
	
	@Override
	public NumberValue doMath(double[] args) {
		return new NumberValue(Math.sqrt(args[0]));
	}
	
}
