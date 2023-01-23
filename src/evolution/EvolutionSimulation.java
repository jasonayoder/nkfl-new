package evolution;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import control.Constants;
import control.NDArrayManager;
import landscape.DynamicFitnessLandscape;
import landscape.FitnessLandscape;
import seededrandom.SeededRandom;

public class EvolutionSimulation {

	// Currently runs random sim with given params

	// Sumulation Paramaters (move to config file eventually)
	DynamicFitnessLandscape landscape;
	String simNum = "N/A";
	String selectionType = "N/A";
	int tau;
	
	public int phenotypicInheritanceMask;
	public int genotypicInheritanceMask;
	public int developmentalAdaptationMask;
	public int evolutionaryAdaptationMask;

	// Instance variables
	public ArrayList<Generation> generations = new ArrayList<Generation>();
	// generations ArrayList contains which step we are on

	public EvolutionSimulation(DynamicFitnessLandscape landscape, int tau) {
		this.landscape = landscape;
		this.selectionType = Constants.SELECTION_TYPE;
		this.tau = tau;
		setupSimulation();
	}
	
	public EvolutionSimulation(DynamicFitnessLandscape landscape, int tau, String startingGeneration, int startingGenerationIndex) {
		this.landscape = landscape;
		this.selectionType = Constants.SELECTION_TYPE;
		this.tau = tau;
		// temp solution don't know how acob might want to fix.
		int numPhenotypic = Constants.PHENOTYPIC_NUM;
		int numGenotypic = Constants.GENOTYPIC_NUM;
		int numEpigenetic = Constants.EPIGENETIC_NUM;
		int numPredisposed = landscape.n-numPhenotypic-numGenotypic-numEpigenetic;
		
		for(int i = 0; i<landscape.n; i++) {
			int roll = SeededRandom.rnd.nextInt(numPredisposed+numGenotypic+numEpigenetic+numPhenotypic);
			if(roll<numPredisposed) {
				genotypicInheritanceMask |= 1<<i;
				developmentalAdaptationMask |= 1<<i;
				evolutionaryAdaptationMask |= 1<<i;
				numPredisposed--;
			}else if(roll<numGenotypic) {
				genotypicInheritanceMask |= 1<<i;
				evolutionaryAdaptationMask |= 1<<i;
				numGenotypic--;
			}else if(roll<numEpigenetic) {
				phenotypicInheritanceMask |= 1<<i;
				developmentalAdaptationMask |= 1<<i;
				numEpigenetic--;
			}else {
				genotypicInheritanceMask |= 1<<i;
				developmentalAdaptationMask |= 1<<i;
				numPhenotypic |= 1<<i;
			}
		}
		
		//insert dummies until we actuall want to resume
		for(int i=0; i<startingGenerationIndex; i++)
		{
			generations.add(new Generation());
		}
		
		Generation startGen = new Generation(startingGeneration);
		startGen.runAllStrategies();
		generations.add(startGen);
	}

	public void setupSimulation() {
		int numPhenotypic = Constants.PHENOTYPIC_NUM;
		int numGenotypic = Constants.GENOTYPIC_NUM;
		int numEpigenetic = Constants.EPIGENETIC_NUM;
		int numPredisposed = landscape.n-numPhenotypic-numGenotypic-numEpigenetic;
		for(int i = 0; i<landscape.n; i++) {
			int roll = SeededRandom.rnd.nextInt(numPredisposed+numGenotypic+numEpigenetic+numPhenotypic);
			if(roll<numPredisposed) {
				genotypicInheritanceMask |= 1<<i;
				developmentalAdaptationMask |= 1<<i;
				evolutionaryAdaptationMask |= 1<<i;
				numPredisposed--;
			}else if(roll<numGenotypic) {
				genotypicInheritanceMask |= 1<<i;
				evolutionaryAdaptationMask |= 1<<i;
				numGenotypic--;
			}else if(roll<numEpigenetic) {
				phenotypicInheritanceMask |= 1<<i;
				developmentalAdaptationMask |= 1<<i;
				numEpigenetic--;
			}else {
				genotypicInheritanceMask |= 1<<i;
				developmentalAdaptationMask |= 1<<i;
				numPhenotypic |= 1<<i;
			}
		}
		Generation gen0;
		if (Constants.SINGLE_START) {
			if(Constants.STARTING_LOCATION != -1)
			{
				gen0 = new Generation(landscape, Constants.STARTING_LOCATION, phenotypicInheritanceMask, genotypicInheritanceMask,developmentalAdaptationMask,evolutionaryAdaptationMask);
			}
			else
			{
				gen0 = new Generation(landscape, SeededRandom.rnd.nextInt((int) (Math.pow(2, landscape.k))), phenotypicInheritanceMask, genotypicInheritanceMask,developmentalAdaptationMask,evolutionaryAdaptationMask);
			}
		} else {
			gen0 = new Generation(landscape, phenotypicInheritanceMask, genotypicInheritanceMask,developmentalAdaptationMask,evolutionaryAdaptationMask);
		}
		generations.add(gen0);
		gen0.runAllStrategies();
	}

