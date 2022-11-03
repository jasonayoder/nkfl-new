package config;

/**
 * This file should list exactly the strings to be referenced from the code
 * base. It will load the user specified values from a configuration file and
 * other files that are not meant to be user adjustable.
 * 
 * @author Jason Yoder
 *
 */
public class Constants {

	// Properties (user configurable)
	public static final int STRATEGY_LENGTH = Integer.parseInt(PropParser.getProperty("strategyLength"));
	public static final String STRATEGY_STEPS = PropParser.getProperty("strategySteps");
	public static final String COMPARISON_STRATEGY = PropParser.getProperty("startingStrategy");
	
	public static final boolean EVOLVE_STRATEGY =Boolean.parseBoolean(PropParser.getProperty("evolveStrategy"));
	public static final boolean EVOLVE_GENOTYPE =Boolean.parseBoolean(PropParser.getProperty("evolveGenotype"));
	
	public static final int N_START = Integer.parseInt(PropParser.getProperty("n"));
	public static final int N_INCREMENT = Integer.parseInt(PropParser.getProperty("nIncrement"));
	public static final int N_INCREMENT_SIZE = Integer.parseInt(PropParser.getProperty("nIncrementSize"));
	
	public static final int K_START = Integer.parseInt(PropParser.getProperty("k"));
	public static final int K_INCREMENT = Integer.parseInt(PropParser.getProperty("kIncrement"));
	public static final int K_INCREMENT_SIZE = Integer.parseInt(PropParser.getProperty("kIncrementSize"));
	
	public static final String SELECTION_TYPE = PropParser.getProperty("selectionType");
	public static final int NUM_GENERATIONS = Integer.parseInt(PropParser.getProperty("numGenerations"));
	public static final int GENERATION_SIZE = Integer.parseInt(PropParser.getProperty("generationSize"));
	public static final double MUTATION_RATE = Double.parseDouble(PropParser.getProperty("mutationRate"));
	
	public static final int SEED = Integer.parseInt(PropParser.getProperty("seed"));
	
	public static final int LANDSCAPES = Integer.parseInt(PropParser.getProperty("landscapes"));
	public static final int STARTS_PER_LANDSCAPE = Integer.parseInt(PropParser.getProperty("startsPerLandscape"));
	public static final int RUNS_PER_START = Integer.parseInt(PropParser.getProperty("runsPerStart"));
	public static final int SAMPLES_PER_RUN = Integer.parseInt(PropParser.getProperty("samplesPerRun"));
	
	// Landscape Params
	public static final int LANDSCAPE_GENERATIONS_PER_CYCLE = Integer.parseInt(PropParser.getProperty("landscapeGenerationsPerCycle"));
	public static final String LANDSCAPE_NAME = PropParser.getProperty("landscapeName");
	public static final int LANDSCAPE_PARAMS =Integer.parseInt(PropParser.getProperty("landscapeParams"));
} 