package com.prasanna.vwapsimulator.orderbook;

import com.prasanna.vwapsimulator.domain.RunningVwap;
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
 * Created by prasniths on 24/02/16.
 */
public class InstrumentOrderBook implements TicksObserver {
    private BoundedPriorityOrderBook buyOrderBook;
    private BoundedPriorityOrderBook sellOrderBook;
    private BlockingQueue<Tick> ticksForInstrument;
    private RunningVwap buyorderVWAP;
    private RunningVwap sellOrderVWAP;

    private ExecutorService executorService;
    private volatile boolean isRunning = true;
    private static final Logger LOGGER = LoggerFactory.getLogger(InstrumentOrderBook.class);

    public InstrumentOrderBook(BoundedPriorityOrderBook buyOrderBook, BoundedPriorityOrderBook sellOrderBook, BlockingQueue<Tick> ticksForInstrument, ExecutorService executorService) {
        this.buyOrderBook = buyOrderBook;
        this.sellOrderBook = sellOrderBook;
        this.ticksForInstrument = ticksForInstrument;
        this.executorService = executorService;
        buyorderVWAP = RunningVwap.initialVWAPValue();
        sellOrderVWAP = RunningVwap.initialVWAPValue();
    }

    public static InstrumentOrderBook newOrderBook() {
        return new InstrumentOrderBook(
                new BoundedPriorityOrderBook(new BuySideTickComparator(), 30), // TODO TO come from config
                new BoundedPriorityOrderBook(new SellSideTickComparator(), 30),
                new LinkedBlockingQueue<>(50),
                Executors.newFixedThreadPool(1));

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

    public RunningVwap getBuyorderVWAP() {
        return buyorderVWAP;
    }

    public RunningVwap getSellOrderVWAP() {
        return sellOrderVWAP;
    }

    public void setBuyorderVWAP(RunningVwap buyorderVWAP) {
        this.buyorderVWAP = buyorderVWAP;
    }

    public void setSellOrderVWAP(RunningVwap sellOrderVWAP) {
        this.sellOrderVWAP = sellOrderVWAP;
    }
}
