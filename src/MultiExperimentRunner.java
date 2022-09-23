import java.io.File;

public class MultiExperimentRunner {
	public static void main(String[] args) {
		if(args.length==0) {
			ExperimentRunner.run("configFiles/config.properties");
		}else {
			for(int i = 0; i<args.length;i++) {
				File f = new File(args[i]);
				run(f);
			}
			
		}
	}
	public static void run(File f) {
		if(f.isDirectory()) {
			for(File child: f.listFiles()) {
				run(child);
			}
		}else {
			ExperimentRunner.run(f.getAbsolutePath());
		}
	}
}
