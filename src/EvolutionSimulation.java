import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

public class EvolutionSimulation {

	//Currently runs random sim with given params
	
	//Sumulation Paramaters (move to config file eventually)
	int popsPerGeneration;
	int numGenerations;
	int childrenPerGeneration;
	double mutationPercentage; //% mutation rate
	int strategyLength;
	int startingLocation;
	DynamicFitnessLandscape landscape;
	String simNum = "N/A";
	String evolutionType = "N/A";
	int strategyRuns;
	int tau;
	
	//Instance variables
	public ArrayList<StrategyGeneration> generations = new ArrayList<StrategyGeneration>();
	//generations ArrayList contains which step we are on
	
	public EvolutionSimulation(DynamicFitnessLandscape landscape, int popsPerGeneration, int numGenerations, double mutationPercentage, int strategyLength, double percentNewPerGeneration, int startingLocation, String evolutionType, int strategyRuns,int tau)
	{
		this.landscape = landscape;
		this.popsPerGeneration = popsPerGeneration;
		this.numGenerations = numGenerations;
		this.mutationPercentage = mutationPercentage;
		this.childrenPerGeneration = (int) ((double)popsPerGeneration * (double)percentNewPerGeneration / 100);
		this.strategyLength = strategyLength;
		this.startingLocation = startingLocation;
		this.evolutionType = evolutionType;
		this.strategyRuns = strategyRuns;
		this.tau = tau;
		setupSimulation();
	}

	public void setStringNum(String simNum)
	{
		this.simNum = simNum;
	}
	
	public String getSimNum()
	{
		return simNum;
	}
	
	public void setupSimulation()
	{
		StrategyGeneration gen0 = new StrategyGeneration(landscape, popsPerGeneration, strategyLength, startingLocation);
		generations.add(gen0);
		gen0.runAllStrategies(strategyRuns);
	}
	
