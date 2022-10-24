package output;

import java.io.IOException;
import java.io.Writer;

import evolutionSimulation.learningStrategy.LearningStrategy;

public abstract class LearningStrategyWriter {
	Writer out;
	public LearningStrategyWriter(Writer out) {
		this.out = out;
	}
	public abstract void write(LearningStrategy in) throws IOException;
}
