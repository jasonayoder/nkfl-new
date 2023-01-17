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
		
		boolean runAfter = false;
		PrintWriter csvWriter2 = null;
		File csvFile2; 
		try {
			System.out.println(Constants.FILENAME2);
			csvFile2 = new File(Constants.FILENAME2);
			csvWriter2 = new PrintWriter(csvFile2 + ".csv");
			runAfter = true;
		}catch(Exception e){
			System.out.println("No filename2 provided not running on new landscapes");
		}
		
		boolean runBestAfter = false;
		PrintWriter csvWriter3 = null;
		File csvFile3; 
		try {
			System.out.println(Constants.FILENAME3);
			csvFile3 = new File(Constants.FILENAME3);
			csvWriter3 = new PrintWriter(csvFile3 + ".csv");
			runBestAfter = true;
		}catch(Exception e){
			System.out.println("No filename2 provided not running on new landscapes");
		}
		
		int maxn = Constants.N_START+(Constants.N_INCREMENT*Constants.N_INCREMENT_SIZE);
		int maxk = Constants.K_START+(Constants.K_INCREMENT*Constants.K_INCREMENT_SIZE);
		

		int maxn2 = 0;
		int maxk2 = 0;
		if(runAfter) {
			maxn2 = Constants.N_START2+(Constants.N_INCREMENT2*Constants.N_INCREMENT_SIZE2);
			maxk2 = Constants.K_START2+(Constants.K_INCREMENT2*Constants.K_INCREMENT_SIZE2);
		}
		
		double numSimsTotal = (((maxn-Constants.N_START)/Math.max(1,Constants.N_INCREMENT))+1) * (((maxk-Constants.K_START)/Math.max(1,Constants.K_INCREMENT))+1) * Constants.LANDSCAPES * Constants.RUNS_PER_LANDSCAPE;
		double numSim = 0;
		
		for(int n=Constants.N_START; n <= maxn; n += Math.max(Constants.N_INCREMENT_SIZE,1))
		{
			for(int k=Constants.K_START; k <= maxk && k<n; k += Math.max(Constants.K_INCREMENT_SIZE, 1))
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
						
						if(runAfter||runBestAfter) {
							for(int n2 = Constants.N_START2; n2<=maxn2; n2+=Math.max(Constants.N_INCREMENT_SIZE2, 1)) {
								for(int k2=Constants.K_START2; k2 <= maxk2 && k2<n2; k2 += Math.max(Constants.K_INCREMENT_SIZE2, 1)){
									if(runAfter) {
										FitnessLandscape landscape2 = new FitnessLandscape(n2,k2,SeededRandom.rnd.nextInt());
										sim.rerunFinalGen(landscape2, csvWriter2);
									}
									if(runBestAfter) {
										FitnessLandscape landscape2 = new FitnessLandscape(n2,k2,SeededRandom.rnd.nextInt());
										sim.rerunBestOfFinalGen(landscape2, csvWriter3);
									}
								}
							}
						}
						
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
        
        if(runAfter) {
        	System.out.println("Data successfully written to " + Constants.FILENAME2 + ".csv");
    		csvWriter2.flush();
            csvWriter2.close();
        }
	}
}
