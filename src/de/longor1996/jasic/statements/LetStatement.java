package de.longor1996.jasic.statements;

import de.longor1996.jasic.Expression;
import de.longor1996.jasic.Jasic;
import de.longor1996.jasic.Parser;
import de.longor1996.jasic.Statement;
import de.longor1996.jasic.Token;
import de.longor1996.jasic.TokenType;

/**
 * ???
 */
public class LetStatement implements Statement {
	/**
	 * 
	 */
	private final Jasic jasic;
	String varName;
	Expression varExp;
	
    public LetStatement(Jasic jasic, Parser parser) {
    	this.jasic = jasic;
		Token tVarName = parser.consume(TokenType.WORD);
		this.varName = tVarName.text;
		parser.consume(TokenType.EQUALS);
		this.varExp = parser.expression();
    	
    }
    
    @Override
	public boolean execute() {
    	this.jasic.variables.put(this.varName, this.varExp.evaluate());
    	return true;
    }
}