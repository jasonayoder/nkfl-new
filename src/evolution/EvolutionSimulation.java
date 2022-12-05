package evolution;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

import control.Constants;
import control.NDArrayManager;
import landscape.DynamicFitnessLandscape;
import landscape.FitnessLandscape;
import seededrandom.SeededRandom;

public class EvolutionSimulation {

	//Currently runs random sim with given params
	
	//Sumulation Paramaters (move to config file eventually)
	DynamicFitnessLandscape landscape;
	String simNum = "N/A";
	String evolutionType = "N/A";
	int tau;
	
	//Instance variables
	public ArrayList<Generation> generations = new ArrayList<Generation>();
	//generations ArrayList contains which step we are on
	
	public EvolutionSimulation(DynamicFitnessLandscape landscape, int tau)
	{
		this.landscape = landscape;
		this.evolutionType = Constants.SELECTION_TYPE;
		this.tau = tau;
		setupSimulation();
	}
	
	public void setupSimulation()
	{
		Generation gen0;
		if(Constants.SINGLE_START)
		{
			gen0 = new Generation(landscape, SeededRandom.rnd.nextInt((int)(Math.pow(2, landscape.k))));
		}
		else
		{
			gen0 = new Generation(landscape);
		}
		generations.add(gen0);
		gen0.runAllStrategies();
	}
	
	public void runSimulation()
	{
		for(int i = generations.size(); i <= Constants.NUM_GENERATIONS; i++) //<= because gen 0 doesn't really count
		{
			//Make the next generation
			Generation nextGen;
			if(evolutionType.toLowerCase().equals("truncation"))
			{
				nextGen = generations.get(generations.size() - 1).getNextGenerationTruncation();
			}
			else
			{
				System.err.println("No valid evolution type chosen");
				nextGen = null;
			}
			generations.add(nextGen);
			//Run the next generation
			nextGen.runAllStrategies();
			System.out.println("Completed Generation " + i);
//			System.out.println(nextGen.averageFitness());
			if(i%tau==0) {
				landscape.nextCycle();
			}
		}
//		writeExperimentToCSV();
	}
	
	//CSV Output Headers
	static final String SimulationHeader = "SIMULATION";
	static final String GenerationHeader = "GENERATION"; 
	static final String ProgramRowHeader = "PROGRAM_ROW";
	static final String BlockRowHeader = "BLOCK_ROW";
	static final String TotalStrategyRowHeader = "TOTAL_STRATEGY_ROW";
	static final String FitnessRowHeader = "FITNESS_ROW";
//	static final String ComparisonStrategyHeader = "COMPARISON_STRATEGIES";
//	static final int numTestsForComparison = 1000;
	public void writeExperimentToCSV(PrintWriter csvWriter) 
	{
		csvWriter.print(SimulationHeader + "," + simNum + "," + "Landscape seed: " + landscape.landscapeSeed + "," + "K Value:" + landscape.k + "\n");
		for(int gen = 0; gen < generations.size(); gen += Constants.INCREMENT_CSV)
		{
			csvWriter.print(GenerationHeader + "," + gen + "\n");
			
			//Write strategy to CSV
			csvWriter.print(ProgramRowHeader);
			Agent bestOfGen = generations.get(gen).getBestStrategyOfGeneration();
			for(String step : bestOfGen.programStringArray())
			{
				csvWriter.print("," + step);
			}
			csvWriter.print("\n");
			
			for(int block = 0; block < Constants.BLOCKS; block++)
			{
				csvWriter.print(BlockRowHeader + "_" + block);
				for(String step : bestOfGen.blockStringArray(block))
				{
					csvWriter.print("," + step);
				}
				csvWriter.print("\n");
			}
			
			csvWriter.print(TotalStrategyRowHeader);
			for(String step : bestOfGen.totalStrategyStringArray())
			{
				csvWriter.print("," + step);
			}
			csvWriter.print("\n");
			
			//Write fitnesses to CSV
			csvWriter.print(FitnessRowHeader);
			for(double d: bestOfGen.fitnessArray)
			{
				csvWriter.print("," + d);
			}
			csvWriter.print("\n");
			
		}

		csvWriter.print("\n");
	}


}
