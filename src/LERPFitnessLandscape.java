
public class LERPFitnessLandscape extends DynamicFitnessLandscape{

	FitnessLandscape b;
	double cycles;
	int cycle = 0;
	
	public LERPFitnessLandscape(int n, int k, int cycles,int seed) {
		super(n,k,seed);
		b = new FitnessLandscape(n,k,super.landscapeRnd.nextInt());
		this.cycles = cycles;
	}
	
	public LERPFitnessLandscape(int n, int k, int cycles) {
		super(n,k);
		b = new FitnessLandscape(n,k,super.landscapeRnd.nextInt());
		this.cycles = cycles;
	}

	public double fitness(int genotype) {
		return lerp(super.fitness(genotype),b.fitness(genotype),(cycle/cycles));
	}
	
	@Override
	public void nextCycle() {
		cycle++;
		
	}
	
	private double lerp(double a, double b, double c) {
		return a + c*(b-a);
	}
	
}
