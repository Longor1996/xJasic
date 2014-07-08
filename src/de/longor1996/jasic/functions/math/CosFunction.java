package de.longor1996.jasic.functions.math;

import de.longor1996.jasic.NumberValue;
import de.longor1996.jasic.functions.AbstractMathFunction;

public class CosFunction extends AbstractMathFunction
{
	
	public CosFunction()
	{
		super("cos");
	}
	
	@Override
	public NumberValue doMath(double[] args) {
		return new NumberValue(Math.cos(args[0]));
	}
	
}
