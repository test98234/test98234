package calculator;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;


public class Main {
	private static Options options = new Options();
	static {
		options.addOption("l", true, "log level ERROR, INFO, DEBUG (or OFF by default)");		
	}
	private static CommandLineParser parser = new DefaultParser();
	
	
	/**
	 * Runs the calculator.
	 * 
	 * @param args Arguments are a list of expressions to evaluate
	 */
	public static void main(String[] args) {
		try {
			CommandLine cmd = parser.parse(options, args);
			
			List<String> expressions = cmd.getArgList();
			if (expressions.isEmpty()) {
				printHelp("Please enter an expression.");
				return;
			}
			
			String logLevel = cmd.getOptionValue('l');
			if ("DEBUG".equals(logLevel)) {
				setLogLevel(Level.ALL);
			} else if ("INFO".equals(logLevel)) {
				setLogLevel(Level.INFO);
			} else if ("ERROR".equals(logLevel)) {
				setLogLevel(Level.SEVERE);
			} else {
				setLogLevel(Level.OFF);
			}
			
			Calculator c = new Calculator();
			
			List<Integer> results = expressions.stream()
					.map(expression -> c.calculate(expression))
					.collect(Collectors.toList());
			
			for (Integer r : results) {
				System.out.println(r != null ? r : "No result.");
			}
			
		} catch (ParseException e) {
			printHelp("Invalid program arguments");
			return;
		}
	}

	private static void setLogLevel(Level level) {
		Calculator.LOGGER.setLevel(level);
	}

	private static void printHelp(String string) {
		if (string != null) {
			System.out.println(string);
		}
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp( "Calculator [-l ERROR|INFO|DEBUG] EXP1 [EXP2 ...]", options );
	}

}
