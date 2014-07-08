package de.longor1996.jasic.functions.math;

import de.longor1996.jasic.NumberValue;
import de.longor1996.jasic.functions.AbstractMathFunction;

public class ASinFunction extends AbstractMathFunction
{
	
	public ASinFunction()
	{
		super("asin");
	}
	
	@Override
	public NumberValue doMath(double[] args) {
		return new NumberValue(Math.asin(args[0]));
	}
	
}
