package fitnessLandscape.dynamic;

import java.util.Random;

import fitnessLandscape.Landscape;

public class Copy implements DynamicLandscape{

	int[] B;
	float r;
	DynamicLandscape landscape;
	Random rand;
	
	public Copy(DynamicLandscape landscape, Random rand, float r) {
		this.rand = rand;
		this.landscape = landscape;
		this.r = r;
		B = new int[1<<landscape.getN()];
		for(int i = 0; i<B.length;i++) {
			B[i] = i;
		}
	}
	
	
	@Override
	public float getFitness(int genotype) {
		return landscape.getFitness(B[genotype]);
	}

	@Override
	public int getMinLocation() {
		int min = 0;
		for(int i = 0; i<1<<getN(); i++) {
			if(getFitness(i)<getFitness(min)) {
				min = i;
			}
		}
		return min;
	}

	@Override
	public int getMaxLocation() {
		int max = 0;
		for(int i = 0; i<1<<getN(); i++) {
			if(getFitness(i)>getFitness(max)) {
				max = i;
			}
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
		for(int i = 0; i<r*(1<<getN()); i++) {
			int a = rand.nextInt(1<<getN());
			int b = rand.nextInt(1<<getN());
			B[a] = b;
		}
	}
	
}
