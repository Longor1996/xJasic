package de.longor1996.jasic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.longor1996.jasic.Statement.EmptyStatement;
import de.longor1996.jasic.expressions.*;
import de.longor1996.jasic.statements.*;

/**
 * This defines the Jasic parser. The parser takes in a sequence of tokens
 * and generates an abstract syntax tree. This is the nested data structure
 * that represents the series of statements, and the expressions (which can
 * nest arbitrarily deeply) that they evaluate. In technical terms, what we
 * have is a recursive descent parser, the simplest kind to hand-write.
 *
 * As a side-effect, this phase also stores off the line numbers for each
 * label in the program. It's a bit gross, but it works.
 */
public class Parser
{
	/**
	 * 
	 */
	private final Jasic jasic;
	
	public Parser(Jasic jasic, List<Token> tokens, Map<String, IStatementParser> statements)
	{
        this.jasic = jasic;
		this.tokens = tokens;
        this.position = 0;
        this.statementParsers = statements;
    }
    
    /**
     * The top-level function to start parsing. This will keep consuming
     * tokens and routing to the other parse functions for the different
     * grammar syntax until we run out of code to parse.
     * 
     * @param  labels   A map of label names to statement indexes. The
     *                  parser will fill this in as it scans the code.
     * @return          The list of parsed statements.
     */
    public List<Statement> parse(Map<String, Integer> labels)
    {
    	System.out.println("[xJasic.Parser] Parsing... ");
        List<Statement> statements = new ArrayList<Statement>();
        int lineNumber = 1;
        
        while (true) {
            // Ignore empty lines.
            while (this.match(TokenType.LINEBREAK)) {
				lineNumber++;
			}
            
            // Parse Instruction, check if method returns null, and stop interpreting if needed
            if(this.parseNextInstruction(statements, lineNumber) == null)
            {
            	if(this.last(1).type == TokenType.LINEBREAK)
            	{
                	System.out.println("[xJasic.Parser] Done!");
                	break;
            	}
            	
            	System.out.println("[xJasic.Parser] Unexpected Token, at token " + this.position + ", at "+this.last(1)+".");
            	break; // Unexpected token, so end.
            }
        }
        
        return statements;
    }
    
