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
import landscape.FitnessLandscapeFactory;
import seededrandom.SeededRandom;

/**
 * The StrategyGeneration class represents a group of
 * learning strategies, and is intended to be used in an evolution simulation
 * 
 * @author Jacob Ashworth
 *
 */
public class Generation {
	
	public ArrayList<Agent> agents = new ArrayList<Agent>();
	public FitnessLandscape landscape;
	int startingLocation = -1;
	
	public Generation()//Makes a 'dummy generation'
	{
		
	}
	
	/**
	 * Declares a strategy generation with a given landscape, number of strategies,
	 * and strategy length.  The Agents are then randomly generated.
	 * 
	 * @param landscape FitnessLandscape
	 */
	public Generation(FitnessLandscape landscape)
	{
		int genotypicInheritanceMask = 0;
		int phenotypicInheritanceMask = 0;
		int developmentMask = 0;
		int evolutionMask = 0;
		int numPhenotypic = Constants.PHENOTYPIC_COUNT;
		int numGenotypic = Constants.GENOTYPIC_COUNT;
		int numEpigenetic = Constants.EPIGENETIC_COUNT;
		int numPredisposed = landscape.n-numPhenotypic-numGenotypic-numEpigenetic;
		for(int i = 0; i < landscape.n; i++) {
			int roll = SeededRandom.rnd.nextInt(numPhenotypic+numGenotypic+numEpigenetic+numPredisposed);
			if(roll<numPredisposed) {
				numPredisposed--;
				evolutionMask|=1<<i;
				genotypicInheritanceMask|=1<<i;
				developmentMask|=1<<i;
				continue;
			}
			roll-=numPredisposed;
			if(roll<numEpigenetic) {
				numEpigenetic--;
				phenotypicInheritanceMask|=1<<i;
				developmentMask|=1<<i;
				continue;
			}
			roll-=numEpigenetic;
			if(roll<numGenotypic) {
				numGenotypic--;
				genotypicInheritanceMask|=1<<i;
				evolutionMask|=1<<i;
				continue;
			}else {
				numPhenotypic--;
				genotypicInheritanceMask|=1<<i;
				developmentMask|=1<<i;
			}
		}
		for(int i = 0; i < Constants.GENERATION_SIZE; i++)
		{
			agents.add(new Agent(landscape,genotypicInheritanceMask,phenotypicInheritanceMask,developmentMask,evolutionMask));
		}
	}
	
	public Generation(FitnessLandscape landscape, int genotype)
	{
		this.landscape = landscape;
		
		int genotypicInheritanceMask = 0;
		int phenotypicInheritanceMask = 0;
		int developmentMask = 0;
		int evolutionMask = 0;
		int numPhenotypic = Constants.PHENOTYPIC_COUNT;
		int numGenotypic = Constants.GENOTYPIC_COUNT;
		int numEpigenetic = Constants.EPIGENETIC_COUNT;
		int numPredisposed = landscape.n-numPhenotypic-numGenotypic-numEpigenetic;
		for(int i = 0; i < landscape.n; i++) {
			int roll = SeededRandom.rnd.nextInt(numPhenotypic+numGenotypic+numEpigenetic+numPredisposed);
			if(roll<numPredisposed) {
				numPredisposed--;
				evolutionMask|=1<<i;
				genotypicInheritanceMask|=1<<i;
				developmentMask|=1<<i;
				continue;
			}
			roll-=numPredisposed;
			if(roll<numEpigenetic) {
				numEpigenetic--;
				phenotypicInheritanceMask|=1<<i;
				developmentMask|=1<<i;
				continue;
			}
			roll-=numEpigenetic;
			if(roll<numGenotypic) {
				numGenotypic--;
				genotypicInheritanceMask|=1<<i;
				evolutionMask|=1<<i;
				continue;
			}else {
				numPhenotypic--;
				genotypicInheritanceMask|=1<<i;
				developmentMask|=1<<i;
			}
		}
		
		for(int i = 0; i < Constants.GENERATION_SIZE; i++)
		{
			agents.add(new Agent(landscape, genotype,genotypicInheritanceMask,phenotypicInheritanceMask,developmentMask,evolutionMask));
		}
	}
	
