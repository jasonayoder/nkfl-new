package evolution;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import landscape.FitnessLandscape;
import seededrandom.SeededRandom;

/**
 * The StrategyGeneration class represents a group of
 * learning strategies, and is intended to be used in an evolution simulation
 * 
 * @author Jacob Ashworth
 *
 */
public class StrategyGeneration {
	
	public ArrayList<Agent> strategies = new ArrayList<Agent>();
	public FitnessLandscape landscape;
	public int startingLocation;
	public int strategyLength;
	
	/**
	 * Declares a strategy generation with a given landscape, number of strategies,
	 * and strategy length.  The LearningStrategies are then randomly generated.
	 * 
	 * @param landscape FitnessLandscape
	 * @param numStrategies Number of strategies
	 * @param strategyLength Length of the strategies
	 */
	public StrategyGeneration(FitnessLandscape landscape, int numStrategies, int strategyLength, int startingLocation)
	{
		this.landscape = landscape;
		this.strategyLength = strategyLength;
		this.startingLocation = startingLocation;
		
		for(int i = 0; i < numStrategies; i++)
		{
			strategies.add(new Agent(landscape, strategyLength, startingLocation));
		}
	}
	
	/**
	 * Declares a strategy generation from an arraylist of strategies
	 * 
	 * @param strategies
	 */
	public StrategyGeneration(ArrayList<Agent> strategies)
	{
		this.strategies = strategies;
		if(strategies.size() == 0)
		{
			System.err.println("Cannot create an empty generation");
			return;
		}
		landscape = strategies.get(0).landscape;
		strategyLength = strategies.get(0).developmentalProgram.size();
		startingLocation = strategies.get(0).genotype;
	}

	public Agent getBestStrategyOfGeneration()
	{
		this.sortStrategies();
		return strategies.get(strategies.size() - 1);
	}
	
	public void runAllStrategies() {
		for(Agent strategy : strategies)
		{
			strategy.executeStrategy();
		}
	}
	
	public void runAllStrategies(int sampleSize) {
		for(Agent strategy : strategies)
		{
			strategy.executeStrategy(sampleSize);
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
			sumOfFitnesses += strategy.getFitnessAtStep(step);
		}
		return sumOfFitnesses / strategies.size();
	}
	
	public int getNumStrategies() {
		return strategies.size();
	}
	
	public int getStrategyLength() {
		return strategyLength;
	}
	
	public Agent getDirectChild(int index)
	{
		return strategies.get(index).getDirectChild();
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
	
	public void mutateGeneration(double mutationPercentage)
	{
		for(Agent strat : strategies)
		{
			strat.mutate(mutationPercentage);
		}
	}
	
	public double[] getAverageFitnessAtSteps()
	{
		double[] avg = new double[strategyLength];
		for(int stepNum = 0; stepNum < strategyLength; stepNum++)
		{
			double currentAvg = 0;
			for(Agent strat : strategies)
			{
				currentAvg += strat.getFitnessAtStep(stepNum);
			}
			currentAvg /= strategies.size();
			avg[stepNum] = currentAvg;
		}
		return avg;
	}
	
	public void sortStrategies() {
		Collections.sort(strategies);
	}
}
