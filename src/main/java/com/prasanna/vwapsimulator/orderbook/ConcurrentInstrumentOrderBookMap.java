package com.prasanna.vwapsimulator.orderbook;

import com.prasanna.vwapsimulator.domain.Instrument;
import com.prasanna.vwapsimulator.domain.Tick;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * ThreadSafe OrderBookMap for each request instrument
 */
public class ConcurrentInstrumentOrderBookMap implements OrderBookMap {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConcurrentInstrumentOrderBookMap.class);
    private ConcurrentHashMap<Instrument, InstrumentOrderBook> map;
    private int instrumentOrderBookDepth;
    private int instrumentTickQueueDepth;

    public ConcurrentInstrumentOrderBookMap(int instrumentOrderBookDepth, int instrumentTickQueueDepth) {
        map = new ConcurrentHashMap<>();
        this.instrumentOrderBookDepth = instrumentOrderBookDepth;
        this.instrumentTickQueueDepth = instrumentTickQueueDepth;
    }

    /**
     * To add instrument to get market ticks
     *
     * @param instrument
     * @return
     */
    @Override
    public boolean addInstrument(Instrument instrument) {
        boolean result = true;
        InstrumentOrderBook orderBook = InstrumentOrderBook.newOrderBook(instrumentOrderBookDepth, instrumentTickQueueDepth);
        InstrumentOrderBook instrumentOrderBook = map.putIfAbsent(instrument, orderBook);
        if (instrumentOrderBook == null) {
            LOGGER.info("Added {} instrument for receiving market ticks", instrument);
            orderBook.init();
        } else {
            LOGGER.info("Ignoring Instrument {} add request", instrument);
            result = false;
        }
        return result;
    }


    /**
     * To update tick to InstrumentOrderBook
     *
     * @param tick
     */
    @Override
    public void update(Tick tick) {
        Instrument instrument = tick.getInstrument();
        InstrumentOrderBook instrumentOrderBook = map.get(instrument);
        if (instrumentOrderBook == null) {
            LOGGER.debug("Ignoring  tick for  {}", instrument);
        } else {
            LOGGER.debug("Updating OrderBook for {}", instrument);
            instrumentOrderBook.update(tick);
        }
    }

    @Override
    public void shutDown() {
        map.entrySet().stream()
                .map(instrumentInstrumentOrderBookEntry -> instrumentInstrumentOrderBookEntry.getValue())
                .forEach(InstrumentOrderBook::shutdown);

    }
}
