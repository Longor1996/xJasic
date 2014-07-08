package de.longor1996.jasic.functions.math;

import de.longor1996.jasic.NumberValue;
import de.longor1996.jasic.functions.AbstractMathFunction;

public class CeilFunction extends AbstractMathFunction
{
	
	public CeilFunction()
	{
		super("ceil");
	}
	
	@Override
	public NumberValue doMath(double[] args) {
		return new NumberValue(Math.ceil(args[0]));
	}
	
}
