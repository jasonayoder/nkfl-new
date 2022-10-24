package evolutionSimulation.learningStrategy;

import java.util.Random;
import java.util.Set;

import fitnessLandscape.Landscape;

public class WalkStep implements Step{

	@Override
	public int apply(Landscape landscape, int phenotype, Set<Integer> lookedLocations, Random rand) {
		if(lookedLocations.size() == 0)
		{
			//RW
			int location = rand.nextInt(landscape.getN());
			int newPhenotype = phenotype;
			newPhenotype = newPhenotype ^ (1<<location);
			return newPhenotype;
		}
		else
		{
			//HC
		
			int bestPhenotype = phenotype;
			double bestPhenotypeFitness = landscape.getFitness(phenotype);
			
			for(Integer location : lookedLocations)
			{
				int newPhenotype = phenotype;
				newPhenotype = newPhenotype ^ 1<<location;
				double newPhenotypeFitness = landscape.getFitness(newPhenotype);
				
				if(newPhenotypeFitness > bestPhenotypeFitness)
				{
					bestPhenotype = newPhenotype;
					bestPhenotypeFitness = newPhenotypeFitness;
				}
			}
			
			lookedLocations.clear(); //Remove all our looked locations for the future
			return bestPhenotype;
		}
	}
	
	public String toString() {
		return "Walk";
	}
}
