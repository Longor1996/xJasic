package de.longor1996.jasic;

public interface Function
{
	String getFunctionName();
	Value callFunction(Expression[] arguments);
}