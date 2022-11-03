import java.util.ArrayList;

import landscape.FitnessLandscape;

public class WaitStep extends Step {
	public WaitStep() {
		
	}
	
	public int execute(FitnessLandscape landscape, int phenotype, ArrayList<Integer> lookedLocations) {
		return phenotype;
	}
	
	public String getStepName() {
		return "Wait";
	}
}
