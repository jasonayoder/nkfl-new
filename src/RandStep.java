import java.util.ArrayList;

/*
 * Walking step
 * 
 * @author Jackson Shen, Jacob Ashworth
 */
public class RandStep extends Step {

	public RandStep() {
	}

	@Override
	public int execute(FitnessLandscape landscape, int phenotype, ArrayList<Integer> lookedLocations, ArrayList<Action> past) {
		int location = SeededRandom.rnd.nextInt(landscape.n);
		int newPhenotype = phenotype;
		newPhenotype = newPhenotype ^ (1<<(landscape.n-1-location)) ;
		past.add(Action.RANDOM);
		return newPhenotype;
	}
	
	public String getStepName() {
		return "Random";
	}

	@Override
	public boolean equals(Step o) {
		return (o instanceof RandStep);
	}

}
