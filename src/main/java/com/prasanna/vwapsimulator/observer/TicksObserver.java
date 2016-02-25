package com.prasanna.vwapsimulator.observer;

import com.prasanna.vwapsimulator.domain.Tick;


public interface TicksObserver {
   /**
    * To updateOrderBook received tick to Obeservers
    * @param tick
    */
   void update(Tick tick);
}