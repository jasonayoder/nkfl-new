package landscape;

/**
 * This is barely a landscape.  Used to mask a simple fitness function (concurrent 1s from position 0) as
 * a landscape, with a special change on nextCycle that inverts the function
 * @author jacob
 *
 *	When using this the scale between fitness values is a bit nonsensical, so you should use tournament
 *	selection.
 */
public class SimpleSequence extends DynamicFitnessLandscape{
	
	boolean inverted = false;
	
	public SimpleSequence(int n, int k, int seed) {
		super(n,k,seed);
		this.fitTable = new double[1<<n];
		
		for(int i=0; i<1<<n; i++)
		{
			fitTable[i] = 0;
		}
		setNormal();
	}
	
	private void setNormal()
	{
		for(int i=0; i<1<<n; i++)
		{
			fitTable[i] = 0;
		}
		int[] phenotype = new int[n];
		fitTable[0]=0;
		for(int i=0; i<phenotype.length; i++)
		{
			phenotype[i] = 1;
			int phenonum = 0;
			for(int j=0; j<phenotype.length; j++)
			{
				if(phenotype[j]==1){
					phenonum += Math.pow(2, j);
				}
			}
			fitTable[phenonum] = i+1;
		}
//		System.out.println(fitTable[8+4+3]);
	}
	
	private void setInverted()
	{
		for(int i=0; i<1<<n; i++)
		{
			fitTable[i] = 0;
		}
		int[] phenotype = new int[n];
		fitTable[0]=0.01;
		for(int i=0; i<phenotype.length; i++)
		{
			phenotype[i] = 1;
			int phenonum = 0;
			for(int j=0; j<phenotype.length; j++)
			{
				if(phenotype[j]==1){
					phenonum += Math.pow(2, j);
				}
			}
			fitTable[phenonum] = ((-2*((i+1)%2))+1)*(i+1);
		}
	}

	@Override
	public void nextCycle() {
		inverted = !inverted;
		if(!inverted)
		{
			setNormal();
		}
		if(inverted)
		{
			setInverted();
		}
	}
}