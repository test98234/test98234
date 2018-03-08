package calculator;
import ca.uqac.lif.bullwinkle.Builds;
import ca.uqac.lif.bullwinkle.ParseTreeObjectBuilder;

/**
 * Builds an expression from a ParseNode tree.
 */
public class CalculationBuilder extends ParseTreeObjectBuilder<Expression>
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