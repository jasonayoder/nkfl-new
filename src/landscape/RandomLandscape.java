package landscape;

import seededrandom.SeededRandom;

public class RandomLandscape extends DynamicFitnessLandscape{
	int nmin,nmax,kmin,kmax,n,k;
	FitnessLandscape land;
	public RandomLandscape(int nmin, int kmin, int nmax, int kmax, int seed) {
		super(nmin, kmin, seed);
		this.nmin = nmin;
		this.kmin = kmin;
		this.nmax = nmax;
		this.kmax = kmax+1;
		try {
			n = nmin+SeededRandom.rnd.nextInt(nmax-nmin);
		}catch(Exception e) {
			n = nmin;
		}
		try {
			k = kmin+SeededRandom.rnd.nextInt(Math.min(n-1, kmax)-kmin);
		}catch(Exception e) {
			k = kmin;
		}
		land = new FitnessLandscape(n,k,super.landscapeRnd.nextInt());
	}

	@Override
	public void nextCycle() {
		try {
			n = nmin+SeededRandom.rnd.nextInt(nmax-nmin);
		}catch(Exception e) {
			n = nmin;
		}
		try {
			k = kmin+SeededRandom.rnd.nextInt(Math.min(n, kmax)-kmin);
		}catch(Exception e) {
			k = kmin;
		}
		land = new FitnessLandscape(n,k,super.landscapeRnd.nextInt());
	}
	
	public double fitness(int genotype) {
		return land.fitness(genotype);
	}
	
}
