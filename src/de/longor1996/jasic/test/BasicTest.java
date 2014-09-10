package de.longor1996.jasic.test;

import org.junit.Test;

import de.longor1996.jasic.Jasic;

public class BasicTest {

	@Test
	public void test()
	{
		String code = "";
		
		code += "PRINT \"Hello, World!\"\n";
		code += "PRINT \"The Squareroot of 17 is: \" + sqrt(17)\n";
		code += "PRINT pow(2, 32) + 0.5\n";
		code += "LOL(\"HI!\")\n";
		code += '\n';
		
		System.out.print(code);
		System.out.println("===================");
		
		Jasic jasic = new Jasic();
		jasic.addBasicFunctions();
		
		try
		{
			jasic.interpret(code, null);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
	}

}
