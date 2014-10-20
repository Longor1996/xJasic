package de.longor1996.jasic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import de.longor1996.jasic.functions.TimeFunction;
import de.longor1996.jasic.functions.math.*;

/**
 * <pre>
 * This defines a single class that contains an entire interpreter for a
 * language very similar to the original BASIC. Everything is here (albeit in
 * very simplified form): tokenizing, parsing, and interpretation. The file is
 * organized in phases, with each appearing roughly in the order that they
 * occur when a program is run. You should be able to read this top-down to walk
 * through the entire process of loading and running a program.
 * 
 * Jasic language syntax
 * ---------------------
 * 
 * Comments start with ' and proceed to the end of the line:
 * 
 *     print "hi there" ' this is a comment
 * 
 * Numbers and strings are supported. Strings should be in "double quotes", and
 * only positive floating point numbers can be parsed (though numbers are signed internally).
 * 
 * Variables are identified by name which must start with a letter and can
 * contain letters or numbers or underscores. Case is significant for names and keywords.
 * 
 * Each statement is on its own line. Optionally, a line may have a label before
 * the statement. A label is a name that ends with a colon:
 * 
 *     foo:
 * 
 * 
 * The following statements are supported:
 * 
 * {name} = {expression}
 *     Evaluates the expression and assigns the result to the given named
 *     variable. All variables are globally scoped.
 * 
 *     pi = (314159 / 10000)
 * 
 * {name}[{expression}] = {expression}
 *     Evaluates the right expression and assigns the result to the given named
 *     variable array, at the index that is evaluated from the left expression.
 *     All variables are globally scoped.
 * 
 *     numbers[2*2] = (1 / 314159)
 * 
 * let {name} = {expression}
 *     Evaluates the expression and assigns the result to the given named
 *     variable. The variable will be overwritten by this if it exists.
 *     If the variable does not exist, it will be created.
 * 
 * free {name}
 *     Removes the variable with the given name. The variable WILL be gone after calling this.
 *     If the variable does not exist already, nothing will happen.
 * 
 * 
 * print {expression}
 *     Evaluates the expression and prints the result.
 * 
 *     print "hello, " + "world"
 * 
 * input {name}
 *     Reads in a line of input from the user and stores it in the variable with
 *     the given name.
 * 
 *     input guess
 * 
 * goto {label}
 *     Jumps to the statement after the label with the given name.
 * 
 *     goto loop
 * 
 * if {expression} then {STATEMENT}
 *     Evaluates the expression. If it evaluates to a non-zero number, then
 *     executes the statement after the 'then'.
 * 
 *     if a < b then print "a is smaller then b"
 * 
 * end
 *     Stops the script.
 * 
 * system
 *     Stops the script.
 * 
 * {name}({expression}, {expression}, {expression}, ...)
 *     A name as a statement, followed by any count of expressions delimeted by COMMAs, enclosed in parentheses,
 *     will act as a function-call. There is no way yet to define functions within the code.
 * 
 * 
 * The following expressions are supported:
 * 
 * {expression} = {expression}
 *     Evaluates to 1 if the two expressions are equal, 0 otherwise.
 * 
 * {expression} + {expression}
 *     If the left-hand expression is a number, then adds the two expressions,
 *     otherwise concatenates the two strings.
 * 
 * {expression} - {expression}
 * {expression} * {expression}
 * {expression} / {expression}
 * {expression} < {expression}
 * {expression} > {expression}
 *     You can figure it out.
 * 
 * {name}
 *     A name in an expression simply returns the value of the variable with
 *     that name. If the variable was never set, it defaults to 0.
 * 
 * {name}[{expression}]
 *     A name in an expression with brackets with an expression inside, simply returns the value of the variable-array with
 *     that name at the given evaluated index. If the variable was never set, it defaults to 0.
 * 
 * {name}({expression}, {expression}, {expression}, ...)
 *     A name in an expression, followed by any count of expressions delimeted by COMMAs, enclosed in parentheses,
 *     will act as a function-call.
 * 
 * All binary operators have the same precedence. Sorry, I had to cut corners
 * somewhere.
 * 
 * To keep things simple, I've omitted some stuff or hacked things a bit. When
 * possible, I'll leave a "HACK" note there explaining what and why. If you
 * make your own interpreter, you'll want to address those.
 * </pre><hr>
 * 
 * Information:<br>
 * <b>
 * This class was modified (a lot) by 'Longor1996', so it can be used to script against a java interface.<br>
 * There are also many additions to make the entire thing even more dynamic, and powerful,<br>
 * while still mostly keeping the contract of everything being in one class.<br><br>
 * All additions in a list:<br>
 * <ul>
 * <li>Completely refactored the whole thing.</li>
 * <li>All classes are in their own files.</li>
 * <li>Added support for arrays.</li>
 * <li>Added support to call Java methods from inside the sandbox using reflection.</li>
 * <li>Added some important math functions.</li>
 * <li>Added a stack to the emulator, with statements to modify it.</li>
 * <li>Added new statements to better control memory.</li>
 * <li>Changed the 'if' handling, so any expression and statement can be used.</li>
 * <li>Added ability to directly type floating point numbers into the code.</li>
 * <li>Variable names can now consists of letters, numbers, and underscores.</li>
 * <li>Refined the parser, so it can do more awesome stuff.</li>
 * <li>Fixed some serious bugs.</li>
 * </ul>
 * <br>
 * (Nearly) All added commands:<br>
 * <ul>
 * <li>letarray {var-name} {length}, creates a new array with the given name and size.</li>
 * <li>push {variable}, pushes a variable onto the stack.</li>
 * <li>pop {variable}, pops a variable from the stack, into the specified variable.</li>
 * <li>peek {variable}, peeks a variable from the stack, into the specified variable.</li>
 * <li>sin,cos,tan,asin,acos,atan,sqrt,cbrt,pow,log,abs,floor,ceil - Math Functions</li>
 * <li>rem, line comment starter.</li>
 * <li>invoke {method-name} {method-parameter-count}, pops the given amount of values from the stack,
 *     then calls the method with the given name with these.</li>
 * <li> And many more small changes... </li>
 * </ul>
 * <br>
 * </b>
 * <br>
 * 
 * @author 'Bob Nystrom' & 'L Longor K'
 */
