package calculator;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import ca.uqac.lif.bullwinkle.*;
import ca.uqac.lif.bullwinkle.BnfParser.InvalidGrammarException;
import ca.uqac.lif.bullwinkle.BnfParser.ParseException;
import ca.uqac.lif.bullwinkle.ParseTreeObjectBuilder.BuildException;

public class Calculator {
	
	/**
	 * Calculates the integer result of an expression, or 
	 */
    public Integer calculate(String input) {
    	
    	input = balanceTrailingParens(input);
    	
    	try {
    		String bnf = "<exp> := <add> | <sub> | <mult> | <div> | <num> | <let> | <var> ;\n" + 
    				"<add> := add( <exp> , <exp> );\n" + 
    				"<sub> := sub( <exp> , <exp> );\n" + 
    				"<mult> := mult( <exp> , <exp> );\n" +
    				"<div> := div( <exp> , <exp> );\n" + 
    				"<let> := let( <var> , <exp> , <exp> );\n" + 
    				"<num> := ^\\-?[0-9]+;\n" + 
    				"<var> := ^[A-Za-z]+;\n";
			
			InputStream stream = new ByteArrayInputStream(
					bnf.getBytes(StandardCharsets.UTF_8));
			
			BnfParser parser = new BnfParser(stream);
//			parser.setDebugMode(true);
			ParseNode tree = parser.parse(input);
			
			CalculationBuilder builder = new CalculationBuilder();
			Expression exp = builder.build(tree);
			
			return exp.result();
			
		} catch (InvalidGrammarException e) {
			e.printStackTrace();
			
		} catch (ParseException e) {
			e.printStackTrace();
			
		} catch (BuildException e) {
			e.printStackTrace();
			
		} catch (NullPointerException e) {
			// missing var
		} catch (ArithmeticException e) {
			// division by zero
		}
		
        return null;
    }
    
    /**
     * If trailing parenthesis are missing, return a string
     * with the correct amount appended. Other parenthesis
     * errors are not corrected.
     */
	private String balanceTrailingParens(String text) {
		int counter = 0;
		for (char ch : text.toCharArray()) {
		    if (ch == '(') counter++;
		    else if (ch == ')') counter--;

		    if (counter < 0) break;
		}
		if (counter > 0) {
			StringBuilder sb = new StringBuilder(text);
			while (counter-- > 0) {
				sb.append(")");
			}
			return sb.toString();
		}
		return text;
	}
}
