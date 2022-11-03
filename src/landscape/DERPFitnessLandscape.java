package landscape;
public class DERPFitnessLandscape extends LERPFitnessLandscape{
	double min = 0f;
	double max = 1f;
	public DERPFitnessLandscape(int n, int k, int cycles,int seed) {
		super(n,k,cycles,seed);
	}
	
	public DERPFitnessLandscape(int n, int k, int cycles) {
		super(n,k,cycles);
	}
	
	public double fitness(int genotype) {
		return derp(super.fitness(genotype));
	}
	
	private double derp(double fitness) {
		return (fitness-min)/(max-min);
	}

	public void nextCycle() {
		super.nextCycle();
		min = 0;
		max = 1;
		double temp = this.maxFit();
		min = this.minFit();
		max = temp;
	}
}
