import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

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
public class LearningStrategy implements Comparable<LearningStrategy>{
	static int setNumberOfWalks = 0;
	//Data needed to function
	ArrayList<Step> strategy;
	ArrayList<Integer> lookedLocations = new ArrayList<Integer>();
	private final Step[] steps;
//	private final Step[] steps = {new AscendIfLookedStep(),new AscendIfLookedHigherStep(), new DescendIfLookedStep(), new DescendIfLookedLowerStep(), new DescendStep(), new LookStep(), new RandStep(), new RepeatStep(), new WaitStep(), new WalkStep()};
//	private final Step[] steps = {new LookStep(), new WalkStep()};
	public double[] fitnessArray; //fitnesses at each step
	public int[] phenotypeArray;
	public FitnessLandscape landscape; // This LearningStrategy's NKFL
	public static boolean usingWait = false;
	
	public int phenotype; //
	public double phenotypeFitness; // the current fitness of the genotype
	
	public int genotype;
	public double genotypeFitness; // save this data so we don't have to recompute it every time we reset
	
	public boolean ignoreWalkNumber = false;
	boolean strategyExecuted = false;
	ArrayList<Action> past;
	
	public LearningStrategy(FitnessLandscape landscape, ArrayList<Step> strategy, int genotype, boolean ignoreWalkNumber) {
		if(usingWait) {
//			steps = new Step[] {new AscendIfLookedElseLook(),new AscendIfLookedHigherElseLook(), new DescendIfLookedElseLook(), new DescendIfLookedLowerElseLook(), new DescendIfLookedLessElseRandom(), new LookStep(), new RandStep(), new RepeatStep(), new WaitStep(), new AscendIfLookedHigherElseRandom()};
			steps = new Step[] {new LookStep(), new AscendIfLookedHigherElseRandom(), new AscendIfLookedHigherElseLookIfLookedElseLook(), new RepeatStep()};
		}else {
			steps = new Step[] {new LookStep(), new AscendIfLookedHigherElseRandom()};
		}
		this.ignoreWalkNumber = ignoreWalkNumber;
		this.landscape = landscape;
		this.strategy = new ArrayList<Step>();
		
		for(Step s : strategy)
		{
			this.strategy.add(s);
		}
		past = new ArrayList<>();
		past.add(Action.WAIT);
		
		initializeArrays(genotype);
	}
	
	/**
	 * Initializes a LearningStrategy with the specified strategyArray
	 * 
	 * @param landscape     the FitnessLandscape of the LearningStrategy
	 * @param strategyArray the array representing the strategy
	 */
	public LearningStrategy(FitnessLandscape landscape, ArrayList<Step> strategy, int genotype) {
		if(usingWait) {
//			steps = new Step[] {new AscendIfLookedElseLook(),new AscendIfLookedHigherElseLook(), new DescendIfLookedElseLook(), new DescendIfLookedLowerElseLook(), new DescendIfLookedLessElseRandom(), new LookStep(), new RandStep(), new RepeatStep(), new WaitStep(), new AscendIfLookedHigherElseRandom()};
			steps = new Step[] {new LookStep(), new AscendIfLookedHigherElseRandom(), new AscendIfLookedHigherElseLookIfLookedElseLook(), new RepeatStep()};
		}else {
			steps = new Step[] {new LookStep(), new AscendIfLookedHigherElseRandom()};
		}
		this.landscape = landscape;
		this.strategy = new ArrayList<Step>();
		
		for(Step s : strategy)
		{
			this.strategy.add(s);
		}
		past = new ArrayList<>();
		past.add(Action.WAIT);
		initializeArrays(genotype);
	}

	/**
	 * Initializes a LearningStrategy with a random strategy
	 * 
	 * @param landscape      the FitnessLandscape of the LearningStrategy
	 * @param strategyLength the desired length of the strategy
	 */
	public LearningStrategy(FitnessLandscape landscape, int strategyLength, int genotype) {
		if(usingWait) {
//			steps = new Step[] {new AscendIfLookedElseLook(),new AscendIfLookedHigherElseLook(), new DescendIfLookedElseLook(), new DescendIfLookedLowerElseLook(), new DescendIfLookedLessElseRandom(), new LookStep(), new RandStep(), new RepeatStep(), new WaitStep(), new AscendIfLookedHigherElseRandom()};
			steps = new Step[] {new LookStep(), new AscendIfLookedHigherElseRandom(), new AscendIfLookedHigherElseLookIfLookedElseLook(), new RepeatStep()};
		}else {
			steps = new Step[] {new LookStep(), new AscendIfLookedHigherElseRandom()};
		}
		this.landscape = landscape;
		
		
		strategy = new ArrayList<Step>();
		
		for(int i = 0; i < strategyLength; i++)
		{
			strategy.add(this.getRandomStep());
		}
		past = new ArrayList<>();
		past.add(Action.WAIT);
		initializeArrays(genotype);
	}
	
