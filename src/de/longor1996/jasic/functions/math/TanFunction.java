package de.longor1996.jasic.functions.math;

import de.longor1996.jasic.NumberValue;
import de.longor1996.jasic.functions.AbstractMathFunction;

public class TanFunction extends AbstractMathFunction
{
	
	public TanFunction()
	{
		super("tan");
	}
	
	@Override
	public NumberValue doMath(double[] args) {
		return new NumberValue(Math.tan(args[0]));
	}
	
}
