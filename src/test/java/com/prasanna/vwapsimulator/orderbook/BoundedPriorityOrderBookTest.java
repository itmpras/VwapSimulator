package com.prasanna.vwapsimulator.orderbook;

import com.natpryce.makeiteasy.Maker;
import com.prasanna.vwapsimulator.domain.Tick;
import com.prasanna.vwapsimulator.domain.TickDirection;
import com.prasanna.vwapsimulator.orderbook.comparator.BuySideTickComparator;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Iterator;

import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;
import static com.natpryce.makeiteasy.MakeItEasy.with;
import static com.prasanna.vwapsimulator.maker.TickMaker.PRICE_PROPERTY;
import static com.prasanna.vwapsimulator.maker.TickMaker.TICK_INSTANTIATOR;
import static com.prasanna.vwapsimulator.maker.TickMaker.TICK_TICK_DIRECTION_PROPERTY;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by prasniths on 24/02/16.
 */
public class BoundedPriorityOrderBookTest {

    public static final int ORDER_BOOK_DEPTH = 3;
    private BoundedPriorityOrderBook boundedPriorityOrderBook;

    @Before
    public void setUp() throws Exception {
        boundedPriorityOrderBook = new BoundedPriorityOrderBook(new BuySideTickComparator(), ORDER_BOOK_DEPTH);
    }

    @Test
    public void toRemoveTailElementWhenDepthReached() throws Exception {
        BigDecimal startingPrice = new BigDecimal(100);
        int count = 1;
        for (int i = 0; i < 5; i++) {
            startingPrice = startingPrice.add(BigDecimal.TEN);
            boundedPriorityOrderBook.updateOrderBook(make(aBuyTick().but(with(PRICE_PROPERTY, startingPrice))));
        }
        BigDecimal decimal = new BigDecimal(120);
        Iterator<Tick> ticks = boundedPriorityOrderBook.getTicks();
        while (ticks.hasNext()) {
            Tick next = ticks.next();
            assertThat(next.getPrice().compareTo(decimal), is(1));
            count++;
        }

        assertThat(count, is(ORDER_BOOK_DEPTH));

    }


    private Maker<Tick> aBuyTick() {
        return a(TICK_INSTANTIATOR).but(with(TICK_TICK_DIRECTION_PROPERTY, TickDirection.BUY));
    }

}