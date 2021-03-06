package com.prasanna.vwapsimulator.task;

import com.prasanna.vwapsimulator.domain.Tick;
import com.prasanna.vwapsimulator.util.WorkerThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

/**
 * RandomTickSourceTask
 */
public class RandomTickSourceTask implements Runnable {
    public final static Logger logger = LoggerFactory.getLogger(RandomTickSourceTask.class);
    private List<Tick> ticks;
    private BlockingQueue<Tick> destinationQueue;

    public RandomTickSourceTask(List<Tick> ticks, BlockingQueue<Tick> destinationQueue) {
        this.ticks = ticks;
        this.destinationQueue = destinationQueue;
    }

    @Override
    public void run() {
        WorkerThreadUtil.enrichWorkerThread("RandomTickSourceTask");
        Random random = new Random();
        int randomNum = random.nextInt((ticks.size() - 2) + 1) + 0;
        Tick tick = ticks.get(randomNum);
        logger.debug("Adding {} to destinationQueue", tick);
        try {
            destinationQueue.put(tick);
        } catch (InterruptedException e) {
            logger.warn("Thread interrupted by exception {}", e.getCause());
            Thread.currentThread().interrupt();
        }
    }
}