    public Statement parseNextInstruction(List<Statement> statements, int lineNumber)
    {
        Statement out = null;
    	
        if (this.match(TokenType.LABEL))
        {
			// Mark the index of the statement after the label.
            this.jasic.labels.put(this.last(1).text, statements.size());
            out = new EmptyStatement();
        }
        
		else if (this.match(TokenType.WORD, TokenType.EQUALS))
		{
            String name = this.last(2).text;
            Expression value = this.expression();
            statements.add(out = new AssignStatement(this.jasic, name, value));
        }
		else if (this.match(TokenType.WORD, TokenType.LEFT_BRACKET))
		{
            String name = this.last(2).text;
            Expression indexExp = this.expression();
            this.consume(TokenType.RIGHT_BRACKET);
            this.consume(TokenType.EQUALS);
            Expression valueExp = this.expression();
            
            statements.add(out = new ArrayAssignStatement(this.jasic, name, indexExp, valueExp));
		}
        
		else if (this.matchLCT("print")) {
			statements.add(out = new PrintStatement(this.expression()));
		} else if (this.matchLCT("invoke")) {
			statements.add(out = new InvokeNativeStatement(this.jasic, this.expression(), Integer.valueOf(this.consume(TokenType.NUMBER).text)));
		} else if (this.matchLCT("push")) {
			statements.add(out = new StackPushStatement(this.jasic, this.expression()));
		} else if (this.matchLCT("pop")) {
			statements.add(out = new StackPopStatement(this.jasic, this.consume(TokenType.WORD).text));
		} else if (this.matchLCT("peek")) {
			statements.add(out = new StackPeekStatement(this.jasic, this.consume(TokenType.WORD).text));
		} else if (this.matchLCT("end")) {
			statements.add(out = new EndStatement());
		} else if (this.matchLCT("system")) {
			statements.add(out = new EndStatement());
		} else if (this.matchLCT("input")) {
			statements.add(out = new InputStatement(this.jasic, this.consume(TokenType.WORD).text));
		} else if (this.matchLCT("let")) {
			statements.add(out = new LetStatement(this.jasic, this));
		} else if (this.matchLCT("letarray")) {
			statements.add(out = new LetArrayStatement(this.jasic, this));
		} else if (this.matchLCT("free")) {
			statements.add(out = new DeleteStatement(this.jasic, this.consume(TokenType.WORD).text));
		} else if (this.matchLCT("goto")) {
			statements.add(out = new GotoStatement(this.jasic, this.consume(TokenType.WORD).text));
		} else if (this.matchLCT("if")) {
            Expression condition = this.expression();
            this.consumeLCT("then");
            statements.add(out = new IfThenStatement(this.jasic, condition, this.parseNextInstruction(new ThrowList<Statement>(), lineNumber)));
        } else if(this.match(TokenType.WORD, TokenType.LEFT_PAREN)) {
    		String name = this.last(2).text;
    		ArrayList<Expression> parameters = new ArrayList<Expression>();
    		
    		if(!this.match(TokenType.RIGHT_PAREN))
    		{
        		do
        		{
        			Expression inputParameterExp = this.expression();
        			parameters.add(inputParameterExp);
        		}
        		while(this.match(TokenType.COMMA));
    		}
    		
    		this.consume(TokenType.RIGHT_PAREN);
			statements.add(out = new MethodCall(this.jasic, name, parameters));
		} else if (this.matchLCT("rem")) {
			;while(this.get(0).type != TokenType.LINEBREAK)
			{
				System.out.println("> " + this.get(0));
				this.position++;
			}
		} else {
			IStatementParser subParser = this.statementParsers.get(this.getStr(0));
			
			if(subParser != null)
			{
				out = subParser.parse(this);
				statements.add(out);
			}
			else
			{
				// We couldn't parse whatever it was, so stop parsing.
            	;return null;
			}
    	}
        
    	return out;
    }
    
    // The following functions each represent one grammatical part of the
    // language. If this parsed English, these functions would be named like
    // noun() and verb().
    
    /**
     * Parses a single expression. Recursive descent parsers start with the
     * lowest-precedent term and moves towards higher precedence. For Jasic,
     * binary operators (+, -, etc.) are the lowest.
     * 
     * @return The parsed expression.
     */
    public Expression expression() {
        return this.operator();
    }
    
    /**
     * Parses a series of binary operator expressions into a single
     * expression. In Jasic, all operators have the same predecence and
     * associate left-to-right. That means it will interpret:
     *    1 + 2 * 3 - 4 / 5
     * like:
     *    ((((1 + 2) * 3) - 4) / 5)
     * 
     * It works by building the expression tree one at a time. So, given
     * this code: 1 + 2 * 3, this will:
     * 
     * 1. Parse (1) as an atomic expression.
     * 2. See the (+) and start a new operator expression.
     * 3. Parse (2) as an atomic expression.
     * 4. Build a (1 + 2) expression and replace (1) with it.
     * 5. See the (*) and start a new operator expression.
     * 6. Parse (3) as an atomic expression.
     * 7. Build a ((1 + 2) * 3) expression and replace (1 + 2) with it.
     * 8. Return the last expression built.
     * 
     * @return The parsed expression.
     */
    public Expression operator() {
        Expression expression = this.atomic();
        
        // Keep building operator expressions as long as we have operators.
        while (this.match(TokenType.OPERATOR) ||
               this.match(TokenType.EQUALS)) {
            char operator = this.last(1).text.charAt(0);
            Expression right = this.atomic();
            expression = new OperatorExpression(expression, operator, right);
        }
        
        return expression;
    }
    
