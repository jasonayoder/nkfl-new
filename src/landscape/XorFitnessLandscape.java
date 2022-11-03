package landscape;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class XorFitnessLandscape extends DynamicFitnessLandscape{
	
	
	int m = 0;
	int e = 0;
	int r;
	public XorFitnessLandscape (int n, int k, double r, int seed) {
		super(n, k,seed);
		this.r = (int)(r*n);
	}
	public XorFitnessLandscape (int n, int k, double r) {
		super(n, k);
		this.r = (int)(r*n);
	}
	public void nextCycle() {
		e++;
		for(int i = 0; i<r; i++) {
			m^=1<<super.landscapeRnd.nextInt(n);
		}
	}
	
	public double fitness(int genotype) {
		return super.fitness(genotype^m);
	}
}
