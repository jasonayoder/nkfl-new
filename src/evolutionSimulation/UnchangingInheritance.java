package evolutionSimulation;

import evolutionSimulation.learningStrategy.StrategyGeneration;

public class UnchangingInheritance implements GenerationInheritance{

	@Override
	public StrategyGeneration getNext(StrategyGeneration parent) {
		return parent;
	}
	
}
