package evolution;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import control.Constants;
import landscape.FitnessLandscape;
import seededrandom.SeededRandom;

/**
 * The StrategyGeneration class represents a group of
 * learning strategies, and is intended to be used in an evolution simulation
 * 
 * @author Jacob Ashworth
 *
 */
public class Generation {
	
	public ArrayList<Agent> strategies = new ArrayList<Agent>();
	public FitnessLandscape landscape;
	
	/**
	 * Declares a strategy generation with a given landscape, number of strategies,
	 * and strategy length.  The Agents are then randomly generated.
	 * 
	 * @param landscape FitnessLandscape
	 */
	public Generation(FitnessLandscape landscape)
	{
		this.landscape = landscape;
		
		for(int i = 0; i < Constants.GENERATION_SIZE; i++)
		{
			strategies.add(new Agent(landscape));
		}
	}
	
	public Generation(FitnessLandscape landscape, int genotype)
	{
		this.landscape = landscape;
		
		for(int i = 0; i < Constants.GENERATION_SIZE; i++)
		{
			strategies.add(new Agent(landscape, genotype));
		}
	}
	
	/**
	 * Declares a strategy generation from an arraylist of strategies
	 * 
	 * @param strategies
	 */
	public Generation(ArrayList<Agent> strategies)
	{
		this.strategies = strategies;
		if(strategies.size() == 0)
		{
			System.err.println("Cannot create an empty generation");
			return;
		}
		landscape = strategies.get(0).landscape;
	}

	public Agent getBestStrategyOfGeneration()
	{
		this.sortStrategies();
		return strategies.get(0);
	}
	
	public void runAllStrategies() {
		for(Agent strategy : strategies)
		{
			strategy.executeStrategy();
		}
	}
	
	public double averageFitness() {
		double sumOfFitnesses = 0;
		for(Agent strategy : strategies)
		{
			sumOfFitnesses += strategy.phenotypeFitness;
		}
		return sumOfFitnesses / strategies.size();
	}
	
	public double averageFitnessAtStep(int step) {
		double sumOfFitnesses = 0;
		for(Agent strategy : strategies)
		{
			sumOfFitnesses += strategy.fitnessArray[step];
		}
		return sumOfFitnesses / strategies.size();
	}
	
	public int getNumStrategies() {
		return strategies.size();
	}
	
	public Agent getDirectChild(int index)
	{
		return strategies.get(index).identicalChild();
	}
	
	public Agent getRandomStrategy()
	{
		int index = SeededRandom.rnd.nextInt(strategies.size());
		return strategies.get(index);
	}
	
	public Agent getStrategyAtIndex(int index)
	{
		return strategies.get(index);
	}
	
	public double[] getAverageFitnessAtSteps()
	{
		double[] avg = new double[Constants.TOTAL_LENGTH+1];
		for(int stepNum = 0; stepNum < Constants.TOTAL_LENGTH+1; stepNum++)
		{
			avg[stepNum] = this.averageFitnessAtStep(stepNum);
		}
		return avg;
	}
	
	public Generation getNextGenerationTruncation()
	{
		this.sortStrategies();
		ArrayList<Agent> nextGeneration = new ArrayList<Agent>();
		
		for(int i=0; i<Constants.GENERATION_SIZE/2; i++)//Add the top half
		{
			nextGeneration.add(strategies.get(i).identicalChild());
		}
		if(Constants.GENERATION_SIZE % 2 == 1)//If odd number of generations, add one more
		{
			nextGeneration.add(strategies.get(Constants.GENERATION_SIZE/2+1).identicalChild());
		}
		for(int i=0; i<Constants.GENERATION_SIZE/2; i++)//Add the bottom half, but mutated
		{
			Agent child = strategies.get(i).identicalChild();
			child.mutate();
			nextGeneration.add(child);
		}
		
		return new Generation(nextGeneration);
	}
	
//	public Generation getNextGenerationFitnessProportionate()
//	{
//		this.sortStrategies();
//		ArrayList<Agent> nextGeneration = new ArrayList<Agent>();
//		
//		
//		
//		
//		return new Generation(nextGeneration);
//	}
//	
	public Generation getNextGenerationRanked()
	{
		this.sortStrategies();
		ArrayList<Agent> nextGeneration = new ArrayList<Agent>();
		
		for(int i=0; i<Constants.GENERATION_SIZE; i++)
		{
			
		}
		
		return new Generation(nextGeneration);
	}
	
	//The code sorts it so that the best strategies are first
	public void sortStrategies() {
		Collections.sort(strategies);
		Collections.reverse(strategies);
		if(strategies.get(0).phenotypeFitness < strategies.get(strategies.size()-1).phenotypeFitness)
		{
			System.out.println("Sorting backwards");
		}
	}
}
