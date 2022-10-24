package output;

import java.io.IOException;
import java.io.Writer;

import evolutionSimulation.learningStrategy.LearningStrategy;

public class StrategyStepsWriter extends LearningStrategyWriter{
	final String LINE_HEADING = "STRATEGY_ROW";
	public StrategyStepsWriter(Writer out) {
		super(out);
	}

	@Override
	public void write(LearningStrategy in) throws IOException {
		out.write(LINE_HEADING);
		for(int i = 0; i < in.steps.length; i++) {
			out.write(',');
			out.write(in.steps[i].toString());
		}
		out.write('\n');
	}
	
}
