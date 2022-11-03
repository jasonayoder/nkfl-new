package landscape;

public abstract class DynamicFitnessLandscape extends FitnessLandscape{

	public DynamicFitnessLandscape(int n, int k) {
		super(n, k);
	}

	public DynamicFitnessLandscape(int n, int k, int seed) {
		super(n, k,seed);
	}
	public abstract void nextCycle();
	
}
