package fitnessLandscape.dynamic;

import java.util.Random;

import fitnessLandscape.Landscape;
import fitnessLandscape.Template;

public class SingleTemp implements DynamicLandscape{
	Template s;
	int m, n, order;
	DynamicLandscape landscape;
	Random rand;
	
	public SingleTemp(DynamicLandscape landscape, Random rand, int order) {
		this.landscape = landscape;
		n = landscape.getN();
		s = new Template(n,n,rand);
		m = s.getRandom(0);
	}
	
	@Override
	public float getFitness(int genotype) {
		if(s.isElement(genotype)) {
			return landscape.getFitness(m);
		}
		return landscape.getFitness(genotype);
	}

	@Override
	public int getMinLocation() {
		int min = landscape.getMinLocation();
		if(s.isElement(min)) {
			if(m==min) {
				return m;
			}
			for(int i = 0; i<1<<getN(); i++) {
				if(getFitness(i)<getFitness(min)) {
					min = i;
				}
			}
			return min;
		}
		return min;
	}

	@Override
	public int getMaxLocation() {
		int max = landscape.getMaxLocation();
		if(s.isElement(max)) {
			if(m==max) {
				return m;
			}
			for(int i = 0; i<1<<getN(); i++) {
				if(getFitness(i)>getFitness(max)) {
					max = i;
				}
			}
			return max;
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
		s = new Template(n,order,rand);
		m = s.getRandom(rand.nextInt(1<<getN()));
	}
	

}