	public void runSimulation() {
		for (int i = generations.size(); i <= Constants.NUM_GENERATIONS; i++) // <= because gen 0 doesn't really count
		{
			// Make the next generation
			Generation nextGen;
			if (selectionType.toLowerCase().equals("truncation")) {
				nextGen = generations.get(generations.size() - 1).getNextGenerationTruncation();
			} else if (selectionType.toLowerCase().equals("fitprop")) {
				nextGen = generations.get(generations.size() - 1).getNextGenerationFitnessProportionate();
			} else if (selectionType.toLowerCase().equals("tournament")) {
				nextGen = generations.get(generations.size() - 1).getNextGenerationTournament();
			} else {
				System.err.println("No valid evolution type chosen");
				nextGen = null;
			}
			generations.add(nextGen);
			// Run the next generation
			nextGen.runAllStrategies();
//			System.out.println("Completed Generation " + i);
//			System.out.println(nextGen.averageFitness());
			if (i % tau == 0) {
				landscape.nextCycle();
			}
		}
//		writeExperimentToCSV();
	}

	// CSV Output Headers
	static final String SimulationHeader = "SIMULATION";
	static final String GenerationHeader = "GENERATION";
	static final String ProgramRowHeader = "PROGRAM_ROW";
	static final String BlockRowHeader = "BLOCK_ROW";
	static final String TotalStrategyRowHeader = "TOTAL_STRATEGY_ROW";
	static final String FitnessRowHeader = "FITNESS_ROW";

//	static final String ComparisonStrategyHeader = "COMPARISON_STRATEGIES";
//	static final int numTestsForComparison = 1000;
	public void writeExperimentToCSV(PrintWriter csvWriter) {
		csvWriter.print(SimulationHeader + "," + simNum + "," + "Landscape seed: " + landscape.landscapeSeed + ","
				+ "K Value:" + landscape.k + "\n");
		for (int gen = 0; gen < generations.size(); gen += Constants.INCREMENT_CSV) {
			csvWriter.print(GenerationHeader + "," + gen);
			if(generations.get(gen).agents.size()==0)//dummy generation
			{
				csvWriter.print("," + "DUMMY" + "\n");
				continue;
			}
			if(Constants.OUTPUT_GENSTR)
			{
				csvWriter.print("," + generations.get(gen).getStringRepresentation());
			}
			csvWriter.print("\n");
			// Write strategy to CSV
			csvWriter.print(ProgramRowHeader);
			Agent bestOfGen = generations.get(gen).getBestStrategyOfGeneration();
			for (String step : bestOfGen.programStringArray()) {
				csvWriter.print("," + step);
			}
			csvWriter.print("\n");

			for (int block = 0; block < Constants.BLOCKS; block++) {
				csvWriter.print(BlockRowHeader + "_" + block);
				for (String step : bestOfGen.blockStringArray(block)) {
					csvWriter.print("," + step);
				}
				csvWriter.print("\n");
			}

			csvWriter.print(TotalStrategyRowHeader);
			for (String step : bestOfGen.totalStrategyStringArray()) {
				csvWriter.print("," + step);
			}
			csvWriter.print("\n");

			// Write fitnesses to CSV
			csvWriter.print(FitnessRowHeader);
			for (double d : bestOfGen.fitnessArray) {
				csvWriter.print("," + d);
			}
			csvWriter.print("\n");

			// Write E(step) and cov(step,step) to the CSV
			// Map<Step, Integer> StepToCount = new HashMap<>();
			// Map<Step, Map<Step, Integer>> StepPairToCount = new HashMap<>();
			// Map<Step, Map<Step, Integer>> SequentialPair = new HashMap<>();
			// Map<Step, Integer> PairStarts = new HashMap<>();
			// for (Step a : Step.validSteps) {
			// 	StepToCount.put(a, 0);
			// 	PairStarts.put(a, 0);
			// 	StepPairToCount.put(a, new HashMap<>());
			// 	SequentialPair.put(a,new HashMap<>());
			// 	for (Step b : Step.validSteps) {
			// 		StepPairToCount.get(a).put(b, 0);
			// 		SequentialPair.get(a).put(b, 0);
			// 	}
			// }
			// for (Agent a : generations.get(gen).strategies) {
			// 	Map<Step, Integer> StepToCountInAgent = new HashMap<>();
			// 	Step[] steps =  a.totalStrategyStepArray();
			// 	for (Step s : Step.validSteps) {
			// 		StepToCountInAgent.put(s, 0);
			// 	}
			// 	for (Step s : steps) {
			// 		StepToCountInAgent.put(s, StepToCountInAgent.get(s) + 1);
			// 	}
			// 	for (Step s1 : Step.validSteps) {
			// 		StepToCount.put(s1, StepToCount.get(s1) + StepToCountInAgent.get(s1));
			// 		for (Step s2 : Step.validSteps) {
			// 			StepPairToCount.get(s1).put(s2, StepPairToCount.get(s1).get(s2)
			// 					+ StepToCountInAgent.get(s1) * StepToCountInAgent.get(s2));
			// 		}
			// 	}
			// 	for(int i = 0; i<steps.length-1; i++) {
			// 		PairStarts.put(steps[i], 1+PairStarts.get(steps[i]));
			// 		SequentialPair.get(steps[i]).put(steps[i+1],1+SequentialPair.get(steps[i]).get(steps[i+1]));
			// 	}
				
			// }
			// csvWriter.print("VALID_STEPS:,");
			// for (Step s : Step.validSteps) {
			// 	csvWriter.print(s.name() + ",");
			// }
			// csvWriter.print("\nExpectedValues:,");
			// for (Step s : Step.validSteps) {
			// 	csvWriter.printf("%s,", ((double)StepToCount.get(s) / (double)Constants.GENERATION_SIZE));
			// }
			// for (Step s1 : Step.validSteps) {
			// 	csvWriter.printf("\nCovarianceWith:%s,", s1.name());
			// 	for (Step s2 : Step.validSteps) {
			// 		csvWriter.printf("%s,",
			// 				((double)StepPairToCount.get(s1).get(s2)/ (double)Constants.GENERATION_SIZE)-
			// 				(((double)StepToCount.get(s1)/ (double)Constants.GENERATION_SIZE)
			// 						* ((double)StepToCount.get(s2) / (double)Constants.GENERATION_SIZE)));
			// 				//Cov(X,Y) = E(X,Y) - E(X)E(Y)
			// 	}
			// }
			// for (Step s1 : Step.validSteps) {
			// 	csvWriter.printf("\nProbability_Given:%s,", s1.name());
			// 	for (Step s2 : Step.validSteps) {
			// 		if(PairStarts.get(s1)==0) {

			// 			csvWriter.print("0,");
			// 		}else {
			// 			csvWriter.printf("%s,",((double)SequentialPair.get(s1).get(s2)/PairStarts.get(s1)));
			// 		}
			// 	}
			// }
			// csvWriter.print("\nProbabilityEndsWith:,");
			// for (Step s1 : Step.validSteps) {
			// 	if(StepToCount.get(s1)==0) {
			// 		csvWriter.printf("%s,", 0);
			// 	}else {
			// 		csvWriter.printf("%s,", (((double)StepToCount.get(s1)-PairStarts.get(s1))/Constants.GENERATION_SIZE));
			// 	}
			// }
			
			// csvWriter.print("\n");
			// writeAvgVarCount(generations.get(gen),csvWriter);

		}

		csvWriter.print("\n");
	}

