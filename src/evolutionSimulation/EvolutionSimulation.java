package evolutionSimulation;

import java.io.IOException;

import evolutionSimulation.learningStrategy.StrategyGeneration;

public abstract class EvolutionSimulation {
	GenerationInheritance inheritance;
	StrategyGeneration[] generations;
	StrategyGeneration[][] comparisonStrategies;
	String[] comparisonNames;
	GenerationInheritance comparisonInheritance;
	public abstract void run() throws IOException;
}
