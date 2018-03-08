package calculator;

public class Main {
	/**
	 * Runs the calculator.
	 * 
	 * @param args Arguments are a list of strings to calculate 
	 */
	public static void main(String[] args) {
		// java -classpath "bin;libs/bullwinkle.jar" calculator.Main "add(2, 2)"
		
		if (args.length == 0) {
			System.out.println("Please pass in expressions as arguments.");
			return;
		}
		
		Calculator c = new Calculator();
		
		Integer[] results = new Integer[args.length];
		for (int i = 0; i < args.length; i++) {
			results[i] = c.calculate(args[i]);
		}
		
		for (Integer r : results) {
			System.out.println(r != null ? r : "Calculation failed.");
		}
		
	}
}
