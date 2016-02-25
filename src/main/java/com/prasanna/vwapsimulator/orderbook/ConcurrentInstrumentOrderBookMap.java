package com.prasanna.vwapsimulator.orderbook;

import com.prasanna.vwapsimulator.domain.Instrument;
import com.prasanna.vwapsimulator.domain.Tick;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by prasniths on 24/02/16.
 */
public class ConcurrentInstrumentOrderBookMap implements OrderBookMap {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConcurrentInstrumentOrderBookMap.class);
    private ConcurrentHashMap<Instrument, InstrumentOrderBook> map;

    public ConcurrentInstrumentOrderBookMap() {
        map = new ConcurrentHashMap<>();
    }

    @Override
    public boolean addInstrument(Instrument instrument) {
        boolean result = true;
        InstrumentOrderBook orderBook = InstrumentOrderBook.newOrderBook();
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

    @Override
    public void update(Tick tick) {
        Instrument instrument = tick.getInstrument();
        InstrumentOrderBook instrumentOrderBook = map.get(instrument);
        if (instrumentOrderBook == null) {
            LOGGER.info("Ignoring  tick for  {}", instrument);
        } else {
            LOGGER.info("Updating OrderBook for {}", instrument);
            instrumentOrderBook.update(tick);
        }
    }
}
