package control;

import java.util.Random;

import control.Constants;
import control.PropParser;


/**
 * Designed to be run with a properties file specified
 * > java -jar Main filename.properties
 * 
 * 
 * 
 * @author Jason Yoder
 *
 */
public class Main {
	
	public static void main(String[] args) {
		// String filename = args[0];    // to avoid needing to change source code
		String filename = "src/configFiles/config.properties";
		PropParser.load(filename);
	
//		ExperimentRunner.
	
		
		
		
	}
	

	
	

}