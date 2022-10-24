package evolutionSimulation.learningStrategy;

import java.util.Arrays;
import java.util.Random;

import fitnessLandscape.Landscape;

public class StrategyGeneration {
	public LearningStrategy[] strategies;
	
	public StrategyGeneration(Landscape landscape, int numStrategies, int strategyLength, Step[] stepTypes, Random rand, boolean oneStart){
		strategies = new LearningStrategy[numStrategies];
		int start = rand.nextInt(1<<landscape.getN());
		for(int i = 0; i<strategies.length; i++) {
			strategies[i] = new LearningStrategy(landscape, strategyLength, start, stepTypes, rand);
			if(!oneStart) {
				start = rand.nextInt(1<<landscape.getN());
			}
		}
	}
	
	public StrategyGeneration(LearningStrategy[] strategies){
		this.strategies = strategies;
	}
	
	
	public void runAllStrategies(int sampleSize) {
		for(LearningStrategy strategy : strategies)
		{
			strategy.apply(sampleSize);
		}
		Arrays.sort(strategies);
	}
}
