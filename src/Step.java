import java.util.ArrayList;

/*
 * Abstract class of step to make it so we can have various kinds of steps in a learning strategy.
 * 
 * @author Jackson Shen, Jacob Ashworth
 */
public abstract class Step {
	public abstract int execute(FitnessLandscape landscape, int phenotype, ArrayList<Integer> lookedLocations, ArrayList<Action> prev); // return the location after, should be the same after a ig step
	public Step() {
	}
	public abstract String getStepName();
	public abstract boolean equals(Step o);
}