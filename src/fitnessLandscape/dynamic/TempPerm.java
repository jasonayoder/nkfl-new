package fitnessLandscape.dynamic;

import java.util.Random;

import fitnessLandscape.Landscape;
import fitnessLandscape.Template;

public class TempPerm implements DynamicLandscape{
	
	
	Template[] s;
	int[] m;
	int order;
	DynamicLandscape landscape;
	Random rand;
	
	public TempPerm(DynamicLandscape landscape, Random rand, int order, int num) {
		this.landscape = landscape;
		this.rand = rand;
		s = new Template[num];
		this.order = order;
		for(int i = 0 ; i < s.length; i++) {
			s[i] = new Template(getN(),order,rand);
		}
		this.m = new int[num];
	}
	

	@Override
	public float getFitness(int genotype) {
		for(int i = 0; i<s.length; i++) {
			if(s[i].isElement(genotype)) {
				return landscape.getFitness(m[i]);
			}
		}
		return landscape.getFitness(genotype);
	}

	@Override
	public int getMinLocation() {
		int min = landscape.getMinLocation();
		for(int i = 0; i<s.length; i++) {
			if(s[i].isElement(min)) {
				if(m[i]==min) {
					return m[i];
				}
				for(int j = 0; j<1<<getN(); j++) {
					if(getFitness(j)<getFitness(min)) {
						min = j;
					}
				}
				return min;
			}
		}
		return min;
	}

	@Override
	public int getMaxLocation() {
		int max = landscape.getMaxLocation();
		for(int i = 0; i<s.length; i++) {
			if(s[i].isElement(max)) {
				if(m[i]==max) {
					return m[i];
				}
				for(int j = 0; j<1<<getN(); j++) {
					if(getFitness(j)>getFitness(max)) {
						max = j;
					}
				}
				return max;
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
			m[i] = s[i].getRandom(rand.nextInt(1<<getN()));
		}
	}

}
