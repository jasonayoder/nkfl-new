package evolutionSimulation;

import java.util.Random;

import evolutionSimulation.learningStrategy.LearningStrategy;
import evolutionSimulation.learningStrategy.Step;
import evolutionSimulation.learningStrategy.StrategyGeneration;

public class MutationInheritance implements GenerationInheritance{
	float mutationRate;
	float percentChildren;
	Random rand;
	
	public MutationInheritance(float mutationRate, float percentChildren, Random rand) {
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
				strategies[i] = getChild(parent.strategies[rand.nextInt(strategies.length)]);
			}
		}
		return new StrategyGeneration(strategies);
	}
	
	LearningStrategy getChild(LearningStrategy parent) {
		Step[] steps = new Step[parent.steps.length];
		for(int i = 0; i<steps.length; i++) {
			if(rand.nextFloat()<mutationRate) {
				try {
					steps[i] = parent.getRandomStep(parent.steps[i]);
				} catch (Exception e) {
					e.printStackTrace();
					steps[i] = parent.steps[i];
				}
			}else {
				steps[i] = parent.steps[i];
			}
		}
		return new LearningStrategy(parent.landscape,steps,parent.genotype,parent.stepTypes,parent.rand);
	}
}
