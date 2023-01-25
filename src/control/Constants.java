package control;

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
	public static final int PROGRAM_LENGTH = Integer.parseInt(PropParser.getProperty("programLength"));
	public static final String COMPARISON_PROGRAM = PropParser.getProperty("comparisonProgram");
	public static final String STARTING_GENERATION = PropParser.getProperty("startingGeneration");
	public static final int STARTING_GENERATION_INDEX = Integer.parseInt(PropParser.getProperty("startingGenerationIndex"));
	
	public static final int BLOCKS = Integer.parseInt(PropParser.getProperty("blocks"));
	public static final int BLOCK_LENGTH = Integer.parseInt(PropParser.getProperty("blockLength"));
	public static final String BLOCK_STEPS = PropParser.getProperty("blockSteps");
	
	public static final int TOTAL_LENGTH = PROGRAM_LENGTH * BLOCK_LENGTH;
	
	public static final int N_START = Integer.parseInt(PropParser.getProperty("n"));
	public static final int N_INCREMENT = Integer.parseInt(PropParser.getProperty("nIncrements"));
	public static final int N_INCREMENT_SIZE = Integer.parseInt(PropParser.getProperty("nIncrementSize"));
	
	public static final int K_START = Integer.parseInt(PropParser.getProperty("k"));
	public static final int K_INCREMENT = Integer.parseInt(PropParser.getProperty("kIncrements"));
	public static final int K_INCREMENT_SIZE = Integer.parseInt(PropParser.getProperty("kIncrementSize"));
	
	public static final String SELECTION_TYPE = PropParser.getProperty("selectionType");
	public static final int NUM_GENERATIONS = Integer.parseInt(PropParser.getProperty("numGenerations"));
	public static final int GENERATION_SIZE = Integer.parseInt(PropParser.getProperty("generationSize"));
	public static final int ELITISM = Integer.parseInt(PropParser.getProperty("elitism"));
	public static final int TOURNAMENT_SIZE = Integer.parseInt(PropParser.getProperty("tournamentSize"));
	
	public static final double PLASTICITY_MUTATION_RATE = Double.parseDouble(PropParser.getProperty("plasticityMutationRate"));
	public static final double PROGRAM_MUTATION_RATE = Double.parseDouble(PropParser.getProperty("programMutationRate"));
	public static final double BLOCK_MUTATION_RATE = Double.parseDouble(PropParser.getProperty("blockMutationRate"));
	public static final double GENOTYPE_MUTATION_RATE = Double.parseDouble(PropParser.getProperty("genotypeMutationRate"));
	public static final double BLOCK_OVERWRITE_ANY = Double.parseDouble(PropParser.getProperty("blockOverwriteAny"));
	public static final double BLOCK_OVERWRITE_UNUSED = Double.parseDouble(PropParser.getProperty("blockOverwriteUnused"));
	
	public static final int SEED = Integer.parseInt(PropParser.getProperty("seed"));
	
	public static final int LANDSCAPES = Integer.parseInt(PropParser.getProperty("landscapes"));
	public static final int RUNS_PER_LANDSCAPE = Integer.parseInt(PropParser.getProperty("runsPerLandscape"));
	public static final boolean SINGLE_START = Boolean.parseBoolean(PropParser.getProperty("singleStart"));
	public static final int STARTING_LOCATION = Integer.parseInt(PropParser.getProperty("startLocation"));
	public static final int SAMPLES_PER_RUN = Integer.parseInt(PropParser.getProperty("samplesPerRun"));
	
	public static final int PHENOTYPIC_COUNT = Integer.parseInt(PropParser.getProperty("phenotypicCount"));
	public static final int GENOTYPIC_COUNT = Integer.parseInt(PropParser.getProperty("genotypicCount"));
	public static final int EPIGENETIC_COUNT = Integer.parseInt(PropParser.getProperty("epigeneticCount"));
	public static final int[] PLASTICITY_INIT = parstIntegerArray(PropParser.getProperty("plasticityInit"));
	
	public static int[] parstIntegerArray(String array) {
		if(array==null) {
			return new int[0];
		}
		String[] elements = array.split("(, *)|( +)");
		int[] ret = new int[elements.length];
		for(int i = 0; i< ret.length; i++) {
			ret[i] = Integer.parseInt(elements[i]);
		}
		return ret;
	}
	
	// Landscape Params
	public static final int LANDSCAPE_GENERATIONS_PER_CYCLE = Integer.parseInt(PropParser.getProperty("landscapeGenerationsPerCycle"));
	public static final String LANDSCAPE_NAME = PropParser.getProperty("landscapeName");
	public static final String LANDSCAPE_PARAMS =PropParser.getProperty("landscapeParams");
	
	//File Params
	public static final String FILENAME = PropParser.getProperty("filename");
	public static final int INCREMENT_CSV = Integer.parseInt(PropParser.getProperty("incrementCSVoutput"));
	public static final boolean OUTPUT_GENSTR = Boolean.parseBoolean(PropParser.getProperty("outputGenstr"));
	
	
	//Rerun Params
	

	public static final String FILENAME3 = PropParser.getProperty("filename3");
	public static final String FILENAME2 = PropParser.getProperty("filename2");
	public static final int N_START2 = Integer.parseInt(PropParser.getProperty("n2"));
	public static final int N_INCREMENT2 = Integer.parseInt(PropParser.getProperty("nIncrements2"));
	public static final int N_INCREMENT_SIZE2 = Integer.parseInt(PropParser.getProperty("nIncrementSize2"));
	
	public static final int K_START2 = Integer.parseInt(PropParser.getProperty("k2"));
	public static final int K_INCREMENT2 = Integer.parseInt(PropParser.getProperty("kIncrements2"));
	public static final int K_INCREMENT_SIZE2 = Integer.parseInt(PropParser.getProperty("kIncrementSize2"));
	
} 