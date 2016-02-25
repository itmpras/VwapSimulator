package com.prasanna.vwapsimulator.orderbook;

import com.natpryce.makeiteasy.MakeItEasy;
import com.prasanna.vwapsimulator.domain.Instrument;
import com.prasanna.vwapsimulator.domain.Tick;
import com.prasanna.vwapsimulator.maker.TickMaker;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.States;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.concurrent.DeterministicExecutor;
import org.jmock.lib.concurrent.Synchroniser;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static com.natpryce.makeiteasy.MakeItEasy.make;


/**
 * Created by prasniths on 24/02/16.
 */
public class InstrumentOrderBookRepositoryTest {

    private InstrumentOrderBookRepository repository;
    private Mockery context;
    private OrderBookMap orderBookMap;
    private BlockingQueue<Tick> sourceQueue;
    private DeterministicExecutor deterministicExecutor1;
    private DeterministicExecutor deterministicExecutor2;
    private States tickUpdateState;
    private Synchroniser threadingPolicy = new Synchroniser();

    @Before
    public void setUp() throws Exception {
        context = new JUnit4Mockery();
        orderBookMap = context.mock(OrderBookMap.class);
        sourceQueue = new LinkedBlockingQueue<>();
        deterministicExecutor1 = new DeterministicExecutor();
        deterministicExecutor2 = new DeterministicExecutor();
        repository = new InstrumentOrderBookRepository(sourceQueue, orderBookMap, deterministicExecutor1);
        tickUpdateState = context.states("tickUpdate").startsAs("Initial");

    }

    @Test
    public void shouldAddInstrumentTickRequestToOrderBookMap() throws Exception {

        context.checking(new Expectations() {
            {
                oneOf(orderBookMap).addInstrument(with(any(Instrument.class)));
            }
        });

        repository.requestTicksFeedForInstrument(Instrument.from("VOD.L"));
        context.assertIsSatisfied();
    }

    @Ignore
    // TODO
    public void shouldUpdateTickToOrderMap() throws Exception {

        sourceQueue.add(make(MakeItEasy.a(TickMaker.TICK_INSTANTIATOR)));
        context.checking(new Expectations() {
            {
                oneOf(orderBookMap).update(with(any(Tick.class)));
                then(tickUpdateState.is("updateMap"));

            }
        });

        repository.start();
        deterministicExecutor1.runUntilIdle();

        threadingPolicy.waitUntil(tickUpdateState.is("updateMap"), 1000);
        context.assertIsSatisfied();
        repository.shutDown();

    }


}