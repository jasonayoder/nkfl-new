
public class PLTFitnessLandscape extends DynamicFitnessLandscape{
	int r;
	int[] B;
	public PLTFitnessLandscape (int n, int k, double r, int seed) {
		super(n, k, seed);
		this.r = (int)(r*n);
		B = new int[1<<n];
		for(int i = 0; i<B.length;i++) {
			B[i] = i;
		}
	}
	public PLTFitnessLandscape (int n, int k, double r) {
		super(n, k);
		this.r = (int)(r*(1<<n));
		B = new int[1<<n];
		for(int i = 0; i<B.length;i++) {
			B[i] = i;
		}
	}
	@Override
	public void nextCycle() {
		for(int i = 0; i<r; i++) {
			int a = super.landscapeRnd.nextInt(1<<n);
			int b = super.landscapeRnd.nextInt(1<<n);
			int temp = B[a];
			B[a] = B[b];
			B[b] = temp;
		}
	}
	
	public double fitness(int genotype) {
		return super.fitness(B[genotype]);
	}
}
