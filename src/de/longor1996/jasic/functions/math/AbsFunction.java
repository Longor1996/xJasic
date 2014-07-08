package de.longor1996.jasic.functions.math;

import de.longor1996.jasic.NumberValue;
import de.longor1996.jasic.functions.AbstractMathFunction;

public class AbsFunction extends AbstractMathFunction
{

	public AbsFunction()
	{
		super("abs");
	}

	@Override
	public NumberValue doMath(double[] args) {
		return new NumberValue(args[0] < 0 ? -args[0] : args[0]);
	}

}
