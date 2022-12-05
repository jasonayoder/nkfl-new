package control;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
		
		int tau = Integer.MAX_VALUE;
		try{
			tau = Constants.LANDSCAPE_GENERATIONS_PER_CYCLE; 
		}catch(NumberFormatException e) {
			System.out.println("generationsPerCycle not provided continuing with SOP");
		}
		
		System.out.println(Constants.FILENAME);
		PrintWriter csvWriter;
		File csvFile = new File(Constants.FILENAME);

		//Setup CSV writer
		try {
			csvWriter = new PrintWriter(csvFile + ".csv");
		} catch (FileNotFoundException e) {
			System.err.println("could not create csv writer");
			e.printStackTrace();
			return;
		}
		
		int maxn = Constants.N_START+(Constants.N_INCREMENT*Constants.N_INCREMENT_SIZE);
		int maxk = Constants.K_START+(Constants.K_INCREMENT*Constants.K_INCREMENT_SIZE);
		
		double numSimsTotal = (((maxn-Constants.N_START)/Math.max(1,Constants.N_INCREMENT))+1) * (((maxk-Constants.K_START)/Math.max(1,Constants.K_INCREMENT))+1) * Constants.LANDSCAPES * Constants.RUNS_PER_LANDSCAPE;
		double numSim = 0;
		
		for(int n=Constants.N_START; n <= maxn; n += Math.max(Constants.N_INCREMENT_SIZE,1))
		{
			for(int k=Constants.K_START; k <= maxk; k += Math.max(Constants.K_INCREMENT_SIZE, 1))
			{
				for(int landscapeNum = 0; landscapeNum < Constants.LANDSCAPES; landscapeNum++)
				{
					DynamicFitnessLandscape landscape = FitnessLandscapeFactory.getLandscape(n,k,SeededRandom.rnd.nextInt(),Constants.LANDSCAPE_NAME,Constants.LANDSCAPE_PARAMS);
					for(int run=0; run < Constants.RUNS_PER_LANDSCAPE; run++)
					{
						long startTime = System.currentTimeMillis()/1000;
						
						EvolutionSimulation sim = new EvolutionSimulation(landscape, tau);
						sim.runSimulation();
						sim.writeExperimentToCSV(csvWriter);
						
						long endTime = System.currentTimeMillis()/1000;
						long timeOfLastRun = endTime - startTime;
						numSim++;
						System.out.println(numSim + " complete, progress = " + 100*numSim/numSimsTotal + "%, estimated time remaning: " + timeOfLastRun*(numSimsTotal-numSim)/60 + " minutes");
					}
				}
			}
		}
		
		
		//
		
		System.out.println("Data successfully written to " + Constants.FILENAME + ".csv");
		
		//cleanup
		csvWriter.flush();
        csvWriter.close();
	}
}
