package fitnessLandscape;

public class NormalizationDecorator implements Landscape{
	float min;
	float max;
	Landscape landscape;
	
	public NormalizationDecorator(Landscape landscape, int min, int max) {
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
	
}
