package output;

import java.io.Writer;

import evolutionSimulation.EvolutionSimulation;

public abstract class SimulationWriter {
	Writer out;
	public SimulationWriter(Writer out) {
		this.out = out;
	}
	public abstract void write(EvolutionSimulation in);
}
