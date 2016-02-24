package com.prasanna.vwapsimulator.listner;

import com.prasanna.vwapsimulator.domain.Instrument;

public interface TicksFeedSource {
   /**
    * request tick source from all the venues for given instrument
    *
    * @param instrument
    * @return true if the request successfull
    * false if the request is unsuccessfull
    */
   boolean requestTicksFeedForInstrument(Instrument instrument);
}
