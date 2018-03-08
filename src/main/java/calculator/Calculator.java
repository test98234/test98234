package calculator;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import ca.uqac.lif.bullwinkle.*;
import ca.uqac.lif.bullwinkle.BnfParser.InvalidGrammarException;
import ca.uqac.lif.bullwinkle.BnfParser.ParseException;
import ca.uqac.lif.bullwinkle.ParseTreeObjectBuilder.BuildException;
import calculator.Expression.MissingContextException;

/**
 * Evaluates simple arithmetic expressions.
 */
public class Calculator {
	public final static Logger LOGGER = Logger.getLogger(Calculator.class.getName());
	
	private final static String BNF = 
			"<exp> := <add> | <sub> | <mult> | <div> | <num> | <let> | <var> ;\n" + 
			"<add> := add( <exp> , <exp> );\n" + 
			"<sub> := sub( <exp> , <exp> );\n" + 
			"<mult> := mult( <exp> , <exp> );\n" +
			"<div> := div( <exp> , <exp> );\n" + 
			"<let> := let( <var> , <exp> , <exp> );\n" + 
			"<num> := ^\\-?[0-9]+;\n" + 
			"<var> := ^[A-Za-z]+;\n";
	/**
	 * Calculates the integer result of an expression, or null if the evaluation fails.
	 */
	public Integer calculate(String input) {
		LOGGER.fine("Starting calculation");
		
		input = balanceTrailingParens(input);
		
		try {
			LOGGER.fine("Parsing tree");
			InputStream stream = new ByteArrayInputStream(
					BNF.getBytes(StandardCharsets.UTF_8));			
			BnfParser parser = new BnfParser(stream);
			parser.setDebugMode(true, LOGGER); // produces INFO messages
			ParseNode tree = parser.parse(input);

			LOGGER.fine("Building expression");
			CalculationBuilder builder = new CalculationBuilder();
			Expression exp = builder.build(tree);

			LOGGER.fine("Computing result");
			int result = exp.result();

			LOGGER.fine("Finished calculation");
			return result;
			
		} catch (InvalidGrammarException e) {
			// should never happen, grammar is fixed
			LOGGER.log(Level.SEVERE, "Could not parse grammar", e);
			
		} catch (ParseException e) {
			LOGGER.log(Level.SEVERE, "Could not parse expression", e);
			
		} catch (BuildException e) {
			LOGGER.log(Level.SEVERE, "Error building expression tree, "
					+ "please check that your syntax is correct", e);
			
		} catch (MissingContextException e) {
			LOGGER.severe(e.getMessage());
			
		} catch (ArithmeticException e) {
			LOGGER.severe(e.getMessage());
			
		}

		LOGGER.fine("Calculation returning null");
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
		if (counter < 0) {
			LOGGER.severe("Parenthesis are not balanced");
		}
		return text;
	}
}
