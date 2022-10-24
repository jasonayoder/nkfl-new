package fitnessLandscape.dynamic;

import java.util.Random;

import fitnessLandscape.Landscape;
import fitnessLandscape.Template;

public class SumTemp implements DynamicLandscape{

	Template[] s;
	float[] delta;
	int order;
	float r;
	float frange;
	DynamicLandscape landscape;
	Random rand;
	
	public SumTemp(DynamicLandscape landscape, Random rand, int order, int num, float r) {
		this.landscape = landscape;
		this.rand = rand;
		s = new Template[num];
		delta = new float[num];
		this.order = order;
		this.r = r;
		for(int i = 0 ; i < s.length; i++) {
			s[i] = new Template(getN(),order,rand);
		}
		for(int g = 0; g<1<<landscape.getN();g++) {
			frange += landscape.getFitness(g);
		}
		frange /= 1<<landscape.getN();
		frange = 1 - frange;
	}
	
	
	@Override
	public float getFitness(int genotype) {
		float fit = landscape.getFitness(genotype);
		for(int i = 0; i<s.length;i++) {
			if(s[i].isElement(genotype)) {
				fit+=delta[i];
			}
		}
		return fit;
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
		for(int i = 0; i<s.length; i++) {
			s[i].permute(order/getN());
			delta[i] = (float) (rand.nextGaussian()*r*frange);
		}
	}

}
