package evolution;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import control.Constants;
import landscape.FitnessLandscape;
import seededrandom.SeededRandom;

/**
 * The Learning Strategy class is a single strategy to navigate around a NKFL.
 * 
 * The strategy is represented as an array of integers, and each integer
 * corresponds to a single step in a strategy. Ex: strategyArray [0, 0, 0, 1, 1,
 * 1, 0] will first take three steps of strategy zero, then three steps of
 * strategy 1, then another one step of strategy zero.
 * 
 * Strategies: 0 = random walk 1 = steepest climb
 * 
 * The Learning Strategy currently is capable of only random walking or steepest
 * climbing. This is to attempt to reproduce the results in Dr. Yoder's original
 * code. The code can easily be expanded to accommodate additional possible
 * strategies (such as doing nothing or non-steepest climbing)
 * 
 * @author Jacob Ashworth, Edward Kim, Lyra Lee
 *
 */
public class Agent implements Comparable<Agent>{
	//Data needed to function
	
	Integer[] developmentalProgram;
	HashMap<Integer, ArrayList<Step>> blockSteps;
	
	public double[] fitnessArray; //fitnesses at each step
	public FitnessLandscape landscape; // This LearningStrategy's NKFL
	
	public int phenotype; //
	public double phenotypeFitness; // the current fitness of the genotype
	public int genotype;
	public double genotypeFitness; // save this data so we don't have to recompute it every time we reset
	
	//Default constructor for random genotype
	public Agent(FitnessLandscape landscape)
	{
		this.landscape = landscape;
		this.setupStrategy();
		this.genotype = SeededRandom.rnd.nextInt((int)Math.pow(2, landscape.n));
		this.genotypeFitness = landscape.fitness(genotype);
		this.phenotype = genotype;
		this.phenotypeFitness = genotypeFitness;
	}
	
	//Overloaded constructor for specific genotype
	public Agent(FitnessLandscape landscape, int genotype)
	{
		this(landscape);
		//Overwrite the random genotype with ours
		this.genotype = genotype;
		this.genotypeFitness = landscape.fitness(genotype);
		this.phenotype = genotype;
		this.phenotypeFitness = genotypeFitness;
	}
	
	/**
	 * Executes steps of the LearningStrategy
	 * 
	 * This implementation has changed greatly, it sacrifices the ability to run a small part of a strategy
	 * for a bit of efficiency.
	 * 
	 * @param steps number of steps to execute
	 * @return the fitness once the steps are executed
	 */
	public double executeStrategy() {
		//we will repeatedly update this array alongside the genotype to execute the strategy faster
		int[] phenotypeArray = new int[landscape.n];
		int genotypeTracker = genotype;
		//The reason we gain efficency doing it all at once is because we would have to do this
		//power-of-2 decomposition every single step otherwise
		for(int i=landscape.n-1; i>=0; i--)
		{
			if(genotypeTracker >= Math.pow(2, i))
			{
				phenotypeArray[i]=1;
				genotypeTracker -= Math.pow(2, i);
			}
		}
		if(genotypeTracker != 0)//Remove this check eventually if we feel confident
		{
			System.err.println("Error 2-decomposing genotype");
		}
		
		//actually execute our strategy
		for(int step=0; step < developmentalProgram.length; step++)
		{
			if(developmentalProgram[step].equals("RW"))
			{
				RWStep(phenotypeArray);
				fitnessArray[phenotype] = landscape.fitness(phenotype);
			}
			else
			{
				System.err.println("Step " + step + " not recognized.");
			}
		}
		
		return fitnessArray[fitnessArray.length-1];
	}
	
	private void RWStep(int[] phenotypeArray)
	{
		int index = SeededRandom.rnd.nextInt(phenotypeArray.length);
		if(phenotypeArray[index] == 0)
		{
			phenotype += Math.pow(2, index);
			phenotypeArray[index] = 1;
		}
		else if(phenotypeArray[index] == 1)
		{
			phenotype -= Math.pow(2, index);
			phenotypeArray[index] = 0;
		}
		else
		{
			System.err.println("Invalid bit in genotype");
		}
	}
	
	private void setupStrategy() {
		Integer[] strategy = new Integer[Constants.STRATEGY_LENGTH];
		
		if(Constants.STARTING_STRATEGY.equals("NONE"))
		{
			for(int block=0; block < Constants.BLOCKS; block++)
			{
				ArrayList<Step> thisBlockSteps = new ArrayList<Step>();
				for(int step=0; step<Constants.BLOCK_LENGTH; step++)
				{
					thisBlockSteps.add(Step.randomStep());
				}
				blockSteps.put(block, thisBlockSteps);
			}
			
			for(int step=0; step < Constants.STRATEGY_LENGTH; step++)
			{
//				strategy
			}
		}
		else if(Constants.STARTING_STRATEGY.equals("PURERANDOMWALK"))
		{
			//TODO
		}
		else
		{ 
			System.err.println("startingStrategy not recognized.  Set to 'NONE' for standard evolutionary run");
		}
		
		return strategy;
	}
	


	/**
	 * Compares fitness for sorting
	 */
	@Override
	public int compareTo(Agent otherStrategy) {
		if(this.phenotypeFitness > otherStrategy.phenotypeFitness)
		{
			return 1;
		}
		else if(this.phenotypeFitness == otherStrategy.phenotypeFitness)
		{
			return 0;
		}
		else
		{
			return -1;
		}
	}
}