	/**
	 * Declares a strategy generation from an arraylist of strategies
	 * 
	 * @param strategies
	 */
	public Generation(ArrayList<Agent> strategies)
	{
		this.agents = strategies;
		if(strategies.size() == 0)
		{
			System.err.println("Cannot create an empty generation");
			return;
		}
		landscape = strategies.get(0).landscape;
	}
	
	public Generation(ArrayList<Agent> strategies, int startingLocation)
	{
		this.agents = strategies;
		this.startingLocation = startingLocation;
		if(strategies.size() == 0)
		{
			System.err.println("Cannot create an empty generation");
			return;
		}
		landscape = strategies.get(0).landscape;
	}

	public Generation(Generation generation, FitnessLandscape landscape2) {
		for(Agent a : generation.agents) {
			this.agents.add(a.childOnNewLandscape(landscape2));
		}
		this.landscape = landscape2;
	}

	public Generation(Agent bestStrategyOfGeneration, FitnessLandscape landscape2) {
		this.landscape = landscape2;
		for(int i = 0; i < Constants.GENERATION_SIZE; i++)
		{
			agents.add(bestStrategyOfGeneration.childOnNewLandscape(landscape2));
		}
	}

	public Agent getBestStrategyOfGeneration()
	{
		this.sortAgents();
		return agents.get(0);
	}
	
	public void runAllStrategies() {
		for(Agent strategy : agents)
		{
			strategy.executeStrategy();
		}
	}
	
	public double averageFitness() {
		double sumOfFitnesses = 0;
		for(Agent strategy : agents)
		{
			sumOfFitnesses += strategy.phenotypeFitness;
		}
		return sumOfFitnesses / agents.size();
	}
	
	public double averageFitnessAtStep(int step) {
		double sumOfFitnesses = 0;
		for(Agent strategy : agents)
		{
			sumOfFitnesses += strategy.fitnessArray[step];
		}
		return sumOfFitnesses / agents.size();
	}
	
	public int getNumStrategies() {
		return agents.size();
	}
	
	public Agent getDirectChild(int index)
	{
		return agents.get(index).identicalChild();
	}
	
	public Agent getRandomStrategy()
	{
		int index = SeededRandom.rnd.nextInt(agents.size());
		return agents.get(index);
	}
	
	public Agent getStrategyAtIndex(int index)
	{
		return agents.get(index);
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
		this.sortAgents();
		ArrayList<Agent> nextGeneration = new ArrayList<Agent>();
		
		for(int i=0; i<Constants.GENERATION_SIZE/2; i++)//Add the top half
		{
			nextGeneration.add(agents.get(i).identicalChild());
		}
		if(Constants.GENERATION_SIZE % 2 == 1)//If odd number of generations, add one more
		{
			nextGeneration.add(agents.get(Constants.GENERATION_SIZE/2+1).identicalChild());
		}
		for(int i=0; i<Constants.GENERATION_SIZE/2; i++)//Add the bottom half, but mutated
		{
			Agent child = agents.get(i).identicalChild();
			child.mutate();
			nextGeneration.add(child);
		}
		
		if(startingLocation == -1)
		{
			return new Generation(nextGeneration);
		}
		else
		{
			return new Generation(nextGeneration, startingLocation);
		}
	}
	
