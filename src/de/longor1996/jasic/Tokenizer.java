package de.longor1996.jasic;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer
{

	// Many tokens are a single characters, like operators and ().
    public static final String charTokens = "\n=+-*/<>()&|^.,[]";
    public static final TokenType[] tokenTypes = {
    	TokenType.LINEBREAK,
    	TokenType.EQUALS,
    	TokenType.OPERATOR,
    	TokenType.OPERATOR,
    	TokenType.OPERATOR,
    	TokenType.OPERATOR,
    	TokenType.OPERATOR,
    	TokenType.OPERATOR,
    	TokenType.LEFT_PAREN,
    	TokenType.RIGHT_PAREN,
    	TokenType.OPERATOR,
    	TokenType.OPERATOR,
    	TokenType.OPERATOR,
    	TokenType.DOT,
    	TokenType.COMMA,
    	TokenType.LEFT_BRACKET,
    	TokenType.RIGHT_BRACKET
    };
    
    
    /**
     * This function takes a script as a string of characters and chunks it into
     * a sequence of tokens. Each token is a meaningful unit of program, like a
     * variable name, a number, a string, or an operator.
     */
    public static final List<Token> tokenize(String source)
    {
        List<Token> tokens = new ArrayList<Token>();
        
        StringBuilder token = new StringBuilder("");
        TokenizeState state = TokenizeState.NOT_TOKENIZING;
        
        
        
        // Scan through the code one character at a time, building up the list
        // of tokens.
        for (int i = 0; i < source.length(); i++)
        {
            char c = source.charAt(i);
            
            switch (state)
            {
	            case NOT_TOKENIZING:
	                if (charTokens.indexOf(c) != -1)
	                {
						tokens.add(new Token(Character.toString(c), tokenTypes[charTokens.indexOf(c)]));
					}
	                else if (Character.isLetter(c) || (c == '_'))
					{
	                    token.append(c);
	                    state = TokenizeState.WORD;
	                }
					else if (Character.isDigit(c))
					{
	                	token.append(c);
	                    state = TokenizeState.NUMBER;
	                }
					else if (c == '"')
					{
						state = TokenizeState.STRING;
					}
					else if (c == '\'')
					{
						state = TokenizeState.COMMENT;
					}
					
					break;
	            
	            case WORD:
	                if (isWordCharacter(c))
	                {
	                	token.append(c);
					}
	                else if (c == ':')
	                {
	                    tokens.add(new Token(token.toString(), TokenType.LABEL));
	                    token.setLength(0);
	                    state = TokenizeState.NOT_TOKENIZING;
	                }
	                else
	                {
	                    tokens.add(new Token(token.toString(), TokenType.WORD));
	                    token.setLength(0);
	                    state = TokenizeState.NOT_TOKENIZING;
	                    i--; // Reprocess this character in the default state.
	                }
	                break;
	            
	            case NUMBER:
	                // HACK: Negative numbers and floating points aren't supported.
	                // To get a negative number, just do 0 - <your number>.
	                // To get a floating point, divide.
	            	// CORRECTION: Now floating point is supported! - Longor1996
	                if (Character.isDigit(c) || ((c == '.') && !token.toString().contains(".")))
	                {
	                	token.append(c);
					}
	                else
	                {
	                    tokens.add(new Token(token.toString(), TokenType.NUMBER));
	                    token.setLength(0);
	                    state = TokenizeState.NOT_TOKENIZING;
	                    i--; // Reprocess this character in the default state.
	                }
	                break;
	            
	            case STRING:
	                if (c == '"')
	                {
	                    tokens.add(new Token(token.toString(), TokenType.STRING));
	                    token.setLength(0);
	                    state = TokenizeState.NOT_TOKENIZING;
	                }
	                else
	                {
	                	token.append(c);
					}
	                break;
	            
	            case COMMENT:
	                if (c == '\n')
	                {
						state = TokenizeState.NOT_TOKENIZING;
					}
	                break;
            }
        }
        
        // HACK: Silently ignore any in-progress token when we run out of
        // characters. This means that, for example, if a script has a string
        // that's missing the closing ", it will just ditch it.
        return tokens;
    }
    
    
    
    public static final boolean isWordCharacter(char c)
    {
    	switch(c)
    	{
    	// Known Characters!
	    	case '0':
	    	case '1':
	    	case '2':
	    	case '3':
	    	case '4':
	    	case '5':
	    	case '6':
	    	case '8':
	    	case '9':
	    	case 'a':
	    	case 'b':
	    	case 'c':
	    	case 'd':
	    	case 'e':
	    	case 'f':
	    	case 'g':
	    	case 'h':
	    	case 'i':
	    	case 'j':
	    	case 'k':
	    	case 'l':
	    	case 'm':
	    	case 'n':
	    	case 'o':
	    	case 'p':
	    	case 'q':
	    	case 'r':
	    	case 's':
	    	case 't':
	    	case 'u':
	    	case 'v':
	    	case 'w':
	    	case 'x':
	    	case 'y':
	    	case 'z':
	    	case 'A':
	    	case 'B':
	    	case 'C':
	    	case 'D':
	    	case 'E':
	    	case 'F':
	    	case 'G':
	    	case 'H':
	    	case 'I':
	    	case 'J':
	    	case 'K':
	    	case 'L':
	    	case 'M':
	    	case 'N':
	    	case 'O':
	    	case 'P':
	    	case 'Q':
	    	case 'R':
	    	case 'S':
	    	case 'T':
	    	case 'U':
	    	case 'V':
	    	case 'W':
	    	case 'X':
	    	case 'Y':
	    	case 'Z':
	    	case '_':
	    	case '$':
	    		return true;
	    
	    // Unknown Character!
	    	default:
	    		return false;
    	}
	}
    
}
