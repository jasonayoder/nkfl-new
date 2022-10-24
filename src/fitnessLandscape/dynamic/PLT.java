package fitnessLandscape.dynamic;

import java.util.Random;

import fitnessLandscape.Landscape;

public class PLT implements DynamicLandscape{
	float r;
	int[] B;
	DynamicLandscape landscape;
	Random rand;
	
	public PLT(DynamicLandscape landscape, Random rand, float r) {
		this.landscape = landscape;
		this.rand = rand;
		this.r = r;
		B = new int[1<<landscape.getN()];
		for(int i = 0; i<B.length;i++) {
			B[i] = i;
		}
	}

	@Override
	public float getFitness(int genotype) {
		return getFitness(B[genotype]);
	}
	@Override
	public int getMinLocation() {
		int min = landscape.getMinLocation();
		while(B[min]!=landscape.getMinLocation()) {
			min = B[min];
		}
		return min;
	}

	@Override
	public int getMaxLocation() {
		int max = landscape.getMaxLocation();
		while(B[max]!=landscape.getMaxLocation()) {
			max = B[max];
		}
		return max;
	}

	@Override
	public int getN() {
		return landscape.getN();
	}

	@Override
	public void nextCycle() {
		landscape.nextCycle();
		for(int i = 0; i<r; i++) {
			int a = rand.nextInt(1<<getN());
			int b = rand.nextInt(1<<getN());
			int temp = B[a];
			B[a] = B[b];
			B[b] = temp;
		}
	}

}
