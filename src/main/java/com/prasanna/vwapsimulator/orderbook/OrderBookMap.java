package com.prasanna.vwapsimulator.orderbook;

import com.prasanna.vwapsimulator.domain.Instrument;
import com.prasanna.vwapsimulator.observer.TicksObserver;

/**
 * Created by prasniths on 24/02/16.
 */
public interface OrderBookMap extends TicksObserver {
    boolean addInstrument(Instrument instrument);
}
