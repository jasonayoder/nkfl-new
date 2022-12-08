import java.util.ArrayList;

public class RepeatStep extends Step{

	@Override
	public int execute(FitnessLandscape landscape, int phenotype, ArrayList<Integer> lookedLocations, ArrayList<Action> prev) {
		Step step;// = new LookStep();
		switch(prev.get(prev.size()-1)) {
		case LOOK:{
			step = new LookStep();
			break;
		}
		case ASCEND:{
			step = new AscendIfLookedHigherElseRandom();
			break;
		}
		case DESCEND:{
			step = new DescendIfLookedLessElseRandom();
			break;
		}
		case WAIT:{
			step = new WaitStep();
			break;
		}
		case RANDOM:{
			step = new RandStep();
			break;
		}
		default:{
			step = new WaitStep();
		}
		}
		return step.execute(landscape, phenotype, lookedLocations, prev);
	}

	@Override
	public String getStepName() {
		return "Repeat";
	}

	@Override
	public boolean equals(Step o) {
		return o instanceof RepeatStep;
	}
	
}
