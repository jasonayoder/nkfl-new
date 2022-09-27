import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

public class DynamicFitnessLandscapeAnalyzer {
	public static void main(String[] args) throws IOException {
//		DFLfitOverTime(new XorFitnessLandscape(15,10,.1,0),"fitnessOverCyclesXor.csv");
//		DFLfitOverTime(new LERPFitnessLandscape(15,10,100,0),"fitnessOverCyclesLERP.csv");
		DFLfitOverTime(new DERPFitnessLandscape(15,10,100,0),"fitnessOverCyclesDERP.csv");
//		DFLfitOverTime(new PLTFitnessLandscape(15,10,.1,0),"fitnessOverCyclesPLT.csv");
//		DFLfitOverTime(new CopyFitnessLandscape(15,10,.2,0),"fitnessOverCyclesCopy.csv");
//		DFLfitOverTime(new TempPermFitnessLandscape(15,10,5,3,0),"fitnessOverCyclesTempPerm.csv");
//		DFLfitOverTime(new SingleTempFitnessLandscape(15,10,1,0),"fitnessOverCyclesSingleTemp.csv");
//		DFLfitOverTime(new SumTempFitnessLandscape(15,10,5,3,.1,0),"fitnessOverCyclesSumTemp.csv");	
//		DFLLocalMaximaOverTime(new XorFitnessLandscape(15,10,.1,0),"LocalMaximaOverCyclesXor.csv");
//		DFLLocalMaximaOverTime(new LERPFitnessLandscape(15,10,100,0),"LocalOverCyclesLERP.csv");
	}
	
	public static void DFLfitOverTime(DynamicFitnessLandscape DFL, String filename) throws IOException {
		long start = System.nanoTime();
		PrintWriter writer = new PrintWriter (new BufferedWriter(new FileWriter(filename)));
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
	
	public static void DFLLocalMaximaOverTime(DynamicFitnessLandscape DFL, String filename) throws IOException {
		long start = System.nanoTime();
		PrintWriter writer = new PrintWriter (new BufferedWriter(new FileWriter(filename)));
		for(int i = 0; i<100; i++) {
			boolean isFirst = true;
			for(int g = 1; g<1<<15;g++) {
				if(DFL.isLocalMaxima(g)) {
					if(!isFirst) {
						writer.print(",");
					}else {
						isFirst = false;
					}
					writer.print(DFL.fitness(g));
				}
			}
			writer.println();
			DFL.nextCycle();
		}
		writer.flush();
		writer.close();
		System.out.println(filename+": "+(System.nanoTime()-start));
	}
}
