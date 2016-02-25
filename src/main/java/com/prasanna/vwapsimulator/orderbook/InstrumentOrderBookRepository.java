package com.prasanna.vwapsimulator.orderbook;

import com.prasanna.vwapsimulator.domain.Instrument;
import com.prasanna.vwapsimulator.domain.Tick;
import com.prasanna.vwapsimulator.listner.TicksFeedSource;
import com.prasanna.vwapsimulator.observer.TicksObserver;
import com.prasanna.vwapsimulator.task.OrderBookMapUpdateTask;
import com.prasanna.vwapsimulator.util.WorkerThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;


public class InstrumentOrderBookRepository implements TicksFeedSource, TicksObserver {

    private static final Logger LOGGER = LoggerFactory.getLogger(InstrumentOrderBookRepository.class);

    private BlockingQueue<Tick> sourceQueue;
    private OrderBookMap orderBookMap;
    private volatile boolean isRunning = true;
    private Executor executorService;


    public InstrumentOrderBookRepository(BlockingQueue<Tick> sourceQueue, OrderBookMap orderBookMap, Executor executorService) {
        this.sourceQueue = sourceQueue;
        this.orderBookMap = orderBookMap;
        this.executorService = executorService;

    }

    public void start() {
        executorService.execute(new ReadTickSourceTask());
    }

    @Override
    public boolean requestTicksFeedForInstrument(Instrument instrument) {
        return orderBookMap.addInstrument(instrument);
    }

    @Override
    public void update(Tick tick) {
        OrderBookMapUpdateTask command = new OrderBookMapUpdateTask(orderBookMap, tick);
        executorService.execute(command);
    }

    public void shutDown() {
        isRunning = false;
    }

    class ReadTickSourceTask implements Runnable {

        @Override
        public void run() {
            WorkerThreadUtil.enrichWorkerThread("InstrumentOrderBookRepository.ReadTickSourceTask");

            LOGGER.info("Starting to read SourceQueue");
            while (isRunning) {
                try {
                    Tick take = sourceQueue.take();
                    update(take);
                } catch (InterruptedException e) {
                    LOGGER.error("Interrupted");
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
