package fitnessLandscape.dynamic;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;
import java.util.Random;


public class PropParserDynamicLandscapeFactory {
	public static DynamicLandscape get(Properties p) throws Exception{
		switch (p.getProperty("landscapeType")) {
		case "LERP":{
			int dur = Integer.parseInt(p.getProperty("dur"));
			return new LERP(get(p.getProperty("landscape")),get(p.getProperty("landscape")),dur);
		}
		case "DynamicNormalizationDecorator":{
			int min = Integer.parseInt(p.getProperty("min"));
			int max = Integer.parseInt(p.getProperty("max"));
			return new DynamicNormalizationDecorator(get(p.getProperty("landscape")),min,max);
		}
		case "Copy":{
			int seed = Integer.parseInt(p.getProperty("seed"));
			float r = Float.parseFloat(p.getProperty("r"));
			return new Copy(get(p.getProperty("landscape")),new Random(seed),r);
		}
		case "PLT":{
			int seed = Integer.parseInt(p.getProperty("seed"));
			float r = Float.parseFloat(p.getProperty("r"));
			return new PLT(get(p.getProperty("landscape")),new Random(seed),r);
		}
		case "SingleTemp":{
			int seed = Integer.parseInt(p.getProperty("seed"));
			int order = Integer.parseInt(p.getProperty("order"));
			return new SingleTemp(get(p.getProperty("landscape")),new Random(seed),order);
		}
		case "SumTemp":{
			int seed = Integer.parseInt(p.getProperty("seed"));
			int order = Integer.parseInt(p.getProperty("order"));
			int num  = Integer.parseInt(p.getProperty("num"));
			float r = Float.parseFloat(p.getProperty("r"));
			return new SumTemp(get(p.getProperty("landscape")),new Random(seed),order,num,r);
		}
		case "TempPerm":{
			int seed = Integer.parseInt(p.getProperty("seed"));
			int order = Integer.parseInt(p.getProperty("order"));
			int num  = Integer.parseInt(p.getProperty("num"));
			return new TempPerm(get(p.getProperty("landscape")),new Random(seed),order,num);
		}
		case "Xor":{
			int seed = Integer.parseInt(p.getProperty("seed"));
			float r = Float.parseFloat(p.getProperty("r"));
			return new Xor(get(p.getProperty("landscape")),new Random(seed),r);
		}
		}
		return new NonDynamic(fitnessLandscape.PropParserFitnessLandscapeFactory.get(p));
	}
	
	public static DynamicLandscape get(String path) throws Exception {
		Properties p2 = new Properties();
		p2.load(new FileReader(new File(path)));
		return get(p2);
	}
}
