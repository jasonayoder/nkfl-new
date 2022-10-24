package fitnessLandscape;

import java.util.function.Function;

public class StrictlyIncreasingFunctionDecorator extends FunctionDecorator{

	public StrictlyIncreasingFunctionDecorator(Function<Float, Float> function, Landscape landscape) {
		super(function, landscape);
	}

	@Override
	public void setMinMax() {
		min = landscape.getMinLocation();
		max = landscape.getMaxLocation();
	}

}
