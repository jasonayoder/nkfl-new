package fitnessLandscape.dynamic;

import java.util.function.Function;

import fitnessLandscape.Landscape;

public class LERP implements DynamicLandscape{
	
	DynamicLandscape a;
	DynamicLandscape b;
	Function<Integer, Float> funct;
	int cycle;
	
	public LERP(DynamicLandscape a, DynamicLandscape b, int dur) throws Exception {
		if(a.getN()!=b.getN()) {
			throw new Exception("Landscape genotype ranges must match");
		}
		this.a = a;
		this.b = b;
		this.funct = new Function<Integer,Float>(){
			@Override
			public Float apply(Integer t) {
				if(t<=0) {
					return 0f;
				}
				if(t>=dur) {
					return 1f;
				}
				return t*1f/dur;
			}
		};
	}
	
	public LERP(DynamicLandscape a, DynamicLandscape b, Function<Integer,Float> funct) throws Exception {
		if(a.getN()!=b.getN()) {
			throw new Exception("Landscape genotype ranges must match");
		}
		this.a = a;
		this.b = b;
		this.funct = funct;
	}

	@Override
	public void nextCycle() {
		a.nextCycle();
		b.nextCycle();
		cycle++;
	}

	public float interpolate(float a, float b) {
		return (b-a)*funct.apply(cycle)+a;
	}
	
	@Override
	public float getFitness(int genotype) {
		return interpolate(a.getFitness(genotype),b.getFitness(genotype));
	}

	@Override
	public int getMinLocation() {
		int min = 0;
		for(int i = 0; i<1<<getN();i++) {
			if(getFitness(i)<getFitness(min)) {
				min = i;
			}
		}
		return min;
	}

	@Override
	public int getMaxLocation() {
		int max = 0;
		for(int i = 0; i<1<<getN();i++) {
			if(getFitness(i)>getFitness(max)) {
				max = i;
			}
		}
		return max;
	}

	@Override
	public int getN() {
		return a.getN();
	}

}
