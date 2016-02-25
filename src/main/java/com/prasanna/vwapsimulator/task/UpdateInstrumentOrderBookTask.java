package com.prasanna.vwapsimulator.task;

import com.prasanna.vwapsimulator.domain.Instrument;
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
                Instrument instrument = take.getInstrument();
                LOGGER.info("Updating {} orderbook with {}", instrument, take);
                TickDirection tickDirection = take.getTickDirection();
                LOGGER.info("Buy VWAP {}, Sell VWAP {} - Before update for {}", instrumentOrderBook.getBuyorderVWAP(), instrumentOrderBook.getSellOrderVWAP(),instrument);
                // As only one thread will working to update InstrumentOrderBook wth tick , we don't have to use synchronization
                tickDirection.updateOrderBook(instrumentOrderBook, take);
                // Technically we can populate this information to a different queue for all sort of processings
                LOGGER.info("Buy VWAP {}, Sell VWAP {} - After Update for {}", instrumentOrderBook.getBuyorderVWAP(), instrumentOrderBook.getSellOrderVWAP(),instrument);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        }
    }
}
