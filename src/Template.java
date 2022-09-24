import java.util.HashSet;

public class Template {
	int[] zeros;
	int[] ones;
	int n, order;
	
	public Template(int n, int order) {
		this.n = n;
		this.order = order;
		ones = new int[SeededRandom.rnd.nextInt(order)];
		zeros = new int[order-ones.length];
		HashSet<Integer> used = new HashSet<>();
		for(int i = 0; i<ones.length;i++) {
			int j = SeededRandom.rnd.nextInt(n);
			while(used.contains(j)) {
				j = SeededRandom.rnd.nextInt(n);
			}
			ones[i] = j;
			used.add(j);
		}
		for(int i = 0; i<zeros.length;i++) {
			int j = SeededRandom.rnd.nextInt(n);
			while(used.contains(j)) {
				j = SeededRandom.rnd.nextInt(n);
			}
			zeros[i] = j;
			used.add(j);
		}
	}
	
	public void permute(double r) {
		for(int i = 0; i<zeros.length;i++) {
			if(SeededRandom.rnd.nextDouble()<r) {
				swap(true, i);
			}
		}
		for(int i = 0; i<ones.length;i++) {
			if(SeededRandom.rnd.nextDouble()<r) {
				swap(false, i);
			}
		}
	}
	
	public int getRandom(int n) {
		for(int i = 0; i<zeros.length;i++) {
			n &= ~(1<<zeros[i]);
		}
		for(int i = 0; i<ones.length;i++) {
			n |= 1<<ones[i];
		}
		return n;
	}
	
	public void swap(boolean isZero, int i) {
		int j = SeededRandom.rnd.nextInt(n);
		for(int k = 0; k<ones.length; k++) {
			if(ones[k]==j) {
				if(isZero) {
					ones[k] = zeros[i];
					zeros[i] = j;
				}
				return;
			}
		}
		for(int k = 0; k<zeros.length; k++) {
			if(zeros[k]==j) {
				if(!isZero) {
					zeros[k] = ones[i];
					ones[i] = j;
				}
				return;
			}
		}
		if(isZero) {
			zeros[i] = j;
			return;
		}
		ones[i] = j;
	}
	
	public boolean isElement(int x) {
		for(int z: zeros) {
			if(0!=(x&(1<<z))) {
				return false;
			}
		}
		for(int o: ones) {
			if(0==(x&(1<<o))) {
				return false;
			}
		}
		return true;
	}
}
