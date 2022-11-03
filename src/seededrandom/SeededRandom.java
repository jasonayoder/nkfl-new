package seededrandom;
import java.util.Random;

/*
 * Allows us to set a seed for reproducible result
 */
public class SeededRandom {

	public static SeededRandom thisSingle = null;
	public static Random rnd = new Random();
//	static Random rnd = new Random(1);

	/*
	 * Insures we only have one instance of this class
	 */
	public static SeededRandom getInstance() {
		if (thisSingle == null) {
			thisSingle = new SeededRandom();
			rnd.setSeed(1);
		}
		return thisSingle;
	}
}