    /**
     * Parses an "atomic" expression. This is the highest level of
     * precedence and contains single literal tokens like 123 and "foo", as
     * well as parenthesized expressions.
     * 
     * @return The parsed expression.
     */
    public Expression atomic() {
    	Token c = this.get(0);
    	
    	
    	// ----- ARRAY ACCESS
    	if (this.match(TokenType.WORD, TokenType.LEFT_BRACKET))
    	{
    		String name = this.last(2).text;
    		Expression indexExp = this.expression();
    		this.consume(TokenType.RIGHT_BRACKET);
    		return new ArrayVariableExpression(this.jasic, name, indexExp);
    	}
    	
    	// ----- FUNCTION CALL
    	else if (this.match(TokenType.WORD, TokenType.LEFT_PAREN))
    	{
    		String name = this.last(2).text;
    		ArrayList<Expression> parameters = new ArrayList<Expression>();
    		
    		if(!this.match(TokenType.RIGHT_PAREN))
    		{
        		do
        		{
        			Expression inputParameterExp = this.expression();
        			parameters.add(inputParameterExp);
        		}
        		while(this.match(TokenType.COMMA));
    		}
    		
    		this.consume(TokenType.RIGHT_PAREN);
    		return new MethodCall(this.jasic, name, parameters);
    	}
    	
    	// ----- VARIABLE ACCESS
    	else if (this.match(TokenType.WORD)) {
            return new VariableExpression(this.jasic, this.last(1).text);
		}
    	
        // ----- STATIC NUMBER
    	else if (this.match(TokenType.NUMBER)) {
			return new NumberValue(Double.parseDouble(this.last(1).text));
		}
    	
        // ----- STATIC STRING
    	else if (this.match(TokenType.STRING)) {
			return new StringValue(this.last(1).text);
		}
    	
        // ----- INNER EXPRESSION
    	else if (this.match(TokenType.LEFT_PAREN)) {
            // The contents of a parenthesized expression can be any
            // expression. This lets us "restart" the precedence cascade
            // so that you can have a lower precedence expression inside
            // the parentheses.
            Expression expression = this.expression();
            this.consume(TokenType.RIGHT_PAREN);
            return expression;
        }
    	
        throw new RuntimeException("Couldn't parse :(  -> " + c);
    }

	/**
     * Consumes the next token if it's a word token with the given name.
     * 
     * @param  name  Expected name of the next word token.
     * @return       True if the token was consumed.
     */
    public boolean match(String name) {
        if (this.get(0).type != TokenType.WORD) {
			return false;
		}
        if (!this.get(0).text.equals(name)) {
			return false;
		}
        this.position++;
        return true;
    }
    
    public boolean match(String name, TokenType token1, TokenType token2) {
        if (this.get(0).type != token1) {
			return false;
		}
        if (!this.get(0).text.equals(name)) {
			return false;
		}
        if (this.get(1).type != token2) {
			return false;
		}
        this.position++;
        this.position++;
    	return true;
	}
    
    /**
     * Consumes the next token if it's a word token with the given name.
     * 
     * @param  name  Expected name of the next word token.
     * @return       True if the token was consumed.
     */
    public boolean matchLCT(String name) {
        if (this.get(0).type != TokenType.WORD) {
			return false;
		}
        if (!this.get(0).text.toLowerCase().equals(name)) {
			return false;
		}
        this.position++;
        return true;
    }
    
    /**
     * Consumes the next token if it's the given type.
     * 
     * @param  type  Expected type of the next token.
     * @return       True if the token was consumed.
     */
    public boolean match(TokenType type) {
        if (this.get(0).type != type) {
			return false;
		}
        this.position++;
        return true;
    }
    
    // The following functions are the core low-level operations that the
    // grammar parser is built in terms of. They match and consume tokens in
    // the token stream.
    
    /**
     * Consumes the next two tokens if they are the given type (in order).
     * Consumes no tokens if either check fails.
     * 
     * @param  type1 Expected type of the next token.
     * @param  type2 Expected type of the subsequent token.
     * @return       True if tokens were consumed.
     */
    public boolean match(TokenType type1, TokenType type2) {
        if (this.get(0).type != type1) {
			return false;
		}
        if (this.get(1).type != type2) {
			return false;
		}
        this.position += 2;
        return true;
    }
    
