package fitnessLandscape.NK;

import java.util.Random;

import fitnessLandscape.Landscape;

public abstract class AbstractNKLandscape implements Landscape {
	int min;
	int max;
	final int N;
	final int K;
	float[] fitnessTable;
	Random rand;
	
	public AbstractNKLandscape(int N, int K, Random rand) {
		this.N = N;
		this.K = K;
		this.rand = rand;
		fitnessTable = new float[1<<N];
	}
	
	abstract void generateFitnessTable();
	
	public float getFitness(int genotype) {
		return fitnessTable[genotype];
	}
	
	// avoid searching entire space each time
	public int getMinLocation() {
		return min;
	}
	// avoid searching entire space each time
	public int getMaxLocation() {
		return max;
	}
	
	// the greatest legal genotype is less than 1<<N
	public int getN() {
		return N;
	}
}
