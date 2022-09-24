
public class SumTempFitnessLandscape extends DynamicFitnessLandscape{
	public SumTempFitnessLandscape(int n, int k, int order, int num, double r,  int seed) {
		super(n, k, seed);
		s = new Template[num];
		delta = new double[num];
		this.r = r;
		this.order = order;
		for(int i = 0 ; i < s.length; i++) {
			s[i] = new Template(n,order);
		}
		for(int g = 0; g<1<<n;g++) {
			frange += super.fitness(g);
		}
		frange /= 1<<n;
		frange = 1- frange;
	}
	public SumTempFitnessLandscape(int n, int k, int order, int num, double r) {
		super(n, k);
		s = new Template[num];
		delta = new double[num];
		this.order = order;
		this.r = r;
		for(int i = 0 ; i < s.length; i++) {
			s[i] = new Template(n,order);
		}
		for(int g = 0; g<1<<n;g++) {
			frange += super.fitness(g);
		}
		frange /= 1<<n;
		frange = 1- frange;
	}
	Template[] s;
	double[] delta;
	int order;
	double r;
	double frange;
	
	public double fitness(int x) {
		double fit = super.fitness(x);
		for(int i = 0; i<s.length;i++) {
			if(s[i].isElement(x)) {
				fit+=delta[i];
			}
		}
		return fit;
	}
	@Override
	public void nextCycle() {
		for(int i = 0; i<s.length; i++) {
			s[i].permute(((double)order)/n);
			delta[i] = landscapeRnd.nextGaussian()*r*frange;
		}
	}
}
