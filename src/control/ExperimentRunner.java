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
		if(args.length == 1)
		{
			filename = args[0];
		}
		run(filename);
	}
	
	public static void run(String configPath) {
		PropParser.load(configPath);
		
		if(Constants.SEED != -1)
		{
			SeededRandom.rnd.setSeed(Constants.SEED);
		}
		else
		{
			SeededRandom.rnd.setSeed(SeededRandom.rnd.nextLong());//this line does nothing :P
		}
		
		int tau = Integer.MAX_VALUE;
		try{
			tau = Constants.LANDSCAPE_GENERATIONS_PER_CYCLE; 
		}catch(NumberFormatException e) {
			System.out.println("generationsPerCycle not provided continuing with SOP");
		}
		
		String filename = Constants.FILENAME;
		if(filename.contains("RENAME"))
		{
			filename = ""+SeededRandom.rnd.nextInt();
		}
		System.out.println(filename);
		PrintWriter csvWriter;
		File csvFile = new File(filename);

		//Setup CSV writer
		try {
			csvWriter = new PrintWriter(csvFile + ".csv");
		} catch (FileNotFoundException e) {
			System.err.println("could not create csv writer");
			e.printStackTrace();
			return;
		}

		
		double maxpm = Constants.PROGRAM_MUTATION_START+(Constants.PROGRAM_MUTATION_INCS*Constants.PROGRAM_MUTATION_SIZE);
		double maxbm = Constants.BLOCK_MUTATION_START+(Constants.BLOCK_MUTATION_INCS*Constants.BLOCK_MUTATION_SIZE);
		
//		System.out.println(maxpm);
//		System.out.println(maxbm);
		
		int numSimsTotal = (int)((((maxpm-Constants.PROGRAM_MUTATION_START)/Math.max(0.0001,Constants.PROGRAM_MUTATION_SIZE))+1)) * (int)((((maxbm-Constants.BLOCK_MUTATION_START)/Math.max(0.0001,Constants.BLOCK_MUTATION_SIZE))+1)) * Constants.LANDSCAPES * Constants.RUNS_PER_LANDSCAPE;
		
//		System.out.println((maxpm-Constants.PROGRAM_MUTATION_START)/Math.max(0.0001,Constants.PROGRAM_MUTATION_SIZE));
		int numSim = 0;
		
		for(double pm=Constants.PROGRAM_MUTATION_START; pm <= maxpm; pm += Math.max(Constants.PROGRAM_MUTATION_SIZE,0.0001))
		{
			Constants.PROGRAM_MUTATION_RATE = pm;
			for(double bm=Constants.BLOCK_MUTATION_START; bm <= maxbm; bm += Math.max(Constants.BLOCK_MUTATION_SIZE,0.0001))
			{
				Constants.BLOCK_MUTATION_RATE = bm;
				for(int landscapeNum = 0; landscapeNum < Constants.LANDSCAPES; landscapeNum++)
				{
					DynamicFitnessLandscape landscape = FitnessLandscapeFactory.getLandscape(Constants.N_START,Constants.K_START,SeededRandom.rnd.nextInt(),Constants.LANDSCAPE_NAME,Constants.LANDSCAPE_PARAMS);
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
