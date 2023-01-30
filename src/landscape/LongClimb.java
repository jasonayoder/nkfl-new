package landscape;

public class LongClimb extends DynamicFitnessLandscape {

	public LongClimb(int n, int k, int seed) {
		super(2,2,seed);
		this.fitTable = null;
		this.visitedTable = null;
		this.n = n;
		this.k = k;
	}
	//n is number of bits
	
	int sp = n/2;
	int lcode = 10000;
	@Override
	public double fitness(int genotype) {
		if(genotype)
	}
	
	

	@Override
	public void nextCycle() {
		//not dynamic, so we don't do anything
	}

}
