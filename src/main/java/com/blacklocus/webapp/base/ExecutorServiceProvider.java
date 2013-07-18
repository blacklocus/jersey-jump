package com.blacklocus.webapp.base;

import javax.ws.rs.ext.Provider;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * General purpose thread pools
 *
 * @author Jason Dunkelberger (dirkraft)
 */
@Provider
public class ExecutorServiceProvider {

    private final ExecutorService roots = new ThreadPoolExecutor(8, 8, 1, TimeUnit.HOURS,
            new SynchronousQueue<Runnable>());

    private final ExecutorService monitors = Executors.newCachedThreadPool();

    private final ExecutorService workers = new ThreadPoolExecutor(32, 32, 1, TimeUnit.HOURS,
            new LinkedBlockingQueue<Runnable>(256));

    /**
     * @return executor service for root level tasks or monitors. The number of concurrent processes is severely limited.
     */
    public ExecutorService forRoots() {
        return roots;
    }

    /**
     * @return an unbounded executor service for lightweight, monitoring-purposed processes. Root-level monitors should
     *         still be given to {@link #forRoots()} (e.g. the top of a Job), but component monitors that themselves
     *         don't do any heavy lifting may be given to this pool.
     */
    public ExecutorService forMonitors() {
        return monitors;
    }

    /**
     * @return executor service that limits number of concurrent process. Should be used with worker-level tasks to
     *         rate control the amount of resource contention.
     */
    public ExecutorService forWorkers() {
        return workers;
    }
}