	public void rerunFinalGen(FitnessLandscape landscape2, PrintWriter csvWriter) {
		Generation genF = new Generation(generations.get(generations.size()-1),landscape2, phenotypicInheritanceMask, genotypicInheritanceMask,developmentalAdaptationMask,evolutionaryAdaptationMask);
		genF.runAllStrategies();
		csvWriter.print(SimulationHeader + "," + simNum + ",Landscape seed:," + landscape.landscapeSeed +",N value:,"+ landscape.n+ ",K value:," + landscape.k + "\n");
		csvWriter.print("RERUN_ON:,Landscape seed:," + landscape2.landscapeSeed +",N value:,"+ landscape2.n+ ",K value:," + landscape2.k + "\n");
		csvWriter.print(ProgramRowHeader);
		Agent bestOfGen = genF.getBestStrategyOfGeneration();
		for (String step : bestOfGen.programStringArray()) {
			csvWriter.print("," + step);
		}
		csvWriter.print("\n");

		for (int block = 0; block < Constants.BLOCKS; block++) {
			csvWriter.print(BlockRowHeader + "_" + block);
			for (String step : bestOfGen.blockStringArray(block)) {
				csvWriter.print("," + step);
			}
			csvWriter.print("\n");
		}

		csvWriter.print(TotalStrategyRowHeader);
		for (String step : bestOfGen.totalStrategyStringArray()) {
			csvWriter.print("," + step);
		}
		csvWriter.print("\n");

		// Write fitnesses to CSV
		csvWriter.print(FitnessRowHeader);
		for (double d : bestOfGen.fitnessArray) {
			csvWriter.print("," + d);
		}
		
		csvWriter.print("\n");
		writeAvgVarCount(genF,csvWriter);

	}

