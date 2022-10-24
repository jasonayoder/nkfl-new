package evolutionSimulation.learningStrategy;

import java.util.Random;
import java.util.Set;

import fitnessLandscape.Landscape;

public class WaitStep implements Step{

	@Override
	public int apply(Landscape landscape, int phenotype, Set<Integer> lookedLocations, Random rand) {
		return phenotype;
	}
	
	public String toString() {
		return "Wait";
	}
	
}