	private void initializeArrays(int genotype)
	{
		this.phenotype = genotype;
		this.phenotypeFitness = landscape.fitness(genotype);

		this.genotype = genotype;
		this.genotypeFitness = this.phenotypeFitness;
		
		//Set our original values
		fitnessArray = new double[strategy.size()];
		fitnessArray[0] = this.genotypeFitness;
		phenotypeArray = new int[strategy.size()];
		phenotypeArray[0] = this.phenotype;
		
		if(setNumberOfWalks != 0 && !ignoreWalkNumber)
		{
			enforceNumberOfWalks();
		}
	}
	
	private void enforceNumberOfWalks() {
		int walks = getNumberOfWalks();
		int diff = setNumberOfWalks - walks;
		
		while(diff < 0)//too many walks
		{
			turnRandomStepToLook();
			diff++;
		}
		while(diff > 0)//too few walks
		{
			turnRandomStepToWalk();
			diff--;
		}
	}
	
	public int getNumberOfWalks() {
		int numberOfWalks = 0;
		for(Step s : strategy)
		{
			if(s.getStepName().equals("Walk"))
			{
				numberOfWalks++;
			}
		}
		return numberOfWalks;
	}
	
	//Not sure if this is the best implementation
	public Step getRandomStep() {
		return steps[SeededRandom.rnd.nextInt(steps.length)];
//		int roll;
//		if(!usingWait)
//		{
//			roll = SeededRandom.rnd.nextInt(2);
//		}
//		else
//		{
//			roll = SeededRandom.rnd.nextInt(3);
//		}
//		if(roll == 0)
//		{
//			return new AscendIfLookedHigherElseRandom();
//		}
//		else if(roll == 1)
//		{
//			return new LookStep();
//		}
//		else if(roll == 2)
//		{
//			return new WaitStep();
//		}
//		else
//		{
//			System.err.println("Step not implemented");
//			return null;
//		}
	}

	/**
	 * Executes steps of the LearningStrategy
	 * 
	 * @param steps number of steps to execute
	 * @return the fitness once the steps are executed
	 */
	public double executeStrategy() {
		if(!strategyExecuted)
		{
			for(int i = 0; i < strategy.size(); i++)
			{
				Step current = strategy.get(i);
				
				this.phenotype = current.execute(landscape, phenotype, lookedLocations, past);
				this.phenotypeFitness = landscape.fitness(phenotype);
				
				fitnessArray[i] = this.phenotypeFitness;
				phenotypeArray[i] = this.phenotype;
			}
			strategyExecuted = true;
			return this.phenotypeFitness;
		}
		else
		{
			System.out.print("Double-executing strategy not permitted");
			return 0;
		}
	}
	
	public double executeStrategy(int sampleSize) 
	{
		if(!strategyExecuted)
		{
			double[] avgFitnessArray = new double[fitnessArray.length];
			for(int sample = 0; sample < sampleSize; sample++)
			{
				for(int i = 0; i < strategy.size(); i++)
				{
					Step current = strategy.get(i);
					
					this.phenotype = current.execute(landscape, phenotype, lookedLocations, past);
					this.phenotypeFitness = landscape.fitness(phenotype);
					fitnessArray[i] = this.phenotypeFitness;
					phenotypeArray[i] = this.phenotype;
				}
				
				for(int i = 0; i < fitnessArray.length; i++)
				{
					avgFitnessArray[i] += fitnessArray[i];
					fitnessArray[i] = 0;
				}
				this.phenotype = this.genotype;
				this.phenotypeFitness = this.genotypeFitness;
			}
			
			for(int i = 0; i < fitnessArray.length; i++)
			{
				fitnessArray[i] = avgFitnessArray[i] / sampleSize;
			}
			this.phenotypeFitness = fitnessArray[fitnessArray.length - 1];
			this.strategyExecuted = true;
			return this.phenotypeFitness;
		}
		else
		{
			System.out.print("Double-executing strategy not permitted");
			return 0;
		}
	}

