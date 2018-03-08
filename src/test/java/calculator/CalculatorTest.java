package calculator;

import org.junit.Test;

import static org.junit.Assert.*;

public class CalculatorTest {
	
	private Calculator c = new Calculator();

    @Test public void testSimpleParsing() {
    	// numbers
    	assertEquals((Integer) 2, c.calculate("2"));
    	assertEquals((Integer) 0, c.calculate("0"));
    	assertEquals((Integer) 0, c.calculate("-0"));
    	assertEquals((Integer) (-2), c.calculate("-2"));
    	assertEquals("does not parse incorrectly-placed negative sign", 
    			null, c.calculate("2-"));
    	
    	assertEquals((Integer) Integer.MIN_VALUE, c.calculate(""+Integer.MIN_VALUE));
    	assertEquals((Integer) Integer.MAX_VALUE, c.calculate(""+Integer.MAX_VALUE));
    	
    	// arithmetic
        assertEquals((Integer) (1 + 2), c.calculate("add(1, 2"));
        assertEquals((Integer) (1 - 2), c.calculate("sub(1, 2"));
        assertEquals((Integer) (1 * 2), c.calculate("mult(1, 2"));
        assertEquals((Integer) (1 / 2), c.calculate("div(1, 2"));
        assertEquals("division by zero should return null", 
        		null, c.calculate("div(1, 0"));
        
        // let and variables
        assertEquals((Integer) 5, c.calculate("let(a, 5, a)"));
        assertEquals((Integer) 5, c.calculate("let(aZ, 5, aZ)"));
        assertEquals("variables should be case sensitive", 
        		null, c.calculate("let(A, 5, a)"));
        assertEquals("should reject non-alphabetic characters", 
        		null, c.calculate("let(a_a, 5, a_a)"));
    }
    
    @Test public void testCompoundParsing() {
    	assertEquals((Integer) 3, c.calculate("add(1, 2)"));
    	assertEquals((Integer) 7, c.calculate("add(1, mult(2, 3))"));
    	assertEquals((Integer) 12, c.calculate("mult(add(2, 2), div(9, 3))"));
    	assertEquals((Integer) 10, c.calculate("let(a, 5, add(a, a))"));
    	assertEquals((Integer) 55, c.calculate("let(a, 5, let(b, mult(a, 10), add(b, a)))"));
    	assertEquals((Integer) 40, c.calculate("let(a, let(b, 10, add(b, b)), let(b, 20, add(a, b))"));
    }

    @Test public void testFormatted() {
    	assertEquals((Integer) 3, c.calculate("add(   1   ,2)"));
    	assertEquals((Integer) 40, c.calculate(""
    			+ "let(\n"
    			+ "    a, \n"
    			+ "    let(b, 10, add(b, b)), \n"
    			+ "    let(b, 20, add(a, b)) \n"
    			+ ")"));
    	
    	assertEquals("should not allow spaces between names and parenthesis", 
    			null, c.calculate("add (1,2)"));
    }

    @Test public void testMultipleContexts() {
    	// inner variable should override outer
    	assertEquals((Integer) 5, c.calculate(""
    			+ "let(a, 2, add("
    			+ "    let(a, 3, a),"
    			+ "    a"
    			+ "))"));
    }
}
