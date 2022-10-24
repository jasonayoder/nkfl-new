package fitnessLandscape;

import java.util.function.Function;

public class GeneralFunctionDecorator extends FunctionDecorator{

	public GeneralFunctionDecorator(Function<Float, Float> function, Landscape landscape) {
		super(function, landscape);
	}

	@Override
	public void setMinMax() {
		max = min = 0;
		float minFit, maxFit;
		minFit = maxFit = function.apply(landscape.getFitness(min));
		for(int i = 0; i<1<<landscape.getN();i++) {
			float fit = function.apply(landscape.getFitness(i));
			if(fit<minFit) {
				min = i;
				minFit = fit;
			}else if(fit>maxFit) {
				max = i;
				maxFit = fit;
			}
		}
	}
	
}