	public Generation getNextGenerationFitnessProportionate()
	{
		this.sortAgents();
		ArrayList<Agent> nextGeneration = new ArrayList<Agent>();
		
		double fitnessSum = 0;
		for(Agent a : agents)
		{
			fitnessSum += a.phenotypeFitness;
		}
		
		for(int i=0; i<Constants.ELITISM; i++)
		{
			nextGeneration.add(agents.get(i).identicalChild());
		}
		
		for(int i=0; i<Constants.GENERATION_SIZE-Constants.ELITISM; i++)
		{
			double selectedFitness = SeededRandom.rnd.nextDouble()*fitnessSum;
			Agent selected = null;
			for(int agent=0; agent < Constants.GENERATION_SIZE; agent++)
			{
				selectedFitness -= agents.get(agent).phenotypeFitness;
				if(selectedFitness <= 0)
				{
					selected = agents.get(agent);
					break;
				}
			}
			
			Agent child = selected.identicalChild();
			child.mutate();
			nextGeneration.add(child);
		}
		
		if(startingLocation == -1)
		{
			return new Generation(nextGeneration);
		}
		else
		{
			return new Generation(nextGeneration, startingLocation);
		}
	}
	
	
	//https://en.wikipedia.org/wiki/Tournament_selection.  Should be fast.
	public Generation getNextGenerationTournament()
	{
		this.sortAgents();
		ArrayList<Agent> nextGeneration = new ArrayList<Agent>();
		
//		System.out.println(gene*
		for(int i=0; i<Constants.ELITISM; i++)
		{
			nextGeneration.add(agents.get(i).identicalChild());
		}
		
		for(int i=0; i<Constants.GENERATION_SIZE-Constants.ELITISM; i++)
		{
			Agent topOfTournament = agents.get(SeededRandom.rnd.nextInt(agents.size()));
			for(int contestant=1; contestant < Constants.TOURNAMENT_SIZE; contestant++)
			{
				Agent contest = agents.get(SeededRandom.rnd.nextInt(agents.size()));
				if(contest.phenotypeFitness > topOfTournament.phenotypeFitness)
				{
					topOfTournament = contest;
				}
			}
			Agent child = topOfTournament.identicalChild();
			child.mutate();
			nextGeneration.add(child);
		}
		
		if(startingLocation == -1)
		{
			return new Generation(nextGeneration);
		}
		else
		{
			return new Generation(nextGeneration, startingLocation);
		}
	}
	
	//These are really long, don't go saving them every generation.
	//I would love to use a stringbuilder here, but then a race condition happens
	public String getStringRepresentation()
	{
		String sb = "";
	
		sb = sb + "STRINGREPV1:";//If a change is ever made to stringrep, change the version so we don't try to load old versions into new versions
		sb = sb + landscape.landscapeSeed + ":";
		sb = sb + landscape.n + ":" + landscape.k + ":" + Constants.LANDSCAPE_NAME + ":" + Constants.LANDSCAPE_PARAMS + ":" + startingLocation + ":" + agents.size();
		
		for(Agent a : agents)
		{
			 sb = sb + ":" + a.getStringRepresentation();
		}

		return sb;
	}
	
	//Get generation from string rep
	public Generation(String stringrep)
	{
		String[] sr = stringrep.split(":");
		if(!sr[0].equals("STRINGREPV2"))
		{
			System.out.println("Stringrep version mismatch");
			return;//We can't do anything with the wrong version
		}
		
		//Not yet made to work with dynamics.
		landscape = FitnessLandscapeFactory.getLandscape(Integer.parseInt(sr[2]), Integer.parseInt(sr[3]), Integer.parseInt(sr[1]), sr[4], sr[5]);
		startingLocation = Integer.parseInt(sr[6]);
		int numAgents = Integer.parseInt(sr[7]);
		
		for(int agentloc = 8; agentloc < 8 + numAgents; agentloc++)
		{
			agents.add(new Agent(sr[agentloc], landscape));
		}
		
		if(8+numAgents != sr.length)
		{
			System.out.println("Stringrep size mismatch, expected size " + (8+numAgents) + "but got size" + sr.length);
		}
	}

	
	//The code sorts it so that the best strategies are first
	public void sortAgents() {
		Collections.sort(agents);
		Collections.reverse(agents);
		if(agents.get(0).phenotypeFitness < agents.get(agents.size()-1).phenotypeFitness)
		{
			System.out.println("Sorting backwards");
		}
	}
}
