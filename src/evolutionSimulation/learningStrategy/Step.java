package evolutionSimulation.learningStrategy;

import java.util.Random;
import java.util.Set;

public interface Step {
	public int apply(fitnessLandscape.Landscape landscape,int phenotype,Set<Integer>lookedLocations, Random rand);
}
