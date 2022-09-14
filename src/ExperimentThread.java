import java.util.Queue;
import java.util.concurrent.locks.Lock;

public class ExperimentThread extends Thread{
	Queue<ExperimentRunner> queue;
	Lock lock;
	public ExperimentThread(Queue<ExperimentRunner> queue, Lock lock) {
		this.queue = queue;
		this.lock = lock;
	}
	public void run() {
		lock.lock();
		while(!queue.isEmpty()) {
			ExperimentRunner runner = queue.poll();
			lock.unlock();
			runner.run();
			System.out.println("completed config");
			lock.lock();
		}
		lock.unlock();
	}
}