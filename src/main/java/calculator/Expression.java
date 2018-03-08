package calculator;
import java.util.HashMap;
import java.util.Map;

/**
 * Expressions that form an expression tree. An
 * expression can be queried for an integer result.
 */
public abstract class Expression {
	
	public int result() {
		return this.result(new HashMap<String, Integer>());
	}

	/**
	 * The result of the particular expression subtree,
	 * given a certain context. The context is created
	 * using the Let expression. A MissingContextException
	 * is thrown if a variable cannot be found in the
	 * provided context.
	 */
	public abstract int result(Map<String, Integer> context);
	
	public static class Add extends Expression {
		private Expression left;
		private Expression right;
		public Add(Expression left, Expression right) {
			this.left = left;
			this.right = right;
		}
		@Override
		public int result(Map<String, Integer> context) {
			return left.result(context) + right.result(context);
		}
	}
	
	public static class Sub extends Expression {
		private Expression left;
		private Expression right;
		public Sub(Expression left, Expression right) {
			this.left = left;
			this.right = right;
		}
		@Override
		public int result(Map<String, Integer> context) {
			return left.result(context) - right.result(context);
		}
	}
	
	public static class Mult extends Expression {
		private Expression left;
		private Expression right;
		public Mult(Expression left, Expression right) {
			this.left = left;
			this.right = right;
		}
		@Override
		public int result(Map<String, Integer> context) {
			return left.result(context) * right.result(context);
		}
	}
	
	public static class Div extends Expression {
		private Expression left;
		private Expression right;
		public Div(Expression left, Expression right) {
			this.left = left;
			this.right = right;
		}
		@Override
		public int result(Map<String, Integer> context) {
			return left.result(context) / right.result(context);
		}
	}
	
	public static class Num extends Expression {
		private int val;
		public Num(int val) {
			this.val = val;
		}
		@Override
		public int result(Map<String, Integer> context) {
			return val;
		}
	}
	
	public static class Let extends Expression {
		private Var name;
		private Expression left;
		private Expression right;
		public Let(
				Var name, 
				Expression left, 
				Expression right) 
		{
			this.name = name;
			this.left = left;
			this.right = right;
		}
		
		/**
		 * Given a variable name, and a pair of expressions, determine
		 * the value of the variable name based on the left expression,
		 * and pass in a new context including that name with that value
		 * into the evaluation for the right expression, and return the
		 * result of that evaluation.
		 */
		@Override
		public int result(Map<String, Integer> context) {
			int value = left.result(context);
			Map<String, Integer> newContext = new HashMap<>(context);
			newContext.put(name.var, value);
			return right.result(newContext);
		}
	}

	public static class Var extends Expression {
		private String var;
		public Var(String var) {
			this.var = var;
		}
		/**
		 * Get the value of this variable in the context,
		 * or throw a MissingContextException if not
		 * present.
		 */
		@Override
		public int result(Map<String, Integer> context) {
			Integer value = context.get(var);
			if (value == null) {
				throw new MissingContextException(var);
			}
			return value;
		}
	}
	
	/**
	 * Exception thrown if variable is not present in the context
	 * defined by a prior 'let'.
	 */
	public static class MissingContextException extends RuntimeException {
		public MissingContextException(String var) {
			super("Invalid expression, '" 
					+ var
					+ "' was not defined by a 'let'");
		}

		private static final long serialVersionUID = -4363949632760910132L;
	}
}