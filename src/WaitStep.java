import java.util.ArrayList;

public class WaitStep extends Step {
	public WaitStep() {
		
	}
	
	public int execute(FitnessLandscape landscape, int phenotype, ArrayList<Integer> lookedLocations, ArrayList<Action> past) {
		past.add(Action.WAIT);
		return phenotype;
	}
	
	public String getStepName() {
		return "Wait";
	}

	@Override
	public boolean equals(Step o) {
		return (o instanceof WaitStep);
	}
}