	/**
	 * Returns a child that has the exactly the same strategy as the parent
	 * @param index
	 * @return
	 */
	public LearningStrategy getDirectChild() {
		ArrayList<Step> childStrategy = new ArrayList<Step>();
		
		//Since our steps don't depend on the specific genotype(just the landscape), this is fine
		for(Step step : strategy)
		{
			childStrategy.add(step);
		}
		
		LearningStrategy child = new LearningStrategy(landscape, childStrategy, genotype, ignoreWalkNumber);
		return child;
	}
	
	public LearningStrategy getMutatedChild(double mutationRate) {
		ArrayList<Step> childStrategy = new ArrayList<Step>();
		
		//Since our steps don't depend on the specific genotype(just the landscape), this is fine
		for(Step step : strategy)
		{
			childStrategy.add(step);
		}
		
		LearningStrategy child = new LearningStrategy(landscape, childStrategy, genotype, ignoreWalkNumber);
		child.mutate(mutationRate);
		return child;
	}
	
	public int getStrategyLength() {
		return strategy.size();
	}
	
	public String getStepAtIndex(int i) {
		return strategy.get(i).getStepName(); //We should just pass the name of the step, not the step itself
	}
	
	public ArrayList<String> getStrategyStringArray() {
		ArrayList<String> strArray = new ArrayList<String>();
		for(Step s : strategy)
		{
			strArray.add(s.getStepName());
		}
		return strArray;
	}
	
	public void mutate(double mutationPercentage) {
		for(int i = 0; i < strategy.size(); i++)
		{
			double roll = SeededRandom.rnd.nextDouble() * 100;
			if(roll < mutationPercentage)
			{
				this.mutateStep(i);
			}
		}
		if(setNumberOfWalks!= 0 && !ignoreWalkNumber)
		{
			enforceNumberOfWalks();
		}
	}
	
	public double hammingDistance(LearningStrategy ls) {
		int ham = 0;
		for(int i = 0; i<strategy.size(); i++) {
			if(!this.strategy.get(i).equals(ls.strategy.get(i))) {
				ham++;
			}
		}
		return ham/(1.0*strategy.size());
	}
	
	/**
	 * Randomly mutates step i of the strategy array
	 * @param i
	 */
	
	//Pretty sure this is a bad solution, but with classes we don't have another option...
	public void mutateStep(int i) {
		strategy.set(i, getRandomStep());
//		int roll = 0;
//		if(usingWait)
//		{
//		    roll = SeededRandom.rnd.nextInt(2);
//		}
//		if(strategy.get(i).getStepName() == "Look")
//		{
//			if(roll == 0)
//			{
//				strategy.set(i, new WalkStep());
//			}
//			else
//			{
//				strategy.set(i, new WaitStep());
//			}
//		}
//		else if(strategy.get(i).getStepName() == "Walk")
//		{
//			if(roll == 0)
//			{
//				strategy.set(i, new LookStep());
//			}
//			else
//			{
//				strategy.set(i, new WaitStep());
//			}
//		}
//		else if(strategy.get(i).getStepName() == "Wait")
//		{
//			if(roll == 0)
//			{
//				strategy.set(i, new WalkStep());
//			}
//			else
//			{
//				strategy.set(i, new LookStep());
//			}
//		}
//		else
//		{
//			System.err.println("Could not determine step to mutate");
//		}
	}
	
	public void turnRandomStepToLook() {
		int stepIndex = SeededRandom.rnd.nextInt(strategy.size());
		while(strategy.get(stepIndex).getStepName() == "Look")//If this is a look step, changing it won't help
		{
			stepIndex = SeededRandom.rnd.nextInt(strategy.size());
		}
		strategy.set(stepIndex, new LookStep());
	}
	
	public void turnRandomStepToWalk() {
		int stepIndex = SeededRandom.rnd.nextInt(strategy.size());
		while(strategy.get(stepIndex).getStepName() == "Walk")//If this is a walk step, changing it won't help
		{
			stepIndex = SeededRandom.rnd.nextInt(strategy.size());
		}
		strategy.set(stepIndex, new AscendIfLookedHigherElseRandom());
	}
	
	public double getFitnessAtStep(int step) {
		return fitnessArray[step];
	}
	
	public double[] getFitnessArray() {
		return fitnessArray;
	}
	
	public int[] getPhenotypeArray() {
		return phenotypeArray;
	}

	/**
	 * Compares fitness for sorting
	 */
	@Override
	public int compareTo(LearningStrategy otherStrategy) {
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
