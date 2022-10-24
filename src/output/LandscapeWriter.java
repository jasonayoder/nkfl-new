package output;

import java.io.IOException;
import java.io.Writer;

import fitnessLandscape.Landscape;

public abstract class LandscapeWriter {
	Writer out;
	public LandscapeWriter(Writer out) {
		this.out = out;
	}
	public abstract void write(Landscape in, int gen) throws IOException;
}
