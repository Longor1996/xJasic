package de.longor1996.jasic.statements.math;

import de.longor1996.jasic.Expression;
import de.longor1996.jasic.Jasic;
import de.longor1996.jasic.Value;
import de.longor1996.jasic.expressions.OperatorExpression;
import de.longor1996.jasic.statements.BasicMathStatement;

public class Pow extends BasicMathStatement
{
	public Pow(Jasic jasic, String output, Expression inputL, Expression inputR) {
		super(jasic, output, new OperatorExpression(inputL, 'P', inputR));
	}
	
	@Override public Value calc(Value evaluationResult) {
		return evaluationResult;
	}
}