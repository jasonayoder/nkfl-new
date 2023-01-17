package landscape;

public class EvenOddLandscape extends DynamicFitnessLandscape{
	int cycle = 0;
	FitnessLandscape b;
	public EvenOddLandscape(int n, int k, int seed) {
		super(n, k, seed);
		b = new FitnessLandscape(n,k,seed+1);
	}

	public EvenOddLandscape(int n, int k) {
		super(n, k);
		b = new FitnessLandscape(n,k);
	}
	
	@Override
	public void nextCycle() {
		cycle++;
	}
	

	
	public double fitness(int genotype) {
		if(cycle%2==0) {
			return b.fitness(genotype);
		}else {
			return super.fitness(genotype);
		}
	}
}
