import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class DynamicFitnessLandscapeAnalyzer {
	public static void main(String[] args) throws FileNotFoundException {
		DFLfitOverTime(new XorFitnessLandscape(15,10,.1,0),"fitnessOverCyclesXor.csv");
		DFLfitOverTime(new LERPFitnessLandscape(15,10,100,0),"fitnessOverCyclesLERP.csv");
		DFLfitOverTime(new PLTFitnessLandscape(15,10,.1,0),"fitnessOverCyclesPLT.csv");
		DFLfitOverTime(new CopyFitnessLandscape(15,10,.2,0),"fitnessOverCyclesCopy.csv");
		DFLfitOverTime(new TempPermFitnessLandscape(15,10,5,3,0),"fitnessOverCyclesTempPerm.csv");
		DFLfitOverTime(new SingleTempFitnessLandscape(15,10,1,0),"fitnessOverCyclesSingleTemp.csv");
		DFLfitOverTime(new SumTempFitnessLandscape(15,10,5,3,.1,0),"fitnessOverCyclesSumTemp.csv");		
	}
	
	public static void DFLfitOverTime(DynamicFitnessLandscape DFL, String filename) throws FileNotFoundException {
		long start = System.nanoTime();
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
		System.out.println(filename+": "+(System.nanoTime()-start));
	}
}
