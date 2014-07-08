package de.longor1996.jasic;


/**
 * This is a single meaningful chunk of code. It is created by the tokenizer
 * and consumed by the parser.
 */
public class Token
{
    public Token(String text, TokenType type) {
        this.text = text;
        this.type = type;
    }
    
    @Override
	public String toString()
    {
    	return "Token:{Type:'"+this.type+"',Content:'"+this.text.replaceAll("[^\\p{L}]"," ")+"'}";
    }
    
    public final String text;
    public final TokenType type;
}