package control;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import LookStep;
import NDArrayManager;
import evolution.EvolutionSimulation;
import landscape.DynamicFitnessLandscape;
import landscape.FitnessLandscape;
import landscape.FitnessLandscapeFactory;
import seededrandom.SeededRandom;

/**
 * New & Improved Experiment Runner!
 * 
 * 
 * @author jacob
 *
 */
public class ExperimentRunner {
	
	
	public static void main(String[] args) {
		String filename = "configFiles/config.properties";
		run(filename);
	}
	
	public static void run(String configPath) {
		
		PropParser.load(configPath);
		SeededRandom.rnd.setSeed(Constants.SEED);

		//Data Reporting Parameters
		int incrementCSVoutput = Integer.parseInt(PropParser.getProperty("incrementCSVoutput"));
		String experimentName = PropParser.getProperty("filename");
		if(experimentName == null) {
			experimentName = "Config_Experiment_" + Constants.SEED + "_" + Constants.SELECTION_TYPE;
		}
		
		String landscapeName = PropParser.getProperty("landscapeName");
		String landscapeParams = PropParser.getProperty("landscapeParams");
		int tau = Integer.MAX_VALUE;
		try{
			tau = Integer.parseInt(PropParser.getProperty("generationsPerCycle"));
		}catch(NumberFormatException e) {
			System.out.println("generationsPerCycle not provided continuing with SOP");
		}
		
		System.out.println(experimentName);
		PrintWriter csvWriter;
		File csvFile = new File(experimentName);

		
		//Num Simulation Parameters
		int simulations = Integer.parseInt(PropParser.getProperty("simulations"));
		int starts = Integer.parseInt(PropParser.getProperty("starts"));
		int runs = Integer.parseInt(PropParser.getProperty("runs"));
		int strategyRuns = Integer.parseInt(PropParser.getProperty("strategyRuns"));
		
		//Setup CSV writer
		try {
			csvWriter = new PrintWriter(csvFile + ".csv");
		} catch (FileNotFoundException e) {
			System.err.println("could not create csv writer");
			e.printStackTrace();
			return;
		}
		
		//
		
		System.out.println("Data successfully written to " + experimentName + ".csv");
		
		//cleanup
		csvWriter.flush();
        csvWriter.close();
	}
}
