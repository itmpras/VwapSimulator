package com.prasanna.vwapsimulator.task;

import com.prasanna.vwapsimulator.domain.Tick;
import com.prasanna.vwapsimulator.domain.TickDirection;
import com.prasanna.vwapsimulator.orderbook.InstrumentOrderBook;
import com.prasanna.vwapsimulator.util.WorkerThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * UpdateInstrumentOrderBookTask
 */
public class UpdateInstrumentOrderBookTask implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateInstrumentOrderBookTask.class);
    private InstrumentOrderBook instrumentOrderBook;


    public UpdateInstrumentOrderBookTask(InstrumentOrderBook instrumentOrderBook) {
        this.instrumentOrderBook = instrumentOrderBook;
    }

    @Override
    public void run() {
        WorkerThreadUtil.enrichWorkerThread("UpdateInstrumentOrderBookTask");
        while (instrumentOrderBook.isRunning()) {

            try {
                Tick take = instrumentOrderBook.getTicksForInstrument().take();
                // This can be technically populated to some other queue , to do all sort
                // of processing
                LOGGER.info("Updating {} orderbook with {}", take.getInstrument(), take);
                TickDirection tickDirection = take.getTickDirection();
                LOGGER.info("Buy VWAP {}, Sell VWAP {} - Before update", instrumentOrderBook.getBuyorderVWAP(), instrumentOrderBook.getSellOrderVWAP());
                // As only one thread will working to update InstrumentOrderBook wth tick , we don't have to use synchronization
                tickDirection.updateOrderBook(instrumentOrderBook, take);
                LOGGER.info("Buy VWAP {}, Sell VWAP {} - After Update", instrumentOrderBook.getBuyorderVWAP(), instrumentOrderBook.getSellOrderVWAP());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        }
    }
}
