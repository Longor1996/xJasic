package de.longor1996.jasic.functions.math;

import de.longor1996.jasic.NumberValue;
import de.longor1996.jasic.functions.AbstractMathFunction;

public class ATanFunction extends AbstractMathFunction
{
	
	public ATanFunction()
	{
		super("atan");
	}
	
	@Override
	public NumberValue doMath(double[] args) {
		return new NumberValue(Math.atan(args[0]));
	}
	
}
