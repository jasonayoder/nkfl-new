
public class PointSwapFitnessLandscape extends DynamicFitnessLandscape{
	int cycle = 0;
	int timeToSwap;
	FitnessLandscape b;
	public PointSwapFitnessLandscape(int n, int k, int timeToSwap, int seed) {
		super(n, k, seed);
		b = new FitnessLandscape(n,k,seed+1);
		this.timeToSwap = timeToSwap;
	}

	public PointSwapFitnessLandscape(int n, int k, int timeToSwap) {
		super(n, k);
		b = new FitnessLandscape(n,k);
	}
	
	@Override
	public void nextCycle() {
		cycle++;
	}
	
	public int maxLoc() {
		if(cycle<timeToSwap) {
			return b.maxLoc();
		}else {
			return super.maxLoc();
		}	
	}
	
	public double fitness(int genotype) {
		if(cycle<timeToSwap) {
			return b.fitness(genotype);
		}else {
			return super.fitness(genotype);
		}
	}
	
}
