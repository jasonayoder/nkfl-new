import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MultiExperimentRunner {
	public static void main(String[] args) {
		if(args.length==0) {
			ExperimentRunner runner = new ExperimentRunner();
			runner.run();
		}else {
			Queue<ExperimentRunner> configs = new LinkedList<>();
			for(int i = 0; i<args.length;i++) {
				File f = new File(args[i]);
				getFiles(f,configs);
			}
			while(!configs.isEmpty()) {
				configs.poll().run();
			}
			run(configs);
		}
	}
	
	private static void run(Queue<ExperimentRunner> configs) {
		ArrayList<Thread> threads = new ArrayList<>();
		Lock lock = new ReentrantLock();
		lock.lock();
		for(int i = 0; i<Runtime.getRuntime().availableProcessors()&&i<configs.size();i++) {
			Thread t = new ExperimentThread(configs,lock);
			threads.add(t);
			t.start();
		}
		lock.unlock();
		for(Thread t: threads) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.print("done");
		
	}

//	public static void run(File f) {
//		if(f.isDirectory()) {
//			for(File child: f.listFiles()) {
//				run(child);
//			}
//		}else {
//			ExperimentRunner runner = new ExperimentRunner();
//			runner.init(f.getAbsolutePath());
//			runner.run();
//		}
//	}
	
	public static void getFiles(File f, Queue<ExperimentRunner> q){
		if(f.isDirectory()) {
			for(File child: f.listFiles()) {
				getFiles(child,q);
			}
		}else {
			ExperimentRunner runner = new ExperimentRunner();
			runner.init(f.getAbsolutePath());
			q.add(runner);
		}
	}
}

