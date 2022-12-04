package evolution;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import seededrandom.SeededRandom;

public enum Step {
	RandomWalk;
	
	private static final List<Step> VALUES =
    Collections.unmodifiableList(Arrays.asList(values()));
	private static final int SIZE = VALUES.size();

	public static Step randomStep()  {
		return VALUES.get(SeededRandom.rnd.nextInt(SIZE));
	}
}
