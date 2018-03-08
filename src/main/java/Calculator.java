import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Deque;

import ca.uqac.lif.bullwinkle.*;
import ca.uqac.lif.bullwinkle.BnfParser.InvalidGrammarException;
import ca.uqac.lif.bullwinkle.BnfParser.ParseException;
import ca.uqac.lif.bullwinkle.ParseTreeObjectBuilder.BuildException;

public class Calculator {
	
	
	
    public Integer calculate(String input) {
    	
    	input = balanceTrailingParens(input);
    	
    	try {
    		String bnf = "<exp> := <add> | <sub> | <mult> | <div> | <num> | <let> | <var> ;\n" + 
    				"<add> := add( <exp> , <exp> );\n" + 
    				"<sub> := sub( <exp> , <exp> );\n" + 
    				"<mult> := mult( <exp> , <exp> );\n" +
    				"<div> := div( <exp> , <exp> );\n" + 
    				"<let> := let( <var> , <exp> , <exp> );\n" + 
    				"<num> := ^[0-9]+;\n" + 
    				"<var> := ^[A-Za-z]+;\n";
			
			InputStream stream = new ByteArrayInputStream(
					bnf.getBytes(StandardCharsets.UTF_8));
			
			BnfParser parser = new BnfParser(stream);
//			parser.setDebugMode(true);
			ParseNode tree = parser.parse(input);
			
			MyBuilder builder = new MyBuilder();
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

	public static class MyBuilder extends ParseTreeObjectBuilder<Expression>
	{
		
		@Builds(rule = "<add>", pop = true, clean = true)
		public Expression buildAdd(Object ... e)
		{
			return new Expression.Add((Expression) e[0], (Expression) e[1]);
		}

		@Builds(rule = "<sub>", pop = true, clean = true)
		public Expression buildSub(Object ... e)
		{
			return new Expression.Sub((Expression) e[0], (Expression) e[1]);
		}

		@Builds(rule = "<mult>", pop = true, clean = true)
		public Expression buildMult(Object ... e)
		{
			return new Expression.Mult((Expression) e[0], (Expression) e[1]);
		}

		@Builds(rule = "<div>", pop = true, clean = true)
		public Expression buildDiv(Object ... e)
		{
			return new Expression.Div((Expression) e[0], (Expression) e[1]);
		}

		@Builds(rule = "<num>", pop = true)
		public Expression buildNum(Object ... e)
		{
			return new Expression.Num(Integer.parseInt((String) e[0]));
		}

		@Builds(rule = "<let>", pop = true, clean = true)
		public Expression buildLet(Object ... e)
		{
			return new Expression.Let(
					(Expression.Var) e[0], 
					(Expression) e[1],
					(Expression) e[2]);
		}

		@Builds(rule = "<var>", pop = true)
		public Expression buildVar(Object ... e)
		{
			return new Expression.Var((String) e[0]);
		}
	}
}
