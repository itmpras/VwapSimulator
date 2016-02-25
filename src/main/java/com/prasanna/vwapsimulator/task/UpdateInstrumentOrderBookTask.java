package com.prasanna.vwapsimulator.task;

import com.prasanna.vwapsimulator.domain.Instrument;
import com.prasanna.vwapsimulator.domain.Tick;
import com.prasanna.vwapsimulator.domain.TickDirection;
import com.prasanna.vwapsimulator.orderbook.InstrumentOrderBook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by prasniths on 25/02/16.
 */
// TODO Thread exception handler
// TODO Shutdown
// TODO remove instrument

public class UpdateInstrumentOrderBookTask implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateInstrumentOrderBookTask.class);
    private InstrumentOrderBook instrumentOrderBook;


    public UpdateInstrumentOrderBookTask(InstrumentOrderBook instrumentOrderBook) {
        this.instrumentOrderBook = instrumentOrderBook;
    }

    @Override
    public void run() {
        Thread.currentThread().setName("UpdateInstrumentOrderBookTask");
        while (instrumentOrderBook.isRunning()) {

            try {
                Tick take = instrumentOrderBook.getTicksForInstrument().take();
                LOGGER.info("Updating {} orderbook with {}", take.getInstrument(), take);
                TickDirection tickDirection = take.getTickDirection();
                LOGGER.info(" Buy VWAP {}, Sell VWAP {} - Before update", instrumentOrderBook.getBuyorderVWAP(), instrumentOrderBook.getSellOrderVWAP());
                tickDirection.updateOrderBook(instrumentOrderBook, take);
                LOGGER.info("Buy VWAP {}, Sell VWAP {} - After Update", instrumentOrderBook.getBuyorderVWAP(), instrumentOrderBook.getSellOrderVWAP());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        }
    }
}
