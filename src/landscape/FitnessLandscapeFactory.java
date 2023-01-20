package landscape;

/*
 * I would like to establish, before I write this, that I dislike the factory pattern, but do not currently see another solution as we did not plan out our design thoroughly
 * */
public class FitnessLandscapeFactory {

	public static DynamicFitnessLandscape getLandscape(int n, int k, int seed, String landscapeName,
			String landscapeParams) {
		if(null!=landscapeParams) {
			String[] paramsStrings = landscapeParams.split(" ");
			switch(landscapeName) {
			case "DERPFitnessLandscape":{
				return new DERPFitnessLandscape(n,k,Integer.parseInt(paramsStrings[0]),seed);
			}case "LERPFitnessLandscape":{
				return new LERPFitnessLandscape(n,k,Integer.parseInt(paramsStrings[0]),seed);
			}case "PLTFitnessLandscape":{
				return new PLTFitnessLandscape(n,k,Double.parseDouble(paramsStrings[0]),seed);
			}case "SingleTempFitnessLandscape":{
				return new SingleTempFitnessLandscape(n,k,Integer.parseInt(paramsStrings[0]),seed);
			}case "XorFitnessLandscape":{
				return new XorFitnessLandscape(n,k,Double.parseDouble(paramsStrings[0]),seed);
			}case "CopyFitnessLandscape":{
				return new CopyFitnessLandscape(n,k,Double.parseDouble(paramsStrings[0]),seed);
			}case "SumTempFitnessLandscape":{
				return new SumTempFitnessLandscape(n,k,Integer.parseInt(paramsStrings[0]),Integer.parseInt(paramsStrings[1]),Double.parseDouble(paramsStrings[2]),seed);
			}case "TempPermFitnessLandscape":{
				return new TempPermFitnessLandscape(n,k,Integer.parseInt(paramsStrings[0]),seed);
			}case "SimpleSequence":{
				return new SimpleSequence(n,k,seed);
			}case "EvenOddLandscape":{
				return new EvenOddLandscape(n,k,seed);
			}case "RandomLandscape":{
				return new RandomLandscape(n,k,Integer.parseInt(paramsStrings[0]),Integer.parseInt(paramsStrings[1]),seed);
			}
			}
		}
		return new NonDynamicFitnessLandscape(n,k,seed);
	}
	
}