	public void rerunBestOfFinalGen(FitnessLandscape landscape2, PrintWriter csvWriter) {
		Generation genF = new Generation(generations.get(generations.size()-1).getBestStrategyOfGeneration(),landscape2, phenotypicInheritanceMask, genotypicInheritanceMask,developmentalAdaptationMask,evolutionaryAdaptationMask);
		genF.runAllStrategies();
		csvWriter.print(SimulationHeader + "," + simNum + ",Landscape seed:," + landscape.landscapeSeed +",N value:,"+ landscape.n+ ",K value:," + landscape.k + "\n");
		csvWriter.print("RERUN_ON:,Landscape seed:," + landscape2.landscapeSeed +",N value:,"+ landscape2.n+ ",K value:," + landscape2.k + "\n");
		Agent bestOfGen = genF.getBestStrategyOfGeneration();

		// Write fitnesses to CSV
		csvWriter.print(FitnessRowHeader);
		for (double d : bestOfGen.fitnessArray) {
			csvWriter.print("," + d);
		}
		csvWriter.print("\n");
		writeAvgVarCount(genF,csvWriter);
	}
	
	private void writeAvgVarCount(Generation g, PrintWriter csvWriter) {
		double[] avg = new double[Constants.BLOCK_LENGTH*Constants.PROGRAM_LENGTH];
		int count = g.getNumStrategies();
		double[] var = new double[Constants.BLOCK_LENGTH*Constants.PROGRAM_LENGTH];
		for(Agent a: g.agents) {
			count++;
			for(int i = 0; i < avg.length; i++) {
				avg[i]+=a.fitnessArray[i];
			}
		}
		for(int i = 0; i < avg.length; i++) {
			avg[i]/=count;
		}
		for(Agent a: g.agents) {
			for(int i = 0; i < var.length; i++) {
				var[i] += (avg[i]-a.fitnessArray[i])*(avg[i]-a.fitnessArray[i]);
			}
		}
		for(int i = 0; i < var.length; i++) {
			var[i]/=count;
		}
		csvWriter.print("Average_Fitness:");
		for (double d : avg) {
			csvWriter.print("," + d);
		}
		csvWriter.print("\n");
		csvWriter.print("Variance:");
		for (double d : var) {
			csvWriter.print("," + d);
		}
		csvWriter.print("\n");
		csvWriter.printf("Count:,%d\n", count);
	}
}
