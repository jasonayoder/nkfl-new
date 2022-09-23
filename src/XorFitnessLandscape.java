import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class XorFitnessLandscape extends FitnessLandscape{
	
	public static void main(String[] args) throws FileNotFoundException {
		XorFitnessLandscape xor = new XorFitnessLandscape(15,10,.1,0);
		PrintWriter writer = new PrintWriter (new File("fitnessOverCycles.csv"));
		for(int i = 0; i<100; i++) {
			writer.print(xor.fitness(0));
			for(int g = 1; g<1<<15;g++) {
				writer.print(","+xor.fitness(g));
			}
			writer.println();
			xor.nextCycle();
		}
		writer.flush();
		writer.close();
	}
	
	
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
