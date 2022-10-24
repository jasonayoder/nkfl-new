package fitnessLandscape.NK;

import java.util.Random;

public class Local extends AbstractNKLandscape{

	public Local(int N, int K, Random rand) {
		super(N, K, rand);
		generateFitnessTable();
	}

	@Override
	void generateFitnessTable() {
		float[][] interactions = generateRandomInteractionsTable();
		min = max = 0;
		for(int i = 0; i<1<<N; i++) {
			float fitness = 0;
			for(int j = 0; j<N; j++) {
				int interaction = 0;
				for(int k = 0; k<K+1; k++) {
					int offset = (j+k)%N;
					interaction |= ((i>>>offset)&1)<<k;
				}
				fitness += interactions[j][interaction];
			}
			fitnessTable[i] = fitness;
			if(fitness>fitnessTable[max]) {
				max = i;
			}
			if(fitness<fitnessTable[min]) {
				min = i;
			}
			
		}
	}

	private float[][] generateRandomInteractionsTable(){
		float[][] interactions = new float[N][1<<(K+1)];
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < (1<<(K+1));j++) {
				interactions[i][j] = rand.nextFloat();
			}
		}
		return interactions;
	}
}
