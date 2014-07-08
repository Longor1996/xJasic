package de.longor1996.jasic;

import java.text.NumberFormat;

/**
 * A numeric value. Jasic uses doubles internally for all numbers.
 */
public class NumberValue implements Value
{
	private static final NumberFormat numberFormat = NumberFormat.getInstance();
	static
	{
		numberFormat.setMaximumFractionDigits(4);
		numberFormat.setMinimumFractionDigits(0);
		numberFormat.setMaximumIntegerDigits(100);
		numberFormat.setMinimumIntegerDigits(1);
		numberFormat.setGroupingUsed(false);
	}
	
	public NumberValue(double value)
	{
		this.value = value;
	}
	
	public NumberValue(double value, boolean binaryParameterFlag)
	{
		this.value = value != 0 ? 1 : 0;
	}
	
	@Override
	public String toString()
	{
		return numberFormat.format(this.value).replace(',', '.');
	}
	
	@Override
	public double toNumber()
	{
		return this.value;
	}
	
	@Override
	public Value evaluate()
	{
		return this;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o instanceof NumberValue) {
			if (((NumberValue) o).toNumber() == this.value)
			{
				return true;
			}
		}
		
		return false;
	}
	
	double value;
}