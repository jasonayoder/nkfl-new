package output;

import java.io.IOException;
import java.io.Writer;

import evolutionSimulation.learningStrategy.LearningStrategy;
import fitnessLandscape.Landscape;

public class StrategyAverageAfterStepWriter extends LearningStrategyWriter {
	final String LINE_HEADING = "FITNESS_ROW";
	public StrategyAverageAfterStepWriter(Writer out) {
		super(out);
	}

	@Override
	public void write(LearningStrategy in) throws IOException {
		out.write(LINE_HEADING);
		for(int i = 1; i<in.fitnesses[0].length;i++) {
			float avg = 0;
			for(int j = 0; j<in.fitnesses.length;j++) {
				avg += in.fitnesses[j][i];
			}
			avg /= in.fitnesses.length;
			out.write(',');
			out.write(Float.toString(avg));
		}
		out.write('\n');
	}

}
