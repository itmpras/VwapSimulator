package com.prasanna.vwapsimulator.ticksimulator;


import com.google.gson.stream.JsonReader;
import com.prasanna.vwapsimulator.Parser.GsonParser;
import com.prasanna.vwapsimulator.Parser.Parser;
import com.prasanna.vwapsimulator.domain.Instrument;
import com.prasanna.vwapsimulator.domain.Tick;
import com.prasanna.vwapsimulator.domain.TickDirection;
import com.prasanna.vwapsimulator.domain.Venue;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Created by prasniths on 23/02/16.
 */
public class TicksSimulatorTest {
    TicksSimulator ticksSimulator;
    Parser parser;

    @Before
    public void setUp() throws Exception {
        parser = new GsonParser();
        ticksSimulator = new TicksSimulator(parser);
    }

    @Test
    public void shouldSerializeJsonToTick() throws Exception {


        String instrument = "Instrument";
        String venue = "Venue";
        long size = 100L;
        BigDecimal price = new BigDecimal(100);
        Tick tick = Tick.buyTickFor(Instrument.from(instrument), Venue.from(venue), size, price);

        String tickJsonString = getTicksJsonStringFor(instrument, venue, TickDirection.BUY, size, price);
        System.out.println(tickJsonString);

        List<Tick> ticks = ticksSimulator.generateTicksFor(tickJsonString);

        assertThat(ticks, is(notNullValue()));
        assertThat(ticks.size(), is(1));
        assertThat(ticks.get(0), is(tick));

    }


    private String getTicksJsonStringFor(String instrument, String venue, TickDirection tickDirection
            , long size, BigDecimal price) {
        return "[{\"instrument\":{\"symbol\":\"" + instrument + "\"}," +
                "\"venue\":{\"marketVenue\":\"" + venue + "\"}," +
                "\"tickDirection\":\"" + tickDirection + "\"," +
                "\"size\":" + size + "," +
                "\"price\":" + price + "," +
                "\"localDateTime\":{\"date\":{\"year\":2016,\"month\":2,\"day\":23},\"time\":{\"hour\":23,\"minute\":55,\"second\":41,\"nano\":894000000}}}" +
                "]";
    }
}