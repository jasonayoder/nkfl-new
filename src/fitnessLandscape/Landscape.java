package fitnessLandscape;
/**
 * The interface for fitness landscapes in the architecture
 * @author fioritjx
 *
 */
public interface Landscape {
	/**
	 * gets the fitness of a given genotype on this fitness landscape
	 * @param genotype
	 * @return fitness
	 */
	public float getFitness(int genotype);
	/**
	 * gets the phenotype of the minimum fitness on the landscape
	 * @return minimumPhenotype
	 */
	public int getMinLocation();
	/**
	 * gets the phenotype of the maximum fitness on the landscape
	 * @return minimumPhenotype
	 */
	public int getMaxLocation();
	/**
	 * all genotypes >=0 and < 1<<N are legal
	 * I was originally going to have getGenotypeRange, but the conversion is more expensive, 
	 * and allowing for not power of 2 genotype ranges could cause errors elsewhere
	 * @return N
	 */
	public int getN();
}
