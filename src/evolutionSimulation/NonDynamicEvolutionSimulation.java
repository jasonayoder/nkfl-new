package evolutionSimulation;

import evolutionSimulation.learningStrategy.StrategyGeneration;

public class NonDynamicEvolutionSimulation extends EvolutionSimulation{
	int strategyRuns;
	
	public NonDynamicEvolutionSimulation(GenerationInheritance inheritance, StrategyGeneration gen0,GenerationInheritance comparisonInheritance,StrategyGeneration[] comp0, int numGens, int strategyRuns) {
		this.inheritance = inheritance;
		this.comparisonInheritance = comparisonInheritance;
		generations = new StrategyGeneration[numGens];
		generations[0] = gen0;
		comparisonStrategies = new StrategyGeneration[numGens][comp0.length];
		comparisonStrategies[0] = comp0;
		this.strategyRuns = strategyRuns;
	}
	
	public void run() {
		for(int i = 0; i<generations.length; i++) {
			generations[i].runAllStrategies(strategyRuns);
			generations[i+1] = inheritance.getNext(generations[i]);
			for(int j = 0; j<comparisonStrategies[0].length; j++) {
				comparisonStrategies[i][j].runAllStrategies(strategyRuns);
				comparisonStrategies[i+1][j] = comparisonInheritance.getNext(comparisonStrategies[i][j]);
			}
		}
	}
}
