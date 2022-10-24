package evolutionSimulation.learningStrategy;

import java.util.Random;
import java.util.Set;

import fitnessLandscape.Landscape;

public class LookStep implements Step{

	@Override
	public int apply(Landscape landscape, int phenotype, Set<Integer> lookedLocations, Random rand) {
		int n = landscape.getN();
		int[] order = new int[n];
		for(int i = 0; i<n; i++) {
			order[i] = i;
		}
		for(int i = 0; i<n; i++) {
			int temp = order[i];
			int j = rand.nextInt(n);
			order[i] = order[j];
			order[j] = temp;
		}
		for(int i = 0; i<n; i++) {
			if(lookedLocations.add(phenotype^1<<order[i])) {
				return phenotype;
			}
		}
		return phenotype;
	}
	public String toString() {
		return "Look";
	}
	
}
