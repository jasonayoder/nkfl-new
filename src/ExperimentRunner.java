import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import config.PropParser;

public class ExperimentRunner {
	
	
	public static void main(String[] args) {
		String filename = "src/config.properties";
		run(filename);
	}
	public static void run(String configPath) {
		
		PropParser.load(configPath);
		
//		//Strategy Parameters
		int strategyLength = Integer.parseInt(PropParser.getProperty("strategyLength"));
		int sensitivity = Integer.parseInt(PropParser.getProperty("sensitivity"));
		int maxSensitivity = Integer.parseInt(PropParser.getProperty("maxSensitivity")); //Set this equal to sensitivity for a single sensitiviry run
		int sensitivityInceremet = Integer.parseInt(PropParser.getProperty("sensitivityInceremet"));
		
		//Set to 0 if you don't want to control
		int numberOfWalks = Integer.parseInt(PropParser.getProperty("numberOfWalks"));
		LearningStrategy.setNumberOfWalks = numberOfWalks;
		LearningStrategy.usingWait = Boolean.parseBoolean(PropParser.getProperty("usingWait"));

		//Landscape Parameters
		int n = Integer.parseInt(PropParser.getProperty("n"));
		int setk = Integer.parseInt(PropParser.getProperty("setk"));
		int k = Integer.parseInt(PropParser.getProperty("k"));
		int maxk = Integer.parseInt(PropParser.getProperty("maxk")); //Set this equal to k for a single k run
		int kincrement = Integer.parseInt(PropParser.getProperty("kincrement"));
		
		//Evolution Parameters
		String selectionType = PropParser.getProperty("selectionType"); 
		int numGenerations = Integer.parseInt(PropParser.getProperty("numGenerations"));
		int popsPerGeneration = Integer.parseInt(PropParser.getProperty("popsPerGeneration"));
		int childrenPercentage = Integer.parseInt(PropParser.getProperty("childrenPercentage")); //always set to 100 for ranked
		double mutationPercentage = Double.parseDouble(PropParser.getProperty("mutationPercentage"));
		
		//Seed parameters
		long seed = Integer.parseInt(PropParser.getProperty("seed")); //Set Seed
//		long seed = SeededRandom.rnd.nextInt(); //Random seed
		SeededRandom.rnd.setSeed(seed);

		//Data Reporting Parameters
		int incrementCSVoutput = Integer.parseInt(PropParser.getProperty("incrementCSVoutput"));
		String experimentName = "Config_Experiment_" + seed + "_" + selectionType + "k=" + k;
		PrintWriter csvWriter;
		File csvFile = new File(experimentName);

		
		//Num Simulation Parameters
		int simulations = Integer.parseInt(PropParser.getProperty("simulations"));
		int starts = Integer.parseInt(PropParser.getProperty("starts"));
		int runs = Integer.parseInt(PropParser.getProperty("runs"));
		int strategyRuns = Integer.parseInt(PropParser.getProperty("strategyRuns"));
		
		
		
		
//		//Setup CSV writer
//		try {
//            csvFile.createNewFile();
//        } catch (IOException e) {
//            System.err.println("CSV file not created");
//        }
		
		try {
			csvWriter = new PrintWriter(csvFile + ".csv");
		} catch (FileNotFoundException e) {
			System.err.println("could not create csv writer");
			e.printStackTrace();
			return;
		}
		
		HashMap<String, ArrayList<Step>> strats = new HashMap<String, ArrayList<Step>>();
		
		ArrayList<Step> pureWalk = new ArrayList<Step>();
		for(int i = 0; i < strategyLength; i++)
		{
			pureWalk.add(new WalkStep());
		}
		strats.put("PureWalk", pureWalk);
		
//		ArrayList<Step> alternateLookWalk = new ArrayList<Step>();
//		for(int i = 0; i < strategyLength/2; i++)
//		{
//			alternateLookWalk.add(new LookStep());
//			alternateLookWalk.add(new WalkStep());
//		}
//		strats.put("AlternateLookWalk", alternateLookWalk);
//		
		//New, smarter alternate
		ArrayList<Step> alternateLookWalk = new ArrayList<Step>();
		int looksperwalka = (int) Math.floor((n * 1.0/sensitivity));
		
		while(alternateLookWalk.size() + looksperwalka*2 + 3 < strategyLength)
		{
			alternateLookWalk.add(new WalkStep());
			for(int j = 0; j < looksperwalka; j++)
			{
				alternateLookWalk.add(new LookStep());
			}
			alternateLookWalk.add(new WalkStep());
			for(int j = 0; j < looksperwalka; j++)
			{
				alternateLookWalk.add(new LookStep());
			}
			alternateLookWalk.add(new WalkStep());
			
		}
		strats.put("AlternateLookWalk", alternateLookWalk);
		
		ArrayList<Step> SHC = new ArrayList<Step>();
		int looksperwalk = (int) Math.floor((n * 1.0/sensitivity));
		
		for(int i = 0; i < strategyLength/(looksperwalk + 1); i++)
		{
			for(int j = 0; j < looksperwalk; j++)
			{
				SHC.add(new LookStep());
			}
			SHC.add(new WalkStep());
		}
		strats.put("Steep Hill Climb", SHC);
		
		if(numberOfWalks != 0)
		{
			ArrayList<Step> balanced = new ArrayList<Step>();
			int avgLooksPerWalk = (strategyLength - numberOfWalks) / numberOfWalks;
			
			for(int i = 0; i < numberOfWalks; i++)
			{
				for(int j = 0; j < avgLooksPerWalk; j++)
				{
					balanced.add(new LookStep());
				}
				balanced.add(new WalkStep());
			}
			
			strats.put("Balanced", balanced);
		}
		
		double numSimsTotal = (((maxk-k)/kincrement)+1) * (((maxSensitivity-sensitivity)/sensitivityInceremet)+1) * simulations * starts * runs;
		double numSim = 0;
		
		//Run Simulation
		for(int thisk = k; thisk <= maxk; thisk+=kincrement)
		{
			for(int sense = sensitivity; sense <= maxSensitivity; sense+=sensitivityInceremet)
			{
				LookStep.DEFAULT_NUM_CHECKS = sense;
				for(int simulation = 0; simulation < simulations; simulation++)
				{
					FitnessLandscape landscape = new FitnessLandscape(n, thisk, SeededRandom.rnd.nextInt());
					for(int start = 0; start < starts; start++)
					{
						int[] startingLocation = NDArrayManager.array1dRandInt(n, 2);
						//Setup comparison strategies
//						Map<String, LearningStrategy> comparisonStrategies = new HashMap<String, LearningStrategy>();
//						
//						for(String name : strats.keySet())
//						{
//							LearningStrategy comparison = new LearningStrategy(landscape, strats.get(name), startingLocation);
//							comparisonStrategies.put(name, comparison);
//						}
						
						
						for(int run = 0; run < runs; run++)
						{
							long startTime = System.currentTimeMillis()/1000;
							numSim++;
							String simNum = "" + thisk + "." + sense + "." + simulation + "." + start + "." + run;
							
							EvolutionSimulation sim = new EvolutionSimulation(
									landscape,
									popsPerGeneration,
									numGenerations,
									mutationPercentage,
									strategyLength,
									childrenPercentage,
									startingLocation,
									selectionType,
									strategyRuns
									);
							sim.setStringNum(simNum);
							
							sim.runSimulation();
							long endTime = System.currentTimeMillis()/1000;
							long timeOfLastRun = endTime - startTime;
							
							double simsLeft = numSimsTotal-numSim;
							System.out.println(simNum + " complete, progress = " + 100*numSim/numSimsTotal + "%, estimated time remaning: " + timeOfLastRun*simsLeft/60 + " minutes");
							
							sim.writeExperimentToCSV(csvWriter, strats, incrementCSVoutput);
							
						}
					}
				}
			}
		}
		
		System.out.println("Data successfully written to " + experimentName + ".csv");
		
		//cleanup
		csvWriter.flush();
        csvWriter.close();
	}
}
