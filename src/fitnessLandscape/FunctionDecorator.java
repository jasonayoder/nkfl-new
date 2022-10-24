package fitnessLandscape;

import java.util.function.Function;

public abstract class FunctionDecorator implements Landscape{
	Function<Float,Float> function;
	int min;
	int max;
	Landscape landscape;
	public FunctionDecorator (Function<Float,Float> function, Landscape landscape) {
		this.function = function;
		this.landscape = landscape;
		setMinMax();
	}
	
	public abstract void setMinMax();
	
	@Override
	public float getFitness(int genotype) {
		return function.apply(landscape.getFitness(genotype));
	}
	@Override
	public int getMinLocation() {
		return min;
	}
	@Override
	public int getMaxLocation() {
		return max;
	}

	@Override
	public int getN() {
		return landscape.getN();
	}
	
}
