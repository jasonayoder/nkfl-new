package evolution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import control.Constants;
import seededrandom.SeededRandom;

public enum Step {
	RandomWalk, SteepestClimb, SteepestFall, SameStep, OppositeStep, RandomIfMaximaElseSteepestClimb, RandomIfMinimaElseSteepestFall, ReturnToMaxima, ReturnToMinima, Wait, SameAction, OppositeAction;
	
	private static final List<Step> VALUES =
			Collections.unmodifiableList(Arrays.asList(values()));
	private static final int SIZE = VALUES.size();
	
	private static List<Step> getValidSteps() {
		if(Constants.BLOCK_STEPS.charAt(Constants.BLOCK_STEPS.length()-1) != ',' || Constants.BLOCK_STEPS.charAt(0) != ',')
		{
			System.out.println("Please append a , to the beginning and end of blockSteps paramater, I cannot ensure all blocks are correctly added otherwise");
		}
		
		List<Step> validSteps = new ArrayList<Step>();
		
		for(Step s : VALUES)
		{
			if(Constants.BLOCK_STEPS.contains(',' + s.name() + ','))
			{
				validSteps.add(s);
			}
		}
		return validSteps;
	}
	
	public static final List<Step> validSteps = Collections.unmodifiableList(getValidSteps());
	
	public static Step randomStep()  {
		return validSteps.get(SeededRandom.rnd.nextInt(validSteps.size()));
	}
	
	/***
	 * This is where opposites are defined for the OppositeStep's use.
	 * Someday it may make sense to replace this with a map, but for
	 * now it's just a static method on Step.
	 * 
	 * @param s
	 * @return opposite step of s
	 */
	public static Step getOppositeOfStep(Step s) {
		switch(s) {
			case RandomWalk:
				return Wait;
			case SteepestClimb:
				return SteepestFall;
			case SteepestFall:
				return SteepestClimb;
			case SameStep:
				return OppositeStep;
			case OppositeStep:
				return SameStep;
			case RandomIfMaximaElseSteepestClimb:
				return RandomIfMinimaElseSteepestFall;
			case RandomIfMinimaElseSteepestFall:
				return RandomIfMaximaElseSteepestClimb;
			case ReturnToMaxima:
				return ReturnToMinima;
			case ReturnToMinima:
				return ReturnToMaxima;
			case Wait:
				return RandomWalk;
			case SameAction:
				return OppositeAction;
			case OppositeAction:
				return SameAction;
			default:
				System.out.println("getOppositeOfStep not implemented for given step");
				return null;//Opposite is not implemented for the step given
		}
		
	}
	
	public static Step getStepWithName(String name)
	{
		for(Step s : VALUES)
		{
			if(s.name().equals(name))
			{
				return s;
			}
		}
		return null;
	}
}
