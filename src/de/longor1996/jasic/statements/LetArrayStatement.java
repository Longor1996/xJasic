package de.longor1996.jasic.statements;

import de.longor1996.jasic.ArrayObjectValue;
import de.longor1996.jasic.Expression;
import de.longor1996.jasic.Jasic;
import de.longor1996.jasic.Parser;
import de.longor1996.jasic.Statement;
import de.longor1996.jasic.Token;
import de.longor1996.jasic.TokenType;

/**
 * ???
 */
public class LetArrayStatement implements Statement {
	/**
	 * 
	 */
	private final Jasic jasic;
	String arrayName;
	Expression arrayLengthExp;
	
    public LetArrayStatement(Jasic jasic, Parser parser) {
    	this.jasic = jasic;
		Token tVarName = parser.consume(TokenType.WORD);
		this.arrayName = tVarName.text;
		parser.consume(TokenType.COMMA);
		this.arrayLengthExp = parser.expression();
    	
    }
    
    @Override
	public boolean execute() {
    	this.jasic.variables.put(this.arrayName, new ArrayObjectValue((int) Math.floor(this.arrayLengthExp.evaluate().toNumber())));
    	return true;
    }
    
}