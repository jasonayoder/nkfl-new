package fitnessLandscape.dynamic;


public class DynamicNormalizationDecorator implements DynamicLandscape{
	float min;
	float max;
	DynamicLandscape landscape;
	
	public DynamicNormalizationDecorator(DynamicLandscape landscape, int min, int max) {
		this.landscape = landscape;
		this.min = min;
		this.max = max;
	}
	
	@Override
	public float getFitness(int genotype) {
		return (max-min)*(landscape.getFitness(genotype)-landscape.getFitness(landscape.getMinLocation()))/(landscape.getFitness(landscape.getMaxLocation())-landscape.getFitness(landscape.getMinLocation()))+min;
	}

	@Override
	public int getMinLocation() {
		return landscape.getMinLocation();
	}

	@Override
	public int getMaxLocation() {
		return landscape.getMaxLocation();
	}

	@Override
	public int getN() {
		return landscape.getN();
	}

	@Override
	public void nextCycle() {
		landscape.nextCycle();
	}
}
