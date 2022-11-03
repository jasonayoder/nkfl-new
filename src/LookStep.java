import java.util.ArrayList;

import landscape.FitnessLandscape;
import seededrandom.SeededRandom;

/*
 * Look step
 * 
 * @author Jackson Shen, Jacob Ashworth
 */
public class LookStep extends Step {
	public static int DEFAULT_NUM_CHECKS = 1;
	
	public int numChecks; // change this to change the number of info-gathering steps
	
	public LookStep(int numChecks) {
		this.numChecks = numChecks;
	}
	
	public LookStep() {
		this.numChecks = DEFAULT_NUM_CHECKS;
	}

	//Just add an index we haven't looked at yet to lookedLocations
	@Override
	public int execute(FitnessLandscape landscape, int phenotype, ArrayList<Integer> lookedLocations) {
		for (int x = 0; x < numChecks; x++) {
			if(lookedLocations.size() < landscape.n)//clause to prevent infinite loops from overlooking
			{
				int lookIndex;
				do {
						lookIndex = SeededRandom.rnd.nextInt(landscape.n);
				} while (lookedLocations.contains(lookIndex));
				
				lookedLocations.add(lookIndex);
			}
		}
		return phenotype;
	}
	
	public String getStepName() {
		return "Look";
	}

}