public class Jasic
{
	
    /**
     * Runs the interpreter as a command-line app. Takes one argument: a path
     * to a script file to load and run. The script should contain one
     * statement per line.
     * 
     * @param args Command-line arguments.
     */
	/*
    public static void main(String[] args) {
        // Just show the usage and quit if a script wasn't provided.
        if (args.length != 1) {
            System.out.println("Usage: jasic <script>");
            System.out.println("Where <script> is a relative path to a .jas script to run.");
            return;
        }
        
        // Read the file.
        String contents = readFile(args[0]);
        
        // Run it.
        Jasic jasic = new Jasic();
        jasic.interpret(contents);
    }
    */
    
    // Interpreter -------------------------------------------------------------
    
    /**
     * Constructs a new Jasic instance. The instance stores the global state of
     * the interpreter such as the values of all of the variables and the
     * current statement.
     */
    public Jasic() {
        this.variables = new HashMap<String, Value>();
        this.functions = new HashMap<String, Function>();
        this.labels = new HashMap<String, Integer>();
        this.statements = new HashMap<String, IStatementParser>();
        
        InputStreamReader converter = new InputStreamReader(System.in);
        this.lineIn = new BufferedReader(converter);
    }
    
    public void addSpecialStatement(String key, IStatementParser value)
    {
		this.statements.put(key, value);
	}
    
    public void addFunction(Function value)
    {
		this.functions.put(value.getFunctionName(), value);
	}
    
    public void addVariable(String name, Value value)
    {
		this.variables.put(name, value);
	}
    
    public void addBasicFunctions()
    {
    	this.addFunction(new TimeFunction());
    	
    	this.addFunction(new AbsFunction());
    	this.addFunction(new ACosFunction());
    	this.addFunction(new ASinFunction());
    	this.addFunction(new ATanFunction());
    	this.addFunction(new CbrtFunction());
    	this.addFunction(new CeilFunction());
    	this.addFunction(new CosFunction());
    	this.addFunction(new FloorFunction());
    	this.addFunction(new LogFunction());
    	this.addFunction(new LogTenFunction());
    	this.addFunction(new PowFunction());
    	this.addFunction(new SinFunction());
    	this.addFunction(new SqrtFunction());
    	this.addFunction(new TanFunction());
    }
    