    /**
     * Consumes the next two tokens if they are the given type (in order).
     * Consumes no tokens if either check fails.
     * 
     * @param  type1 Expected type of the next token.
     * @param  type2 Expected type of the subsequent token.
     * @param  type3 Expected type of the subsequent subsequent token.
     * @return       True if tokens were consumed.
     */
    public boolean match(TokenType type1, TokenType type2, TokenType type3) {
        if (this.get(0).type != type1) {
			return false;
		}
        if (this.get(1).type != type2) {
			return false;
		}
        if (this.get(2).type != type2) {
			return false;
		}
        this.position += 3;
        return true;
    }
    
    /**
     * Consumes the next two tokens if they are the given type (in order).
     * Consumes no tokens if either check fails.
     * 
     * @param  type1 Expected type of the next token.
     * @param  type2 Expected type of the subsequent token.
     * @param  type3 Expected type of the subsequent subsequent token.
     * @param  type4 Expected type of the subsequent subsequent subsequent token.
     * @return       True if tokens were consumed.
     */
    public boolean match(TokenType type1, TokenType type2, TokenType type3, TokenType type4) {
        if (this.get(0).type != type1) {
			return false;
		}
        if (this.get(1).type != type2) {
			return false;
		}
        if (this.get(2).type != type2) {
			return false;
		}
        if (this.get(3).type != type2) {
			return false;
		}
        this.position += 4;
        return true;
    }
    
    /**
     * Consumes the next token if it's the given type. If not, throws an
     * exception. This is for cases where the parser demands a token of a
     * certain type in a certain position, for example a matching ) after
     * an opening (.
     * 
     * @param  type  Expected type of the next token.
     * @return       The consumed token.
     */
    public Token consume(TokenType type) {
        if (this.get(0).type != type) {
			throw new RuntimeException("Expected " + type + ".");
		}
        return this.tokens.get(this.position++);
    }
    
    public Token[] consumeMultiple(TokenType... types) {
    	Token[] out = new Token[types.length];
    	
    	for(int i = 0; i < types.length; i++) {
			if(this.get(i).type != types[i]) {
				throw new RuntimeException("Expected " + types[i] + " as "+i+"th type, but got "+this.get(i).type+".");
			} else {
				out[i] = this.get(i);
			}
		}
    	
    	return out;
    }
    
    public Token consume(TokenType type1, TokenType type2) {
        if (this.get(0).type != type1) {
			throw new RuntimeException("Expected " + type1 + ".");
		}
        if (this.get(1).type != type2) {
			throw new RuntimeException("Expected " + type2 + ".");
		}
        Token t = this.tokens.get(this.position++);
        this.position++;
        return t;
    }
    
    /**
     * Consumes the next token if it's a word with the given name. If not,
     * throws an exception.
     * 
     * @param  name  Expected name of the next word token.
     * @return       The consumed token.
     */
    public Token consume(String name) {
        if (!this.match(name)) {
			throw new RuntimeException("Expected " + name + ".");
		}
        return this.last(1);
    }
    
    /**
     * Consumes the next token if it's a word with the given name. If not,
     * throws an exception.
     * 
     * @param  name  Expected name of the next word token.
     * @return       The consumed token.
     */
    public Token consumeLCT(String name) {
        if (!this.matchLCT(name)) {
			throw new RuntimeException("Expected " + name + ".");
		}
        return this.last(1);
    }

    /**
     * Gets a previously consumed token, indexing backwards. last(1) will
     * be the token just consumed, last(2) the one before that, etc.
     * 
     * @param  offset How far back in the token stream to look.
     * @return        The consumed token.
     */
    public Token last(int offset) {
        return this.tokens.get(this.position - offset);
    }
    
    /**
     * Gets an unconsumed token, indexing forward. get(0) will be the next
     * token to be consumed, get(1) the one after that, etc.
     * 
     * @param  offset How far forward in the token stream to look.
     * @return        The yet-to-be-consumed token.
     */
    public Token get(int offset) {
        if ((this.position + offset) >= this.tokens.size()) {
			return new Token("", TokenType.EOF);
		}
        
        return this.tokens.get(this.position + offset);
    }
    
    public String getStr(int offset)
    {
        if ((this.position + offset) >= this.tokens.size()) {
			return "";
		}
        
        return this.tokens.get(this.position + offset).text;
    }
    
    private final Map<String, IStatementParser> statementParsers;
    private final List<Token> tokens;
    private int position;
}