import java.util.ArrayList;

/*
 * Walking step
 * 
 * @author Jackson Shen, Jacob Ashworth
 */
public class DescendIfLookedLowerElseLook extends Step {

	public DescendIfLookedLowerElseLook() {
	}

	@Override
	public int execute(FitnessLandscape landscape, int phenotype, ArrayList<Integer> lookedLocations,
			ArrayList<Action> prev) {
		Step step = new LookStep();

		double bestPhenotypeFitness = landscape.fitness(phenotype);

		for (Integer location : lookedLocations) {
			int newPhenotype = phenotype;
			newPhenotype = newPhenotype ^ (1 << (landscape.n - 1 - location));
			double newPhenotypeFitness = landscape.fitness(newPhenotype);

			if (newPhenotypeFitness < bestPhenotypeFitness) {
				step = new DescendIfLookedLessElseRandom();
				break;
			}
		}
		return step.execute(landscape, phenotype, lookedLocations, prev);

	}

	public String getStepName() {
		return "DescendIfLower";
	}

	@Override
	public boolean equals(Step o) {
		return (o instanceof DescendIfLookedLowerElseLook);
	}

}
