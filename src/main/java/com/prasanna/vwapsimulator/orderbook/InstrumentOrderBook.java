package com.prasanna.vwapsimulator.orderbook;

import com.prasanna.vwapsimulator.domain.Vwap;
import com.prasanna.vwapsimulator.domain.Tick;
import com.prasanna.vwapsimulator.observer.TicksObserver;
import com.prasanna.vwapsimulator.orderbook.comparator.BuySideTickComparator;
import com.prasanna.vwapsimulator.orderbook.comparator.SellSideTickComparator;
import com.prasanna.vwapsimulator.task.UpdateInstrumentOrderBookTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * OrderBook for each instrument
 *
 * Not thread safe - Shouldn't use multiple thread to update instance of this class
 */
public class InstrumentOrderBook implements TicksObserver {
    // Buy order tick books
    private BoundedPriorityOrderBook buyOrderBook;
    // Sell order tick books
    private BoundedPriorityOrderBook sellOrderBook;
    // Tick source
    private BlockingQueue<Tick> ticksForInstrument;
    // Running BuySide VWAP
    private Vwap buyorderVWAP;
    // Running SellSide VWAP
    private Vwap sellOrderVWAP;

    private ExecutorService executorService;
    private volatile boolean isRunning = true;
    private static final Logger LOGGER = LoggerFactory.getLogger(InstrumentOrderBook.class);

    private InstrumentOrderBook(BoundedPriorityOrderBook buyOrderBook, BoundedPriorityOrderBook sellOrderBook, BlockingQueue<Tick> ticksForInstrument, ExecutorService executorService) {
        this.buyOrderBook = buyOrderBook;
        this.sellOrderBook = sellOrderBook;
        this.ticksForInstrument = ticksForInstrument;
        this.executorService = executorService;
        buyorderVWAP = Vwap.initialVWAPValue();
        sellOrderVWAP = Vwap.initialVWAPValue();
    }

    public static InstrumentOrderBook newOrderBook(int orderBookDepth,int blockingQueueCapacity) {
        // We just need only one thread to process updates for each instrument.
        // This is to avoid synchronization , if we have to use multiple thread.
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        return new InstrumentOrderBook(
                new BoundedPriorityOrderBook(new BuySideTickComparator(), orderBookDepth),
                new BoundedPriorityOrderBook(new SellSideTickComparator(), orderBookDepth),
                new LinkedBlockingQueue<>(blockingQueueCapacity),

                executorService);

    }

    public void init() {
        LOGGER.info("Initialising Instrument OrderBook");
        executorService.execute(new UpdateInstrumentOrderBookTask(this));
    }

    public void shutdown() {
        isRunning = false;
    }

    @Override
    public void update(Tick tick) {
        ticksForInstrument.add(tick);
    }

    public boolean isRunning() {
        return isRunning;
    }

    public BoundedPriorityOrderBook getBuyOrderBook() {
        return buyOrderBook;
    }

    public BoundedPriorityOrderBook getSellOrderBook() {
        return sellOrderBook;
    }

    public BlockingQueue<Tick> getTicksForInstrument() {
        return ticksForInstrument;
    }

    public Vwap getBuyorderVWAP() {
        return buyorderVWAP;
    }

    public Vwap getSellOrderVWAP() {
        return sellOrderVWAP;
    }

    public void setBuyorderVWAP(Vwap buyorderVWAP) {
        this.buyorderVWAP = buyorderVWAP;
    }

    public void setSellOrderVWAP(Vwap sellOrderVWAP) {
        this.sellOrderVWAP = sellOrderVWAP;
    }
}
