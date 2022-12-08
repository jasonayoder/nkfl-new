import java.util.ArrayList;

/*
 * Walking step
 * 
 * @author Jackson Shen, Jacob Ashworth
 */
public class DescendIfLookedLessElseRandom extends Step {

	public DescendIfLookedLessElseRandom() {
	}

	@Override
	public int execute(FitnessLandscape landscape, int phenotype, ArrayList<Integer> lookedLocations, ArrayList<Action> past) {
		if(lookedLocations.size() == 0)
		{
			//RW
			int location = SeededRandom.rnd.nextInt(landscape.n);
			int newPhenotype = phenotype;
			newPhenotype = newPhenotype ^ (1<<(landscape.n-1-location)) ;
			past.add(Action.RANDOM);
			return newPhenotype;
		}
		else
		{
			//HC
			past.add(Action.DESCEND);
			int bestPhenotype = phenotype;
			double bestPhenotypeFitness = landscape.fitness(phenotype);
			
			for(Integer location : lookedLocations)
			{
				int newPhenotype = phenotype;
				newPhenotype = newPhenotype ^ (1<<(landscape.n-1-location));
				double newPhenotypeFitness = landscape.fitness(newPhenotype);
				
				if(newPhenotypeFitness < bestPhenotypeFitness)
				{
					bestPhenotype = newPhenotype;
					bestPhenotypeFitness = newPhenotypeFitness;
				}
			}
			
			lookedLocations.clear(); //Remove all our looked locations for the future
			return bestPhenotype;
		}
	}
	
	public String getStepName() {
		return "Descend";
	}

	@Override
	public boolean equals(Step o) {
		return (o instanceof DescendIfLookedLessElseRandom);
	}

}
