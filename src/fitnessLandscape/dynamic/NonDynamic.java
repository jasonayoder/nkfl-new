package fitnessLandscape.dynamic;

import fitnessLandscape.Landscape;

public class NonDynamic implements DynamicLandscape{

	Landscape landscape;
	public NonDynamic(Landscape landscape) {
		this.landscape = landscape;
	}
	
	@Override
	public float getFitness(int genotype) {
		return landscape.getFitness(genotype);
	}

	@Override
	public int getMinLocation() {
		return landscape.getMinLocation();
	}

	@Override
	public int getMaxLocation() {
		return landscape.getMinLocation();
	}

	@Override
	public int getN() {
		return landscape.getN();
	}

	@Override
	public void nextCycle() {}

}
