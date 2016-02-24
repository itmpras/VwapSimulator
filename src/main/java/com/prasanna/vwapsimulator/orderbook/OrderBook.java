package com.prasanna.vwapsimulator.orderbook;

import com.prasanna.vwapsimulator.domain.Tick;

import java.util.Iterator;

public interface OrderBook {

   boolean updateOrderBook(Tick tick);

   Iterator<Tick> getTicks();
}
