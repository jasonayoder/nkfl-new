
public class NonDynamicFitnessLandscape extends DynamicFitnessLandscape{
	public NonDynamicFitnessLandscape(int n, int k) {
		super(n, k);
	}

	public NonDynamicFitnessLandscape(int n, int k, int seed) {
		super(n, k,seed);
	}
	public void nextCycle() {}
}
