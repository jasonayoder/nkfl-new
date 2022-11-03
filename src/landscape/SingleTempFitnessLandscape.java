package landscape;
public class SingleTempFitnessLandscape extends DynamicFitnessLandscape{
	Template s;
	int m;
	int order;
	public SingleTempFitnessLandscape(int n, int k, int order,  int seed) {
		super(n, k, seed);
		this.order = order;
		s = new Template(n,n);
		m = s.getRandom(0);
	}
	public SingleTempFitnessLandscape(int n, int k, int order) {
		super(n, k);
		this.order = order;
		s = new Template(n,n);
		m = s.getRandom(0);
	}
	@Override
	public void nextCycle() {
		s = new Template(n,order);
		m = s.getRandom(landscapeRnd.nextInt(1<<n));
	}
	public double fitness(int x) {
		if(s.isElement(x)) {
			return super.fitness(m);
		}
		return super.fitness(x);
	}
}
