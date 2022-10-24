package evolutionSimulation;

import java.util.Random;

import evolutionSimulation.learningStrategy.LearningStrategy;
import evolutionSimulation.learningStrategy.Step;
import evolutionSimulation.learningStrategy.StrategyGeneration;

public class TruncationInheritance implements GenerationInheritance{
	float mutationRate;
	float percentChildren;
	Random rand;
	
	public TruncationInheritance(float mutationRate, float percentChildren, Random rand) {
		this.mutationRate = mutationRate;
		this.percentChildren = percentChildren;
		this.rand = rand;
	}
	
	@Override
	public StrategyGeneration getNext(StrategyGeneration parent) {
		LearningStrategy[] strategies = new LearningStrategy[parent.strategies.length];
		for(int i = 0; i<strategies.length;i++) {
			if(i<(1-percentChildren)*strategies.length) {
				strategies[i] = parent.strategies[i];
			}else {
				LearningStrategy p1 = parent.strategies[rand.nextInt(strategies.length)];
				LearningStrategy p2 = parent.strategies[rand.nextInt(strategies.length)];
				Step[] steps = new Step[p1.steps.length];
				int index = rand.nextInt(steps.length);
				for(int j = 0; j<steps.length; j++) {
					if(j<index) {
						steps[j] = p1.steps[j];
					}else {
						steps[j] = p2.steps[j];
					}
				}
				strategies[i] = new LearningStrategy(p1.landscape,steps,p1.genotype,p1.stepTypes,p1.rand);
			}
		}
		return new StrategyGeneration(strategies);
	}

}
