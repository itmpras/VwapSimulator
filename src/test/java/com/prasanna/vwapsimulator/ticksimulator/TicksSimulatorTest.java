package com.prasanna.vwapsimulator.ticksimulator;

import com.prasanna.vwapsimulator.Parser.GsonParser;
import com.prasanna.vwapsimulator.Parser.Parser;
import com.prasanna.vwapsimulator.domain.Tick;
import org.hamcrest.Description;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.Action;
import org.jmock.api.Invocation;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;

public class TicksSimulatorTest {
    Mockery context;
    TicksSimulator ticksSimulator;
    Parser parser;
    Reader reader;
    BlockingQueue<Tick> queue;

    @Before
    public void setUp() throws Exception {
        context = new JUnit4Mockery();
        parser = context.mock(Parser.class);
        reader = new StringReader("");
        queue = context.mock(BlockingQueue.class);
        ticksSimulator = new TicksSimulator(reader, parser, queue);
    }

    @Test
    public void shouldGenerateRandomTicks() throws Exception {

        context.checking(new Expectations() {
            {
                oneOf(parser).parse(reader, Tick.class);
                will(returnTicks());
                oneOf(queue).put(with(any(Tick.class)));
            }
        });

        ticksSimulator.generateRandomTicks();
        context.assertIsSatisfied();
    }

    private Action returnTicks() {
        return new Action() {
            @Override
            public Object invoke(Invocation invocation) throws Throwable {
                GsonParser parser = new GsonParser();
                InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("Ticks.json");
                return parser.parse(new BufferedReader(new InputStreamReader(resourceAsStream)), Tick.class);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Returning Ticks");
            }
        };
    }
}