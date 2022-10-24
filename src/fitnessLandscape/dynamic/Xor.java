package fitnessLandscape.dynamic;

import java.util.Random;

import fitnessLandscape.Landscape;

public class Xor implements DynamicLandscape{

	int m = 0;
	float r;
	Random rand;
	DynamicLandscape landscape;
	
	public Xor (DynamicLandscape landscape, Random rand, float r) {
		this.landscape = landscape;
		this.rand = rand;
		this.r = r; 
	}
	
	@Override
	public float getFitness(int genotype) {
		return landscape.getFitness(genotype^m);
	}

	@Override
	public int getMinLocation() {
		return landscape.getMinLocation()^m;
	}

	@Override
	public int getMaxLocation() {
		return landscape.getMaxLocation()^m;
	}

	@Override
	public void nextCycle() {
		landscape.nextCycle();
		for(int i =0; i<getN()*r; i++) {
			m ^= 1<<rand.nextInt(getN());
		}
	}

	@Override
	public int getN() {
		return landscape.getN();
	}
	
}
