package evolutionSimulation.learningStrategy;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

import fitnessLandscape.Landscape;

public class LearningStrategy implements Comparable<LearningStrategy>{
	public Step[] steps;
	public LinkedList<Integer> lookedLocation;
	public float[][] fitnesses;
	public Landscape landscape;
	public Step[] stepTypes;
	public int phenotype;
	public int genotype;
	public Random rand;
	
	public LearningStrategy(Landscape landscape, Step[] steps, int genotype, Step[] stepTypes, Random rand) {
		this.steps = steps;
		this.landscape = landscape;
		this.genotype = genotype;
		this.phenotype = genotype;
		this.rand = rand;
		this.stepTypes = stepTypes;
	}
	
	public LearningStrategy(Landscape landscape, int numSteps, int genotype, Step[] stepTypes, Random rand) {
		steps = new Step[numSteps];
		this.landscape = landscape;
		this.genotype = genotype;
		this.phenotype = genotype;
		this.rand = rand;
		this.stepTypes = stepTypes;
		for(int i = 0; i<steps.length;i++) {
			this.steps[i] = getRandomStep();
		}
	}
	
	public LearningStrategy(Landscape landscape, int[] numSteps, int genotype, Step[] stepTypes, Random rand) {
		this.landscape = landscape;
		this.genotype = genotype;
		this.phenotype = genotype;
		this.rand = rand;
		this.stepTypes = stepTypes;
		int total = 0;
		for(int i = 0; i<numSteps.length; i++) {
			total+=numSteps[i];
		}
		this.steps = new Step[total];
		total=0;
		for(int i = 0; i<numSteps.length; i++) {
			for(int j = 0; j<numSteps[i]; j++) {
				total++;
				int index = rand.nextInt(total);
				Step temp = steps[index];
				steps[index] = stepTypes[i];
				steps[total-1] = temp;
			}
		}
	}
	
	Step getRandomStep() {
		int rnd = rand.nextInt(stepTypes.length);
		return stepTypes[rnd];
	}
	
	public Step getRandomStep(Step s) throws Exception {
		int rnd = rand.nextInt(stepTypes.length);
		if(stepTypes.length<=1) {
			throw new Exception("not enough step types");
		}
		while(stepTypes[rnd].equals(s)) {
			rnd = rand.nextInt(stepTypes.length);
		}
		return stepTypes[rnd];
	}
	
	public void apply(int sampleSize) {
		fitnesses = new float[sampleSize][steps.length+1];
		phenotype = genotype;
		for(int i = 0; i<sampleSize; i++) {
			fitnesses[i][0] = landscape.getFitness(genotype);
			HashSet<Integer> lookedLocations = new HashSet<Integer>();
			for(int j = 0; j<steps.length; j++) {
				phenotype = steps[j].apply(landscape, phenotype, lookedLocations, rand);
				fitnesses[i][j+1] = landscape.getFitness(phenotype);
			}
		}
	}

	@Override
	public int compareTo(LearningStrategy o) {
		if(landscape.getFitness(phenotype)>o.landscape.getFitness(o.phenotype)) {
			return 1;
		}else if(landscape.getFitness(phenotype)==o.landscape.getFitness(o.phenotype)) {
			return 0;
		}
		return -1;
	}
}