	public void runSimulation()
	{
		for(int i = generations.size(); i <= numGenerations; i++) //<= because gen 0 doesn't really count
		{
			generations.get(generations.size() - 1).sortStrategies();
			//Make the next generation
			StrategyGeneration nextGen;
			if(evolutionType.toLowerCase().equals("mutation"))
			{
				nextGen = StrategyGenerationFactory.generateMutation(generations.get(generations.size() - 1), childrenPerGeneration, mutationPercentage);
			}
			else if(evolutionType.toLowerCase().equals("truncation"))
			{
				nextGen = StrategyGenerationFactory.generateTruncation(generations.get(generations.size() - 1), childrenPerGeneration, mutationPercentage);
			}
//			else if(evolutionType.toLowerCase().equals("ranked_linear"))
//			{
//				nextGen = StrategyGenerationFactory.generateRankedLinear(generations.get(generations.size() - 1), childrenPerGeneration, startingLocation);
//			}
//			else if(evolutionType.toLowerCase().equals("ranked_exponential"))
//			{
//				nextGen = StrategyGenerationFactory.generateRankedExponential(generations.get(generations.size() - 1), childrenPerGeneration, startingLocation);
//			}
			else
			{
				System.err.println("No evolution type chosen");
				nextGen = null;
			}
			generations.add(nextGen);
			//Run the next generation
			nextGen.runAllStrategies(strategyRuns);
//			System.out.println("Completed Generation " + i);
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
	static final String StrategyRowHeader = "STRATEGY_ROW";
	static final String WalkProbabilityRowHeader = "WALK_PROBABILITY_ROW";
	static final String RandomWalkProbabilityRowHeader = "RANDOM_WALK_PROBABILITY_ROW";
	static final String FitnessRowHeader = "FITNESS_ROW";
	static final String HammingDistanceFromBestHeader = "HAMMING_DISTANCE_FROM_BEST";
	static final String AverageFitnessRowHeader = "AVGFITNESS_ROW";
	static final String ComparisonStrategyHeader = "COMPARISON_STRATEGIES";
	static final String HammingDistanceFromMaxRowHeader = "HAMMING_DISTANCE_FROM_PHENOTYPE_TO_BEST";
	static final String AverageHammingDistance = "AVERAGE_STRATEGY_HAMMING_DISTANCE";
	static final int numTestsForComparison = 1000;
	public void writeExperimentToCSV(PrintWriter csvWriter, Map<String, ArrayList<Step>> comparisonStrategies, int csvIncrement, int n)
	{
		csvWriter.print(SimulationHeader + "," + simNum + "," + "Sensitivity: " + LookStep.DEFAULT_NUM_CHECKS  + "," + "Landscape seed: " + landscape.landscapeSeed + "," + "Starting Location" + NDArrayManager.array1dAsString(FitnessLandscape.ind2gen(startingLocation,n)) + "," + "K Value:" + landscape.k + "\n");
		for(int gen = 0; gen < generations.size(); gen += csvIncrement)
		{
			csvWriter.print(GenerationHeader + "," + gen + "\n");
			//Write Average Hamming Distance
			csvWriter.write(AverageHammingDistance);
			int[][] counts = new int[strategyLength][3]; //TODO: Update when new steps are added or update to be dynamic
			for(LearningStrategy ls: generations.get(gen).strategies) {
				for(int i = 0; i<strategyLength; i++) {
					if(ls.strategy.get(i) instanceof WalkStep) {
						counts[i][0]++;
					}else if(ls.strategy.get(i) instanceof LookStep) {
						counts[i][1]++;
					}else {
						counts[i][1]++;
					}
				}
			}
			double total = 0;
			for(int i = 0; i<counts.length; i++) {
				for(int j = 0; j<counts[i].length;j++) {
					for(int k = j+1; k<counts[i].length;k++) {
						total += counts[i][j]*counts[i][k];
					}
				}
			}
			total *= 2;
			total /= popsPerGeneration*(popsPerGeneration-1)*strategyLength;
			csvWriter.write(","+total+"\n");
			//Write strategy to CSV
			csvWriter.print(StrategyRowHeader);
			LearningStrategy bestOfGen = generations.get(gen).getBestStrategyOfGeneration();
			for(String step : bestOfGen.getStrategyStringArray())
			{
				csvWriter.print("," + step);
			}
			csvWriter.print("\n");
			
			//Write fitnesses to CSV
			csvWriter.print(FitnessRowHeader);
			for(double d: bestOfGen.getFitnessArray())
			{
				csvWriter.print("," + d);
			}
			csvWriter.print("\n");
			
			//Write hamming distance to best
			csvWriter.print(HammingDistanceFromBestHeader);
			for(LearningStrategy ls: generations.get(gen).strategies)
			{
				csvWriter.print("," + ls.hammingDistance(bestOfGen));
			}
			csvWriter.print("\n");
			
			//Calculate aggregates
			double[] probs = new double[strategyLength];
			double[] randProbs = new double[strategyLength];
			double[] avgFitnesses = new double[strategyLength];
			for(LearningStrategy ls : generations.get(gen).strategies) {
				for(int i = 0; i<strategyLength; i++) {
					if(ls.strategy.get(i) instanceof WalkStep) {
						probs[i]++;
						if(i>0&&(ls.strategy.get(i-1) instanceof WalkStep)) {
							randProbs[i]++;
						}
					}
					avgFitnesses[i] += ls.fitnessArray[i];
				}
			}
			//Write walk probs
			csvWriter.print(WalkProbabilityRowHeader);
			for(int i = 0; i<strategyLength; i++) {
				csvWriter.print(","+(probs[i]/generations.get(gen).strategies.size()));
			}
			csvWriter.print('\n');
			//Random Walk probs
			csvWriter.print(RandomWalkProbabilityRowHeader);
			for(int i = 0; i<strategyLength; i++) {
				csvWriter.print(","+(randProbs[i]/generations.get(gen).strategies.size()));
			}
			csvWriter.print('\n');
			//Write avg fitnesses
			csvWriter.print(AverageFitnessRowHeader);
			for(int i = 0; i<strategyLength; i++) {
				csvWriter.print(","+(avgFitnesses[i]/generations.get(gen).strategies.size()));
			}
			csvWriter.print('\n');
//			//Write distance to max of phenotype
//			csvWriter.write(HammingDistanceFromMaxRowHeader);
//			int max = landscape.maxLoc();
//			for(int phenotype: bestOfGen.getPhenotypeArray()) {
//				int ham = phenotype ^ max;
//				int dist = 0;
//				for(int i = 0; i<landscape.n;i++) {
//					if(0!=(ham&(1<<i))) {
//						dist++;
//					}
//				}
//				csvWriter.print(","+dist);
//			}
//			csvWriter.print('\n');
			
			
		}
		
		csvWriter.print(ComparisonStrategyHeader);
		for(String name : comparisonStrategies.keySet())
		{
			StrategyGeneration tested = landscape.testStrategyOnLandscape(comparisonStrategies.get(name), numTestsForComparison, startingLocation);
//			System.out.println(name + ", " + tested.getBestStrategyOfGeneration().strategy);
			
			csvWriter.print("\n" + name);
			csvWriter.print("\n" + StrategyRowHeader);
			for(Step s : comparisonStrategies.get(name))
			{
				csvWriter.print("," + s.getStepName());
			}
			csvWriter.print("\n");
			csvWriter.print(FitnessRowHeader);
			for(double d: tested.getAverageFitnessAtSteps())
			{
				csvWriter.print("," + d);
			}
		}
		csvWriter.print("\n");
	}


}
