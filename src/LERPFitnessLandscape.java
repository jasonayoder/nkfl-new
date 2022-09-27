
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
		return lerp(super.fitness(genotype),b.fitness(genotype));
	}
	
	@Override
	public void nextCycle() {
		cycle++;
		if(cycle>cycles) {
			cycle-=2*cycles;
		}
	}
	
	private double lerp(double a, double b) {
		if(cycle>=0) {
			return a + (cycle/cycles)*(b-a);
		}
		return a+(-cycle/cycles)*(b-a);
	}
	
}
