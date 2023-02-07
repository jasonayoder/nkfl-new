package evolution;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
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
	private Agent parent = null;
	
	private Integer[] developmentalProgram;
	private HashMap<Integer, ArrayList<Step>> blockStepsMap;
	private ArrayList<Step> stepsExecuted;
	private ArrayList<Step> actionsExecuted;
	
	public double[] fitnessArray; //fitnesses at each step
	public int maxPhenotype;
	public int minPhenotype;
	public FitnessLandscape landscape; // This LearningStrategy's NKFL
	
	public int phenotype; //
	public double phenotypeFitness; // the current fitness of the genotype
	public int genotype;
	public double genotypeFitness; // save this data so we don't have to recompute it every time we reset
	
	public int genotypicInheritanceMask;
	public int phenotypicInheritanceMask;
	public int developmentMask;
	public int evolutionMask;
	
	public int[] plasticity;
	
	//Default constructor for random genotype
	public Agent(FitnessLandscape landscape, int genotypicInheritanceMask, int phenotypicInheritanceMask, int developmentMask, int evolutionMask)
	{
		this.landscape = landscape;
		this.setupStrategy();
		this.genotype = SeededRandom.rnd.nextInt((int)Math.pow(2, landscape.n));
		this.genotypeFitness = landscape.fitness(genotype);
		this.phenotype = genotype;
		this.phenotypeFitness = genotypeFitness;
		this.genotypicInheritanceMask = genotypicInheritanceMask;
		this.phenotypicInheritanceMask = phenotypicInheritanceMask;
		this.developmentMask = developmentMask;
		this.evolutionMask = evolutionMask;
		this.plasticity = new int[Constants.BLOCK_LENGTH*Constants.PROGRAM_LENGTH];
		if(Constants.PLASTICITY_INIT.length>0) {
			for(int i = 0; i<plasticity.length; i++) {
				plasticity[i] = Constants.PLASTICITY_INIT[(i*Constants.PLASTICITY_INIT.length)/plasticity.length];
			}
		}else {
			for(int i = 0; i<plasticity.length; i++) {
				plasticity[i] = SeededRandom.rnd.nextInt();
			}
		}
	}
	
	//Overloaded constructor for specific genotype
	public Agent(FitnessLandscape landscape, int genotype, int genotypicInheritanceMask, int phenotypicInheritanceMask, int developmentMask, int evolutionMask)
	{
		this(landscape, genotypicInheritanceMask, phenotypicInheritanceMask, developmentMask, evolutionMask);
		//Overwrite the random genotype with ours
		
		this.genotype = genotype;
		this.genotypeFitness = landscape.fitness(genotype);
		this.phenotype = genotype;
		this.phenotypeFitness = genotypeFitness;
	}
	
	//This constructor is specially built for making children
	public Agent(FitnessLandscape landscape, int genotype, Integer[] developmentalProgram, HashMap<Integer, ArrayList<Step>> blockSteps, Agent parent)
	{
		//no other constructor calls because we don't want to call setupStrategy
		this.genotypicInheritanceMask = parent.genotypicInheritanceMask;
		this.phenotypicInheritanceMask = parent.phenotypicInheritanceMask;
		this.developmentMask = parent.developmentMask;
		this.evolutionMask = parent.evolutionMask;
		this.plasticity = parent.plasticity.clone();
		this.landscape = landscape;
		this.genotype = genotype;
		this.genotypeFitness = landscape.fitness(genotype);
		this.phenotype = genotype;
		this.phenotypeFitness = genotypeFitness;
		this.developmentalProgram = developmentalProgram;
		this.blockStepsMap = blockSteps;
		this.parent = parent;
	}
	
	private void setupStrategy() {
		developmentalProgram = new Integer[Constants.PROGRAM_LENGTH];
		blockStepsMap = new HashMap<>();
		
		plasticity = new int[Constants.PROGRAM_LENGTH*Constants.BLOCK_LENGTH];
		for(int i = 0; i<plasticity.length; i++) {
			if(Constants.PLASTICITY_INIT.length>0) {
				plasticity[i] = Constants.PLASTICITY_INIT[(Constants.PLASTICITY_INIT.length*i)/plasticity.length];
			}else {
				plasticity[i] = SeededRandom.rnd.nextInt();
			}
		}
		
		if(Constants.COMPARISON_PROGRAM.equals("NONE"))//This means we're doing a 'normal' evolutionary run
		{
			//Construct the blocks
			for(int block=0; block < Constants.BLOCKS; block++)
			{
				ArrayList<Step> thisBlockSteps = new ArrayList<Step>();
				for(int step=0; step<Constants.BLOCK_LENGTH; step++)
				{
					thisBlockSteps.add(Step.randomStep());
				}
				blockStepsMap.put(block, thisBlockSteps);
			}
			
			//Construct the program
			for(int step=0; step < Constants.PROGRAM_LENGTH; step++)
			{
				developmentalProgram[step] = SeededRandom.rnd.nextInt(Constants.BLOCKS);
			}
		}
		//These are the comparison programs.  The can also be used as a specific
		//start of an evolutionary run, that just initializes the blocks in very specific
		//ways, which is why they still use the block mode.
		else if(Constants.COMPARISON_PROGRAM.equals("PURERANDOMWALK"))
		{
			//Construct the blocks
			for(int block=0; block < Constants.BLOCKS; block++)
			{
				ArrayList<Step> thisBlockSteps = new ArrayList<Step>();
				if(!Step.validSteps.contains(Step.RandomWalk))
				{
					System.out.println("Cannot use PURERANDOMWALK comparsion without RandomWalk step");
					return;
				}
				for(int step=0; step<Constants.BLOCK_LENGTH; step++)
				{
					thisBlockSteps.add(Step.RandomWalk);
				}
				blockStepsMap.put(block, thisBlockSteps);
			}
			
			//Construct the program
			for(int step=0; step < Constants.PROGRAM_LENGTH; step++)
			{
				developmentalProgram[step] = SeededRandom.rnd.nextInt(Constants.BLOCKS);
			}
		}
		else if(Constants.COMPARISON_PROGRAM.equals("PURESTEEPESTCLIMB"))
		{
			//Construct the blocks
			for(int block=0; block < Constants.BLOCKS; block++)
			{
				ArrayList<Step> thisBlockSteps = new ArrayList<Step>();
				if(!Step.validSteps.contains(Step.SteepestClimb))
				{
					System.out.println("Cannot use PURERANDOMWALK comparsion without RandomWalk step");
					return;
				}
				for(int step=0; step<Constants.BLOCK_LENGTH; step++)
				{
					thisBlockSteps.add(Step.SteepestClimb);
				}
				blockStepsMap.put(block, thisBlockSteps);
			}
			
			//Construct the program
			for(int step=0; step < Constants.PROGRAM_LENGTH; step++)
			{
				developmentalProgram[step] = SeededRandom.rnd.nextInt(Constants.BLOCKS);
			}
		}
		else
		{ 
			System.err.println("comparisonProgram not recognized.  Set to 'NONE' for standard evolutionary run");
		}
	}
	
	private int scalePlasticity(int plasticity) {
		int ret = 0;
		for(int i=0; i<32; i++) {
			int index = i/Constants.PLASTICITY_SCALE;
			if((plasticity&(1<<index))!=0) {
				ret |= 1<<i;
			}
		}
		return ret;
	}
	
	private void executeSingleStrategy() {
		//we will repeatedly update this array alongside the genotype to execute the strategy faster
		int[] phenotypeArray = new int[landscape.n];
		int genotypeTracker = genotype;
		//The reason we gain efficiency doing it all at once is because we would have to do this
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
		maxPhenotype = genotype;
		minPhenotype = genotype;
		int stepIndex = 0;
		fitnessArray[stepIndex] = landscape.fitness(genotype);
		//actually execute our strategy
		for(Integer block : developmentalProgram)
		{
//			System.out.println(block);
			for(Step step : blockStepsMap.get(block))
			{
				executeStep(step, phenotypeArray, scalePlasticity(plasticity[stepIndex])&developmentMask);
				
				stepIndex = stepIndex + 1;
				//We update fitnessArray after because of step 0, which is the fitness before we ever moved
				//So the fitness after step i is at fitnessArray[stepIndex + 1]
				fitnessArray[stepIndex] = landscape.fitness(phenotype);
				if(landscape.fitness(phenotype)>landscape.fitness(maxPhenotype)) {
					maxPhenotype=phenotype;
				}
				if(landscape.fitness(phenotype)<landscape.fitness(minPhenotype)) {
					minPhenotype=phenotype;
				}
				
				//Comment this out to run more efficiently.  Just a sanity check to make sure phenotypeArray works
				if(!ensurePheontypeConsistency(phenotypeArray))
				{
					System.out.println("Phenotype Error:");
					for(int i = landscape.n-1; i>=0; i--) {
						System.out.print(phenotypeArray[i]);
					}
					System.out.println();
					System.out.println(phenotype);
					System.out.println(Integer.toBinaryString(phenotype));
				}
			}
		}
	}
	
	/**
	 * Home of the big switch statement
	 * 
	 *
	 */
	private void executeStep(Step step, int[] phenotypeArray,int mask)
	{
		//Just a note, this switch doesn't check if steps are included, that should be managed
		//by setupStrategy() and anything that manages mutation
		switch(step) {
			case RandomWalk:
				//walk randomly
				randomWalk(phenotypeArray,mask);
				break;
			case SteepestClimb:
				//climb steeply
				steepestClimb(phenotypeArray,mask);
				break;
			case SteepestFall:
				//fall steeply
				steepestFall(phenotypeArray,mask);
				break;
			case RandomIfMinimaElseSteepestFall:
				randomIfMinimaElseSteepestFall(phenotypeArray,mask);
				break;
			case RandomIfMaximaElseSteepestClimb:
				randomIfMaximaElseSteepestClimb(phenotypeArray,mask);
				break;
			case SameStep:
				sameStep(phenotypeArray,mask);
				//Do not modify stepsExecuted.  sameStep re-calls executeStep, which should then use another case to do so.
				break;
			case OppositeStep:
				oppositeStep(phenotypeArray,mask);
				//Do not modify stepsExecuted.  sameStep re-calls executeStep, which should then use another case to do so.
				break;
			case ReturnToMaxima:
				returnToMaxima(phenotypeArray);
				break;
			case ReturnToMinima:
				returnToMinima(phenotypeArray);
				break;
			case SameAction:
				sameAction(phenotypeArray,mask);
				break;
			case OppositeAction:
				oppositeAction(phenotypeArray,mask);
				break;
			case Wait:
				wait(phenotypeArray,mask);
				break;
			default:
				System.out.println("Step not recognized: " + step);
				break;
		}
	}
	
	/**
	 * Executes steps of the LearningStrategy
	 * 
	 * This implementation has changed greatly, it sacrifices the ability to run a small part of a strategy
	 * for a bit of efficiency.  Executes the strategy Constants.SAMPLES_PER_RUN times, then sets the fitness
	 * array to the average fitness at each step
	 * 
	 * @param steps number of steps to execute
	 * @return the fitness once the steps are executed
	 */
	public double executeStrategy()
	{
		double[] compoundFitnessArray = new double[Constants.TOTAL_LENGTH+1]; //The +1 accounts for recording the initial fitness
		
		for(int sample=0; sample < Constants.SAMPLES_PER_RUN; sample++)
		{
			phenotype = genotype;
			phenotypeFitness = genotypeFitness;
			this.stepsExecuted = new ArrayList<Step>();//Note for future - does this make sense for nondeterministic strategies?
			this.actionsExecuted = new ArrayList<Step>();
			fitnessArray = new double[compoundFitnessArray.length];
			
			executeSingleStrategy();
			
			for(int step=0; step < fitnessArray.length; step++)
			{
				compoundFitnessArray[step] += fitnessArray[step];
			}
		}
		
		for(int step=0; step < fitnessArray.length; step++)
		{
			compoundFitnessArray[step] /= Constants.SAMPLES_PER_RUN;
		}
		
		this.fitnessArray = compoundFitnessArray;
		
		//#NOTE: When samplesPerRun > 1, phenotyoe is always -1 at end of run
		//#since it doesn't make sense to map a single phenotype to an average of fitnesses
		if(Constants.SAMPLES_PER_RUN != 1)
		{
			phenotype = -1;
			phenotypeFitness = fitnessArray[fitnessArray.length - 1];
		}
		else
		{
			phenotypeFitness = fitnessArray[fitnessArray.length - 1];
		}
		return phenotypeFitness;
	}
	
	//This just checks consistency between phenotypeArray and phenotype itself.
	private boolean ensurePheontypeConsistency(int[] phenotypeArray)
	{
		int calculatedPhenotype = 0;
		for(int i=0; i<phenotypeArray.length; i++)
		{
			if(phenotypeArray[i]==1){
			calculatedPhenotype += Math.pow(2, i);
			}
		}
		return calculatedPhenotype == phenotype;
	}
	
	private void randomWalk(int[] phenotypeArray, int mask)
	{
		stepsExecuted.add(Step.RandomWalk);
		int num = 0;
		for(int i = 0; i<landscape.n;i++) {
			if(0!=(mask&(1<<i))) {
				num++;
			}
		}
		if(num!=0) {
		num = SeededRandom.rnd.nextInt(num);
			for(int i = 0; i<landscape.n;i++) {
				if(0!=(mask&(1<<i))) {
					if(num == 0) {
						flipPhenotypeAndArray(i, phenotypeArray);
						actionsExecuted.add(Step.RandomWalk);
						return;
					}
					num--;
				}
			}
		}
		actionsExecuted.add(Step.Wait);
	}
	
	private void steepestClimb(int[] phenotypeArray, int mask)
	{
		int locationDiff = landscape.greatestNeighborBit(phenotype, mask);
		stepsExecuted.add(Step.SteepestClimb);
		if(locationDiff==-1)
		{
			//we're at a local optima
			actionsExecuted.add(Step.Wait);
			return;
		}
		actionsExecuted.add(Step.SteepestClimb);
		flipPhenotypeAndArray(locationDiff, phenotypeArray);
	}
	
	private void steepestFall(int[] phenotypeArray, int mask)
	{
		int locationDiff = landscape.leastNeighborBit(phenotype, mask);
		stepsExecuted.add(Step.SteepestFall);
		if(locationDiff==-1)
		{
			//we're at a local optima
			actionsExecuted.add(Step.Wait);
			return;
		}
		actionsExecuted.add(Step.SteepestFall);
		flipPhenotypeAndArray(locationDiff, phenotypeArray);
	}

	private void randomIfMinimaElseSteepestFall(int[] phenotypeArray, int mask)
	{
		int locationDiff = landscape.leastNeighborBit(phenotype, mask);
		stepsExecuted.add(Step.RandomIfMaximaElseSteepestClimb);
		if(locationDiff==-1)
		{
			int num = 0;
			for(int i = 0; i<landscape.n;i++) {
				if(0!=(mask&(1<<i))) {
					num++;
				}
			}
			if(num!=0) {
				num = SeededRandom.rnd.nextInt(num);
				for(int i = 0; i<landscape.n;i++) {
					if(0!=(mask&(1<<i))) {
						if(num == 0) {
							flipPhenotypeAndArray(i, phenotypeArray);
							actionsExecuted.add(Step.RandomWalk);
							return;
						}
						num--;
					}
				}
			}
			actionsExecuted.add(Step.Wait);
			return;
		}
		actionsExecuted.add(Step.SteepestFall);
		flipPhenotypeAndArray(locationDiff, phenotypeArray);
	}
	private void randomIfMaximaElseSteepestClimb(int[] phenotypeArray, int mask)
	{
		int locationDiff = landscape.greatestNeighborBit(phenotype,mask);
		stepsExecuted.add(Step.RandomIfMaximaElseSteepestClimb);
		if(locationDiff==-1)
		{
			int num = 0;
			for(int i = 0; i<landscape.n;i++) {
				if(0!=(mask&(1<<i))) {
					num++;
				}
			}
			if(num != 0) {
				num = SeededRandom.rnd.nextInt(num);
				for(int i = 0; i<landscape.n;i++) {
					if(0!=(mask&(1<<i))) {
						if(num == 0) {
							flipPhenotypeAndArray(i, phenotypeArray);
							actionsExecuted.add(Step.RandomWalk);
							return;
						}
						num--;
					}
				}
			}
			actionsExecuted.add(Step.Wait);
			return;
		}
		actionsExecuted.add(Step.SteepestClimb);
		flipPhenotypeAndArray(locationDiff, phenotypeArray);
	}
	/**
	 * @param phenotypeArray
	 */
	private void sameStep(int[] phenotypeArray,int mask)
	{
		if(stepsExecuted.size() == 0)
		{
			executeStep(Step.RandomWalk, phenotypeArray, mask);
		}
		else
		{
			executeStep(stepsExecuted.get(stepsExecuted.size()-1), phenotypeArray, mask);
		}
	}
	private void oppositeStep(int[] phenotypeArray, int mask)
	{
		if(stepsExecuted.size() == 0)
		{
			executeStep(Step.RandomWalk, phenotypeArray, mask);
		}
		else
		{
			executeStep(Step.getOppositeOfStep(stepsExecuted.get(stepsExecuted.size()-1)), phenotypeArray, mask);
		}
	}
	private void wait(int[] phenotypeArray, int mask) {
		stepsExecuted.add(Step.Wait);
		actionsExecuted.add(Step.Wait);
	}
	
	private void sameAction(int[] phenotypeArray, int mask)
	{
		if(actionsExecuted.size() == 0)
		{
			executeStep(Step.RandomWalk, phenotypeArray, mask);
		}
		else
		{
			executeStep(actionsExecuted.get(actionsExecuted.size()-1), phenotypeArray, mask);
		}
	}
	private void oppositeAction(int[] phenotypeArray, int mask)
	{
		if(actionsExecuted.size() == 0)
		{
			executeStep(Step.RandomWalk, phenotypeArray, mask);
		}
		else
		{
			executeStep(Step.getOppositeOfStep(actionsExecuted.get(stepsExecuted.size()-1)), phenotypeArray, mask);
		}
	}
	
	
	private void returnToMaxima(int[] phenotypeArray) {
		//Copy pasted from above to ensure consistency, some code duplication....D:
		phenotype = maxPhenotype;
		int genotypeTracker = phenotype;
		//The reason we gain efficiency doing it all at once is because we would have to do this
		//power-of-2 decomposition every single step otherwise
		stepsExecuted.add(Step.ReturnToMaxima);
		actionsExecuted.add(Step.ReturnToMaxima);
		for(int i=landscape.n-1; i>=0; i--)
		{
			phenotypeArray[i]=0;
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
	}
	
	private void returnToMinima(int[] phenotypeArray) {
		//Copy pasted from above to ensure consistency, some code duplication....D:
		stepsExecuted.add(Step.ReturnToMinima);
		actionsExecuted.add(Step.ReturnToMinima);
		phenotype = minPhenotype;
		int genotypeTracker = phenotype;
		//The reason we gain efficiency doing it all at once is because we would have to do this
		//power-of-2 decomposition every single step otherwise
		for(int i=landscape.n-1; i>=0; i--)
		{
			phenotypeArray[i]=0;
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
	}
	
	private void flipPhenotypeAndArray(int index, int[] phenotypeArray)
	{
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
			System.err.println("Invalid bit in phenotype");
		}
	}
	
	//If we need more performance than this, we can look into https://stackoverflow.com/questions/64036/how-do-you-make-a-deep-copy-of-an-object
	//This method is called a lot, so might be worth doing
	public Agent identicalChild()
	{
		//create child with reinstanced program, blocksteps
		Integer[] newDP = new Integer[developmentalProgram.length];
		for(int i=0; i<newDP.length; i++)
		{
			newDP[i] = developmentalProgram[i];
		}
		
		HashMap<Integer, ArrayList<Step>> newBS = new HashMap<>();
		for(Integer bs : blockStepsMap.keySet())
		{
			ArrayList<Step> a = new ArrayList<Step>();
			for(Step s : blockStepsMap.get(bs))
			{
				a.add(s);//it's okay to directly copy enums
			}
			newBS.put(bs, a);
		}
		
		return new Agent(landscape, genotype, newDP, newBS, this);
	}
	
	public Agent childOnNewLandscape(FitnessLandscape landscape)
	{
		//create child with reinstanced program, blocksteps
		Integer[] newDP = new Integer[developmentalProgram.length];
		for(int i=0; i<newDP.length; i++)
		{
			newDP[i] = developmentalProgram[i];
		}
		
		HashMap<Integer, ArrayList<Step>> newBS = new HashMap<>();
		for(Integer bs : blockStepsMap.keySet())
		{
			ArrayList<Step> a = new ArrayList<Step>();
			for(Step s : blockStepsMap.get(bs))
			{
				a.add(s);//it's okay to directly copy enums
			}
			newBS.put(bs, a);
		}
		
		return new Agent(landscape, genotype, newDP, newBS, this);
	}
	
	public void mutate()
	{
		
		if(parent!=null) {
			for(int i = 0; i<landscape.n;i++) {
				if(0!=(phenotypicInheritanceMask&(1<<i))) {
					if((parent.phenotype&(1<<i))!=0) {
						this.genotype |= 1<<i;
						this.phenotype |= 1<<i;
					}else {
						this.genotype &= ~(1<<i);
						this.phenotype &= ~(1<<i);
					}
				}
			}
		}
		
		if(Constants.GENOTYPE_MUTATION_RATE > 0)
		{
			int[] phenotypeArray = new int[landscape.n];
			int genotypeTracker = genotype;
			//The reason we gain efficiency doing it all at once is because we would have to do this
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
			
			for(int index=0; index < phenotypeArray.length; index++)
			{
				if(0!=((evolutionMask)&(1<<index))) {
					double roll = SeededRandom.rnd.nextDouble();
					if(roll < Constants.GENOTYPE_MUTATION_RATE)
					{
						this.flipPhenotypeAndArray(index, phenotypeArray);
					}
				}
			}
			this.genotype = phenotype;
		}
		
		if(Constants.BLOCK_MUTATION_RATE > 0)
		{
			for(int block=0; block<Constants.BLOCKS; block++)
			{
				ArrayList<Step> blockSteps = blockStepsMap.get(block);
				for(int step=0; step<blockSteps.size(); step++)
				{
					double roll = SeededRandom.rnd.nextDouble();
					if(roll < Constants.BLOCK_MUTATION_RATE)
					{
						blockSteps.set(step, Step.randomStep());
					}
				}
			}
		}
		
		if(Constants.PROGRAM_MUTATION_RATE > 0)
		{
			for(int programStep=0; programStep<Constants.PROGRAM_LENGTH; programStep++)
			{
				double roll = SeededRandom.rnd.nextDouble();
				if(roll < Constants.PROGRAM_MUTATION_RATE)
				{
					developmentalProgram[programStep] = SeededRandom.rnd.nextInt(Constants.BLOCKS);
				}
			}
		}
		
		if(Constants.BLOCK_OVERWRITE_ANY > 0)
		{
			for(int block=0; block<Constants.BLOCKS; block++)
			{
				double roll = SeededRandom.rnd.nextDouble();
				if(roll < Constants.BLOCK_OVERWRITE_ANY)
				{
					int blockToCopy = SeededRandom.rnd.nextInt(Constants.BLOCKS);//yeah it can roll the same one, but that's not an issue
					ArrayList<Step> copied = new ArrayList<Step>();
					for(Step s : blockStepsMap.get(blockToCopy))//We do this to make sure it is a new arraylist, don't want them to be dependent on each other
					{
						copied.add(s);
					}
					blockStepsMap.put(block, copied);
				}
			}
		}
		
		if(Constants.BLOCK_OVERWRITE_UNUSED > 0)
		{
			ArrayList<Integer> usedBlocks = new ArrayList<Integer>();
			for(Integer i : developmentalProgram)
			{
				if(!usedBlocks.contains(i))//If you comment out this if, it becomes proportional to how much it is used.
				{
					usedBlocks.add(i);
				}
			}
			for(int block=0; block<Constants.BLOCKS; block++)
			{
				if(usedBlocks.contains(block))
				{
					continue;
				}
				double roll = SeededRandom.rnd.nextDouble();
				if(roll < Constants.BLOCK_OVERWRITE_UNUSED)
				{
					int blockToCopy = usedBlocks.get(SeededRandom.rnd.nextInt(usedBlocks.size()));//yeah it can roll the same one, but that's not an issue
					ArrayList<Step> copied = new ArrayList<Step>();
					for(Step s : blockStepsMap.get(blockToCopy))//We do this to make sure it is a new arraylist, don't want them to be dependent on each other
					{
						copied.add(s);
					}
					blockStepsMap.put(block, copied);
				}
			}
		}
		
		if(Constants.PLASTICITY_MUTATION_RATE>0) {
			for(int i = 0; i<landscape.n; i++) {
				for(int j = 0; j < plasticity.length; j++) {
					if(SeededRandom.rnd.nextDouble()<Constants.PLASTICITY_MUTATION_RATE) {
						plasticity[j]^=1<<i;
					}
				}
			}
		}

	}
	
	public String[] programStringArray()
	{
		String[] stringArray = new String[Constants.PROGRAM_LENGTH];
		for(int blockNum = 0; blockNum < developmentalProgram.length; blockNum++)
		{
			stringArray[blockNum] = "" + developmentalProgram[blockNum];
		}
		return stringArray;
	}
	
	public String[] blockStringArray(int blockNum)
	{
		String[] stringArray = new String[Constants.BLOCK_LENGTH];
		ArrayList<Step> block = blockStepsMap.get(blockNum);
		
		for(int step=0; step < block.size(); step++)
		{
			stringArray[step] = "" + block.get(step).name();
		}
		
		return stringArray;
	}
	
	public String[] totalStrategyStringArray()
	{
		String[] stringArray = new String[Constants.TOTAL_LENGTH + 1];
		stringArray[0] = "Initial Fitness";
		int stepIndex = 1;
		stringArray[stepIndex] = ""+landscape.fitness(genotype);
		//actually execute our strategy
		for(Integer block : developmentalProgram)
		{
			for(Step step : blockStepsMap.get(block))
			{
				stringArray[stepIndex] = step.name();
				stepIndex++;
			}
		}
		return stringArray;
	}
	
	public Step[] totalStrategyStepArray()
	{
		Step[] stringArray = new Step[Constants.TOTAL_LENGTH];
		int stepIndex = 0;
		//actually execute our strategy
		for(Integer block : developmentalProgram)
		{
			for(Step step : blockStepsMap.get(block))
			{
				stringArray[stepIndex] = step;
				stepIndex++;
			}
		}
		return stringArray;
	}
	
	public String getStringRepresentation()
	{
		String sb = "";
		
		sb+=genotype + "_" + Constants.BLOCKS + "_";
		
		for(Integer step : developmentalProgram)
		{
			sb+=step + "|";
		}
		
		
		for(Integer key : blockStepsMap.keySet())
		{
			sb+="_";
			sb+=key;
			for(Step s : blockStepsMap.get(key))
			{
				sb+= "|" + s.name();
			}
		}
		
		return sb.toString().replace("RandomWalk", "RW").replace("SteepestFall", "SF").replace("SteepestClimb", "SC");
	}
	
	public Agent(String stringrep, FitnessLandscape l)
	{
//		System.out.println(stringrep);
		String[] sr = stringrep.replace("RW", "RandomWalk").replace("SF", "SteepestFall").replace("SC", "SteepestClimb").split("_");
		this.landscape = l;
		this.genotype = Integer.parseInt(sr[0]);
		
//		System.out.println(sr[2]);
		String[] dpstring = sr[2].split("\\|");
//		System.out.println(Arrays.toString(dpstring));
		Integer[] dp = new Integer[dpstring.length];
		for(int i=0; i<dpstring.length; i++)
		{
//			if(dp[i]==null)
//			{
//				continue;
//			}
//			System.out.println(dpstring[i]);
//			System.out.println("aa");
			dp[i] = Integer.parseInt(dpstring[i]);
		}
//		System.out.println(Arrays.toString(dp));
		this.developmentalProgram = dp;
		this.genotypeFitness = landscape.fitness(genotype);
		this.phenotype = genotype;
		this.phenotypeFitness = genotypeFitness;
		this.blockStepsMap = new HashMap<Integer, ArrayList<Step>>();
		
		for(int blockindex = 3; blockindex < sr.length; blockindex++)
		{
			String[] blockstr = sr[blockindex].split("\\|");
			int key = Integer.parseInt(blockstr[0]);
			ArrayList<Step> block = new ArrayList<Step>();
			for(int stepindex = 1; stepindex < blockstr.length; stepindex++)
			{
//				System.out.println(blockstr[stepindex]);
				block.add(Step.getStepWithName(blockstr[stepindex]));
			}
//			System.out.println(block);
			blockStepsMap.put(key, block);
		}
		
		//TODO: Make these properties save with the sr
		this.plasticity = new int[Constants.BLOCK_LENGTH*Constants.PROGRAM_LENGTH];
		for(int i = 0; i<plasticity.length; i++) {
			plasticity[i] = -1;
		}
		this.genotypicInheritanceMask = -1;
		this.developmentMask = -1;
		this.phenotypicInheritanceMask = 0;
		this.evolutionMask = 0;
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
