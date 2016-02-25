package com.prasanna.vwapsimulator.orderbook;

import com.prasanna.vwapsimulator.domain.Instrument;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by prasniths on 25/02/16.
 */
public class ConcurrentInstrumentOrderBookMapTest {
    private ConcurrentInstrumentOrderBookMap concurrentInstrumentOrderBookMap;
    private Mockery context;

    @Before
    public void setUp() throws Exception {
        context = new JUnit4Mockery();
        Map<Instrument, InstrumentOrderBook> map = context.mock(Map.class);
       // concurrentInstrumentOrderBookMap = new ConcurrentInstrumentOrderBookMap(map);

    }
}