package com.prasanna.vwapsimulator.orderbook.comparator;

import com.natpryce.makeiteasy.Maker;
import com.prasanna.vwapsimulator.domain.Tick;
import com.prasanna.vwapsimulator.domain.TickDirection;
import org.junit.Before;
import org.junit.Test;

import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;
import static com.natpryce.makeiteasy.MakeItEasy.with;
import static com.prasanna.vwapsimulator.maker.TickMaker.*;

public class BuySideTickCompartorTest {
   BuySideTickCompartor buySideTickCompartor;

   @Before
   public void setUp() throws Exception {
      buySideTickCompartor = new BuySideTickCompartor();
   }

   @Test(expected = IllegalArgumentException.class)
   public void shouldThrowExceptionForTicksInDifferentDirection() throws Exception {
      Tick buyTick = make(aBuyTick());
      Tick sellTick = make(aSellTick());
      buySideTickCompartor.compare(buyTick, sellTick);
   }

   @Test(expected = IllegalArgumentException.class)
   public void shouldThrowExceptionForNullInputs() throws Exception {
      Tick buyTick = aBuyTick();
      buySideTickCompartor.compare(buyTick, null);
   }

   private Maker<Tick> aSellTick() {
      return a(TICK_INSTANTIATOR).but(with(TICK_TICK_DIRECTION_PROPERTY, TickDirection.SELL));
   }
   private Maker<Tick> aBuyTick() {
      return a(TICK_INSTANTIATOR).but(with(TICK_TICK_DIRECTION_PROPERTY, TickDirection.BUY));
   }
}