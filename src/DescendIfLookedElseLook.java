import java.util.ArrayList;

public class DescendIfLookedElseLook extends Step{

	@Override
	public int execute(FitnessLandscape landscape, int phenotype, ArrayList<Integer> lookedLocations, ArrayList<Action> prev) {
		Step step = new LookStep();
		if(prev.get(prev.size()-1)==Action.ASCEND) {
			step = new DescendIfLookedLessElseRandom();
		}
		return step.execute(landscape, phenotype, lookedLocations, prev);
	}

	@Override
	public String getStepName() {
		return "DescIfLooked";
	}

	@Override
	public boolean equals(Step o) {
		return o instanceof DescendIfLookedElseLook;
	}
	
}