    /**
     * This is where the magic happens. This runs the code through the parsing
     * pipeline to generate the AST. Then it executes each statement. It keeps
     * track of the current line in a member variable that the statement objects
     * have access to. This lets "goto" and "if then" do flow control by simply
     * setting the index of the current statement.
     *
     * In an interpreter that didn't mix the interpretation logic in with the
     * AST node classes, this would be doing a lot more work.
     * 
     * @param source A string containing the source code of a .jas script to
     *               interpret.
     */
    public void interpret(String source, Object nativeObject)
    {
        // Tokenize.
        List<Token> tokens = Tokenizer.tokenize(source.concat("\n\n"));
        
        // Parse.
        Parser parser = new Parser(this, tokens, this.statements);
        List<Statement> statements = parser.parse(this.labels);
        
        // Interpret until we're done.
        this.currentStatement = 0;
        this.interpreterStack = new Stack<Value>();
        this.nativeObject = nativeObject;
        this.nativeObjectClass = nativeObject != null ? nativeObject.getClass() : null;
    	
        // Add Timer Variable
        long START = System.currentTimeMillis();
        NumberValue TIMER = new NumberValue(System.currentTimeMillis()-START);
        this.variables.put("TIME", TIMER);
        
        while (this.currentStatement < statements.size()){
        	// PRE STATEMENT
        	TIMER.value = System.currentTimeMillis()-START;
        	
            int thisStatement = this.currentStatement;
            this.currentStatement++;
            
            // STATEMENT
            boolean $continue$ = statements.get(thisStatement).execute();
            
            // BREAK-CHECK
            if(!$continue$)
            {
				break;
			}
            
        }
        
    }
    
	public void setVariable(String var, Value calc) {
		this.variables.put(var, calc);
	}
    
	public Value getVariable(String var)
	{
		return this.variables.get(var);
	}
	
	public final Map<String,IStatementParser> statements;
	public final Map<String, Value> variables;
    public final Map<String, Function> functions;
    public final Map<String, Integer> labels;
    
    public final BufferedReader lineIn;
    
    public int currentStatement;
    
    public Object nativeObject;
    public Class<?> nativeObjectClass;
    public Stack<Value> interpreterStack;
    
    
    
    
    
    
    
    
    
    
    
    // Utility stuff -----------------------------------------------------------
    
    /**
     * Reads the file from the given path and returns its contents as a single
     * string.
     * 
     * @param  path  Path to the text file to read.
     * @return       The contents of the file or null if the load failed.
     * @throws       IOException
     */
    public static String readFile(String path)
    {
        try {
            FileInputStream stream = new FileInputStream(path);
            
            try {
                InputStreamReader input = new InputStreamReader(stream, Charset.defaultCharset());
                Reader reader = new BufferedReader(input);
                
                StringBuilder builder = new StringBuilder();
                char[] buffer = new char[8192];
                int read;
                
                while ((read = reader.read(buffer, 0, buffer.length)) > 0) {
					builder.append(buffer, 0, read);
				}
                
                // HACK: The parser expects every statement to end in a newline,
                // even the very last one, so we'll just tack one on here in
                // case the file doesn't have one.
                builder.append("\n");
                
                return builder.toString();
            } finally {
                stream.close();
            }
        } catch (IOException ex) {
            return null;
        }
    }
    
    /**
     * Reads the given file and returns its contents as a single
     * string.
     * 
     * @param  file  The file to read.
     * @return       The contents of the file or null if the load failed.
     * @throws       IOException
     */
    public static String readFile(File file)
    {
        try {
            FileInputStream stream = new FileInputStream(file);
            
            try {
                InputStreamReader input = new InputStreamReader(stream, Charset.defaultCharset());
                Reader reader = new BufferedReader(input);
                
                StringBuilder builder = new StringBuilder();
                char[] buffer = new char[8192];
                int read;
                
                while ((read = reader.read(buffer, 0, buffer.length)) > 0) {
					builder.append(buffer, 0, read);
				}
                
                // HACK: The parser expects every statement to end in a newline,
                // even the very last one, so we'll just tack two on here in
                // case the file doesn't have one.
                builder.append("\n");
                builder.append("\n");
                
                return builder.toString();
            } finally {
                stream.close();
            }
        } catch (IOException ex) {
            return null;
        }
    }
	
}
