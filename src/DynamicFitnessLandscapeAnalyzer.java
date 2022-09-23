import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class DynamicFitnessLandscapeAnalyzer {
	public static void main(String[] args) throws FileNotFoundException {
		DFLfitOverTime(new XorFitnessLandscape(15,10,.1,0),"fitnessOverCyclesXor.csv");
//		DFLfitOverTime(new LERPFitnessLandscape(15,10,100,0),"fitnessOverCyclesLERP.csv");
	}
	
	public static void DFLfitOverTime(DynamicFitnessLandscape DFL, String filename) throws FileNotFoundException {
		PrintWriter writer = new PrintWriter (new File(filename));
		for(int i = 0; i<100; i++) {
			double dfl = DFL.fitness(0);
			writer.print(dfl);
			for(int g = 1; g<1<<15;g++) {
				dfl = DFL.fitness(g);
				writer.print(",");
				writer.print(dfl);
			}
			writer.println();
			DFL.nextCycle();
		}
		writer.flush();
		writer.close();
	}
}
