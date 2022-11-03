package landscape;

public class TempPermFitnessLandscape extends DynamicFitnessLandscape{
	public TempPermFitnessLandscape(int n, int k, int order, int num,  int seed) {
		super(n, k, seed);
		s = new Template[num];
		m = new int[num];
		this.order = order;
		for(int i = 0 ; i < s.length; i++) {
			s[i] = new Template(n,order);
		}
	}
	public TempPermFitnessLandscape(int n, int k, int order, int num) {
		super(n, k);
		s = new Template[num];
		m = new int[num];
		this.order = order;
		for(int i = 0 ; i < s.length; i++) {
			s[i] = new Template(n,order);
		}
	}
	Template[] s;
	int[] m;
	int order;
	@Override
	public void nextCycle() {
		for(int i = 0; i<s.length; i++) {
			s[i].permute(((double)order)/n);
			m[i] = s[i].getRandom(super.landscapeRnd.nextInt(1<<n));
		}
	}
	public double fitness(int x) {
		for(int i = 0; i<s.length; i++) {
			if(s[i].isElement(x)) {
				return super.fitness(x^m[i]);
			}
		}
		return super.fitness(x);
	}
}
