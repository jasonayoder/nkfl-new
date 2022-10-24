package evolutionSimulation;

import java.io.IOException;

import evolutionSimulation.learningStrategy.StrategyGeneration;
import fitnessLandscape.dynamic.DynamicLandscape;
import output.GenerationWriter;
import output.LandscapeWriter;

public class DynamicEvolutionSimulation extends EvolutionSimulation{
	int strategyRuns;
	DynamicLandscape landscape;
	int tau;
	LandscapeWriter landscapeWriter;
	GenerationWriter generationWriter;
	
	public DynamicEvolutionSimulation(GenerationInheritance inheritance, StrategyGeneration gen0,GenerationInheritance comparisonInheritance,StrategyGeneration[] comp0, int numGens, int strategyRuns, DynamicLandscape landscape, int tau) {
		this.inheritance = inheritance;
		this.comparisonInheritance = comparisonInheritance;
		generations = new StrategyGeneration[numGens];
		generations[0] = gen0;
		comparisonStrategies = new StrategyGeneration[numGens][comp0.length];
		comparisonStrategies[0] = comp0;
		this.strategyRuns = strategyRuns;
		this.landscape = landscape;
		this.tau = tau;
	}
	public DynamicEvolutionSimulation(GenerationInheritance inheritance, StrategyGeneration gen0,GenerationInheritance comparisonInheritance,StrategyGeneration[] comp0, int numGens, int strategyRuns, DynamicLandscape landscape, int tau, LandscapeWriter landscapeWriter, GenerationWriter generationWriter) {
		this.inheritance = inheritance;
		this.comparisonInheritance = comparisonInheritance;
		generations = new StrategyGeneration[numGens];
		generations[0] = gen0;
		comparisonStrategies = new StrategyGeneration[numGens][comp0.length];
		comparisonStrategies[0] = comp0;
		this.strategyRuns = strategyRuns;
		this.landscape = landscape;
		this.tau = tau;
		this.generationWriter = generationWriter;
		this.landscapeWriter = landscapeWriter;
	}
	
	public void run() throws IOException {
		for(int i = 0; i<generations.length; i++) {
			generations[i].runAllStrategies(strategyRuns);
			generations[i+1] = inheritance.getNext(generations[i]);
			if(generationWriter!=null) {
				generationWriter.write(generations[i],i);
			}
			for(int j = 0; j<comparisonStrategies[0].length; j++) {
				comparisonStrategies[i][j].runAllStrategies(strategyRuns);
				comparisonStrategies[i+1][j] = comparisonInheritance.getNext(comparisonStrategies[i][j]);
				if(generationWriter!=null) {
					generationWriter.write(comparisonStrategies[i][j],i);
				}
			}
			if(landscapeWriter!=null) {
				landscapeWriter.write(landscape,i);
			}
			if(i%tau==0) {
				landscape.nextCycle();
			}
		}
	}
}
