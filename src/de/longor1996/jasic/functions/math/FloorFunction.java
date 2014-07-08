package de.longor1996.jasic.functions.math;

import de.longor1996.jasic.NumberValue;
import de.longor1996.jasic.functions.AbstractMathFunction;

public class FloorFunction extends AbstractMathFunction
{
	
	public FloorFunction()
	{
		super("floor");
	}
	
	@Override
	public NumberValue doMath(double[] args) {
		return new NumberValue(Math.floor(args[0]));
	}
	
}
