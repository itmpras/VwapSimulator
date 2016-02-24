package com.prasanna.vwapsimulator.maker;

import com.natpryce.makeiteasy.Instantiator;
import com.natpryce.makeiteasy.PropertyLookup;
import com.prasanna.vwapsimulator.domain.Instrument;
import com.prasanna.vwapsimulator.domain.Tick;
import com.natpryce.makeiteasy.Property;
import com.prasanna.vwapsimulator.domain.TickDirection;
import com.prasanna.vwapsimulator.domain.Venue;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Created by gopalp on 24/02/2016.
 */
public class TickMaker {

   public TickMaker() {
   }

   public static final Property<Tick, Instrument> INSTRUMENT_PROPERTY = new Property<>();
   public static final Property<Tick, Venue> VENUE_PROPERTY = new Property<>();
   public static final Property<Tick, TickDirection> TICK_TICK_DIRECTION_PROPERTY = new Property<>();
   public static final Property<Tick, Long> SIZE_PROPERTY = new Property<>();
   public static final Property<Tick, BigDecimal> PRICE_PROPERTY = new Property<>();
   public static final Property<Tick, LocalDateTime> TIME_STAMP_PROPRTY = new Property<>();

   public static final Instantiator<Tick> TICK_INSTANTIATOR = propertyLookup -> {

      Tick tick = new Tick(
         propertyLookup.valueOf(INSTRUMENT_PROPERTY, Instrument.from("VOD.L")),
         propertyLookup.valueOf(VENUE_PROPERTY, Venue.from("SETS")),
         propertyLookup.valueOf(TICK_TICK_DIRECTION_PROPERTY, TickDirection.BUY),
         propertyLookup.valueOf(SIZE_PROPERTY, 100L),
         propertyLookup.valueOf(PRICE_PROPERTY, new BigDecimal(100))
      );

      return tick;
   };
}
