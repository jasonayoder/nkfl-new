package fitnessLandscape;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;
import java.util.Random;
import java.util.function.Function;


public class PropParserFitnessLandscapeFactory {	
	public static Landscape get(Properties p) throws Exception {
		switch (p.getProperty("landscapeType")) {
		case "Local":{
			int N = Integer.parseInt(p.getProperty("N"));
			int K = Integer.parseInt(p.getProperty("K"));
			int seed = Integer.parseInt(p.getProperty("seed"));
			return new fitnessLandscape.NK.Local(N, K, new Random(seed));
		}
		case "Original":{
			int N = Integer.parseInt(p.getProperty("N"));
			int K = Integer.parseInt(p.getProperty("K"));
			int seed = Integer.parseInt(p.getProperty("seed"));
			return new StrictlyIncreasingFunctionDecorator(new Function<Float,Float>() {

				@Override
				public Float apply(Float t) {
					t = t*t;//t^2
					t = t*t;//t^4;
					return t*t;//t^8
				}},new fitnessLandscape.NormalizationDecorator(
						new fitnessLandscape.NK.Local(N, K, new Random(seed)),0,1));
		}
		case "NormalizationDecorator":{
			int min = Integer.parseInt(p.getProperty("min"));
			int max = Integer.parseInt(p.getProperty("max"));
			Properties p2 = new Properties();
			p2.load(new FileReader(new File(p.getProperty("landscape"))));
			Landscape landscape = get(p2);
			return new NormalizationDecorator(landscape,min,max);
		}
		}
		throw new Exception("Unknown Landscape Type: "+p.getProperty("landscapeType"));
	}
}
