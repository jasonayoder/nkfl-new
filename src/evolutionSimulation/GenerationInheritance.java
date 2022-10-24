package evolutionSimulation;

import evolutionSimulation.learningStrategy.StrategyGeneration;

public interface GenerationInheritance {
	public StrategyGeneration getNext(StrategyGeneration parent);
}
