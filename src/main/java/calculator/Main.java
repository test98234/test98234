package calculator;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
	/**
	 * Runs the calculator.
	 * 
	 * @param args Arguments are a list of expressions to evaluate
	 */
	public static void main(String[] args) {
		
		if (args.length == 0) {
			System.out.println("Please pass in expressions as arguments.");
			return;
		}
		
		Calculator c = new Calculator();
		
		List<Integer> results = Arrays.stream(args)
				.map(expression -> c.calculate(expression))
				.collect(Collectors.toList());
		
		for (Integer r : results) {
			System.out.println(r != null ? r : "Calculation failed.");
		}
		
	}
}
