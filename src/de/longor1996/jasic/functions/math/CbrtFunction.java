package de.longor1996.jasic.functions.math;

import de.longor1996.jasic.NumberValue;
import de.longor1996.jasic.functions.AbstractMathFunction;

public class CbrtFunction extends AbstractMathFunction
{
	
	public CbrtFunction()
	{
		super("cbrt");
	}
	
	@Override
	public NumberValue doMath(double[] args) {
		return new NumberValue(Math.cbrt(args[0]));
	}
	
}
