package output;

import java.io.Writer;

import evolutionSimulation.learningStrategy.StrategyGeneration;

public abstract class GenerationWriter {
	Writer out;
	public GenerationWriter(Writer out) {
		this.out = out;
	}
	public abstract void write(StrategyGeneration in, int gen);
}
