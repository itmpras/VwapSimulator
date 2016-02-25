package com.prasanna.vwapsimulator.orderbook.comparator;

import com.natpryce.makeiteasy.Maker;
import com.prasanna.vwapsimulator.domain.Tick;
import com.prasanna.vwapsimulator.domain.TickDirection;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;
import static com.natpryce.makeiteasy.MakeItEasy.with;
import static com.prasanna.vwapsimulator.maker.TickMaker.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class BuySideTickComparatorTest {
    BuySideTickComparator buySideTickComparator;

    @Before
    public void setUp() throws Exception {
        buySideTickComparator = new BuySideTickComparator();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForTicksInDifferentDirection() throws Exception {
        Tick buyTick = make(aBuyTick());
        Tick sellTick = make(aSellTick());
        buySideTickComparator.compare(buyTick, sellTick);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForNullInputs() throws Exception {
        Tick buyTick = make(aBuyTick());
        buySideTickComparator.compare(buyTick, null);
    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForSellTick() throws Exception {
        Tick sellTick1 = make(aSellTick());
        Tick sellTick2 = make(aSellTick());
        buySideTickComparator.compare(sellTick1, sellTick2);
    }

    @Test
    public void shouldReturnZeroForEqualTicks() throws Exception {
        Tick buyTick1 = make(aBuyTick());
        Tick buyTick2 = make(aBuyTick());
        int compare = buySideTickComparator.compare(buyTick1, buyTick2);
        assertThat(compare, is(0));

    }

    @Test
    public void shouldReturnMinusOneForGreaterTick() throws Exception {
        Tick buyTick1 = make(aBuyTick().but(with(PRICE_PROPERTY, new BigDecimal(200))));
        Tick buyTick2 = make(aBuyTick().but(with(PRICE_PROPERTY, new BigDecimal(100))));
        int compare = buySideTickComparator.compare(buyTick1, buyTick2);
        assertThat(compare, is(-1));
    }

    @Test
    public void shouldReturnOneForLesserTick() throws Exception {
        Tick buyTick1 = make(aBuyTick().but(with(PRICE_PROPERTY, new BigDecimal(10))));
        Tick buyTick2 = make(aBuyTick().but(with(PRICE_PROPERTY, new BigDecimal(100))));
        int compare = buySideTickComparator.compare(buyTick1, buyTick2);
        assertThat(compare, is(1));
    }


    private Maker<Tick> aSellTick() {
        return a(TICK_INSTANTIATOR).but(with(TICK_TICK_DIRECTION_PROPERTY, TickDirection.SELL));
    }

    private Maker<Tick> aBuyTick() {
        return a(TICK_INSTANTIATOR).but(with(TICK_TICK_DIRECTION_PROPERTY, TickDirection.BUY));
    }
}