package slidingBlockPuzzleSover.com;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.SwingWorker;

/** 
 * SwingWorker Executor that limits how many workers are running at any one moment
 * @author http://greatratrace.blogspot.com/2010/01/throttling-swingworker-using.html
 *
 */

public class SwingWorkerExecutor {

	private static final int MAX_WORKER_THREAD = 1;

	private static final SwingWorkerExecutor executor = new SwingWorkerExecutor();

	/** Thread pool for worker thread execution */ 
	private ExecutorService workerThreadPool = Executors.newFixedThreadPool(MAX_WORKER_THREAD);

	/**
	 * Private constructor required for the singleton pattern.
	 */
	private SwingWorkerExecutor() {
	}

	/**
	 * Returns the singleton instance.
	 * @return SwingWorkerExecutor - Singleton.
	 */
	public static SwingWorkerExecutor getInstance() {

		return executor;

	}

	/**
	 * Adds the SwingWorker to the thread pool for execution.
	 * @param worker - The SwingWorker thread to execute.
	 */
	public void execute(SwingWorker<Void, Object> worker) {

		workerThreadPool.submit(worker);

	}

}


