package de.longor1996.jasic.functions.math;

import de.longor1996.jasic.NumberValue;
import de.longor1996.jasic.functions.AbstractMathFunction;

public class ACosFunction extends AbstractMathFunction
{
	
	public ACosFunction()
	{
		super("acos");
	}
	
	@Override
	public NumberValue doMath(double[] args) {
		return new NumberValue(Math.acos(args[0]));
	}
	
}
