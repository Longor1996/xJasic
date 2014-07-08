package de.longor1996.jasic;

import java.util.Arrays;

public class ArrayObjectValue implements Value
{
	public Value[] content;
	
	public ArrayObjectValue(int length)
	{
		this.content = new Value[length];
		Arrays.fill(this.content, new NumberValue(0));
	}
	
	public void set(int number, Value evaluate) {
		this.content[number] = evaluate;
	}

	@Override
	public Value evaluate() {
		return new NumberValue(this.content.length);
	}
	
	@Override
	public double toNumber() {
		return this.content.length;
	}
	
	@Override
	public String toString()
	{
		return "array[" + this.content.length + "]";
	}
	
}