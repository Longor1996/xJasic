package de.longor1996.jasic.expressions;

import java.lang.reflect.Method;
import java.util.Arrays;

import de.longor1996.jasic.Expression;
import de.longor1996.jasic.Jasic;
import de.longor1996.jasic.NumberValue;
import de.longor1996.jasic.Statement;
import de.longor1996.jasic.StringValue;
import de.longor1996.jasic.Value;

/**
 * A "invoke" statement takes a given string,
 * and uses it to try to call a native java method using the 'native object'.
 * 
 * @author Longor1996
 */
public class InvokeNativeStatement implements Statement {

	/**
	 * 
	 */
	private final Jasic jasic;
	public InvokeNativeStatement(Jasic jasic, Expression expression, Integer integer) {
        this.jasic = jasic;
		this.name = expression;
        this.pCount = integer;
    }
    
    @Override
	public boolean execute() {
    	String methodName = this.name.evaluate().toString();
		Object[] callParameter = null;
    	
    	System.out.println("InvokeNative -> " + methodName);
    	
    	if(this.jasic.nativeObject == null) {
			throw new Error("No native method providing object was given to the interpret() method!");
		}
    	
    	try {
    		Method[] methods = this.jasic.nativeObjectClass.getMethods();
    		Method nativeMethod = null;
    		
    		for(Method nMethod : methods) {
				if(nMethod.getName().equals(methodName)) {
					nativeMethod = nMethod;
				}
			}
    		
    		if(nativeMethod == null) {
				throw new Exception("Native Method '" + methodName + "' does not exist!");
			}
    		
    		// Evaluate the parameter count, and make absolutely sure to floor it,
    		// or else it might be rounded up while being casted.
    		int pCount = this.pCount.intValue();
    		
    		if(pCount > 0)
    		{
        		// Get all the expressions from the stack
        		Value[] params = new Value[pCount];
        		
        		while(pCount != 0) {
					params[--pCount] = this.jasic.interpreterStack.pop();
				}
        		
        		// Reverse the parameters (so we can do PUSH without having to reverse things)
        		for (int i = 0; i < (params.length / 2); i++) {
        			Value temp = params[i];
        			params[i] = params[params.length - 1 - i];
        			params[params.length - 1 - i] = temp;
        		}
        		
        		callParameter = params;
    			System.out.println("Param = " + Arrays.toString(params));
    		}
    		else
    		{
    			System.out.println("Param = NULL");
    			callParameter = new Object[0];
    		}
    		
			Object returnValue = nativeMethod.invoke(this.jasic.nativeObject, callParameter);
			System.out.println("Native Return: " + returnValue);
			
			// Transform the returnValue into a BASIC Value, and put it on the stack.
            try {
                double value = Double.parseDouble(""+returnValue);
                this.jasic.interpreterStack.add(new NumberValue(value));
            } catch (NumberFormatException e) {
            	this.jasic.interpreterStack.add(new StringValue(""+returnValue));
            }
            
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(
					"Something went wrong with a call of the native Method '" + methodName + "'!\n" +
					" -> Call:{Name:\""+methodName+"\", Parameters:<"+callParameter.length + ">" + Arrays.toString(callParameter) + "}"
			);
        	return false;
		}
    	
    	return true;
    }
    
	private final Expression name;
    private final Integer pCount;
}