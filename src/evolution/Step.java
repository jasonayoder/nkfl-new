package evolution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import control.Constants;
import seededrandom.SeededRandom;

public enum Step {
	RandomWalk, SteepestClimb, SteepestFall, RandomIfMaximaElseSteepestClimb, RandomIfMinimaElseSteepestFall;
	
	private static final List<Step> VALUES =
			Collections.unmodifiableList(Arrays.asList(values()));
	private static final int SIZE = VALUES.size();
	
	private static List<Step> getValidSteps() {
		List<Step> validSteps = new ArrayList<Step>();
		
		if(Constants.BLOCK_STEPS.contains("RandomWalk"))
		{
			validSteps.add(RandomWalk);
		}
		if(Constants.BLOCK_STEPS.contains("SteepestClimb"))
		{
			validSteps.add(SteepestClimb);
		}
		if(Constants.BLOCK_STEPS.contains("SteepestFall"))
		{
			validSteps.add(SteepestFall);
		}
		if(Constants.BLOCK_STEPS.contains("RandomIfMaximaElseSteepestClimb"))
		{
			validSteps.add(RandomIfMaximaElseSteepestClimb);
		}
		if(Constants.BLOCK_STEPS.contains("RandomIfMinimaElseSteepestFall"))
		{
			validSteps.add(RandomIfMinimaElseSteepestFall);
		}
		return validSteps;
	}
	
	public static final List<Step> validSteps = Collections.unmodifiableList(getValidSteps());
	
	public static Step randomStep()  {
		return validSteps.get(SeededRandom.rnd.nextInt(validSteps.size()));
	}
	
	
}
